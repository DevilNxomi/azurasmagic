package nl.Naomiora.privateproject.wandsmodule.wands.CommonerSpells;

import nl.Naomiora.privateproject.Utils.GetTargets;
import nl.Naomiora.privateproject.api.SpellBase;
import nl.Naomiora.privateproject.wandsmodule.cooldowns.Cooldown;
import nl.Naomiora.privateproject.wandsmodule.wands.interfaces.Spell;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Objects;
import java.util.Optional;

public class FireBreath extends SpellBase {
    private int usedTime = 0;

    @Override
    public void castSpell(Player player) {
        if(isActive(player))
            activeUsers.remove(player.getUniqueId());
        else {
            usedTime = 0;
            activeUsers.add(player.getUniqueId());
        }
    }

    private void createBeam(Player player) {
        Location loc = player.getEyeLocation();
        Vector dir = player.getLocation().getDirection();
        double step = 1;
        double size = 0;
        double damageregion = 1.5;

        for (double k = 0; k < 10; k += step) {
            loc = loc.add(dir.clone().multiply(step));
            size += 0.005;
            damageregion += 0.01;

            if (loc.getBlock().getType().equals(Material.WATER))
                return;
            for (Entity entity : GetTargets.getEntitiesAroundPoint(loc, damageregion)) {
                if (entity instanceof LivingEntity && entity.getEntityId() != player.getEntityId()) {
                    if (entity instanceof Player) {
                        entity.setFireTicks(3000/50);
                        ((Player) entity).damage(1.5, player);
                    } else {
                        entity.setFireTicks(3000/50);
                        ((LivingEntity) entity).damage(1.5, player);
                    }
                }
            }

            Objects.requireNonNull(loc.getWorld()).playSound(loc, Sound.BLOCK_FIRE_AMBIENT, 10, 1);
            loc.getWorld().spawnParticle(Particle.SMOKE_NORMAL, loc, 3, Math.random(), Math.random(), Math.random(), size);
            loc.getWorld().spawnParticle(Particle.FLAME, loc, 3, Math.random(), Math.random(), Math.random(), size);
        }
    }

    @Override
    public void updateSpell(Player player) {
        System.out.println("yeet?");
        usedTime+=0.05;
        if(usedTime <= 10) {
            if (isActive(player)) createBeam(player);
            else new Cooldown(player, getSpell(), Math.round((usedTime * 1000) * 3));
        } else {
            activeUsers.remove(player.getUniqueId());
            new Cooldown(player, getSpell(), Math.round((usedTime * 1000) * 3));
        }
    }

    @Override
    public Optional<String> getCastMessage(boolean bool) {
        return bool ? Optional.of("You turned off fire breath!") : Optional.of("You turned on fire breath!");
    }

    @Override
    public String getPlainName() {
        return "FireBreath";
    }

    @Override
    public String getSpellName() {
        return "&4&lFire&7Breath";
    }

    @Override
    public String[] getSpellType() {
        return new String[] { "FIRE", "COMMONER" };
    }

    public Spell getSpell(){
        return this;
    }

    @Override
    public boolean isToggleSpell() {
        return true;
    }

    @Override
    public boolean isActive(Player player) {
        return activeUsers.contains(player.getUniqueId());
    }

    @Override
    public boolean isDeveloperMode() {
        return false;
    }
}
