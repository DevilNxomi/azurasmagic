package nl.Naomiora.privateproject.playermodule.listener;

import nl.Naomiora.privateproject.playermodule.PlayerModule;
import nl.Naomiora.privateproject.playermodule.data.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if(!PlayerModule.getInstance().getPlayerData().containsKey(player.getUniqueId()))
            PlayerModule.getInstance().getPlayerData().put(player.getUniqueId(), new PlayerData(player));
    }
}