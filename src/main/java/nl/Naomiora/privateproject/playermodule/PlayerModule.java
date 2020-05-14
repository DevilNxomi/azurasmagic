package nl.Naomiora.privateproject.playermodule;

import lombok.Data;
import lombok.Getter;
import nl.Naomiora.privateproject.PrivateProject;
import nl.Naomiora.privateproject.playermodule.commands.AzurasMagicCommand;
import nl.Naomiora.privateproject.playermodule.commands.BindCommand;
import nl.Naomiora.privateproject.playermodule.commands.UnbindCommand;
import nl.Naomiora.privateproject.playermodule.data.PlayerData;
import nl.Naomiora.privateproject.playermodule.listener.FireworkDamage;
import nl.Naomiora.privateproject.playermodule.listener.PlayerInteract;
import nl.Naomiora.privateproject.playermodule.listener.PlayerJoin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

@Data
public class PlayerModule {
    @Getter
    private static PlayerModule instance;
    private HashMap<UUID, PlayerData> playerData = new HashMap<>();

    public PlayerModule() {
        instance = this;

        Bukkit.getServer().getPluginManager().registerEvents(new PlayerJoin(), PrivateProject.getInstance());
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerInteract(), PrivateProject.getInstance());
        Bukkit.getServer().getPluginManager().registerEvents(new FireworkDamage(), PrivateProject.getInstance());
        Bukkit.getPluginCommand("bindall").setExecutor(new BindCommand());
        Bukkit.getPluginCommand("bind").setExecutor(new BindCommand());
        Bukkit.getPluginCommand("unbindall").setExecutor(new UnbindCommand());
        Bukkit.getPluginCommand("unbind").setExecutor(new UnbindCommand());
        Bukkit.getPluginCommand("am").setExecutor(new AzurasMagicCommand());

        for(Player player : Bukkit.getOnlinePlayers()) {
            this.playerData.put(player.getUniqueId(), new PlayerData((player)));
        }
    }
}
