package nl.Naomiora.privateproject.wandsmodule.wands.CommonerSpells;

import nl.Naomiora.privateproject.PrivateProject;
import nl.Naomiora.privateproject.Utils.GetTargets;
import nl.Naomiora.privateproject.Utils.WandUtils;
import nl.Naomiora.privateproject.api.SpellBase;
import nl.Naomiora.privateproject.wandsmodule.cooldowns.Cooldown;
import nl.Naomiora.privateproject.wandsmodule.wands.interfaces.Spell;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Optional;

public class EarthSmash extends SpellBase {
    @Override
    public void castSpell(Player player) {
        new Cooldown(player, this, 10000);
        Location start = player.getLocation().clone().subtract(0.0D, 1.0D, 0.0D);
        Location curr = start.clone();
        double width = 4.0;
        new BukkitRunnable() {
            int length = 0;
            int maxLength = 24;

            public void run() {
                if (this.length > this.maxLength) {
                    cancel();
                    return;
                }

                this.length += 1;

                for (double i = -width; i <= width; i += 0.5D) {
                    Location check = curr.clone();
                    if (i != 0.0D) {
                        Vector dir = WandUtils.getOrthogonalVector(check.getDirection(), 90.0D, i);
                        check.add(dir);
                    }
                    playEffect(check.getBlock(), player);
                }
                curr.add(start.getDirection().multiply(1));
            }
        }.runTaskTimer(PrivateProject.getInstance(), 1L, 3L);
    }

    private void playEffect(Block b, Player p) {
        b = getTopBlock(b.getLocation());

        if (b.getType() == Material.AIR) {
            return;
        } else {
            b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, b.getType());

            for (Entity e : GetTargets.getEntitiesAroundPoint(b.getLocation(), 2)) {
                if ((e instanceof LivingEntity) && e.getEntityId() != p.getEntityId()) {
                    e.setVelocity(new Vector(0.0, 1.5, 0.0));
                }
            }
        }
    }

    public Block getTopBlock(Location loc) {
        Location l = WandUtils.getTopBlock(loc, 2).getLocation().add(0, 1.0D, 0);
        Block b = l.getBlock();

        b = b.getRelative(BlockFace.DOWN);
        return b;
    }


    @Override
    public String getSpellName() {
        return "&2Earth&7Smash";
    }

    @Override
    public String getPlainName() {
        return "EarthSmash";
    }

    @Override
    public Optional<String> getCastMessage(boolean bool) {
        return Optional.empty();
    }

    @Override
    public String[] getSpellType() {
        return new String[]{"EARTH", "COMMONER"};
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
