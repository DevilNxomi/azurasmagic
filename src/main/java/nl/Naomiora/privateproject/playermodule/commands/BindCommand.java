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

import java.util.UUID;

public class BindCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (command.getName().equalsIgnoreCase("bind")) {
                if (WandUtils.isWandInMainHand(player)) {
                    if (args.length < 1) {
                        player.sendMessage((PrivateProject.getInstance().getPrefix() + "Please name a spell, or use /bindall instead.").replace("&", "§"));
                        return true;
                    }
                    if (!(WandsModule.getInstance().getAllUsingWands().containsKey(WandUtils.getIdFromItemStack(player.getInventory().getItemInMainHand()))))
                        WandUtils.registerNewWand(player, player.getInventory().getItemInMainHand());

                    SomeWand wand = WandsModule.getInstance().getAllUsingWands().get(WandUtils.getIdFromItemStack(player.getInventory().getItemInMainHand()));
                    if (player.hasPermission("azurasmagic.bind." + wand.getPlainWandName())) {
                        for (Spell spell : WandsModule.getInstance().getWandsLoader().getAllSpells())
                            if (!spell.isDeveloperMode() || player.getUniqueId().equals(UUID.fromString("6fe60af2-fc0d-48a3-9c65-9069d904903b"))
                            || player.getUniqueId().equals(spell.getDeveloper()))
                                if (spell.getPlainName().equalsIgnoreCase(args[0]))
                                    for (String type : spell.getSpellType())
                                        if (wand.getWandType().contains(type) || wand.getWandType().equalsIgnoreCase("*")) {
                                            if (!wand.getBoundSpells().contains(spell)) {
                                                wand.getBoundSpells().add(spell);
                                                player.sendMessage(wand.getBindMessage().replace("%spellname%", spell.getSpellName()).replace("&", "§"));
                                            } else
                                                player.sendMessage((PrivateProject.getInstance().getPrefix() + "This spell is already bound.").replace("&", "§"));
                                            return true;
                                        }
                        player.sendMessage((PrivateProject.getInstance().getPrefix() + "Please name a spell, or use /bindall instead.").replace("&", "§"));
                    }
                }
                return true;
            } else if (command.getName().equalsIgnoreCase("bindall")) {
                if (WandUtils.isWandInMainHand(player)) {
                    if (!(WandsModule.getInstance().getAllUsingWands().containsKey(WandUtils.getIdFromItemStack(player.getInventory().getItemInMainHand()))))
                        WandUtils.registerNewWand(player, player.getInventory().getItemInMainHand());
                    SomeWand wand = WandsModule.getInstance().getAllUsingWands().get(WandUtils.getIdFromItemStack(player.getInventory().getItemInMainHand()));

                    if (player.hasPermission("azurasmagic.bind." + wand.getPlainWandName()))
                        for (Spell spell : WandsModule.getInstance().getWandsLoader().getAllSpells())
                            if (!spell.isDeveloperMode() || player.getUniqueId().equals(UUID.fromString("6fe60af2-fc0d-48a3-9c65-9069d904903b"))
                            || player.getUniqueId().equals(spell.getDeveloper()))
                                for (String type : spell.getSpellType())
                                    if (wand.getWandType().contains(type) || wand.getWandType().equalsIgnoreCase("*"))
                                        if (!wand.getBoundSpells().contains(spell)) wand.getBoundSpells().add(spell);

                    player.sendMessage(wand.getBindMessage().replace("%spellname%", "all spells").replace("&", "§"));
                }
                return true;
            }
        }
        return false;
    }
}
