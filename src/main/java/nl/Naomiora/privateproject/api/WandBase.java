package nl.Naomiora.privateproject.api;

import lombok.Data;
import nl.Naomiora.privateproject.PrivateProject;
import nl.Naomiora.privateproject.Utils.WandUtils;
import nl.Naomiora.privateproject.wandsmodule.WandsModule;
import nl.Naomiora.privateproject.wandsmodule.wands.interfaces.SomeWand;
import nl.Naomiora.privateproject.wandsmodule.wands.interfaces.Spell;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.*;

@Data
public class WandBase implements SomeWand {
    private Player player;
    private Material item;
    private Integer id;
    private Spell selectedSpell;
    private ArrayList<Spell> boundSpells;

    public WandBase() {
        this.player = null;
        this.id = -1;
        this.item = Material.STICK;
        this.boundSpells = new ArrayList<>();
        this.selectedSpell = null;
    }

    public WandBase(Player player, Integer id) {
        this.player = player;
        this.id = id;
        this.item = Material.STICK;
        this.boundSpells = new ArrayList<>();
        this.selectedSpell = null;
    }

    @Override
    public Optional<Spell> getSelectedSpell() {
        return this.selectedSpell == null ? Optional.empty() : Optional.of(this.selectedSpell);
    }

    @Override
    public void nextSpell() {
        this.selectedSpell = getNextSpell();
    }

    @Override
    public Spell getNextSpell() {
        if (this.getSelectedSpell().isPresent()) {
            ArrayList<Object> objectArrayList = new ArrayList<>(this.boundSpells);
            try {
                return this.boundSpells.get(WandUtils.getArrayKey(objectArrayList, this.selectedSpell) + 1);
            } catch (Exception exception) {
                return this.boundSpells.get(0);
            }
        } else {
            return this.boundSpells.get(0);
        }
    }

    @Override
    public void previousSpell() {
        this.selectedSpell = getPreviousSpell();
    }

    @Override
    public Spell getPreviousSpell() {
        if (this.getSelectedSpell().isPresent()) {
            ArrayList<Object> objectArrayList = new ArrayList<>(this.boundSpells);

            try {
                return this.boundSpells.get(WandUtils.getArrayKey(objectArrayList, this.selectedSpell) - 1);
            } catch (Exception exception) {
                return this.boundSpells.get(this.boundSpells.size() - 1);
            }
        } else {
            return this.boundSpells.get(this.boundSpells.size() - 1);
        }
    }

    @Override
    public void registerNewWand(Player player, Integer id) {
        WandsModule.getInstance().getAllUsingWands().put(id, new WandBase(player, id));
    }

    @Override
    public ArrayList<Spell> getBoundSpells() {
        return this.boundSpells;
    }

    @Override
    public String getBindMessage() {
        return PrivateProject.getInstance().getPrefix() + "You succesfully bound %spellname% &fto your Base Wand!";
    }

    @Override
    public String getSelectMessage() {
        return PrivateProject.getInstance().getPrefix() + "selected %spellname%";
    }

    @Override
    public String getUnbindMessage() {
        return PrivateProject.getInstance().getPrefix() + "You succesfully unbound %spellname% &ffrom your Base Wand!";
    }

    @Override
    public List<Object> getSelectionParticles() {
        return Arrays.asList(Particle.SNOW_SHOVEL, Particle.CLOUD, new WandParticle(Effect.STEP_SOUND, Material.ICE));
    }

    @Override
    public String getWandName() {
        return "&bBase wand";
    }

    @Override
    public String getPlainWandName() {
        return "BaseWand";
    }

    @Override
    public Optional<Integer> getId() {
        return this.id == 1 ? Optional.empty() : Optional.of(this.id);
    }

    @Override
    public Material getItem() {
        return this.item;
    }

    @Override
    public Optional<Player> boundPlayer() {
        return this.player == null ? Optional.empty() : Optional.of(this.player);
    }

    @Override
    public String getWandType() {
        return "TYPEHERE";
    }

    @Override
    public UUID getDeveloper() {
        return UUID.fromString("6fe60af2-fc0d-48a3-9c65-9069d904903b");
    }

    @Override
    public boolean isDeveloperMode() {
        return false;
    }
}