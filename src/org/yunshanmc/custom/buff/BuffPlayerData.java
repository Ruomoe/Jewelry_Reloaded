package org.yunshanmc.custom.buff;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class BuffPlayerData {
    private static HashMap<String,BuffPlayerData> playerDataMap = new HashMap<>();

    private Player player;
    private HashMap<String,Integer> buffMap = new HashMap<>();
    private HashMap<String,Buff> buffs = new HashMap<>();
    private int addDamage = 0;
    private int addForge = 0;
    private int addHealth = 0;
    private int addExp = 0;

    public BuffPlayerData(Player player){
        this.player = player;
    }

    public void updateBuffTime(){
        addDamage = 0;
        addExp = 0;
        addForge = 0;
        addHealth = 0;
        Iterator<String> it = buffMap.keySet().iterator();
        while(it.hasNext()){
            String buffName = it.next();
            if(buffMap.get(buffName) - 1 < 0){
                buffMap.remove(buffName);
                buffs.remove(Buff.getBuffNameByOName(buffName));
            }else{
                //player.sendMessage("buffName " + buffName);
                //player.sendMessage("buffget " + Buff.getBuffNameByOName(buffName));
                if(buffs.get(Buff.getBuffNameByOName(buffName)).isNeedOnline()){
                    if(player != null && player.isOnline()){
                        buffMap.put(buffName,buffMap.get(buffName) - 1);
                    }
                }else {
                    buffMap.put(buffName, buffMap.get(buffName) - 1);
                }
            }
        }
        for(Buff buff : buffs.values()){
            String keyName = buff.getName();
            if(keyName.equals("damage")){
                addDamage = buff.getData();
                //player.sendMessage("你当前拥有 " + Buff.getBuffNameByDisplay("damage") + " " + buff.getData());
            }else if(keyName.equals("health")){
                addHealth = buff.getData();
            }else if(keyName.equals("forge")){
                addForge = buff.getData();
            }else if(keyName.equals("expPlus")){
                addExp = buff.getData();
            }
        }
    }

    public int getAddDamage() {
        return addDamage;
    }
    public int getAddForge() {
        return addForge;
    }
    public int getAddHealth(){
        return addHealth;
    }
    public int getAddExp(){
        return addExp;
    }
    public Player getPlayer(){
        return player;
    }
    public Set<String> getNowHasBuff(){
        return buffMap.keySet();
    }
    public void addBuff(Buff buff,int time){
        buffMap.put(Buff.getBuffNameByDisplay(buff.getName()),time);
        buffs.put(buff.getName(),buff);
    }
    public void removeBuff(Buff buff){
        buffMap.remove(Buff.getBuffNameByDisplay(buff.getName()));
        buffs.remove(buff.getName());
    }

    public static void addPlayerData(String playerName,BuffPlayerData data){
        playerDataMap.put(playerName,data);
    }
    public static BuffPlayerData getPlayerData(String playerName){
        return playerDataMap.get(playerName);
    }
}
