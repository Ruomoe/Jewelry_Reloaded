package org.yunshanmc.custom.buff.task;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.yunshanmc.custom.buff.BuffPlayerData;

public class BuffTimerTask implements Runnable{
    @Override
    public void run(){
        for(Player player : Bukkit.getOnlinePlayers()){
            if(BuffPlayerData.getPlayerData(player.getName()) != null){
                BuffPlayerData playerData = BuffPlayerData.getPlayerData(player.getName());
                playerData.updateBuffTime();
            }else{
                BuffPlayerData.addPlayerData(player.getName(),new BuffPlayerData(player));
            }
        }
    }
}
