package nl.Naomiora.privateproject.wandsmodule.wands.IceSpells;

import nl.Naomiora.privateproject.api.SpellBase;
import nl.Naomiora.privateproject.wandsmodule.wands.interfaces.Spell;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.util.Vector;

import java.util.Optional;

public class IceInfect extends SpellBase {
    @Override
    public void castSpell(Player player) {
        Snowball s = player.launchProjectile(Snowball.class);
        Location l = player.getEyeLocation();
        Vector v = l.getDirection().multiply(3);
        s.setVelocity(v);
        s.setShooter(player);
        s.setCustomName("iceinfectsnowball");
    }

    @Override
    public String getPlainName() {
        return "IceInfect";
    }

    @Override
    public String getSpellName() {
        return "&bIce&1Infect";
    }

    @Override
    public String[] getSpellType() {
        return new String[]{"ICE"};
    }

    @Override
    public Optional<String> getCastMessage(boolean bool) {
        return Optional.empty();
    }

    @Override
    public boolean isToggleSpell() {
        return false;
    }

    @Override
    public boolean isDeveloperMode() {
        return false;
    }

    @Override
    public boolean isActive(Player player) {
        return false;
    }
}
