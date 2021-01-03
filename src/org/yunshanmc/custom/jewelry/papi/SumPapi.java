package org.yunshanmc.custom.jewelry.papi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.yunshanmc.custom.jewelry.Jewelry;
import org.yunshanmc.custom.jewelry.PlayerData;

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
        return "Error";
    }
}
