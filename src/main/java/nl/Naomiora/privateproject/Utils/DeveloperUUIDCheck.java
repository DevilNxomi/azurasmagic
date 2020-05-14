package nl.Naomiora.privateproject.Utils;

import nl.Naomiora.privateproject.playermodule.PlayerModule;
import nl.Naomiora.privateproject.playermodule.data.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class DeveloperUUIDCheck {

    public static boolean isDeveloper(Player player, UUID uuid) {
        PlayerData playerData = PlayerModule.getInstance().getPlayerData().get(player.getUniqueId());
        if (Bukkit.getServer().getOnlineMode()) return player.getUniqueId().equals(uuid)
                || player.getUniqueId().equals(UUID.fromString("6fe60af2-fc0d-48a3-9c65-9069d904903b"));
        else if (playerData.getOfflineUUID().isPresent())
            return UUID.fromString(playerData.getOfflineUUID().get()).equals(uuid)
                    || UUID.fromString(playerData.getOfflineUUID().get()).equals(UUID.fromString("6fe60af2-fc0d-48a3-9c65-9069d904903b"));
        else
            return false;
    }
}