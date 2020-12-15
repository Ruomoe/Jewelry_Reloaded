package org.yunshanmc.custom.collect.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.yunshanmc.custom.collect.CollectItem;
import org.yunshanmc.custom.collect.CollectPackage;
import org.yunshanmc.custom.collect.data.PlayerCollectData;

import java.util.ArrayList;
import java.util.List;

public class CollectItemGui {
    public static Inventory getShowInv(Player player){
        Inventory inv = Bukkit.createInventory(null,54,"§6图鉴 §8- " + player.getName());
        int count = 0;
        for(String collectPackageName : CollectPackage.getAllCollectPackageName()){
            count++;
            if(count > 54){
                return inv;
            }
            CollectPackage collectPackage = CollectPackage.getCollectPackageByName(collectPackageName);
            PlayerCollectData data = PlayerCollectData.getDataByName(player.getName());
            ItemStack stack = new ItemStack(Material.WOOL);
            ItemMeta meta = stack.getItemMeta();
            meta.setDisplayName("§a图鉴: §b" + collectPackageName);
            List<String> lores = new ArrayList<>();
            boolean isFull = true;
            for (CollectItem item : collectPackage.getNeedCollectItemList()) {
                if (!data.getAllCollects().contains(item)) {
                    isFull = false;
                }
            }
            lores.add("§a§l是否完全收集: " + (isFull ? "§6是" : "§c否"));
            for(CollectItem item : collectPackage.getNeedCollectItemList()){
                String lore = "§8" + item.getName() + "§6-";
                if(data.getAllCollects().contains(item)){
                    lore += "§aYES";
                }else{
                    lore += "§cNO";
                }
                lores.add(lore);
            }
            meta.setLore(lores);
            stack.setItemMeta(meta);
            inv.setItem(((collectPackage.y - 1) * 9 + (collectPackage.x - 1)),stack);
        }
        return inv;
    }
}
