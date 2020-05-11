package nl.Naomiora.privateproject.api.customevents;

import lombok.Getter;
import nl.Naomiora.privateproject.api.SpellBase;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SpellLoadEvent extends Event implements Cancellable {
    @Getter
    private final SpellBase spellBase;
    @Getter
    private boolean isCancelled = false;
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    public SpellLoadEvent(SpellBase spellBase) {
        this.spellBase = spellBase;
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
