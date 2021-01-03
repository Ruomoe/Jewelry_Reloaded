package org.yunshanmc.custom.prefix.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.yunshanmc.custom.prefix.PlayerPrefixData;
import org.yunshanmc.custom.prefix.utils.PrefixUtils;

public class PrefixListener implements Listener {
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        String format = event.getFormat();
        Player player = event.getPlayer();
        if(PlayerPrefixData.getPlayerDataByName(player.getName()) != null){
            PlayerPrefixData data = PlayerPrefixData.getPlayerDataByName(player.getName());
            if(data.getUsing() != null){
                event.setFormat(PrefixUtils.getPrefixByConfigId(data.getUsing()).getDisplay() + format);
            }
        }
    }
}
