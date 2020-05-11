package nl.Naomiora.privateproject.wandsmodule.wands.IceSpells;

import nl.Naomiora.privateproject.PrivateProject;
import nl.Naomiora.privateproject.Utils.InstantFirework;
import nl.Naomiora.privateproject.Utils.WandUtils;
import nl.Naomiora.privateproject.api.SpellBase;
import nl.Naomiora.privateproject.wandsmodule.wands.interfaces.Spell;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Optional;

public class FrozenSnowball extends SpellBase {
    @Override
    public void castSpell(Player player) {
        Snowball s = player.launchProjectile(Snowball.class);
        Location l = player.getEyeLocation();
        Vector v = l.getDirection().multiply(1.75);
        s.setVelocity(v);
        s.setShooter(player);


        new BukkitRunnable() {
            public void run() {
                if (!s.isValid()) {
                    try {
                        if (s.isOnGround()) {
                            s.remove();
                        }
                        Location sLocation = s.getLocation();

                        WandUtils.createBoom(sLocation, 40000, 4, 2.5);

                        s.remove();
                        cancel();
                        return;
                    } catch (Exception localException2) {
                        (localException2).printStackTrace();
                    }
                }
                FireworkEffect fireworkEffect = FireworkEffect.builder().flicker(true).trail(false)
                        .with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(0,191,255)).withColor(Color.fromRGB(0,0,255)).withFade(Color.WHITE).build();
                Location location = s.getLocation();
                new InstantFirework(fireworkEffect, location);
            }
        }.runTaskTimer(PrivateProject.getInstance(), 1L, 1L);

    }

    @Override
    public Optional<String> getCastMessage(boolean bool) {
        return Optional.empty();
    }

    @Override
    public String getSpellName() {
        return "&bFrozen&1Snowball";
    }

    @Override
    public String getPlainName() {
        return "FrozenSnowball";
    }

    @Override
    public boolean isToggleSpell() {
        return false;
    }

    @Override
    public String[] getSpellType() {
        return new String[]{"ICE"};
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
