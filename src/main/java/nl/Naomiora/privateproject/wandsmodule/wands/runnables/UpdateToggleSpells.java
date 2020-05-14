package nl.Naomiora.privateproject.wandsmodule.wands.runnables;

import nl.Naomiora.privateproject.api.SpellBase;
import nl.Naomiora.privateproject.wandsmodule.WandsModule;
import org.bukkit.Bukkit;

import java.util.Objects;
import java.util.UUID;

public class UpdateToggleSpells implements Runnable {

    @Override
    public void run() {
        System.out.println("attempted check");
        for (SpellBase toggledSpell : WandsModule.getInstance().getToggledSpells())
            if (!toggledSpell.activeUsers.isEmpty()) {
                System.out.println("toggle skills isn't empty");
                for (UUID uuid : toggledSpell.activeUsers) {
                    System.out.println("toggled user = " + Bukkit.getPlayer(uuid).getName());
                    if (Objects.requireNonNull(Bukkit.getPlayer(uuid)).isOnline())
                        toggledSpell.updateSpell(Bukkit.getPlayer(uuid));
                }
            }
    }
}
