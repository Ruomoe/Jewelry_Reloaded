package org.yunshanmc.custom.jewelry;

import java.util.Map;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

public class ItemData {
    private final int health;

    private final int damage;

    private final int forgeRate;

    private final Map<PotionEffectType, Integer> effects;

    private final ItemStack item;

    public ItemData(int health, int damage, int forgeRate, Map<PotionEffectType, Integer> effects, ItemStack item) {
        this.health = health;
        this.damage = damage;
        this.forgeRate = forgeRate;
        this.effects = effects;
        this.item = item;
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

    public ItemStack getItem() {
        return this.item;
    }
}
