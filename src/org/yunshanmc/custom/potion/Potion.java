package org.yunshanmc.custom.potion;

import java.util.HashMap;

public class Potion {
    private static HashMap<String,Potion> potionHashMap = new HashMap<>();

    public String name;
    public HashMap<String,Double> configNameForVaultMap;

    public boolean isHealth = false;

    public Potion(String name,HashMap<String,Double> map){
        this.name = name;
        this.configNameForVaultMap = map;
    }

    public HashMap<String,Double> getMap(){
        return configNameForVaultMap;
    }
    public void updateHealth(){
        isHealth = true;
    }

    public static void putMap(String potionName, Potion potion){
        potionHashMap.put(potionName, potion);
    }
    public static Potion getPotionByName(String name){
        return potionHashMap.get(name);
    }
    public Potion clone(){
        return new Potion(name,configNameForVaultMap);
    }
}
