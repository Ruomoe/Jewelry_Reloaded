package org.yunshanmc.custom.prefix.utils;


import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yunshanmc.custom.jewelry.Jewelry;
import org.yunshanmc.custom.prefix.PlayerPrefixData;
import org.yunshanmc.custom.prefix.Prefix;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    public static void load(){
        File file = new File(Jewelry.root,"prefixData.yml");
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        for(String playerName : config.getKeys(false)){
            PlayerPrefixData playerPrefixData = new PlayerPrefixData(playerName,new ArrayList<>());
            List<String> names = config.getStringList(playerName + ".has");
            for(String name : names){
                Prefix prefix = PrefixUtils.getPrefixByConfigId(name).clone();
                playerPrefixData.addPrefix(prefix);
            }
            playerPrefixData.setUsing(config.getString(playerName + ".using"));
        }
    }
    public static void save(){
        File file = new File(Jewelry.root,"prefixData.yml");
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        for(String playerName : PlayerPrefixData.players()){
            PlayerPrefixData data = PlayerPrefixData.getPlayerDataByName(playerName);
            List<String> names = new ArrayList<>();
            for(Prefix prefix : data.getPrefixList()){
                names.add(prefix.getConfigId());
            }
            config.set(playerName + ".has",names);
            config.set(playerName + ".using",data.getUsing());
        }
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
