package org.yunshanmc.custom.collect;

import java.util.HashMap;

public class CollectItem {
    private static HashMap<String,String> nameToDisplayName = new HashMap<>();
    private static HashMap<String,CollectItem> itemHashMap = new HashMap<>();
    public CollectItem(String name,int damage,int health){
        this.name = name;
        this.addDamage = damage;
        this.addHealth = health;
    }

    private String name;
    private int addDamage = 0;
    private int addHealth = 0;

    public String getName() {
        return name;
    }

    public int getAddDamage() {
        return addDamage;
    }

    public int getAddHealth() {
        return addHealth;
    }

    public static void putItem(String name,CollectItem item){
        itemHashMap.put(name,item);
    }
    public static CollectItem getItem(String name){
        return itemHashMap.get(name);
    }
    public static void putChangeMap(String name,String display){
        nameToDisplayName.put(name,display);
    }
    public static String getDisplay(String name){
        return nameToDisplayName.get(name);
    }
}
