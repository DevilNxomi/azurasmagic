package nl.Naomiora.privateproject.api.customevents;

import lombok.Getter;
import lombok.Setter;
import nl.Naomiora.privateproject.wandsmodule.wands.interfaces.SomeWand;
import nl.Naomiora.privateproject.wandsmodule.wands.interfaces.Spell;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nullable;

public class SpellSelectEvent extends Event implements Cancellable {
    @Getter
    private boolean isCancelled = false;
    @Getter
    @Nullable
    private final Spell from;
    @Getter
    @Setter
    private Spell to;
    @Getter
    private final SomeWand wand;
    @Getter
    private final Player player;
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    public SpellSelectEvent(@Nullable Spell from, Spell to, SomeWand wand, Player player) {
        this.from = from;
        this.to = to;
        this.wand = wand;
        this.player = player;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }
}
