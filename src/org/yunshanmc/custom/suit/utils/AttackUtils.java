package org.yunshanmc.custom.suit.utils;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.yunshanmc.custom.suit.PlayerSuitCache;

public class AttackUtils {
    public static void onDamage(EntityDamageByEntityEvent event, Player player){
        PlayerSuitCache cache = PlayerSuitCache.getPlayerCacheByName(player.getName());
        if(cache != null){
            double damage = event.getDamage();
            if(cache.getDamagePercentage() != 0){
                damage += damage * (cache.getDamagePercentage() / 100);
            }
            if(cache.getCritProbability() != 0 && cache.getCritProbability() >= SuitUtils.getProbability()){
                damage = damage * (1 + cache.getCrit() / 100);
            }
            if(cache.getAbsorb() != 0){
                double health = player.getHealth() + cache.getAbsorb();
                if(health > player.getMaxHealth()){
                    player.setHealth(player.getMaxHealth());
                }else{
                    player.setHealth(health);
                }
            }
            if(cache.getAngryProbability() != 0){
                double health = player.getHealth() + damage * (cache.getAngryProbability() / 100);
                if(health > player.getMaxHealth()){
                    player.setHealth(player.getMaxHealth());
                }else{
                    player.setHealth(health);
                }
            }
        }
    }
}
