package nl.Naomiora.privateproject.wandsmodule.wands.loader;

import lombok.Data;
import nl.Naomiora.privateproject.PrivateProject;
import nl.Naomiora.privateproject.api.SpellBase;
import nl.Naomiora.privateproject.api.WandBase;
import nl.Naomiora.privateproject.api.customevents.SpellLoadEvent;
import nl.Naomiora.privateproject.api.customevents.SpellLoadedEvent;
import nl.Naomiora.privateproject.api.customevents.WandLoadEvent;
import nl.Naomiora.privateproject.api.customevents.WandLoadedEvent;
import nl.Naomiora.privateproject.wandsmodule.wands.CommonerSpells.*;
import nl.Naomiora.privateproject.wandsmodule.wands.CommonerWand;
import nl.Naomiora.privateproject.wandsmodule.wands.ElementalWand;
import nl.Naomiora.privateproject.wandsmodule.wands.IceSpells.*;
import nl.Naomiora.privateproject.wandsmodule.wands.IceWand;
import nl.Naomiora.privateproject.wandsmodule.wands.interfaces.SomeWand;
import nl.Naomiora.privateproject.wandsmodule.wands.interfaces.Spell;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

@Data
public class WandsLoader {
    private ArrayList<SomeWand> allWands;
    private ArrayList<Spell> allSpells;
    private WandAPILoader<WandBase> wandAPILoader;
    private WandAPILoader<SpellBase> spellAPILoader;

    public WandsLoader() {
        this.wandAPILoader = new WandAPILoader<>();
        this.spellAPILoader = new WandAPILoader<>();
        this.allWands = new ArrayList<>();
        this.allSpells = new ArrayList<>();
        List<WandBase> loadableWands = this.wandAPILoader.loadClass(WandBase.class, PrivateProject.getInstance().getDataFolder(),
                "addons");
        List<SpellBase> loadableSpells = this.spellAPILoader.loadClass(SpellBase.class, PrivateProject.getInstance().getDataFolder(),
                "addons/spells");

        /* WANDS */

        loadableWands.add(new IceWand());
        loadableWands.add(new CommonerWand());
        loadableWands.add(new ElementalWand());

        /*  ICE SPELLS  */

        loadableSpells.add(new IceStatue());
        loadableSpells.add(new IceInfect());
        loadableSpells.add(new IceBurst());
        loadableSpells.add(new IceInvasion());
        loadableSpells.add(new FrozenSnowball());

        /*  COMMONER SPELLS  */

        loadableSpells.add(new LightningArrow());
        loadableSpells.add(new EarthSmash());
        loadableSpells.add(new FireBreath());
        loadableSpells.add(new NatureGrowth());

        for (WandBase loadableWand : loadableWands) {
            boolean cancelled = false;
            if (!loadableWand.isDeveloperMode() && !loadableWand.getDeveloper().equals(UUID.fromString("6fe60af2-fc0d-48a3-9c65-9069d904903b"))) {
                WandLoadEvent event = new WandLoadEvent(loadableWand);
                Bukkit.getPluginManager().callEvent(event);
                cancelled = event.isCancelled();
            }
            if (!cancelled) {
                Bukkit.getLogger().log(Level.INFO, "Loading wand " + loadableWand.getPlainWandName());
                this.allWands.add(loadableWand);
                Bukkit.getLogger().log(Level.FINEST, "The wand " + loadableWand.getPlainWandName() + " has successfully been loaded");
                Bukkit.getPluginManager().callEvent(new WandLoadedEvent(loadableWand));
            } else
                Bukkit.getLogger().severe("An addon plugin has cancelled the load of this wand.");
        }

        for (SpellBase loadableSpell : loadableSpells) {
            boolean cancelled = false;
            if (!loadableSpell.isDeveloperMode() && !loadableSpell.getDeveloper()
                    .equals(UUID.fromString("6fe60af2-fc0d-48a3-9c65-9069d904903b"))) {
                SpellLoadEvent event = new SpellLoadEvent(loadableSpell);
                Bukkit.getPluginManager().callEvent(event);
                cancelled = event.isCancelled();
            }
            if (!cancelled) {
                Bukkit.getLogger().log(Level.INFO, "Loading spell " + loadableSpell.getPlainName());
                this.allSpells.add(loadableSpell);
                Bukkit.getLogger().log(Level.FINEST, "The spell " + loadableSpell.getPlainName() + " has successfully been loaded");
                Bukkit.getPluginManager().callEvent(new SpellLoadedEvent(loadableSpell));
            } else
                Bukkit.getLogger().severe("An addon plugin has cancelled the load of this wand.");
        }
    }
}
