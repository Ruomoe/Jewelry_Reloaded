package org.yunshanmc.custom.buff.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.yunshanmc.custom.buff.Buff;
import org.yunshanmc.custom.buff.BuffPackage;
import org.yunshanmc.custom.buff.BuffPlayerData;
import org.yunshanmc.custom.jewelry.Jewelry;


public class BuffCommands implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(label.equalsIgnoreCase("buff")){
            if(args.length == 1){
                if(args[0].equalsIgnoreCase("look")){
                    if(sender instanceof Player){
                        Player player = (Player)sender;
                        BuffPlayerData playerData = BuffPlayerData.getPlayerData(player.getName());
                        if(playerData != null){
                            for(BuffPackage buffPackage : playerData.getBuffPackageMap().values()){
                                player.sendMessage("§6" + buffPackage.getName());
                                for(Buff buff : buffPackage.getBuffs()){
                                    player.sendMessage("§8- §9" + Buff.getBuffNameByOName(buff.getName()) + "§c" + buff.getData());
                                }
                                player.sendMessage("§8- §e剩余时间§6" + playerData.getBuffPackageTime(buffPackage.getName()));
                            }
                            for(Buff buff : playerData.getBuffs()){
                                player.sendMessage("§8- §9" + Buff.getBuffNameByOName(buff.getName()) + "§c" + buff.getData());
                                player.sendMessage("§8- §e剩余时间§6" + playerData.getBuffTime(buff.getName()));
                            }
                        }else{
                            player.sendMessage("§a当前未拥有buff.");
                            return true;
                        }
                    }
                }
            }
            if(args.length == 5){
                if(args[0].equalsIgnoreCase("sendPackageW")){
                    String packageName = args[1];
                    String worldName = args[2];
                    int time = Integer.parseInt(args[3]);
                    int delay = Integer.parseInt(args[4]);
                    World world = Bukkit.getWorld(worldName);
                    if(world == null){
                        sender.sendMessage("§c世界不存在");
                        return true;
                    }
                    BuffPackage buffPackage = BuffPackage.getPackage(packageName).clone();
                    if(buffPackage != null){
                        Bukkit.getScheduler().runTaskLater(Jewelry.plugin,() -> {
                            for(Player player : world.getPlayers()){
                                BuffPlayerData playerData = BuffPlayerData.getPlayerData(player.getName());
                                if(playerData != null){
                                    playerData.addBuffPackage(buffPackage,time);
                                }
                            }
                        },delay * 20);
                    }
                    sender.sendMessage("§aBuff包发送成功.");
                }
            }
            if(args.length == 5){
                if(args[0].equalsIgnoreCase("sendPackageP")){
                    String packageName = args[1];
                    String playerName = args[2];
                    int time = Integer.parseInt(args[3]);
                    int delay = Integer.parseInt(args[4]);
                    Player player = Bukkit.getPlayer(playerName);
                    if(player == null || !player.isOnline()){
                        sender.sendMessage("§c玩家不存在或不在线");
                        return true;
                    }
                    BuffPackage buffPackage = BuffPackage.getPackage(packageName).clone();
                    if(buffPackage != null){
                        Bukkit.getScheduler().runTaskLater(Jewelry.plugin,() -> {
                            BuffPlayerData playerData = BuffPlayerData.getPlayerData(player.getName());
                            if(playerData != null){
                                playerData.addBuffPackage(buffPackage,time);
                            }
                        },delay * 20);
                    }
                    sender.sendMessage("§aBuff包发送成功.");
                }
            }
            if(args.length == 6){
                String buffName = args[0];
                String worldName = args[1];
                int data = Integer.parseInt(args[2]);
                int time = Integer.parseInt(args[3]);
                boolean leave = Boolean.parseBoolean(args[4]);
                int delay = Integer.parseInt(args[5]);
                World world = Bukkit.getWorld(worldName);
                if(world == null){
                    sender.sendMessage("§c世界不存在");
                    return true;
                }
                Bukkit.getScheduler().runTaskLater(Jewelry.plugin,() -> {
                    for(Player player : world.getPlayers()){
                        BuffPlayerData playerData = BuffPlayerData.getPlayerData(player.getName());
                        if(playerData != null){
                            Buff buff = Buff.getBuffByName(Buff.getBuffNameByDisplay(buffName)).clone();
                            buff.setNeedOnline(leave);
                            buff.setData(data);
                            playerData.addBuff(buff,time);
                        }
                    }
                },delay * 20);
                sender.sendMessage("§aBuff发送成功.");
                return true;
            }
        }
        return true;
    }
}
