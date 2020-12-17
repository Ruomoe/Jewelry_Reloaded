package org.yunshanmc.custom.buff.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.yunshanmc.custom.buff.BuffPlayerData;

public class BuffPlayerListener implements Listener {
    @EventHandler
    public void getExp(PlayerExpChangeEvent event){
        Player player = event.getPlayer();
        BuffPlayerData playerData = BuffPlayerData.getPlayerData(player.getName());
        if(playerData != null){
            int data = playerData.getAddExp();
            double point = data / 100.0;
            int exp = event.getAmount();
            exp *= 1 + point;
            event.setAmount(exp);
        }
    }
    @EventHandler
    public void click(InventoryClickEvent event){
        if(event.getWhoClicked() instanceof Player){
            String title = event.getInventory().getTitle();
            if(title.startsWith("§6图鉴")){
                event.setCancelled(true);
            }
        }
    }
}
