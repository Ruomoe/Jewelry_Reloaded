package org.yunshanmc.custom.collect.task;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.yunshanmc.custom.collect.data.PlayerCollectData;

import java.util.ArrayList;

public class PlayerCollectUpdateTask implements Runnable {
    @Override
    public void run() {
        for(Player player : Bukkit.getOnlinePlayers()){
            if(PlayerCollectData.getDataByName(player.getName()) != null){
                PlayerCollectData data = PlayerCollectData.getDataByName(player.getName());
                data.updateData();
            }else{
                PlayerCollectData data = new PlayerCollectData(player.getName(),new ArrayList<>());
                PlayerCollectData.putData(player.getName(),data);
            }
        }
    }
}
