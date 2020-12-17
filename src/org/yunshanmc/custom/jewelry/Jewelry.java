package org.yunshanmc.custom.jewelry;

import com.comphenix.protocol.ProtocolLibrary;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.yunshanmc.custom.buff.commands.BuffCommands;
import org.yunshanmc.custom.buff.listener.BuffPlayerListener;
import org.yunshanmc.custom.buff.task.BuffTimerTask;
import org.yunshanmc.custom.buff.utils.BuffUtils;
import org.yunshanmc.custom.collect.command.CollectCommands;
import org.yunshanmc.custom.collect.task.PlayerCollectUpdateTask;
import org.yunshanmc.custom.collect.utils.CollectUtils;

public final class Jewelry extends JavaPlugin {
    private static Jewelry Instance;

    private PlayerManager playerManager;

    private AddonInvManager addonInvManager;

    public static Jewelry getInstance() {
        return Instance;
    }
    public static String root;
    public static Plugin plugin;
    public void onEnable() {
        Instance = this;
        plugin = Bukkit.getPluginManager().getPlugin("Jewelry");
        saveDefaultConfig();
        this.addonInvManager = new AddonInvManager(getDataFolder().toPath());
        ConfigurationSection section = getConfig().getConfigurationSection("addon-inv");
        Bukkit.getPluginCommand("buff").setExecutor(new BuffCommands());
        Bukkit.getPluginCommand("tj").setExecutor(new CollectCommands());
        root = this.getDataFolder().getAbsolutePath();
        if (section != null)
            for (String key : section.getKeys(false))
                this.addonInvManager.register(key, section
                        .getString(key + ".inv-title"), section
                        .getString(key + ".lore-key"));
        AttributeHandle.init((ConfigurationSection)getConfig());
        this.playerManager = new PlayerManager(this);
        getServer().getPluginManager().registerEvents(this.playerManager, this);
        getServer().getPluginManager().registerEvents(new BuffPlayerListener(),this);
        BuffUtils.update();
        CollectUtils.update();
        CollectUtils.load();
        Bukkit.getScheduler().runTaskTimer(this,new BuffTimerTask(),20L,20L);
        Bukkit.getScheduler().runTaskTimer(this,new PlayerCollectUpdateTask(),20L,20L);
        for (Player player : Bukkit.getServer().getOnlinePlayers())
            this.playerManager.handleJoin(player);
    }

    public void onDisable() {
        CollectUtils.save();
        HandlerList.unregisterAll((Plugin)this);
        ProtocolLibrary.getProtocolManager().removePacketListeners((Plugin)this);
        for (Player player : Bukkit.getServer().getOnlinePlayers())
            this.playerManager.handleQuit(player);
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 2 &&
                "gui".equals(args[0])) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("§c只能在游戏中使用");
                return true;
            }
            Player player = (Player)sender;
            Inventory inv = this.addonInvManager.getInv(player, args[1]);
            if (inv != null) {
                player.openInventory(inv);
            } else {
                sender.sendMessage("§c指定的背包不存在");
            }
            return true;
        }
        sender.sendMessage("§c参数错误");
        return true;
    }

    public PlayerManager getPlayerManager() {
        return this.playerManager;
    }
}
