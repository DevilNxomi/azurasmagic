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

    /**
     * Get the currently selected spell
     * @return Optional spell may be empty if none is selected
     */
    @Override
    public Optional<Spell> getSelectedSpell() {
        return this.selectedSpell == null ? Optional.empty() : Optional.of(this.selectedSpell);
    }

    /**
     * Select the next spell
     */
    @Override
    public void nextSpell() {
        this.selectedSpell = getNextSpell();
    }

    /**
     * Searches for the next spell
     * @return Spell which is next on the list
     */
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

    /**
     * Select the previous spell
     */
    @Override
    public void previousSpell() {
        this.selectedSpell = getPreviousSpell();
    }

    /**
     * Searches for the previous spell
     * @return Spell which is previous on the list
     */
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

    /**
     * Registers a new wand
     * @param player current holder of the wand
     * @param id of the new wand
     */
    @Override
    public void registerNewWand(Player player, Integer id) {
        WandsModule.getInstance().getAllUsingWands().put(id, new WandBase(player, id));
    }

    /**
     * Get all currently bound spells
     * @return List of spells which are bound
     */
    @Override
    public ArrayList<Spell> getBoundSpells() {
        return this.boundSpells;
    }

    /**
     * Get binding message
     * @return String message
     */
    @Override
    public String getBindMessage() {
        return PrivateProject.getInstance().getPrefix() + "You succesfully bound %spellname% &fto your Base Wand!";
    }

    /**
     * Get spell selection message
     * @return String message
     */
    @Override
    public String getSelectMessage() {
        return PrivateProject.getInstance().getPrefix() + "selected %spellname%";
    }

    /**
     * Get unbind message
     * @return String message
     */
    @Override
    public String getUnbindMessage() {
        return PrivateProject.getInstance().getPrefix() + "You succesfully unbound %spellname% &ffrom your Base Wand!";
    }

    /**
     * Get the particles displayed when right click is pressed with this wand
     * @return List of Particles and WandParticles
     * @see WandParticle
     */
    @Override
    public List<Object> getSelectionParticles() {
        return Arrays.asList(Particle.SNOW_SHOVEL, Particle.CLOUD, new WandParticle(Effect.STEP_SOUND, Material.ICE));
    }

    /**
     * Get the wand name including color codes
     * @return String display name
     */
    @Override
    public String getWandName() {
        return "&bBase wand";
    }

    /**
     * Get the wand name excluding color codes and spaces
     * @return String basename
     */
    @Override
    public String getPlainWandName() {
        return "BaseWand";
    }

    /**
     * Get the registered wand Id
     * @return int id of wand may be empty if not registered
     */
    @Override
    public Optional<Integer> getId() {
        return this.id == 1 ? Optional.empty() : Optional.of(this.id);
    }

    /**
     * Get the wand item type
     * @return Material of item
     */
    @Override
    public Material getItem() {
        return this.item;
    }

    /**
     * Get the wand owner
     * @return player owner of the wand may be empty if not registered
     */
    @Override
    public Optional<Player> boundPlayer() {
        return this.player == null ? Optional.empty() : Optional.of(this.player);
    }

    /**
     * Wand type, for example "ICE" and "FIRE". Having "*" as wandtype means it has all types.
     * @return String of wandtype
     */
    @Override
    public String getWandType() {
        return "TYPEHERE";
    }

    /**
     * Get the developer of this wand
     * @return UUID of the developer
     */
    @Override
    public UUID getDeveloper() {
        return UUID.fromString("6fe60af2-fc0d-48a3-9c65-9069d904903b");
    }

    /**
     * Turn this to true to allow only the developer to use this wand.
     * @return boolean true if only the developer is allowed to use this wand.
     */
    @Override
    public boolean isDeveloperMode() {
        return false;
    }
}