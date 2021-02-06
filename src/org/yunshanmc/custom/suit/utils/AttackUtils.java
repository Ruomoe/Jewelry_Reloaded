package org.yunshanmc.custom.suit.utils;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.yunshanmc.custom.suit.PlayerSuitCache;

public class AttackUtils {
    public static void onDamage(EntityDamageByEntityEvent event, Player player){
        PlayerSuitCache cache = PlayerSuitCache.getPlayerCacheByName(player.getName());
        if(cache != null){

        }
    }
}
