package org.yunshanmc.custom.buff;

import java.util.HashMap;

public class Buff {
    private static HashMap<String,Buff> buffMap = new HashMap<>();
    private static HashMap<String,String> changeMap = new HashMap<>();
    public Buff(String keyName,int data){
        this.name = keyName;
    }
    private String name;
    private int data;
    private boolean needOnline;
    public String getName(){
        return name;
    }
    public int getData(){
        return data;
    }
    public void setData(int data){
        this.data = data;
    }
    public boolean isNeedOnline(){
        return needOnline;
    }
    public void setNeedOnline(boolean b){
        needOnline = b;
    }
    public static Buff getBuffByName(String buffName){
        return buffMap.get(buffName);
    }
    public static String getBuffNameByDisplay(String buffName){
        return changeMap.get(buffName);
    }
    public static void putBuffDisplayName(String buffDisName,Buff buff){
        buffMap.put(buffDisName,buff);
        changeMap.put(buff.getName(),buffDisName);
    }
    public Buff clone(){
        return new Buff(this.name,0);
    }
}
