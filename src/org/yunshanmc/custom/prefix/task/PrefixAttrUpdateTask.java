package org.yunshanmc.custom.prefix.task;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.yunshanmc.custom.prefix.PlayerPrefixData;

import java.util.ArrayList;

public class PrefixAttrUpdateTask implements Runnable {
    @Override
    public void run(){
        for(Player player : Bukkit.getOnlinePlayers()){
            if(PlayerPrefixData.getPlayerDataByName(player.getName()) != null){
                PlayerPrefixData.getPlayerDataByName(player.getName()).update();
            }else{
                PlayerPrefixData.putPlayerData(player.getName(),new PlayerPrefixData(player,new ArrayList<>()));
            }
        }
    }
}
