package org.yunshanmc.custom.prefix.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.yunshanmc.custom.prefix.PlayerPrefixData;
import org.yunshanmc.custom.prefix.Prefix;

public class PrefixCommands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(label.equalsIgnoreCase("mtpre")){
            if(args.length == 1){
                String path = args[0];
                if(path.equalsIgnoreCase("look")){
                    Player player = (Player)sender;
                    int nowIndex = 1;
                    PlayerPrefixData data = PlayerPrefixData.getPlayerDataByName(player.getName());
                    player.sendMessage("§a#==================#");
                    player.sendMessage("§b你当前已拥有的称号：");
                    for(Prefix prefix : data.getPrefixList()){
                        player.sendMessage(nowIndex + "- " + prefix.getConfigId());
                        nowIndex++;
                    }
                    player.sendMessage("§c使用/mtpre use 称号id来使用称号");
                    player.sendMessage("§a#==================#");
                    return true;
                }
            }
            if(args.length == 2){
                String path = args[0];
                if(path.equalsIgnoreCase("use")){
                    Player player = (Player)sender;
                    String needUse = args[1];
                    PlayerPrefixData data = PlayerPrefixData.getPlayerDataByName(player.getName());
                    for(Prefix prefix : data.getPrefixList()){
                        if(prefix.getConfigId().equals(needUse)){
                            data.setUsing(needUse);
                            player.sendMessage("§a更换完成.");
                            return true;
                        }
                    }
                    player.sendMessage("§c你没有 §b" + needUse);
                    return true;
                }
            }
        }
        return true;
    }
}
