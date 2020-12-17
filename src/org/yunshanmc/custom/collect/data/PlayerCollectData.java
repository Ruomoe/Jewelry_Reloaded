package org.yunshanmc.custom.collect.data;

import org.yunshanmc.custom.collect.CollectItem;
import org.yunshanmc.custom.collect.CollectPackage;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class PlayerCollectData {
    private static HashMap<String, PlayerCollectData> playerDataMap = new HashMap<>();
    private String playerName;

    public PlayerCollectData(String playerName, List<CollectItem> collectItems) {
        this.playerName = playerName;
        this.allGetCollectItem = collectItems;
    }

    private List<CollectItem> allGetCollectItem;

    public int addDamages = 0;
    public int addHealths = 0;

    public void addCollectItem(CollectItem collectItem) {
        allGetCollectItem.add(collectItem);
    }

    public String getPlayerName() {
        return playerName;
    }

    public void updateData() {
        addHealths = 0;
        addDamages = 0;
        for (String packageName : CollectPackage.getAllCollectPackageName()) {
            CollectPackage collectPackage = CollectPackage.getCollectPackageByName(packageName);
            boolean isFull = true;
            for (CollectItem item : collectPackage.getNeedCollectItemList()) {
                if (allGetCollectItem.contains(item)) {
                    addHealths += item.getAddHealth();
                    addDamages += item.getAddDamage();
                }else{
                    isFull = false;
                }
            }
            if(isFull){
                addDamages += collectPackage.getFinalAddDamage();
                addHealths += collectPackage.getFinalAddHealth();
            }
        }
    }

    public boolean isGot(String collectItemName) {
        for (CollectItem collectItem : allGetCollectItem) {
            if (collectItem.getName().equals(collectItemName)) {
                return true;
            }
        }
        return false;
    }

    public List<CollectItem> getAllCollects() {
        return allGetCollectItem;
    }

    public static void putData(String playerName, PlayerCollectData collectData) {
        playerDataMap.put(playerName, collectData);
    }

    public static PlayerCollectData getDataByName(String playerName) {
        return playerDataMap.get(playerName);
    }

    public static Set<String> playerNames() {
        return playerDataMap.keySet();
    }
}
