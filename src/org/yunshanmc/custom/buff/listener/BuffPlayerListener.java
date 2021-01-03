package org.yunshanmc.custom.buff.listener;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.yunshanmc.custom.buff.BuffPlayerData;
import org.yunshanmc.custom.prefix.PlayerPrefixData;
import org.yunshanmc.custom.prefix.Prefix;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BuffPlayerListener implements Listener {
    public static String absorbBloodStr;
    public static final Pattern pattern = Pattern.compile("\\d+(\\.\\d)?");
    @EventHandler
    public void getExp(PlayerExpChangeEvent event){
        Player player = event.getPlayer();
        BuffPlayerData playerData = BuffPlayerData.getPlayerData(player.getName());
        if(playerData != null){
            int data = PlayerPrefixData.getPlayerDataByName(player.getName()) != null
                    ? playerData.getAddExp() + PlayerPrefixData.getPlayerDataByName(player.getName()).getAddExpPlus()
                    : playerData.getAddExp();
            double point = data / 100.0;
            int exp = event.getAmount();
            exp *= 1 + point;
            event.setAmount(exp);
        }
    }
    @EventHandler
    public void click(InventoryClickEvent event){
        if(event.getWhoClicked() instanceof Player){
            String title = event.getInventory().getTitle();
            if(title.startsWith("§6图鉴")){
                event.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void damage(EntityDamageByEntityEvent event){
        Entity damager = event.getDamager();
        if(damager instanceof Player){
            Player player = (Player)damager;
            ItemStack stack = player.getItemInHand();
            if(stack != null && !stack.getType().equals(Material.AIR)){
                ItemMeta meta = stack.getItemMeta();
                if(meta.hasLore()){
                    double absorbBloodNumber = 0L;
                    for(String lore : meta.getLore()){
                        if(lore.contains(absorbBloodStr)){
                            absorbBloodNumber = getHas(lore);
                            break;
                        }
                    }
                    absorbBloodNumber *= 10;
                    if((player.getHealth() + absorbBloodNumber) > player.getMaxHealth()){
                        player.setHealth(player.getMaxHealth());
                    }else{
                        player.setHealth(player.getHealth() + absorbBloodNumber);
                    }
                }
            }
        }

    }
    public static double getHas(String lore){
        Matcher matcher = pattern.matcher(ChatColor.stripColor(lore));
        if(matcher.find()){
            return Double.parseDouble(matcher.group());
        }else{
            return 0.0;
        }
    }
}
