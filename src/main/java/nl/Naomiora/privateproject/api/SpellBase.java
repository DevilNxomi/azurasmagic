package nl.Naomiora.privateproject.api;

import nl.Naomiora.privateproject.wandsmodule.wands.interfaces.Spell;

import java.util.UUID;

public abstract class SpellBase implements Spell {

    @Override
    public UUID getDeveloper() {
        return UUID.fromString("6fe60af2-fc0d-48a3-9c65-9069d904903b");
    }

    @Override
    public boolean isDeveloperMode() {
        return false;
    }
}
