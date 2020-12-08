package org.yunshanmc.custom.jewelry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.yunshanmc.custom.buff.BuffPlayerData;

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
        return BuffPlayerData.getPlayerData(playerName) != null ? this.health + BuffPlayerData.getPlayerData(playerName).getAddHealth() : this.health;
    }

    public int getDamage() {
        return BuffPlayerData.getPlayerData(playerName) != null ? this.damage + BuffPlayerData.getPlayerData(playerName).getAddDamage() : this.damage;
    }

    public int getForgeRate() {
        return BuffPlayerData.getPlayerData(playerName) != null ? this.forgeRate + BuffPlayerData.getPlayerData(playerName).getAddForge() : this.forgeRate;
    }

    public List<PotionEffect> getEffects() {
        return this.effects;
    }

    public List<ItemStack> getItems() {
        return this.items;
    }
}
