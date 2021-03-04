package org.yunshanmc.custom.potion.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.yunshanmc.custom.potion.Potion;
import org.yunshanmc.custom.potion.data.PlayerPotionData;

public class PotionCommands implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(label.equalsIgnoreCase("mtys")) {
            if (args.length == 4) {
                String playerName = args[1];
                if(Bukkit.getPlayer(playerName) != null && Bukkit.getPlayer(playerName).isOnline()){
                    Player player = Bukkit.getPlayer(playerName);
                    String potionName = args[2];
                    Potion potion = Potion.getPotionByName(potionName).clone();
                    if(potion == null){
                        sender.sendMessage("药水不存在.");
                        return true;
                    }
                    int time = Integer.parseInt(args[3]);
                    PlayerPotionData data = PlayerPotionData.getPotionDataByPlayerName(playerName);
                    if(data != null){
                        data.addPotion(potion,time);
                        sender.sendMessage("添加完成.");
                        player.sendMessage("§3一瓶新的药水已生效.");
                    }else{
                        sender.sendMessage("玩家缓存不存在.请等待一会儿重试.");
                    }
                }else{
                    sender.sendMessage("Player isn't online.");
                    return true;
                }
            }
        }
        return true;
    }
}
