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
import org.yunshanmc.custom.jewelry.papi.SumPapi;
import org.yunshanmc.custom.prefix.command.PrefixCommands;
import org.yunshanmc.custom.prefix.listener.PrefixListener;
import org.yunshanmc.custom.prefix.utils.PrefixUtils;


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
        Bukkit.getPluginCommand("mtpre").setExecutor(new PrefixCommands());
        root = this.getDataFolder().getAbsolutePath();
        if (section != null)
            for (String key : section.getKeys(false))
                this.addonInvManager.register(key, section
                        .getString(key + ".inv-title"), section
                        .getString(key + ".lore-key"));
        AttributeHandle.init((ConfigurationSection)getConfig());
        this.playerManager = new PlayerManager(this);
        Plugin papi = Bukkit.getPluginManager().getPlugin("PlaceholderAPI");
        if(papi != null){
            boolean isLoadPAPI = new SumPapi().register();
            if(isLoadPAPI){
                System.out.println("[Jewelry] Placeholder API Loaded!");
            }else{
                System.out.println("[Jewelry] Placeholder API Unloaded!");
            }
        }
        getServer().getPluginManager().registerEvents(this.playerManager, this);
        getServer().getPluginManager().registerEvents(new BuffPlayerListener(),this);
        Bukkit.getPluginManager().registerEvents(new PrefixListener(),this);
        BuffUtils.update();
        CollectUtils.update();
        CollectUtils.load();
        PrefixUtils.update();
        PrefixUtils.load();
        Bukkit.getScheduler().runTaskTimer(this,new BuffTimerTask(),20L,20L);
        Bukkit.getScheduler().runTaskTimer(this,new PlayerCollectUpdateTask(),20L,20L);
        Bukkit.getScheduler().runTaskTimer(this, CollectUtils::save,3600L,3600L);
        Bukkit.getScheduler().runTaskTimer(this, PrefixUtils::save,3600L,3600L);
        for (Player player : Bukkit.getServer().getOnlinePlayers())
            this.playerManager.handleJoin(player);
    }

    public void onDisable() {
        CollectUtils.save();
        PrefixUtils.save();
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
