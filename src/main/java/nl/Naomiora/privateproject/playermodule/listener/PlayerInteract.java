package nl.Naomiora.privateproject.playermodule.listener;

import nl.Naomiora.privateproject.PrivateProject;
import nl.Naomiora.privateproject.api.WandParticle;
import nl.Naomiora.privateproject.Utils.WandUtils;
import nl.Naomiora.privateproject.api.customevents.SpellSelectEvent;
import nl.Naomiora.privateproject.api.customevents.SpellUseEvent;
import nl.Naomiora.privateproject.wandsmodule.WandsModule;
import nl.Naomiora.privateproject.wandsmodule.cooldowns.Cooldown;
import nl.Naomiora.privateproject.wandsmodule.wands.interfaces.SomeWand;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class PlayerInteract implements Listener {

    @EventHandler
    public void interact(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        try {
            if (event.getHand().equals(EquipmentSlot.OFF_HAND) && WandUtils.isWand(player, player.getInventory().getItemInOffHand()))
                event.setCancelled(true);
            if (event.getHand().equals(EquipmentSlot.OFF_HAND)) return;
        } catch (Exception exception) {
            event.setCancelled(true);
            return;
        }

        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (!itemStack.hasItemMeta()) return;
        if (!(Objects.requireNonNull(itemStack.getItemMeta()).hasDisplayName()) || !(itemStack.getItemMeta().hasLore())) return;
        if (WandUtils.isWand(player, itemStack)) {
            event.setCancelled(true);
            if (event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                if (WandsModule.getInstance().getAllUsingWands().containsKey(WandUtils.getIdFromItemStack(itemStack)))
                    this.castLeftClick(player, itemStack);
                else {
                    WandUtils.registerNewWand(player, itemStack);
                    this.castLeftClick(player, itemStack);
                }
            } else {
                if (WandsModule.getInstance().getAllUsingWands().containsKey(WandUtils.getIdFromItemStack(itemStack)))
                    this.castRightClick(player, itemStack);
                else {
                    WandUtils.registerNewWand(player, itemStack);
                    this.castLeftClick(player, itemStack);
                }
            }
        }
    }

    private void castLeftClick(Player player, ItemStack itemStack) {
        SomeWand wand = WandsModule.getInstance().getAllUsingWands().get(WandUtils.getIdFromItemStack(itemStack));
        if (!wand.getBoundSpells().isEmpty()) {
            if (wand.getSelectedSpell().isPresent()) {
                SpellUseEvent event = new SpellUseEvent(wand, wand.getSelectedSpell().get(), player);
                Bukkit.getPluginManager().callEvent(event);
                if(!event.isCancelled()) {
                    if (Cooldown.cooldowns.containsKey(player.getUniqueId()) && Cooldown.cooldowns.get(player.getUniqueId())
                            .contains(wand.getSelectedSpell().get())) {
                        player.sendMessage((PrivateProject.getInstance().getPrefix() + "You're currently still on a %time% cooldown!")
                                .replace("%time%", Math.round(Cooldown.getCooldownObject(player,
                                        wand.getSelectedSpell().get()).getReadableTime() / 1000) + " seconds").replace("&", "§"));
                        return;
                    }
                    if (wand.getSelectedSpell().get().getCastMessage(false).isPresent()) {
                        if (wand.getSelectedSpell().get().isToggleSpell()) {
                            player.sendMessage((PrivateProject.getInstance().getPrefix() + wand.getSelectedSpell().get()
                                    .getCastMessage(wand.getSelectedSpell().get().isActive(player)).get()).replace("&", "§"));
                        }
                    }
                    wand.getSelectedSpell().get().castSpell(player);
                }
            }
        } else {
            player.sendMessage((PrivateProject.getInstance().getPrefix() + " you don't have any spells bound, please use /bind (spellname) or " +
                    "/bindall to bind spells.").replace("&", "§"));
        }
    }

    private void castRightClick(Player player, ItemStack itemStack) {
        SomeWand wand = WandsModule.getInstance().getAllUsingWands().get(WandUtils.getIdFromItemStack(itemStack));
        if (!wand.getBoundSpells().isEmpty()) {
            SpellSelectEvent event = new SpellSelectEvent(wand.getSelectedSpell().isPresent() ? wand.getSelectedSpell().get() : null,
                    player.isSneaking() ? wand.getPreviousSpell() : wand.getNextSpell(), wand, player);
            Bukkit.getPluginManager().callEvent(event);
            if(!event.isCancelled()) {
                for (Object particle : wand.getSelectionParticles()) {
                    if (particle instanceof WandParticle) {
                        WandParticle wandParticle = (WandParticle) particle;
                        player.getWorld().playEffect(player.getLocation(), wandParticle.getEffect(), wandParticle.getObject());
                    } else if (particle instanceof Particle)
                        player.getWorld().spawnParticle((Particle) particle, player.getLocation(), 15, 0.3F, 0.3F, 0.3F, 0.3F);
                }

                if (player.isSneaking()) wand.previousSpell();
                else wand.nextSpell();
                player.sendMessage(wand.getSelectMessage().replace("%spellname%", wand.getSelectedSpell().get().getSpellName())
                        .replace("&", "§"));
            }
        } else {
            player.sendMessage((PrivateProject.getInstance().getPrefix() + " you don't have any spells bound, please use /bind (spellname) or " +
                    "/bindall to bind spells.").replace("&", "§"));
        }
    }
}
