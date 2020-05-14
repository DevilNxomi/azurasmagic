package nl.Naomiora.privateproject.playermodule.commands;

import nl.Naomiora.privateproject.PrivateProject;
import nl.Naomiora.privateproject.Utils.DeveloperUUIDCheck;
import nl.Naomiora.privateproject.wandsmodule.WandsModule;
import nl.Naomiora.privateproject.wandsmodule.wands.interfaces.SomeWand;
import nl.Naomiora.privateproject.wandsmodule.wands.interfaces.Spell;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.UUID;

public class AzurasMagicCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!(args.length < 1) && args[0].equalsIgnoreCase("getwand")) {
                if (args.length == 1) {
                    player.sendMessage((PrivateProject.getInstance().getPrefix() + "Please enter a valid wand name.")
                            .replace("&", "§"));
                    return true;
                }
                String wandName = args[1];
                if (player.hasPermission("azurasmagic.getwand"))
                    for (SomeWand wand : WandsModule.getInstance().getWandsLoader().getAllWands())
                        if (wand.getPlainWandName().equalsIgnoreCase(wandName) && (!wand.isDeveloperMode()
                                || DeveloperUUIDCheck.isDeveloper(player, wand.getDeveloper()))) {
                            int id = WandsModule.getInstance().getLastId() + 1;
                            wand.registerNewWand(player, id);
                            ItemStack itemStack = new ItemStack(wand.getItem(), 1);
                            ItemMeta itemMeta = itemStack.getItemMeta();
                            itemMeta.setDisplayName(wand.getWandName().replace("&", "§"));
                            itemMeta.setLore(Collections.singletonList("id: " + id));
                            itemStack.setItemMeta(itemMeta);
                            player.getInventory().addItem(itemStack);
                            WandsModule.getInstance().setLastId(id);
                            return true;
                        }
                player.sendMessage((PrivateProject.getInstance().getPrefix() + "Please enter a valid wand name.").replace("&",
                        "§"));
                return true;
            } else if (!(args.length < 1) && args[0].equalsIgnoreCase("wandlist")) {
                StringBuilder wands = new StringBuilder(WandsModule.getInstance().getWandsLoader().getAllWands().get(0).getPlainWandName());
                for (SomeWand wand : WandsModule.getInstance().getWandsLoader().getAllWands())
                    if ((!wand.isDeveloperMode() || DeveloperUUIDCheck.isDeveloper(player, wand.getDeveloper()))
                            && !wands.toString().contains(wand.getPlainWandName()))
                        wands.append(", ").append(wand.getPlainWandName());
                player.sendMessage((PrivateProject.getInstance().getPrefix() + "These are all wands: " + wands).replace("&", "§"));
                return true;
            } else if (!(args.length < 1) && args[0].equalsIgnoreCase("spelllist")) {
                StringBuilder spells = new StringBuilder(WandsModule.getInstance().getWandsLoader().getAllSpells().get(0).getPlainName());
                for (Spell spell : WandsModule.getInstance().getWandsLoader().getAllSpells())
                    if ((!spell.isDeveloperMode() || DeveloperUUIDCheck.isDeveloper(player, spell.getDeveloper()))
                            && !spells.toString().contains(spell.getPlainName()))
                        spells.append(", ").append(spell.getPlainName());
                player.sendMessage((PrivateProject.getInstance().getPrefix() + "These are all spells: " + spells +
                        " (Some spells aren't valid for certain wands.)").replace("&", "§"));
                return true;
            } else {
                player.sendMessage("&8===================================".replace("&", "§"));
                player.sendMessage("&7/am help - &cThe thing you're currently looking at".replace("&", "§"));
                player.sendMessage("&7/bindall - &cBind all spells to your wand".replace("&", "§"));
                player.sendMessage("&7/unbindall - &cUnbind all spells from your wand".replace("&", "§"));
                player.sendMessage("&7/bind (spell name) - &cBind a specific spell to your wand".replace("&", "§"));
                player.sendMessage("&7/unbind (spell name) - &cUnbind a specific spell from your wand.".replace("&", "§"));
                player.sendMessage("&7/am getwand (wand name) - &cGet a wand.".replace("&", "§"));
                player.sendMessage("&7/am spelllist - &cList all spells.".replace("&", "§"));
                player.sendMessage("&7/am wandlist - &cList all wands.".replace("&", "§"));
                player.sendMessage(("&5&lAzuras magic &5version: " + PrivateProject.getInstance().getDescription().getVersion()
                        + " (from &dNaomiii#7935&5)").replace("&", "§"));
                player.sendMessage("&8===================================".replace("&", "§"));
                return true;
            }
        }
        return false;
    }
}
