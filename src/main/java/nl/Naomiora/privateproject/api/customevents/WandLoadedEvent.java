package nl.Naomiora.privateproject.api.customevents;

import lombok.Getter;
import nl.Naomiora.privateproject.api.WandBase;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class WandLoadedEvent extends Event {
    @Getter
    private final WandBase wand;
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    public WandLoadedEvent(WandBase wand) {
        this.wand = wand;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }
}
