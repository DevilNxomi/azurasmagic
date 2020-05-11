package nl.Naomiora.privateproject.wandsmodule.wands.interfaces;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SomeWand {
    Optional<Spell> getSelectedSpell();
    String getWandName();
    String getPlainWandName();
    Optional<Integer> getId();
    void nextSpell();
    void previousSpell();
    ArrayList<Spell> getBoundSpells();
    Optional<Player> boundPlayer();
    String getSelectMessage();
    String getBindMessage();
    String getUnbindMessage();
    Material getItem();
    String getWandType();
    void registerNewWand(Player player, Integer id);
    List<Object> getSelectionParticles();
    Spell getPreviousSpell();
    Spell getNextSpell();
    UUID getDeveloper();
    boolean isDeveloperMode();
}
