package nl.Naomiora.privateproject.wandsmodule.wands.interfaces;

import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

public interface Spell {
    void castSpell(Player player);
    Optional<String> getCastMessage(boolean bool);
    boolean isToggleSpell();
    boolean isActive(Player player);
    String[] getSpellType();
    String getSpellName();
    String getPlainName();
    UUID getDeveloper();
    boolean isDeveloperMode();
}