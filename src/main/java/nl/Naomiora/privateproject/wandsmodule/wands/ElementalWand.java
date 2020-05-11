package nl.Naomiora.privateproject.wandsmodule.wands;

import nl.Naomiora.privateproject.api.WandBase;
import nl.Naomiora.privateproject.PrivateProject;
import nl.Naomiora.privateproject.wandsmodule.WandsModule;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class ElementalWand extends WandBase {
    public ElementalWand() {
        super();
        this.setItem(Material.NETHER_STAR);
    }

    private ElementalWand(Player player, Integer id) {
        super(player, id);
        this.setItem(Material.NETHER_STAR);
    }

    @Override
    public void registerNewWand(Player player, Integer id) {
        WandsModule.getInstance().getAllUsingWands().put(id, new ElementalWand(player, id));
    }

    @Override
    public String getBindMessage() {
        return PrivateProject.getInstance().getPrefix() +  "You succesfully bound %spellname% &fto your Elemental Wand!";
    }

    @Override
    public String getSelectMessage() {
        return PrivateProject.getInstance().getPrefix() +  "selected %spellname%";
    }

    @Override
    public String getUnbindMessage() {
        return PrivateProject.getInstance().getPrefix() +  "You succesfully unbound %spellname% &ffrom your Elemental Wand!";
    }

    @Override
    public List<Object> getSelectionParticles() {
        return Arrays.asList(Particle.SMOKE_LARGE, Particle.CAMPFIRE_COSY_SMOKE);
    }

    @Override
    public String getWandName() {
        return "&5Elemental&0Wand";
    }

    @Override
    public String getPlainWandName() {
        return "ElementalWand";
    }

    @Override
    public String getWandType() {
        return "*";
    }

    @Override
    public boolean isDeveloperMode() {
        return true;
    }
}
