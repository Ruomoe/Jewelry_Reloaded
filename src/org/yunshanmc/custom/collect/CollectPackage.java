package org.yunshanmc.custom.collect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class CollectPackage {
    private static HashMap<String,CollectPackage> collectPackageHashMap = new HashMap<>();

    public CollectPackage(String collectPacketName,int x,int y,int finalAddDamage,int finalAddHealth){
        this.collectPacketName = collectPacketName;
        this.x = x;
        this.y = y;
        this.finalAddDamage = finalAddDamage;
        this.finalAddHealth = finalAddHealth;
    }

    private List<CollectItem> needCollectItemList = new ArrayList<>();
    private String collectPacketName;
    public int x;
    public int y;
    private int finalAddDamage = 0;
    private int finalAddHealth = 0;
    public String getCollectPacketName() {
        return collectPacketName;
    }

    public int getFinalAddDamage() {
        return finalAddDamage;
    }

    public int getFinalAddHealth() {
        return finalAddHealth;
    }

    public List<CollectItem> getNeedCollectItemList() {
        return needCollectItemList;
    }

    public static CollectPackage getCollectPackageByName(String name){
        return collectPackageHashMap.get(name);
    }
    public static void putCollectPackage(String name,CollectPackage collectPackage){
        collectPackageHashMap.put(name,collectPackage);
    }
    public static Set<String> getAllCollectPackageName(){
        return collectPackageHashMap.keySet();
    }
}
