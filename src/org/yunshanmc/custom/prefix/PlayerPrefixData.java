package org.yunshanmc.custom.prefix;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class PlayerPrefixData {
    private static HashMap<String,PlayerPrefixData> playerPrefixDataHashMap = new HashMap<>();
    private Player player;
    private String playerName;
    private List<Prefix> prefixList;
    private int addDamage;
    private int addHealth;
    private int addExpPlus;
    private String using = null;
    public PlayerPrefixData(String player,List<Prefix> list){
        this.playerName = player;
        this.prefixList = list;
    }

    public int getAddDamage() {
        return addDamage;
    }

    public int getAddHealth() {
        return addHealth;
    }

    public int getAddExpPlus() {
        return addExpPlus;
    }

    public String getUsing() {
        return using;
    }
    public void setUsing(String configId){
        using = configId;
    }

    public List<Prefix> getPrefixList() {
        return prefixList;
    }
    public void addPrefix(Prefix prefix){
        if(prefixList.contains(prefix)){
            return;
        }
        prefixList.add(prefix);
    }
    public void update(){
        addDamage = 0;
        addHealth = 0;
        addExpPlus = 0;
        for(Prefix prefix : prefixList){
            for(String key : prefix.getAttrMap().keySet()){
                if(prefix.isNeedUse()) {
                    if(using != null) {
                        if (using.equals(prefix.getConfigId())) {
                            int data = prefix.getAttrMap().get(key);
                            if (key.equals("damage")) {
                                addDamage += data;
                            } else if (key.equals("health")) {
                                addHealth += data;
                            } else if (key.equals("expPlus")) {
                                addExpPlus += data;
                            }
                        }
                    }
                }else{
                    int data = prefix.getAttrMap().get(key);
                    if (key.equals("damage")) {
                        addDamage += data;
                    } else if (key.equals("health")) {
                        addHealth += data;
                    } else if (key.equals("expPlus")) {
                        addExpPlus += data;
                    }
                }
            }
        }
    }
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public static void putPlayerData(String playerName, PlayerPrefixData data){
        playerPrefixDataHashMap.put(playerName,data);
    }
    public static PlayerPrefixData getPlayerDataByName(String playerName){
        return playerPrefixDataHashMap.get(playerName);
    }
    public static Set<String> players(){
        return playerPrefixDataHashMap.keySet();
    }
}
