package nl.Naomiora.privateproject.api;

import lombok.Data;
import org.bukkit.Effect;

@Data
public class WandParticle {
    private Effect effect;
    private Object object;

    public WandParticle(Effect effect, Object object) {
        this.effect = effect;
        this.object = object;
    }
}
