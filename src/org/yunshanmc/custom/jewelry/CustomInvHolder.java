package org.yunshanmc.custom.jewelry;

import java.util.HashMap;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class CustomInvHolder implements InventoryHolder {
    private final String name;

    private final String loreKey;

    private final AddonInvManager addonInvManager;

    private final Inventory inventory;

    public CustomInvHolder(String title, String name, String loreKey, AddonInvManager addonInvManager) {
        this.inventory = Bukkit.createInventory(this, 54, title);
        this.name = name;
        this.loreKey = loreKey;
        this.addonInvManager = addonInvManager;
    }

    public void giveBackInvalids(Player player, List<ItemStack> invalids) {
        HashMap<Integer, ItemStack> map = player.getInventory().addItem(invalids.<ItemStack>toArray(new ItemStack[0]));
        if (!map.isEmpty())
            for (ItemStack item : map.values())
                player.getWorld().dropItem(player.getLocation(), item);
    }

    public void save(Player player) {
        this.addonInvManager.saveInv(player, this.inventory, this.name);
    }

    public String getName() {
        return this.name;
    }

    public String getLoreKey() {
        return this.loreKey;
    }

    public Inventory getInventory() {
        return this.inventory;
    }
}
