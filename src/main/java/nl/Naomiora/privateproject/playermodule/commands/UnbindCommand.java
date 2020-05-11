package nl.Naomiora.privateproject.playermodule.commands;

import nl.Naomiora.privateproject.PrivateProject;
import nl.Naomiora.privateproject.Utils.WandUtils;
import nl.Naomiora.privateproject.wandsmodule.WandsModule;
import nl.Naomiora.privateproject.wandsmodule.wands.interfaces.SomeWand;
import nl.Naomiora.privateproject.wandsmodule.wands.interfaces.Spell;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnbindCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (command.getName().equalsIgnoreCase("unbind")) {
                if (WandUtils.isWandInMainHand(player)) {
                    if (args.length < 1) {
                        player.sendMessage((PrivateProject.getInstance().getPrefix() + "Please name a spell, or use /unbindall instead.").replace("&", "§"));
                        return true;
                    }
                    if (!(WandsModule.getInstance().getAllUsingWands().containsKey(WandUtils.getIdFromItemStack(player.getInventory().getItemInMainHand()))))
                        WandUtils.registerNewWand(player, player.getInventory().getItemInMainHand());

                    SomeWand wand = WandsModule.getInstance().getAllUsingWands().get(WandUtils.getIdFromItemStack(player.getInventory().getItemInMainHand()));
                    if (player.hasPermission("azurasmagic.unbind." + wand.getPlainWandName())) {
                        if (wand.getBoundSpells().isEmpty()) {
                            player.sendMessage((PrivateProject.getInstance().getPrefix() + "There aren't any spells bound.").replace("&", "§"));
                            return true;
                        }
                        for (Spell spell : wand.getBoundSpells()) {
                            if (spell.getPlainName().equalsIgnoreCase(args[0])) {
                                wand.getBoundSpells().remove(spell);
                                player.sendMessage(wand.getUnbindMessage().replace("%spellname%", spell.getSpellName()).replace("&", "§"));
                                return true;
                            }
                        }

                        player.sendMessage((PrivateProject.getInstance().getPrefix() + "You don't have this spell bound.").replace("&", "§"));
                    }
                }
                return true;
            } else if (command.getName().equalsIgnoreCase("unbindall")) {
                if (WandUtils.isWandInMainHand(player)) {
                    if (!(WandsModule.getInstance().getAllUsingWands().containsKey(WandUtils.getIdFromItemStack(player.getInventory().getItemInMainHand()))))
                        WandUtils.registerNewWand(player, player.getInventory().getItemInMainHand());

                    SomeWand wand = WandsModule.getInstance().getAllUsingWands().get(WandUtils.getIdFromItemStack(player.getInventory().getItemInMainHand()));
                    if (player.hasPermission("azurasmagic.unbind." + wand.getPlainWandName())) {
                        if (wand.getBoundSpells().isEmpty()) {
                            player.sendMessage((PrivateProject.getInstance().getPrefix() + "There aren't any spells bound.").replace("&", "§"));
                            return true;
                        }
                        wand.getBoundSpells().removeAll(wand.getBoundSpells());
                        player.sendMessage(wand.getUnbindMessage().replace("%spellname%", "all spells").replace("&", "§"));
                        return true;
                    }
                }
                return true;
            }
        }
        return false;
    }
}