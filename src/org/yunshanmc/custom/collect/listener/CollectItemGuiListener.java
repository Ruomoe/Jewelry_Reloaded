package org.yunshanmc.custom.collect.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryInteractEvent;

public class CollectItemGuiListener implements Listener {
    @EventHandler
    public void click(InventoryInteractEvent event){
        if(event.getWhoClicked() instanceof Player){
            String title = event.getInventory().getTitle();
            if(title.startsWith("§6图鉴")){
                event.setCancelled(true);
            }
        }
    }

}
