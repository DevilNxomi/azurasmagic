package nl.Naomiora.privateproject.wandsmodule.wands.IceSpells;

import nl.Naomiora.privateproject.PrivateProject;
import nl.Naomiora.privateproject.Utils.GetTargets;
import nl.Naomiora.privateproject.Utils.TempChunk;
import nl.Naomiora.privateproject.Utils.WandUtils;
import nl.Naomiora.privateproject.api.SpellBase;
import nl.Naomiora.privateproject.wandsmodule.wands.interfaces.Spell;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class IceInvasion extends SpellBase {
    public static HashMap<UUID, ArrayList<Chunk>> affectedChunk = new HashMap<>();

    @Override
    public void castSpell(Player player) {
        if (affectedChunk.containsKey(player.getUniqueId())) return;
        player.getWorld().strikeLightningEffect(player.getLocation());
        player.getWorld().spawnParticle(Particle.CLOUD, player.getLocation(), 45, 0.3F, 0.3F, 0.3F, 0.3F);
        for (Player onlinePlayer : Bukkit.getServer().getOnlinePlayers()) {
            onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.ENTITY_ENDER_DRAGON_AMBIENT, 100F, 1.0F);
        }

        for (Chunk chunk : WandUtils.getChunksAroundPlayer(player)) {
            new TempChunk(chunk, Biome.SNOWY_TAIGA, 400000);
            if (affectedChunk.containsKey(player.getUniqueId())) affectedChunk.get(player.getUniqueId()).add(chunk);
            else {
                ArrayList<Chunk> list = new ArrayList<>();
                list.add(chunk);
                affectedChunk.put(player.getUniqueId(), list);
            }

            for (Player onlinePlayer : Bukkit.getServer().getOnlinePlayers())
                WandUtils.updateChunk(onlinePlayer, chunk);
        }

        for (Entity entity : GetTargets.getTargetList(player.getLocation(), 6)) {
            if (entity != player) {
                Vector direction = entity.getLocation().toVector().subtract(player.getLocation().toVector()).normalize().add(new Vector(0, 0.3, 0)).multiply(1.5);
                entity.setVelocity(direction);
                player.getWorld().spawnParticle(Particle.CLOUD, player.getLocation(), 10, 0.3F, 0.3F, 0.3F, 0.3F);
                entity.getWorld().strikeLightningEffect(entity.getLocation());
                if (entity instanceof LivingEntity) ((LivingEntity) entity).damage(6);
            }
        }

        player.getWorld().setWeatherDuration(1000 * 20);
        player.getWorld().setStorm(true);
    }

    @Override
    public String getSpellName() {
        return "&bIce&1Invasion";
    }

    @Override
    public String getPlainName() {
        return "IceInvasion";
    }

    @Override
    public Optional<String> getCastMessage(boolean bool) {
        return Optional.empty();
    }

    @Override
    public String[] getSpellType() {
        return new String[]{"ICE"};
    }

    @Override
    public boolean isToggleSpell() {
        return false;
    }

    public static void startCheck() {
        new BukkitRunnable() {
            public void run() {
                for (World world : Bukkit.getServer().getWorlds()) {
                    for (LivingEntity entity : world.getLivingEntities()) {
                        if (entity instanceof LivingEntity) {
                            if (entity instanceof Player) {
                                if (((Player) entity).getGameMode().equals(GameMode.SURVIVAL) || ((Player) entity).getGameMode().equals(GameMode.ADVENTURE)) {
                                    for (UUID key : affectedChunk.keySet()) {
                                        if (!key.equals(entity.getUniqueId())) {
                                            if (affectedChunk.get(key).contains(entity.getLocation().getChunk())) {
                                                if (!WandUtils.hasBlocksAbove(entity)) {
                                                    entity.damage(2D);
                                                    entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 600, 1));
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                for (ArrayList<Chunk> list : affectedChunk.values()) {
                                    if (list.contains(entity.getLocation().getChunk())) {
                                        if (!WandUtils.hasBlocksAbove(entity)) {
                                            entity.damage(2D);
                                            entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 600, 1));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(PrivateProject.getInstance(), 40L, 20L);
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
