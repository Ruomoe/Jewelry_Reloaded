package org.yunshanmc.custom.suit.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.yunshanmc.custom.jewelry.CustomInvHolder;
import org.yunshanmc.custom.jewelry.Jewelry;
import org.yunshanmc.custom.suit.PlayerSuitCache;
import org.yunshanmc.custom.suit.utils.AttackUtils;
import org.yunshanmc.custom.suit.utils.DefenseUtils;

public class SuitAttrListener implements Listener {
    @EventHandler
    public void join(PlayerJoinEvent event){
        PlayerSuitCache cache = PlayerSuitCache.getPlayerCacheByName(event.getPlayer().getName());
        if(cache == null) PlayerSuitCache.putCache(event.getPlayer().getName(),new PlayerSuitCache(event.getPlayer()));
        if(cache != null) Bukkit.getScheduler().runTaskAsynchronously(Jewelry.plugin, cache::update);
    }
    @EventHandler
    public void guiClose(InventoryCloseEvent event){
        Inventory inv = event.getInventory();
        InventoryHolder holder = inv.getHolder();
        HumanEntity player = event.getPlayer();
        if(player instanceof  Player) {
            Player realPlayer = (Player)player;
            PlayerSuitCache cache = PlayerSuitCache.getPlayerCacheByName(realPlayer.getName());
            if (holder instanceof Player) {
                if(cache != null) Bukkit.getScheduler().runTaskAsynchronously(Jewelry.plugin, cache::update);
            } else if (holder instanceof CustomInvHolder) {
                if(cache != null) Bukkit.getScheduler().runTaskAsynchronously(Jewelry.plugin, cache::update);
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event){
        Entity entity = event.getEntity();
        Entity damager = event.getDamager();
        if(damager instanceof Player){
            AttackUtils.onDamage(event,(Player)damager);
        }else if(entity instanceof Player){
            DefenseUtils.byDamage(event,(Player)entity);
        }
    }
}
