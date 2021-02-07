package org.yunshanmc.custom.suit.utils;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yunshanmc.custom.jewelry.Jewelry;
import org.yunshanmc.custom.suit.Suit;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SuitUtils {
    private static final Pattern pattern = Pattern.compile("\\d+(\\.\\d)?");
    public static int dodge;
    public static int reflex;
    public static int angry;
    public static void update(){
        File file = new File(Jewelry.root,"config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        dodge = config.getInt("dodge");
        reflex = config.getInt("reflex");
        angry = config.getInt("angry");

        for(String suitName : config.getConfigurationSection("suit").getKeys(false)){
            int need = config.getInt("suit." + suitName + ".need");
            String[] keys = new String[need];
            List<String> list = config.getStringList("suit." + suitName + ".names");
            for(String string : list){
                keys[list.indexOf(string)] = string;
            }
            HashMap<Integer,List<String>> attrMap = new HashMap<>();
            for(int i = 2; i <= need; i++){
                if(config.get("suit." + suitName + "." + i) != null){
                    attrMap.put(i,config.getStringList("suit." + suitName + "." + i));
                }
            }
            Suit suit = new Suit(suitName,attrMap,need,keys);
            Suit.putSuit(suit,suitName);
        }

        for(String path : config.getConfigurationSection("message").getKeys(false)){
            MessageUtils.putMessage(path,config.getString("message." + path).replace("&","§"));
        }
    }
    public static double getHas(String lore){
        Matcher matcher = pattern.matcher(ChatColor.stripColor(lore));
        if(matcher.find()){
            return Double.parseDouble(matcher.group());
        }else{
            return 0.0;
        }
    }
    public static int getProbability(){
        return new Random().nextInt(101);
    }
}
