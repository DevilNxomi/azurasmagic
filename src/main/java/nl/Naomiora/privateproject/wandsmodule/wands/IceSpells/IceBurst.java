package nl.Naomiora.privateproject.wandsmodule.wands.IceSpells;

import nl.Naomiora.privateproject.PrivateProject;
import nl.Naomiora.privateproject.Utils.GetTargets;
import nl.Naomiora.privateproject.Utils.TempBlockCreation;
import nl.Naomiora.privateproject.Utils.WandUtils;
import nl.Naomiora.privateproject.api.SpellBase;
import nl.Naomiora.privateproject.wandsmodule.wands.interfaces.Spell;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

public class IceBurst extends SpellBase {
    private ArrayList<UUID> isActive = new ArrayList();

    @Override
    public void castSpell(Player player) {
        if(this.isActive.contains(player.getUniqueId())) return;
        this.isActive.add(player.getUniqueId());

        for(Location loca : WandUtils.GenerateSphere(player.getLocation(), 3, true)) {
            if (!WandUtils.isUnbreakable(loca.getBlock())) {
                new TempBlockCreation(loca.getBlock(), Material.OBSIDIAN, Material.OBSIDIAN.createBlockData(), 10000, true);
            }
        }

        for(Player onlinePlayer : Bukkit.getServer().getOnlinePlayers()) {
            onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.ENTITY_WITHER_AMBIENT, 100F, 1.0F);
        }

        new BukkitRunnable() {
            int stage = 1;
            public void run() {
                if (stage < 4) {
                    stage(player, stage);
                } else if (stage <= 8) {
                    stageTwo(player, stage);
                } else {
                    lastStage(player);
                    this.cancel();
                    isActive.remove(player.getUniqueId());
                    return;
                }

                stage++;
            }
        }.runTaskTimer(PrivateProject.getInstance(), 10L, 10L);
    }

    @Override
    public String getPlainName() {
        return "IceBurst";
    }

    @Override
    public String getSpellName() {
        return "&bIce&1Burst";
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

    public void stage(Player player, int stage) {
        for(Location loca : WandUtils.GenerateSphere(player.getLocation(), 2 + stage, false)) {
            if (!WandUtils.isUnbreakable(loca.getBlock())) {
                if (!loca.getBlock().getType().equals(Material.AIR) && !loca.getBlock().getType().equals(Material.OBSIDIAN)) {
                    new TempBlockCreation(loca.getBlock(), Material.PACKED_ICE, Material.PACKED_ICE.createBlockData(), 400000, true);
                }
            }
        }
    }

    public void stageTwo(Player player, int stage) {
        for(Location loc : WandUtils.getCircle(player.getLocation(), 2 + stage, 1, true, false, -1)) {
            Location loca = WandUtils.getTopBlock(loc, 4).getLocation();
            if (!WandUtils.isUnbreakable(loca.getBlock())) {
                if (!loca.getBlock().getType().equals(Material.AIR) && !loca.getBlock().getType().equals(Material.PACKED_ICE) && !loca.getBlock().getType().equals(Material.OBSIDIAN)) {
                    new TempBlockCreation(loca.getBlock(), Material.ICE, Material.ICE.createBlockData(), 300000, true);
                    loca.getWorld().playEffect(loca, Effect.STEP_SOUND, Material.ICE);
                }
            }
        }
    }

    public void lastStage(Player player) {
        for(Location loca : WandUtils.GenerateSphere(player.getLocation(), 6, false)) {
            player.getWorld().spawnParticle(Particle.CLOUD, loca, 2, 0.2F, 0.2F, 0.2F, 0.01F);
        }

        for(Entity entity : GetTargets.getTargetList(player.getLocation(), 6)) {
            if (entity instanceof Player) {
                if(entity != player) {
                    ((Player) entity).damage(2D);
                    ((Player) entity).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 600, 1));
                    ((Player) entity).addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 24, 0));
                    ((Player) entity).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 240, 1));
                }
            } else if (entity instanceof LivingEntity) {
                ((LivingEntity) entity).damage(2D);
                ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 600, 1));
                ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 24, 0));
                ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 240, 1));
            }
        }
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
