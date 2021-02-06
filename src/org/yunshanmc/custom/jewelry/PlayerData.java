package org.yunshanmc.custom.jewelry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.yunshanmc.custom.buff.BuffPlayerData;
import org.yunshanmc.custom.collect.data.PlayerCollectData;
import org.yunshanmc.custom.prefix.PlayerPrefixData;
import org.yunshanmc.custom.suit.PlayerSuitCache;

public class PlayerData {
    private volatile String playerName;

    private volatile int health;

    private volatile int damage;

    private volatile int forgeRate;

    private volatile List<PotionEffect> effects = new ArrayList<>();

    private final Map<PotionEffectType, Integer> effectMap = new HashMap<>(4);

    private volatile List<ItemStack> items = new ArrayList<>();

    private final Map<String, InventoryData> invs = new HashMap<>();
    public PlayerData(String playerName){
        this.playerName = playerName;
    }
    public void setInventoryData(String invName, InventoryData newData) {
        synchronized (this.invs) {
            InventoryData oldData = this.invs.put(invName, newData);
            if (oldData != null) {
                this.health -= oldData.getHealth();
                this.damage -= oldData.getDamage();
                this.forgeRate -= oldData.getForgeRate();
                this.items.removeAll(oldData.getItems());
            }
            this.health += newData.getHealth();
            this.damage += newData.getDamage();
            this.forgeRate += newData.getForgeRate();
            this.items.addAll(newData.getItems());
            boolean oldEffects = (oldData != null && !oldData.getEffects().isEmpty());
            boolean newEffects = !newData.getEffects().isEmpty();
            if (oldEffects || newEffects) {
                this.effectMap.clear();
                for (InventoryData data : this.invs.values()) {
                    for (Map.Entry<PotionEffectType, Integer> entry : data.getEffects().entrySet())
                        this.effectMap.compute(entry.getKey(), (type, lvl) -> (lvl == null) ? (Integer)entry.getValue() : ((lvl.intValue() >= ((Integer)entry.getValue()).intValue()) ? lvl : (Integer)entry.getValue()));
                }
                List<PotionEffect> effects = new ArrayList<>();
                for (Map.Entry<PotionEffectType, Integer> entry : this.effectMap.entrySet())
                    effects.add(new PotionEffect(entry.getKey(), 9999999, ((Integer)entry.getValue()).intValue()));
                this.effects = effects;
            }
        }
    }

    public int getHealth() {
        int temp = BuffPlayerData.getPlayerData(playerName) != null ? this.health + BuffPlayerData.getPlayerData(playerName).getAddHealth() : this.health;
        int temp2 = PlayerPrefixData.getPlayerDataByName(playerName) != null ? PlayerPrefixData.getPlayerDataByName(playerName).getAddHealth() : 0;
        temp += temp2;
        temp += PlayerSuitCache.getPlayerCacheByName(playerName) != null ? PlayerSuitCache.getPlayerCacheByName(playerName).getAddHealth() : 0;
        return PlayerCollectData.getDataByName(playerName) != null ? temp + PlayerCollectData.getDataByName(playerName).addHealths : temp;
    }

    public int getDamage() {
        //Bukkit.getPlayer(playerName).sendMessage("你当前是否可获取攻击力 " + (BuffPlayerData.getPlayerData(playerName) != null));
        //if(BuffPlayerData.getPlayerData(playerName) != null) {
        //   Bukkit.getPlayer(playerName).sendMessage("你当前攻击力加成 " + BuffPlayerData.getPlayerData(playerName).getAddDamage());
        //    Bukkit.getPlayer(playerName).sendMessage("当前getDamage 返回 " + (BuffPlayerData.getPlayerData(playerName) != null ? this.damage + BuffPlayerData.getPlayerData(playerName).getAddDamage() : this.damage));
        //}
        int temp = BuffPlayerData.getPlayerData(playerName) != null ? this.damage + BuffPlayerData.getPlayerData(playerName).getAddDamage() : this.damage;
        int temp2 = PlayerPrefixData.getPlayerDataByName(playerName) != null ? PlayerPrefixData.getPlayerDataByName(playerName).getAddDamage() : 0;
        temp += temp2;
        temp += PlayerSuitCache.getPlayerCacheByName(playerName) != null ? PlayerSuitCache.getPlayerCacheByName(playerName).getAddDamage() : 0;
        return PlayerCollectData.getDataByName(playerName) != null ? temp + PlayerCollectData.getDataByName(playerName).addDamages : temp;
    }

    public int getForgeRate() {
        int temp = forgeRate;
        temp += PlayerSuitCache.getPlayerCacheByName(playerName) != null ? PlayerSuitCache.getPlayerCacheByName(playerName).getAddForge() : 0;
        return BuffPlayerData.getPlayerData(playerName) != null ? temp + BuffPlayerData.getPlayerData(playerName).getAddForge() : temp;
    }

    public List<PotionEffect> getEffects() {
        return this.effects;
    }

    public List<ItemStack> getItems() {
        return this.items;
    }
}
