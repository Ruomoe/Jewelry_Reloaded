package org.yunshanmc.custom.buff;

import java.util.HashMap;
import java.util.List;

public class BuffPackage {
    private static HashMap<String,BuffPackage> packageHashMap = new HashMap<>();
    private String name;
    private List<Buff> buffs;
    public BuffPackage(String name,List<Buff> buffs){
        this.name = name;
        this.buffs = buffs;
    }
    public String getName() {
        return name;
    }

    public List<Buff> getBuffs() {
        return buffs;
    }

    public static void putPackage(BuffPackage buffPackage){
        packageHashMap.put(buffPackage.getName(),buffPackage);
    }
    public static BuffPackage getPackage(String name){
        return packageHashMap.get(name);
    }
    public BuffPackage clone(){
        return new BuffPackage(this.name,this.buffs);
    }
}
