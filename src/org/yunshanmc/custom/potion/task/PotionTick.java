package org.yunshanmc.custom.potion.task;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.yunshanmc.custom.potion.data.PlayerPotionData;

public class PotionTick implements Runnable {
    @Override
    public void run(){
        for(Player player : Bukkit.getOnlinePlayers()){
            if(player.isOnline()){
                PlayerPotionData data = PlayerPotionData.getPotionDataByPlayerName(player.getName());
                if(data != null){
                    data.update();
                }else{
                    PlayerPotionData.putMap(player.getName(), new PlayerPotionData(player.getName()));
                }
            }
        }
    }
}
