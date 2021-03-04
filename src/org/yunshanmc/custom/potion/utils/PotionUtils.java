package org.yunshanmc.custom.potion.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yunshanmc.custom.jewelry.Jewelry;
import org.yunshanmc.custom.potion.Potion;

import java.io.File;
import java.util.HashMap;

public class PotionUtils {
    public static void update(){
        File file = new File(Jewelry.root,"config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        for(String name : config.getConfigurationSection("potion").getKeys(false)){
            HashMap<String,Double> attrMap = new HashMap<>();
            for(String potionAttr : config.getConfigurationSection("potion").getConfigurationSection(name).getKeys(false)){
                attrMap.put(potionAttr,config.getDouble("potion." + name + "." + potionAttr));
            }
            Potion potion = new Potion(name,attrMap);
            Potion.putMap(name,potion);
        }
    }
}
