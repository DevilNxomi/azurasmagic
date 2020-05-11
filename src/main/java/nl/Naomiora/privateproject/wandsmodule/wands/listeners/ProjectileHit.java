package nl.Naomiora.privateproject.wandsmodule.wands.listeners;

import nl.Naomiora.privateproject.Utils.TempBlockCreation;
import nl.Naomiora.privateproject.Utils.WandUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.Random;

public class ProjectileHit implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Snowball) {
            Snowball s = (Snowball) event.getEntity();
            if (s.getShooter() instanceof Player && s.getCustomName() != null) {
                if (s.getCustomName().equals("iceinfectsnowball")) {
                    for (Location loca : WandUtils.GenerateSphere(s.getLocation(), 7, false)) {
                        if (!loca.getBlock().getType().equals(Material.AIR)) {
                            if (!WandUtils.isUnbreakable(loca.getBlock())) {
                                new TempBlockCreation(loca.getBlock(), Material.ICE, Material.ICE.createBlockData(), 400000, true);
                            }
                        }
                    }
                    s.getWorld().playSound(s.getLocation(), Sound.BLOCK_ANVIL_PLACE, 100.0F, 1.0F);
                    s.getWorld().spawnParticle(Particle.CLOUD, s.getLocation(), 120, 2.5F, 2.5F, 2.5F, 0.01F);
                }
            }
        }

        if (event.getEntity() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getEntity();
            if (arrow.getShooter() instanceof  Player && arrow.getCustomName() != null) {
                if(arrow.getCustomName().equals("lightningarrow1")) {
                    if(event.getHitBlock() != null) {
                        for (int i = 0; i < new Random().nextInt(6) + 1; i++)
                            arrow.getWorld().strikeLightning(event.getHitBlock().getLocation());
                    } else if(event.getHitEntity() != null) {
                        for (int i = 0; i < new Random().nextInt(6) + 1; i++)
                            arrow.getWorld().strikeLightning(event.getHitEntity().getLocation());
                    }
                    arrow.remove();
                }
            }
        }
    }
}
