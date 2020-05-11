package nl.Naomiora.privateproject.wandsmodule;

import lombok.Data;
import lombok.Getter;
import nl.Naomiora.privateproject.PrivateProject;
import nl.Naomiora.privateproject.Utils.TempBlock;
import nl.Naomiora.privateproject.Utils.TempBlockCreation;
import nl.Naomiora.privateproject.Utils.TempChunk;
import nl.Naomiora.privateproject.wandsmodule.cooldowns.Cooldown;
import nl.Naomiora.privateproject.wandsmodule.wands.IceSpells.IceInvasion;
import nl.Naomiora.privateproject.wandsmodule.wands.interfaces.SomeWand;
import nl.Naomiora.privateproject.wandsmodule.wands.listeners.ProjectileHit;
import nl.Naomiora.privateproject.wandsmodule.wands.loader.WandsLoader;
import org.bukkit.Bukkit;

import java.util.HashMap;

@Data
public class WandsModule {
    @Getter
    public static WandsModule instance;
    private WandsLoader wandsLoader;
    private HashMap<Integer, SomeWand> allUsingWands = new HashMap<>();
    private int lastId = PrivateProject.getInstance().getConfig().getInt("last-id");

    public WandsModule() {
        instance = this;
        this.wandsLoader = new WandsLoader();

        TempBlock.startReversion();
        TempChunk.startReversion();
        Cooldown.startCheck();
        IceInvasion.startCheck();

        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(PrivateProject.getInstance(), TempBlockCreation::manage, 0L, 1L);
        Bukkit.getServer().getPluginManager().registerEvents(new ProjectileHit(), PrivateProject.getInstance());
    }
}
