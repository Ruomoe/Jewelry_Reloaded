package org.yunshanmc.custom.suit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.yunshanmc.custom.jewelry.Jewelry;
import org.yunshanmc.custom.jewelry.PlayerData;
import org.yunshanmc.custom.suit.utils.SuitUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlayerSuitCache {
    private static HashMap<String, PlayerSuitCache> cacheHashMap = new HashMap<>();

    private List<String> attrs;
    private String playerName;

    private int addDamage = 0;
    private int addHealth = 0;
    private int addForge = 0;

    private double armor = 0;
    private int takeDamage = 0;
    private double dodgeProbability = 0;
    private double reflexProbability = 0;
    private double angryProbability = 0;

    public PlayerSuitCache(Player player) {
        this.playerName = player.getName();
        this.attrs = new ArrayList<>();
    }

    public void update() {
        clearAttr();
        if(Bukkit.getPlayer(playerName) != null && Bukkit.getPlayer(playerName).isOnline()) {
            Player player = Bukkit.getPlayer(playerName);
            PlayerData jewelryData = Jewelry.getInstance().getPlayerManager().getData(player);
            List<ItemStack> itemStackList = jewelryData.getItems();
            List<ItemStack> equipList = new ArrayList<>();
            if(player.getEquipment().getHelmet() != null) equipList.add(player.getEquipment().getHelmet().clone());
            if(player.getEquipment().getChestplate() != null) equipList.add(player.getEquipment().getChestplate().clone());
            if(player.getEquipment().getLeggings() != null) equipList.add(player.getEquipment().getLeggings().clone());
            if(player.getEquipment().getBoots() != null) equipList.add(player.getEquipment().getBoots().clone());
            for(ItemStack stack : itemStackList){
                if(stack.getItemMeta().hasDisplayName()){
                    equipList.add(stack.clone());
                }
            }
            for(Suit suit : Suit.getSuits()) {
                int number = 0;
                for (ItemStack stack : equipList) {
                    ItemMeta meta = stack.getItemMeta();
                    String display = meta.getDisplayName();
                    if(display.contains(suit.getSuitName())){
                        if(display.contains(suit.getKeys()[number])) number++;
                    }
                    //所有包括饰品包括装备;
                }
                List<String> attrs = suit.getAttrMap().get(number);
                //读入套装属性.
                for(String attr : attrs) readAttr(attr);
            }
            for(ItemStack stack : equipList){
                //读入每个饰品和装备的固有属性.
                for(String lore : stack.getItemMeta().getLore()) readAttr(lore);
            }

        }
    }
    private void readAttr(String attr){
        if(attr.contains(Jewelry.getInstance().getConfig().getString("health-name"))){
            addHealth += SuitUtils.getHas(attr);
        }else if(attr.contains(Jewelry.getInstance().getConfig().getString("damage-name"))){
            addDamage += SuitUtils.getHas(attr);
        }else if(attr.contains(Jewelry.getInstance().getConfig().getString("forge-rate-name"))){
            addForge += SuitUtils.getHas(attr);
        }else if(attr.contains(Jewelry.getInstance().getConfig().getString("armor"))){
            armor += SuitUtils.getHas(attr);
        }else if(attr.contains(Jewelry.getInstance().getConfig().getString("takeDamage"))){
            takeDamage += SuitUtils.getHas(attr);
        }else if(attr.contains(Jewelry.getInstance().getConfig().getString("dodgeProbability"))){
            dodgeProbability += SuitUtils.getHas(attr);
        }else if(attr.contains(Jewelry.getInstance().getConfig().getString("reflexProbability"))){
            reflexProbability += SuitUtils.getHas(attr);
        }else if(attr.contains(Jewelry.getInstance().getConfig().getString("angryProbability"))){
            angryProbability += SuitUtils.getHas(attr);
        }
    }

    public int getAddDamage() {
        return addDamage;
    }

    public int getAddHealth() {
        return addHealth;
    }

    public int getAddForge() {
        return addForge;
    }

    public double getArmor() {
        return armor;
    }

    public int getTakeDamage() {
        return takeDamage;
    }

    public double getDodgeProbability() {
        return dodgeProbability;
    }

    public double getReflexProbability() {
        return reflexProbability;
    }

    public double getAngryProbability() {
        return angryProbability;
    }

    public void clearAttr() {
        attrs.clear();
        armor = 0;
        takeDamage = 0;
        dodgeProbability = 0;
        reflexProbability = 0;
        angryProbability = 0;
    }

    public static void putCache(String playerName, PlayerSuitCache cache) {
        cacheHashMap.put(playerName, cache);
    }

    public static PlayerSuitCache getPlayerCacheByName(String playerName) {
        return cacheHashMap.get(playerName);
    }
}
