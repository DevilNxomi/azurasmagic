package nl.Naomiora.privateproject.api;

import lombok.Data;
import org.bukkit.Effect;

@Data
public class WandParticle {
    private Effect effect;
    private Object object;

    /**
     * Create a wand particle, mostly used for block break particles.
     * @param effect type of effect
     * @param object argument for this effect type
     */
    public WandParticle(Effect effect, Object object) {
        this.effect = effect;
        this.object = object;
    }
}
