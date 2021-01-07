package org.yunshanmc.custom.prefix.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.yunshanmc.custom.prefix.PlayerPrefixData;
import org.yunshanmc.custom.prefix.Prefix;
import org.yunshanmc.custom.prefix.utils.PrefixUtils;

import javax.persistence.PreUpdate;

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
                if(path.equalsIgnoreCase("unuse")){
                    Player player = (Player)sender;
                    PlayerPrefixData data = PlayerPrefixData.getPlayerDataByName(player.getName());
                    data.setUsing(null);
                    player.sendMessage("解除完成.");
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
                if(path.equalsIgnoreCase("info")){
                    //称号属性 是否需要使用 总加成 未写
                }
            }
            if(args.length == 3){
                if(args[0].equalsIgnoreCase("give")){
                    String playerName = args[1];
                    String configId = args[2];
                    if(PrefixUtils.getPrefixByConfigId(configId) == null){
                        sender.sendMessage("§c没有这个称号.");
                        return true;
                    }
                    Prefix prefix = PrefixUtils.getPrefixByConfigId(configId).clone();
                    if(Bukkit.getPlayer(playerName) == null || !Bukkit.getPlayer(playerName).isOnline()){
                        sender.sendMessage("§c玩家不存在或不在线.");
                        return true;
                    }
                    PlayerPrefixData data = PlayerPrefixData.getPlayerDataByName(playerName);
                    data.addPrefix(prefix);
                    sender.sendMessage("§a添加完成.");
                    return true;
                }
            }
        }
        return true;
    }
}
