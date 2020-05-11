package nl.Naomiora.privateproject.api.customevents;

import lombok.Getter;
import nl.Naomiora.privateproject.api.SpellBase;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SpellLoadedEvent extends Event {
    @Getter
    private final SpellBase spell;
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    /**
     * Gets called when a spell has been LOADED, you cannot cancel the loading process if you listen to this event.
     * @param spellBase the spell which has been loaded
     */
    public SpellLoadedEvent(SpellBase spellBase) {
        this.spell = spellBase;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }
}
