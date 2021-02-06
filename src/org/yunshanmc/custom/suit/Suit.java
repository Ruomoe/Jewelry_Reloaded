package org.yunshanmc.custom.suit;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class Suit {
    private static HashMap<String,Suit> suitHashMap = new HashMap<>();

    private String suitName;
    private int need;
    private HashMap<Integer, List<String>> attrMap;
    private String[] keys;

    public Suit(String suitName,HashMap<Integer,List<String>> attrMap,int need,String[] keys){
        this.suitName = suitName;
        this.attrMap = attrMap;
        this.need = need;
        this.keys = keys;
    }
    public int getNeed() {
        return need;
    }

    public String getSuitName() {
        return suitName;
    }

    public HashMap<Integer, List<String>> getAttrMap() {
        return attrMap;
    }

    public String[] getKeys() {
        return keys;
    }

    public static void putSuit(Suit suit, String suitName){
        suitHashMap.put(suitName,suit);
    }
    public static Suit getSuitByName(String suitName){
        return suitHashMap.get(suitName);
    }
    public static Collection<Suit> getSuits(){
        return suitHashMap.values();
    }
}
