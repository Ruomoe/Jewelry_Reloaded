package org.yunshanmc.custom.jewelry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

public class InventoryData {
    private final int health;

    private final int damage;

    private final int forgeRate;

    private final Map<PotionEffectType, Integer> effects;

    private final List<ItemStack> items;

    public InventoryData(int health, int damage, int forgeRate, Map<PotionEffectType, Integer> effects, List<ItemStack> items) {
        this.health = health;
        this.damage = damage;
        this.forgeRate = forgeRate;
        this.effects = effects;
        this.items = items;
    }

    public static InventoryData build(List<ItemData> datas) {
        int health = 0;
        int damage = 0;
        int forgeRate = 0;
        Map<PotionEffectType, Integer> effects = new HashMap<>(4);
        List<ItemStack> items = new ArrayList<>();
        for (ItemData data : datas) {
            health += data.getHealth();
            damage += data.getDamage();
            forgeRate += data.getForgeRate();
            for (Map.Entry<PotionEffectType, Integer> entry : data.getEffects().entrySet())
                effects.compute(entry.getKey(), (type, lvl) -> (lvl == null) ? (Integer)entry.getValue() : ((lvl.intValue() >= ((Integer)entry.getValue()).intValue()) ? lvl : (Integer)entry.getValue()));
            items.add(data.getItem());
        }
        return new InventoryData(health, damage, forgeRate, effects, items);
    }

    public int getHealth() {
        return this.health;
    }

    public int getDamage() {
        return this.damage;
    }

    public int getForgeRate() {
        return this.forgeRate;
    }

    public Map<PotionEffectType, Integer> getEffects() {
        return this.effects;
    }

    public List<ItemStack> getItems() {
        return this.items;
    }
}
