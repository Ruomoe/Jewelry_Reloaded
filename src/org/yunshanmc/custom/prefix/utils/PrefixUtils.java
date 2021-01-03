package org.yunshanmc.custom.prefix.utils;


import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yunshanmc.custom.jewelry.Jewelry;
import org.yunshanmc.custom.prefix.Prefix;

import java.io.File;
import java.util.HashMap;

public class PrefixUtils {
    private static HashMap<String, Prefix> prefixHashMap = new HashMap<>();


    public static void putPrefix(String configId,Prefix prefix){
        prefixHashMap.put(configId,prefix);
    }
    public static Prefix getPrefixByConfigId(String configId){
        return prefixHashMap.get(configId);
    }


    public static void update(){
        File file = new File(Jewelry.root,"config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        for(String configId : config.getConfigurationSection("prefix").getKeys(false)){
            Prefix prefix = new Prefix(configId,config.getString("prefix." + configId + ".display"),new HashMap<>(),config.getBoolean("prefix." + configId + ".use"));
            for(String attrKey : config.getConfigurationSection("prefix").getConfigurationSection(configId).getConfigurationSection("attr").getKeys(false)){
                prefix.addAttr(attrKey,config.getInt("prefix." + configId + ".attr." + attrKey));
            }
            PrefixUtils.putPrefix(configId,prefix);
        }
    }
}
