package nl.Naomiora.privateproject.api.customevents;

import lombok.Getter;
import nl.Naomiora.privateproject.wandsmodule.wands.interfaces.SomeWand;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class WandLoadEvent extends Event implements Cancellable {
    @Getter
    private final SomeWand wandClass;
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    @Getter
    private boolean isCancelled = false;

    public WandLoadEvent(SomeWand wandClass) {
        this.wandClass = wandClass;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.isCancelled = b;
    }
}
