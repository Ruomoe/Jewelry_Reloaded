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

            if(cache.getDodgeProbability() >= SuitUtils.getProbability()){
                player.sendMessage(MessageUtils.getMessage("dodge"));
                damage -= SuitUtils.dodge;
            }
            if(cache.getReflexProbability() >= SuitUtils.getProbability()){
                player.sendMessage(MessageUtils.getMessage("reflex"));
                Entity entity = event.getDamager();
                Damageable damageable = (Damageable) entity;
                damageable.damage(SuitUtils.reflex,player);
                if(entity instanceof Player) ((Player)entity).sendMessage(MessageUtils.getMessage("byReflex"));
            }
            if(cache.getAngryProbability() >= SuitUtils.getProbability()){
                player.sendMessage(MessageUtils.getMessage("angry"));
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