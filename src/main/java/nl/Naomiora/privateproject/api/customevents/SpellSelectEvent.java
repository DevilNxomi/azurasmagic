package nl.Naomiora.privateproject.api.customevents;

import lombok.Getter;
import lombok.Setter;
import nl.Naomiora.privateproject.wandsmodule.wands.interfaces.SomeWand;
import nl.Naomiora.privateproject.wandsmodule.wands.interfaces.Spell;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;
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

    /**
     * This gets called when before spell is being selected, you can use this event to cancel the spell selection.
     * @param from previous spell, may be null if there wasn't a spell selected yet.
     * @param to spell being selected
     * @param wand which the player is using to select a spell
     * @param player which is trying to select a new spell
     */
    public SpellSelectEvent(@Nullable Spell from, @Nonnull Spell to, @Nonnull SomeWand wand, @Nonnull Player player) {
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
