package nl.Naomiora.privateproject.wandsmodule.wands.CommonerSpells;

import nl.Naomiora.privateproject.PrivateProject;
import nl.Naomiora.privateproject.Utils.VectorUtils;
import nl.Naomiora.privateproject.Utils.WandUtils;
import nl.Naomiora.privateproject.api.SpellBase;
import nl.Naomiora.privateproject.wandsmodule.wands.interfaces.Spell;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

public class NatureGrowth extends SpellBase {
    public ArrayList<UUID> isActive = new ArrayList<>();

    @Override
    public void castSpell(Player player) {
        if (isActive(player)) {
            isActive.remove(player.getUniqueId());
        } else {
            isActive.add(player.getUniqueId());
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (isActive(player)) {
                        for (Block block : WandUtils.getBlocksAroundCenter(player.getLocation(), 3)) {
                            if (block.getType().equals(Material.COARSE_DIRT)) {
                                block.setType(Material.DIRT);
                            } else if (block.getType().equals(Material.DIRT)) {
                                block.setType(Material.GRASS_BLOCK);
                            } else if (block.getBlockData() instanceof Ageable) {
                                Ageable crop = (Ageable) block.getBlockData();
                                if (crop.getAge() != crop.getMaximumAge()) {
                                    crop.setAge((crop.getAge()+1) == crop.getMaximumAge() ? crop.getAge() + 1 : crop.getAge() + 2);
                                    block.setBlockData(crop);
                                }
                            }
                        }
                    } else {
                        this.cancel();
                        return;
                    }
                }
            }.runTaskTimer(PrivateProject.getInstance(), 40L, 40L);

            new BukkitRunnable() {
                float step = 0;

                public void run() {
                    if (isActive(player)) particles(player, 3, step++);
                    else this.cancel();
                }
            }.runTaskTimer(PrivateProject.getInstance(), 1L, 1L);
        }
    }

    public void particles(Player player, int radius, float step) {
        double radius2 = radius;
        double particles2 = radius2 / 12.0;
        Location location = new Location(player.getLocation().getWorld(), player.getLocation().getX(), player.getLocation().getY() - 0.1, player.getLocation().getZ());
        double inc = (2 * Math.PI) / 20;
        double angle = step * inc;
        Vector v = new Vector();
        v.setX(Math.cos(angle) * radius);
        v.setZ(Math.sin(angle) * radius);
        VectorUtils.rotateVector(v, 0, 0, 0);
        location.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, location.add(v), 10, particles2, particles2, particles2, 0.01);
        location.getWorld().playEffect(location.add(v), Effect.STEP_SOUND, Material.EMERALD_BLOCK);
    }

    @Override
    public Optional<String> getCastMessage(boolean bool) {
        return bool ? Optional.of("You turned off nature growth!") : Optional.of("You turned on nature growth!");
    }

    @Override
    public String getPlainName() {
        return "NatureGrowth";
    }

    @Override
    public String getSpellName() {
        return "&2&lNature&7Growth";
    }

    @Override
    public String[] getSpellType() {
        return new String[]{"NATURE", "COMMONER"};
    }

    public Spell getSpell() {
        return this;
    }

    @Override
    public boolean isToggleSpell() {
        return true;
    }

    @Override
    public boolean isActive(Player player) {
        return isActive.contains(player.getUniqueId());
    }

    @Override
    public boolean isDeveloperMode() {
        return false;
    }
}