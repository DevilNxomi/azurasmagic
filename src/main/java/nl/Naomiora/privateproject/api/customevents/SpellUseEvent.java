package nl.Naomiora.privateproject.api.customevents;

import lombok.Getter;
import nl.Naomiora.privateproject.api.SpellBase;
import nl.Naomiora.privateproject.api.WandBase;
import nl.Naomiora.privateproject.wandsmodule.wands.interfaces.SomeWand;
import nl.Naomiora.privateproject.wandsmodule.wands.interfaces.Spell;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SpellUseEvent extends Event implements Cancellable {
    @Getter
    private final Spell spellBase;
    @Getter
    private final SomeWand wandBase;
    @Getter
    private final Player player;
    @Getter
    private boolean isCancelled = false;
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    public SpellUseEvent(SomeWand wandBase, Spell spellBase, Player player) {
        this.spellBase = spellBase;
        this.wandBase = wandBase;
        this.player = player;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }
}
