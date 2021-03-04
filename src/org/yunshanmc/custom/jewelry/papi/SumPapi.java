package org.yunshanmc.custom.jewelry.papi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.yunshanmc.custom.jewelry.Jewelry;
import org.yunshanmc.custom.jewelry.PlayerData;
import org.yunshanmc.custom.suit.PlayerSuitCache;
import org.yunshanmc.custom.suit.utils.SuitUtils;

public class SumPapi extends PlaceholderExpansion {
    public boolean canRegister() {
        return Bukkit.getPluginManager().getPlugin(getPlugin()) != null;
    }

    public String getAuthor() {
        return "CanYi";
    }

    public String getIdentifier() {
        return "mtsx";
    }

    public String getPlugin() {
        return "Jewelry";
    }

    public String getVersion() {
        return "1.0.0";
    }
    public String onPlaceholderRequest(Player player, String s) {
        PlayerData data = Jewelry.getInstance().getPlayerManager().getData(player);
        if(s.equalsIgnoreCase("damage")){
            return "" + data.getDamage();
        }else if(s.equalsIgnoreCase("health")){
            return "" + data.getHealth();
        }else if(s.equalsIgnoreCase("forge")){
            return "" + data.getForgeRate();
        }
        PlayerSuitCache cache = PlayerSuitCache.getPlayerCacheByName(player.getName());
        if(cache != null){
            if(s.equalsIgnoreCase("armor")) return "" + cache.getArmor();
            else if(s.equalsIgnoreCase("takeDamage")) return "" + cache.getTakeDamage();
            else if(s.equalsIgnoreCase("dodgeProbability")) return "" + cache.getDodgeProbability();
            else if(s.equalsIgnoreCase("dodge")) return "" + SuitUtils.dodge;
            else if(s.equalsIgnoreCase("reflexProbability")) return "" + cache.getReflexProbability();
            else if(s.equalsIgnoreCase("reflex")) return "" + SuitUtils.reflex;
            else if(s.equalsIgnoreCase("angryProbability")) return "" + cache.getAngryProbability();
            else if(s.equalsIgnoreCase("angry")) return "" + SuitUtils.angry;
            else if(s.equalsIgnoreCase("critProbability")) return "" + cache.getCritProbability();
            else if(s.equalsIgnoreCase("crit")) return "" + cache.getCrit();
            else if(s.equalsIgnoreCase("damage-percentage")) return "" + cache.getDamagePercentage();
            else if(s.equalsIgnoreCase("absorb-percentage")) return "" + cache.getAbsorbPercentage();
            else if(s.equalsIgnoreCase("absorb")) return "" + cache.getAbsorb() * 10;
        }
        return "Error";
    }
}
