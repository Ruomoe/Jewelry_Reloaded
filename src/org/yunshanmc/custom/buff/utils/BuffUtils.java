package org.yunshanmc.custom.buff.utils;

import com.sun.media.jfxmedia.events.BufferListener;
import org.bukkit.Particle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yunshanmc.custom.buff.Buff;
import org.yunshanmc.custom.buff.BuffPackage;
import org.yunshanmc.custom.buff.listener.BuffPlayerListener;
import org.yunshanmc.custom.jewelry.Jewelry;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BuffUtils {
    public static void update(){
        File file = new File(Jewelry.root,"config.yml");
        if(file.exists()){
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            BuffPlayerListener.absorbBloodStr = config.getString("absorbBlood");
            for(String path : config.getConfigurationSection("buff").getKeys(false)){
                Buff buff = new Buff(path,0);
                Buff.putBuffDisplayName(config.getString("buff." + path + ".display"),buff);
            }
            for(String name : config.getConfigurationSection("buffPackage").getKeys(false)){
                List<Buff> buffs = new ArrayList<>();
                for(String buffName : config.getConfigurationSection("buffPackage").getConfigurationSection(name).getKeys(false)){
                    Buff buff = Buff.getBuffByName(Buff.getBuffNameByDisplay(buffName)).clone();
                    if(buff != null){
                        buff.setNeedOnline(false);
                        buff.setData(config.getInt("buffPackage." + name + "." + buffName));
                        buffs.add(buff);
                    }
                }
                BuffPackage buffPackage = new BuffPackage(name,buffs);
                BuffPackage.putPackage(buffPackage);
            }
        }
    }
}
