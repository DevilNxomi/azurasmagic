package nl.Naomiora.privateproject.wandsmodule.wands;

import nl.Naomiora.privateproject.api.WandBase;
import nl.Naomiora.privateproject.PrivateProject;
import nl.Naomiora.privateproject.wandsmodule.WandsModule;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class CommonerWand extends WandBase {

    public CommonerWand() {
        super();
        this.setItem(Material.BAMBOO);
    }

    private CommonerWand(Player player, Integer id) {
        super(player, id);
        this.setItem(Material.BAMBOO);
    }

    @Override
    public void registerNewWand(Player player, Integer id) {
        WandsModule.getInstance().getAllUsingWands().put(id, new CommonerWand(player, id));
    }

    @Override
    public String getBindMessage() {
        return PrivateProject.getInstance().getPrefix() +  "You succesfully bound %spellname% &fto your Commoner Wand!";
    }

    @Override
    public String getSelectMessage() {
        return PrivateProject.getInstance().getPrefix() +  "selected %spellname%";
    }

    @Override
    public String getUnbindMessage() {
        return PrivateProject.getInstance().getPrefix() +  "You succesfully unbound %spellname% &ffrom your Commoner Wand!";
    }

    @Override
    public List<Object> getSelectionParticles() {
        return Arrays.asList(Particle.SMOKE_LARGE, Particle.CAMPFIRE_COSY_SMOKE);
    }

    @Override
    public String getWandName() {
        return "&7CommonerWand";
    }

    @Override
    public String getPlainWandName() {
        return "CommonerWand";
    }

    @Override
    public String getWandType() {
        return "COMMONER";
    }
}