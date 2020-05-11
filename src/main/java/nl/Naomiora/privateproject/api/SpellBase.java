package nl.Naomiora.privateproject.api;

import nl.Naomiora.privateproject.wandsmodule.wands.interfaces.Spell;

import java.util.UUID;

public abstract class SpellBase implements Spell {

    /**
     * return the UUID of the developer of this spell.
     * @return UUID of developer
     */
    @Override
    public UUID getDeveloper() {
        return UUID.fromString("6fe60af2-fc0d-48a3-9c65-9069d904903b");
    }

    /**
     * Turn this to true to allow only the developer to use this spell.
     * @return boolean true if only the developer is allowed to use this spell.
     */
    @Override
    public boolean isDeveloperMode() {
        return false;
    }
}
