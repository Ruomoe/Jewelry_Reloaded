package org.yunshanmc.custom.potion.data;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.yunshanmc.custom.jewelry.Jewelry;
import org.yunshanmc.custom.potion.Potion;
import org.yunshanmc.custom.suit.PlayerSuitCache;

import java.util.HashMap;
import java.util.Iterator;

public class PlayerPotionData {
    private static HashMap<String,PlayerPotionData> dataHashMap = new HashMap<>();

    public String playerName;
    public HashMap<Potion,Integer> tickMap = new HashMap<>();

    public PlayerPotionData(String playerName){
        this.playerName = playerName;
    }
    public void update(){
        Iterator<Potion> it = tickMap.keySet().iterator();
        while(it.hasNext()){
            Potion potion = it.next();
            int time = tickMap.get(potion);
            if(time - 1 < 0){
                it.remove();
            }else{
                if(Bukkit.getPlayer(playerName) != null && Bukkit.getPlayer(playerName).isOnline()) {
                    Player player = Bukkit.getPlayer(playerName);
                    HashMap<String, Double> attrMap = potion.getMap();
                    for (String str : attrMap.keySet()) {
                        if (str.equalsIgnoreCase("恢复生命")) {
                            if (!potion.isHealth) {
                                potion.updateHealth();
                                double vault = attrMap.get(str);
                                heal(player,vault);
                            }
                        } else if (str.equalsIgnoreCase("恢复生命百分比")) {
                            if (!potion.isHealth) {
                                potion.updateHealth();
                                double vault = player.getMaxHealth() * (attrMap.get(str) / 100);
                                heal(player,vault);
                            }
                        }else{
                            PlayerSuitCache cache = PlayerSuitCache.getPlayerCacheByName(playerName);
                            if(cache != null){
                                String attr = Jewelry.getInstance().getConfig().getString(str) + ": " + attrMap.get(str);
                                cache.readAttrOther("Potion",attr);
                            }
                        }
                    }
                }
                tickMap.put(potion,time - 1);
            }
        }
    }

    public void addPotion(Potion potion, int second){
        tickMap.put(potion,second);
    }
    public void heal(Player player, double vault){
        if(player.getHealth() + vault >= player.getMaxHealth()){
            player.setHealth(player.getMaxHealth());
        }else{
            player.setHealth(player.getHealth() + vault);
        }
    }
    public static void putMap(String name, PlayerPotionData data){
        dataHashMap.put(name,data);
    }
    public static PlayerPotionData getPotionDataByPlayerName(String playerName){
        return dataHashMap.get(playerName);
    }
}
