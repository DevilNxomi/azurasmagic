package nl.Naomiora.privateproject.wandsmodule.wands.runnables;

import nl.Naomiora.privateproject.api.SpellBase;
import nl.Naomiora.privateproject.wandsmodule.WandsModule;
import org.bukkit.Bukkit;

import java.util.Objects;
import java.util.UUID;

public class UpdateToggleSpells implements Runnable {

    @Override
    public void run() {
        for (SpellBase toggledSpell : WandsModule.getInstance().getToggledSpells())
            if (!toggledSpell.activeUsers.isEmpty())
                for (UUID uuid : toggledSpell.activeUsers)
                    if (Objects.requireNonNull(Bukkit.getPlayer(uuid)).isOnline())
                        toggledSpell.updateSpell(Bukkit.getPlayer(uuid));
    }
}
