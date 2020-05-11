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

import javax.annotation.Nonnull;

public class SpellUseEvent extends Event implements Cancellable {
    @Getter
    private final Spell spell;
    @Getter
    private final SomeWand wand;
    @Getter
    private final Player player;
    @Getter
    private boolean isCancelled = false;
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    /**
     * This gets called before a spell is being cast, you can use this event to cancel the spell cast.
     * @param wandBase which is being used to cast the spell
     * @param spellBase being cast
     * @param player which is casting the spell
     */
    public SpellUseEvent(@Nonnull SomeWand wandBase, @Nonnull Spell spellBase, @Nonnull Player player) {
        this.spell = spellBase;
        this.wand = wandBase;
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
