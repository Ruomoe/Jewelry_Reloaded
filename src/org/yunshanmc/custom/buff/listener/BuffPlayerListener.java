package org.yunshanmc.custom.buff.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
}
