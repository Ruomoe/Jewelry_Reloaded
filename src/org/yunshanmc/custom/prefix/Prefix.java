package org.yunshanmc.custom.prefix;

import java.util.HashMap;

public class Prefix {
    private String configId;
    private String display;
    private HashMap<String,Integer> attrMap;
    private boolean needUse;
    public Prefix(String configId, String display, HashMap<String,Integer> map,boolean use){
        this.configId = configId;
        this.display = display;
        this.attrMap = map;
        this.needUse = use;
    }
    public Prefix clone(){
        return new Prefix(configId,display,attrMap,needUse);
    }

    public void addAttr(String key,int data){
        attrMap.put(key,data);
    }
    public boolean isNeedUse() {
        return needUse;
    }

    public String getConfigId() {
        return configId;
    }

    public HashMap<String, Integer> getAttrMap() {
        return attrMap;
    }

    public String getDisplay() {
        return display;
    }
}
