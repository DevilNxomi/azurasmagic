package nl.Naomiora.privateproject.wandsmodule.wands.CommonerSpells;

import nl.Naomiora.privateproject.PrivateProject;
import nl.Naomiora.privateproject.Utils.InstantFirework;
import nl.Naomiora.privateproject.Utils.WandUtils;
import nl.Naomiora.privateproject.api.SpellBase;
import nl.Naomiora.privateproject.wandsmodule.cooldowns.Cooldown;
import nl.Naomiora.privateproject.wandsmodule.wands.interfaces.Spell;
import org.bukkit.*;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Optional;

public class LightningArrow extends SpellBase {
    @Override
    public void castSpell(Player player) {
        if(player.getInventory().containsAtLeast(new ItemStack(Material.ARROW), 4) || player.getGameMode().equals(GameMode.CREATIVE)) {
            new Cooldown(player, this, 5000);
            if(!player.getGameMode().equals(GameMode.CREATIVE)) WandUtils.removeItems(player.getInventory(), Material.ARROW, 4);
            Arrow arrow = player.launchProjectile(Arrow.class);
            Location l = player.getEyeLocation();
            Vector v = l.getDirection().multiply(25);
            arrow.setCustomName("lightningarrow1");
            arrow.setShooter(player);
            arrow.setDamage(0.5);
            arrow.setVelocity(v);

            new BukkitRunnable() {
                public void run() {
                    FireworkEffect fireworkEffect = FireworkEffect.builder().flicker(true).trail(false)
                            .with(FireworkEffect.Type.BURST).withColor(Color.fromRGB(0,191,255)).withColor(Color.fromRGB(0,0,255)).withFade(Color.WHITE).build();
                    new InstantFirework(fireworkEffect, arrow.getLocation());
                    if (!arrow.isValid()) {
                        cancel();
                    }
                }
            }.runTaskTimer(PrivateProject.getInstance(), 1L, 1L);
        } else {
            player.sendMessage((PrivateProject.getInstance().getPrefix() + "You need at least 4 arrows to use this spell!").replace("&", "§"));
        }
    }

    @Override
    public String getSpellName() {
        return "&bLightning&7Arrow";
    }

    @Override
    public String getPlainName() {
        return "LightningArrow";
    }

    @Override
    public Optional<String> getCastMessage(boolean bool) {
        return Optional.empty();
    }

    @Override
    public String[] getSpellType() {
        return new String[] { "COMMONER", "LIGHTNING" };
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
