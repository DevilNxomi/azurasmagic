package nl.Naomiora.privateproject.wandsmodule.wands;

import nl.Naomiora.privateproject.api.WandBase;
import nl.Naomiora.privateproject.PrivateProject;
import nl.Naomiora.privateproject.api.WandParticle;
import nl.Naomiora.privateproject.wandsmodule.WandsModule;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class IceWand extends WandBase {
    public IceWand() {
        super();
        this.setItem(Material.LAPIS_LAZULI);
    }

    private IceWand(Player player, Integer id) {
        super(player, id);
        this.setItem(Material.LAPIS_LAZULI);
    }

    @Override
    public void registerNewWand(Player player, Integer id) {
        WandsModule.getInstance().getAllUsingWands().put(id, new IceWand(player, id));
    }

    @Override
    public String getBindMessage() {
        return PrivateProject.getInstance().getPrefix() + "You succesfully bound %spellname% &fto your ice wand!";
    }

    @Override
    public String getSelectMessage() {
        return PrivateProject.getInstance().getPrefix() + "Selected %spellname%";
    }

    @Override
    public String getUnbindMessage() {
        return PrivateProject.getInstance().getPrefix() + "You succesfully unbound %spellname% &ffrom your ice wand!";
    }

    @Override
    public List<Object> getSelectionParticles() {
        return Arrays.asList(Particle.SNOW_SHOVEL, Particle.CLOUD, new WandParticle(Effect.STEP_SOUND, Material.ICE));
    }

    @Override
    public String getWandName() {
        return "&b&lIce &1Wand";
    }

    @Override
    public String getPlainWandName() {
        return "IceWand";
    }

    @Override
    public String getWandType() {
        return "ICE";
    }
}