package org.yunshanmc.custom.collect.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.yunshanmc.custom.collect.CollectItem;
import org.yunshanmc.custom.collect.data.PlayerCollectData;
import org.yunshanmc.custom.collect.gui.CollectItemGui;

public class CollectCommands implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(label.equalsIgnoreCase("tj")){
            if(args.length == 0){
                if(sender instanceof Player){
                    Player player = (Player)sender;
                    player.openInventory(CollectItemGui.getShowInv(player));
                    return true;
                    /*
                    String playerName = player.getName();
                    PlayerCollectData data = PlayerCollectData.getDataByName(playerName);
                    player.sendMessage("§a当前拥有图鉴:");
                    for (String packageName : CollectPackage.getAllCollectPackageName()) {
                        CollectPackage collectPackage = CollectPackage.getCollectPackageByName(packageName);
                        boolean isFull = true;
                        for (CollectItem item : collectPackage.getNeedCollectItemList()) {
                            if (!data.getAllCollects().contains(item)) {
                                isFull = false;
                            }
                        }
                       if(isFull){
                           player.sendMessage("§b完整收集: §6" + packageName);
                       }else{
                           player.sendMessage("§b未完整收集（下方列出未收集图鉴）: §6" + packageName);
                           for (CollectItem item : collectPackage.getNeedCollectItemList()) {
                               if (!data.getAllCollects().contains(item)) {
                                   player.sendMessage("§6" + item.getName());
                               }
                           }
                       }
                    }
                    return true;

                     */
                }
            }else if(args.length == 3){
                if(args[0].equalsIgnoreCase("add")){
                    String playerName = args[1];
                    String itemName = args[2];
                    if(PlayerCollectData.getDataByName(playerName) == null){
                        sender.sendMessage("玩家不存在.");
                    }
                    if(CollectItem.getDisplay(itemName) == null){
                        sender.sendMessage("图鉴不存在");
                    }
                    CollectItem item = CollectItem.getItem(CollectItem.getDisplay(itemName));
                    PlayerCollectData data = PlayerCollectData.getDataByName(playerName);
                    data.addCollectItem(item);
                    sender.sendMessage("§a为 §b" + playerName + " 添加 §6"+ item.getName() + "§a 成功.");
                    return true;
                }
            }
        }
        return true;
    }
}
