package org.yunshanmc.custom.jewelry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.server.v1_7_R4.AttributeInstance;
import net.minecraft.server.v1_7_R4.AttributeModifier;
import net.minecraft.server.v1_7_R4.GenericAttributes;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;

public class PlayerManager implements Listener {
    private final Map<UUID, Integer> damages = new HashMap<>();

    private final Map<UUID, PlayerData> dataCache = new HashMap<>();

    private final Plugin plugin;

    private static final UUID ATTR_UUID = UUID.fromString("8d0bde67-98e7-419c-b702-90e182b0ea92");

    private static final String ATTR_NAME = "jewelry_max_health";

    PlayerManager(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        handleJoin(event.getPlayer());
    }

    void handleJoin(Player player) {
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> updateData(player, (Inventory)player.getInventory()), 5L);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        handleQuit(event.getPlayer());
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        handleQuit(event.getPlayer());
    }

    void handleQuit(Player player) {
        this.damages.remove(player.getUniqueId());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void handleDamage(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        if (damager instanceof Player) {
            Integer damage = this.damages.get(damager.getUniqueId());
            if (damage != null)
                event.setDamage(EntityDamageEvent.DamageModifier.BASE, event
                        .getDamage(EntityDamageEvent.DamageModifier.BASE) + damage.intValue());
        }
    }

    @EventHandler
    public void onCloseInventory(InventoryCloseEvent event) {
        Player player = (Player)event.getPlayer();
        Inventory inv = event.getInventory();
        InventoryHolder holder = inv.getHolder();
        if (holder instanceof Player) {
            Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> updateData(player, (Inventory)player.getInventory()));
        } else if (holder instanceof CustomInvHolder) {
            Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
                updateData(player, inv);
                ((CustomInvHolder)holder).save(player);
            });
        }
    }

    private void updateData(Player player, Inventory inv) {
        String invName;
        InventoryData invData;
        InventoryHolder holder = inv.getHolder();
        if (holder instanceof Player) {
            invName = "player";
        } else if (holder instanceof CustomInvHolder) {
            invName = ((CustomInvHolder)holder).getName();
        } else {
            return;
        }
        List<ItemStack> invalids = new ArrayList<>();
        if (holder instanceof CustomInvHolder) {
            invData = AttributeHandle.fetchInvData(player, inv, ((CustomInvHolder)holder).getLoreKey(), invalids);
        } else {
            invData = AttributeHandle.fetchInvData(player, inv, null, null);
        }
        PlayerData data = this.dataCache.get(player.getUniqueId());
        if (data == null)
            synchronized (this.dataCache) {
                data = this.dataCache.get(player.getUniqueId());
                if (data == null) {
                    data = new PlayerData(player.getName());
                    this.dataCache.put(player.getUniqueId(), data);
                }
            }
        data.setInventoryData(invName, invData);
        PlayerData finalData = data;
        Bukkit.getScheduler().runTask(this.plugin, () -> {
            this.damages.put(player.getUniqueId(), Integer.valueOf(finalData.getDamage()));
            if (holder instanceof CustomInvHolder && !invalids.isEmpty())
                ((CustomInvHolder)holder).giveBackInvalids(player, invalids);
            CraftPlayer craftP = (CraftPlayer)player;
            AttributeInstance attr = craftP.getHandle().getAttributeInstance(GenericAttributes.maxHealth);
            AttributeModifier mod = new AttributeModifier(ATTR_UUID, "jewelry_max_health", finalData.getHealth(), 0);
            mod.a(false);
            attr.b(mod);
            attr.a(mod);
            if (player.getHealth() > player.getMaxHealth())
                player.setHealth(player.getMaxHealth());
            removeEffects(player);
            if (!finalData.getEffects().isEmpty())
                player.addPotionEffects(finalData.getEffects());
        });
    }

    private void removeEffects(Player player) {
        Collection<PotionEffect> effects = player.getActivePotionEffects();
        for (PotionEffect effect : effects) {
            if (effect.getDuration() > 720000)
                player.removePotionEffect(effect.getType());
        }
    }

    public PlayerData getData(Player player) {
        return this.dataCache.get(player.getUniqueId());
    }
}
