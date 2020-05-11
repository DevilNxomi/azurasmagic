package nl.Naomiora.privateproject;

import lombok.Getter;
import nl.Naomiora.privateproject.Utils.TempBlock;
import nl.Naomiora.privateproject.Utils.TempBlockCreation;
import nl.Naomiora.privateproject.Utils.TempChunk;
import nl.Naomiora.privateproject.playermodule.PlayerModule;
import nl.Naomiora.privateproject.wandsmodule.WandsModule;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class PrivateProject extends JavaPlugin implements Listener {
    @Getter
    public static PrivateProject instance;
    @Getter
    private final String prefix = "&c[&5AzurasMagic&c]&f ";

    @Override
    public void onEnable() {
        instance = this;

        this.getConfig().addDefault("last-id", 0);
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
        new WandsModule();
        new PlayerModule();
    }

    @Override
    public void onDisable() {
        System.out.println("Reverting changes");
        TempBlockCreation.revertAll();
        TempBlock.removeAll();
        TempChunk.revertAll();
        System.out.println("Reverted changes correctly");
        this.getConfig().set("last-id", WandsModule.getInstance().getLastId());
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
    }
}