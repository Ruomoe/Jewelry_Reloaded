package org.yunshanmc.custom.buff.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.yunshanmc.custom.buff.Buff;
import org.yunshanmc.custom.buff.BuffPlayerData;


public class BuffCommands implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(label.equalsIgnoreCase("buff")){
            if(args.length == 1){
                if(args[0].equalsIgnoreCase("look")){
                    if(sender instanceof Player){
                        Player player = (Player)sender;
                        BuffPlayerData playerData = BuffPlayerData.getPlayerData(player.getName());
                        if(playerData != null){
                            if(playerData.getNowHasBuff().isEmpty()){
                                player.sendMessage("§a当前未拥有buff.");
                                return true;
                            }else{
                                player.sendMessage("§a当前拥有:");
                                for(String buffName : playerData.getNowHasBuff()){
                                    player.sendMessage("§8- §b" + buffName);
                                }
                                return true;
                            }
                        }else{
                            player.sendMessage("§a当前未拥有buff.");
                            return true;
                        }
                    }
                }
            }
            if(args.length == 5){
                String buffName = args[0];
                String worldName = args[1];
                int data = Integer.parseInt(args[2]);
                int time = Integer.parseInt(args[3]);
                boolean leave = Boolean.parseBoolean(args[4]);
                World world = Bukkit.getWorld(worldName);
                if(world == null){
                    sender.sendMessage("§c世界不存在");
                    return true;
                }
                for(Player player : world.getPlayers()){
                    BuffPlayerData playerData = BuffPlayerData.getPlayerData(player.getName());
                    if(playerData != null){
                        Buff buff = Buff.getBuffByName(Buff.getBuffNameByDisplay(buffName)).clone();
                        buff.setNeedOnline(leave);
                        buff.setData(data);
                        playerData.addBuff(buff,time);
                    }
                }
                sender.sendMessage("§aBuff发送成功.");
                return true;
            }
        }
        return true;
    }
}
