package org.yunshanmc.custom.collect.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yunshanmc.custom.collect.CollectItem;
import org.yunshanmc.custom.collect.CollectPackage;
import org.yunshanmc.custom.collect.data.PlayerCollectData;
import org.yunshanmc.custom.jewelry.Jewelry;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CollectUtils {
    public static void update(){
        File file = new File(Jewelry.root,"config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        for(String name : config.getConfigurationSection("collect").getKeys(false)){
            CollectPackage collectPackage = new CollectPackage(
                    config.getString("collect." + name + ".name")
                    ,config.getInt("collect." + name + ".x")
                    ,config.getInt("collect." + name + ".y")
                    ,config.getInt("collect." + name + ".finalAddDamage")
                    ,config.getInt("collect." + name + ".finalAddHealth"));

            for(String itemName : config.getConfigurationSection("collect." + name + ".include").getKeys(false)){
                CollectItem item = new CollectItem(
                        config.getString("collect." + name + ".include." + itemName + ".name")
                        ,config.getInt("collect." + name + ".include." + itemName + ".damage")
                        ,config.getInt("collect." + name + ".include." + itemName + ".health"));
                collectPackage.getNeedCollectItemList().add(item);
                CollectItem.putChangeMap(itemName,item.getName());
                CollectItem.putItem(item.getName(),item);
            }
            CollectPackage.putCollectPackage(config.getString("collect." + name + ".name"),collectPackage);
        }
    }
    public static void save(){
        File file = new File(Jewelry.root,"data.yml");
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        for(String playerName : PlayerCollectData.playerNames()){
            PlayerCollectData data = PlayerCollectData.getDataByName(playerName);
            for(CollectItem item : data.getAllCollects()) {
                config.set(playerName + "." +  item.getName() + ".damage",item.getAddDamage());
                config.set(playerName + "." + item.getName() + ".health",item.getAddHealth());
            }
        }
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void load(){
        File file = new File(Jewelry.root,"data.yml");
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        for(String playerName : config.getKeys(false)){
            System.out.println("正在加载 " + playerName);

            PlayerCollectData data = new PlayerCollectData(playerName,new ArrayList<>());
            for(String itemName : config.getConfigurationSection(playerName).getKeys(false)){
                System.out.println("---加载 " + itemName);
                CollectItem item = CollectItem.getItem(itemName);
                data.addCollectItem(item);
            }
            PlayerCollectData.putData(playerName,data);
        }
    }

}
