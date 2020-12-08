package org.yunshanmc.custom.buff.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yunshanmc.custom.buff.Buff;
import org.yunshanmc.custom.jewelry.Jewelry;

import java.io.File;

public class BuffUtils {
    public static void update(){
        File file = new File(Jewelry.root,"config.yml");
        if(file.exists()){
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            for(String path : config.getConfigurationSection("buff").getKeys(false)){
                Buff buff = new Buff(path,0);
                Buff.putBuffDisplayName(config.getString("buff." + path + ".display"),buff);
            }
        }
    }
}
