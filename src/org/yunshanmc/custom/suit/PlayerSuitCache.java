package org.yunshanmc.custom.suit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.yunshanmc.custom.buff.listener.BuffPlayerListener;
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
    private double absorb = 0;
    private double absorbPercentage = 0;
    private double crit;
    private double critProbability;
    private double damagePercentage = 0;
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
            if(player.getEquipment().getHelmet() != null) if(player.getEquipment().getHelmet().getItemMeta().hasDisplayName()) equipList.add(player.getEquipment().getHelmet().clone());
            if(player.getEquipment().getChestplate() != null)if(player.getEquipment().getChestplate().getItemMeta().hasDisplayName()) equipList.add(player.getEquipment().getChestplate().clone());
            if(player.getEquipment().getLeggings() != null) if(player.getEquipment().getLeggings().getItemMeta().hasDisplayName())equipList.add(player.getEquipment().getLeggings().clone());
            if(player.getEquipment().getBoots() != null) if(player.getEquipment().getBoots().getItemMeta().hasDisplayName()) equipList.add(player.getEquipment().getBoots().clone());
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
                        for(int i = 0 ; i < suit.getNeed(); i++){
                            if(display.contains(suit.getKeys()[i])){
                                number++;
                                break;
                            }
                        }
                    }
                    //所有包括饰品包括装备;
                }
                //player.sendMessage(suit.getSuitName()  + " 套装数 " + number);
                if(number >= 2) {
                    if(suit.getAttrMap().containsKey(number)) {
                        List<String> attrs = suit.getAttrMap().get(number);
                        //读入套装属性.
                        for (String attr : attrs) readAttr(attr);
                    }
                }
            }
            for(ItemStack stack : equipList){
                //读入每个饰品和装备的固有属性.
                for(String lore : stack.getItemMeta().getLore()) readAttr(lore);
            }
            //player.sendMessage("当前含有属性:");
            //for(String attr : attrs){
            //    player.sendMessage(attr);
           // }
        }
    }
    private void readAttr(String attr){
        if(attr.contains(Jewelry.getInstance().getConfig().getString("health-name"))){
            addHealth += SuitUtils.getHas(attr);
            attrs.add(attr);
        }else if(attr.contains(Jewelry.getInstance().getConfig().getString("damage-name")) && !attr.contains(Jewelry.getInstance().getConfig().getString("damage-percentage"))){
            addDamage += SuitUtils.getHas(attr);
            attrs.add(attr);
        }else if(attr.contains(Jewelry.getInstance().getConfig().getString("forge-rate-name"))){
            addForge += SuitUtils.getHas(attr);
            attrs.add(attr);
        }else if(attr.contains(Jewelry.getInstance().getConfig().getString("armor"))){
            armor += SuitUtils.getHas(attr);
            attrs.add(attr);
        }else if(attr.contains(Jewelry.getInstance().getConfig().getString("takeDamage"))){
            takeDamage += SuitUtils.getHas(attr);
            attrs.add(attr);
        }else if(attr.contains(Jewelry.getInstance().getConfig().getString("dodgeProbability"))){
            dodgeProbability += SuitUtils.getHas(attr);
            attrs.add(attr);
        }else if(attr.contains(Jewelry.getInstance().getConfig().getString("reflexProbability"))){
            reflexProbability += SuitUtils.getHas(attr);
            attrs.add(attr);
        }else if(attr.contains(Jewelry.getInstance().getConfig().getString("angryProbability"))){
            angryProbability += SuitUtils.getHas(attr);
            attrs.add(attr);
        }else if(attr.contains(BuffPlayerListener.absorbBloodStr) && !attr.contains(Jewelry.getInstance().getConfig().getString("absorb-percentage"))){
            absorb += SuitUtils.getHas(attr);
            attrs.add(attr);
        }else if(attr.contains(Jewelry.getInstance().getConfig().getString("absorb-percentage"))){
            absorbPercentage += SuitUtils.getHas(attr);
            attrs.add(attr);
        }else if(attr.contains(Jewelry.getInstance().getConfig().getString("damage-percentage"))){
            damagePercentage += SuitUtils.getHas(attr);
            attrs.add(attr);
        }else if(attr.contains(Jewelry.getInstance().getConfig().getString("critProbability"))){
            critProbability += SuitUtils.getHas(attr);
            attrs.add(attr);
        }else if(attr.contains(Jewelry.getInstance().getConfig().getString("crit"))){
            crit += SuitUtils.getHas(attr);
            attrs.add(attr);
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

    public double getAbsorb() {
        return absorb;
    }

    public double getAbsorbPercentage() {
        return absorbPercentage;
    }

    public double getCrit() {
        return crit;
    }

    public double getCritProbability() {
        return critProbability;
    }

    public double getDamagePercentage() {
        return damagePercentage;
    }

    public void clearAttr() {
        attrs.clear();
        addHealth = 0;
        addDamage = 0;
        addForge = 0;
        armor = 0;
        takeDamage = 0;
        dodgeProbability = 0;
        reflexProbability = 0;
        angryProbability = 0;
        absorb = 0;
        absorbPercentage = 0;
        crit = 0;
        critProbability = 0;
        damagePercentage = 0;
    }

    public static void putCache(String playerName, PlayerSuitCache cache) {
        cacheHashMap.put(playerName, cache);
    }

    public static PlayerSuitCache getPlayerCacheByName(String playerName) {
        return cacheHashMap.get(playerName);
    }
}
