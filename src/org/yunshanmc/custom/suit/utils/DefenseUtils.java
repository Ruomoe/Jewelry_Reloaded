package org.yunshanmc.custom.suit.utils;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.yunshanmc.custom.suit.PlayerSuitCache;

public class DefenseUtils {
    public static void byDamage(EntityDamageByEntityEvent event, Player player){
        PlayerSuitCache cache = PlayerSuitCache.getPlayerCacheByName(player.getName());
        if(cache != null){
            double damage = event.getDamage();
            if(cache.getArmor() != 0) damage = damage - damage * (cache.getArmor() / cache.getArmor() + 100);
            damage -= cache.getTakeDamage();

            if(cache.getDodgeProbability() > SuitUtils.getProbability()){
                player.sendMessage("§3§l闪避!");
                damage -= SuitUtils.dodge;
            }
            if(cache.getReflexProbability() > SuitUtils.getProbability()){
                player.sendMessage("§6§l反伤!");
                Entity entity = event.getEntity();
                Damageable damageable = (Damageable) entity;
                damageable.damage(SuitUtils.reflex,player);
                if(entity instanceof Player) ((Player)entity).sendMessage("§6§l被反伤!");
            }
            if(cache.getAngryProbability() > SuitUtils.getProbability()){
                player.sendMessage("§c§l怒气爆发!");
                double health = player.getHealth() + SuitUtils.angry;
                if(health > player.getMaxHealth()){
                    player.setHealth(player.getMaxHealth());
                }else{
                    player.setHealth(health);
                }
            }
            if(damage < 0) event.setDamage(0);
            else event.setDamage(damage);
        }
    }
}
