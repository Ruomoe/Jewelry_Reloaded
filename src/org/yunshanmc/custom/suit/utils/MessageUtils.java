package org.yunshanmc.custom.suit.utils;

import java.util.HashMap;

public class MessageUtils {
    private static HashMap<String,String> messageMap = new HashMap<>();

    public static void putMessage(String path, String message){
        messageMap.put(path,message);
    }
    public static String getMessage(String key){
        return messageMap.containsKey(key) ? messageMap.get(key) : "NotFind " + key;
    }
}
