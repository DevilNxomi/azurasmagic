package nl.Naomiora.privateproject.wandsmodule.cooldowns;

import nl.Naomiora.privateproject.PrivateProject;
import nl.Naomiora.privateproject.wandsmodule.wands.interfaces.Spell;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;


public class Cooldown {
    public static HashMap<UUID, List<Spell>> cooldowns = new HashMap<>();
    public static final PriorityQueue<Cooldown> REVERT_QUEUE = new PriorityQueue<>(100, (t1, t2) -> (int) (t1.revertTime - t2.revertTime));

    private final Player player;
    private final Spell spell;
    private long revertTime;
    private boolean inRevertQueue;

    public Cooldown(Player player, Spell spell, long revertTime) {
        this.player = player;
        this.spell = spell;
        if(!cooldowns.containsKey(player.getUniqueId())) cooldowns.put(player.getUniqueId(), new ArrayList<>());
        cooldowns.get(player.getUniqueId()).add(spell);

        this.setRevertTime(revertTime);
    }

    public long getReadableTime() {
        return this.revertTime - System.currentTimeMillis();
    }

    public Player getPlayer() {
        return player;
    }

    public Spell getSpell() {
        return spell;
    }

    public static Cooldown getCooldownObject(Player player, Spell spell) {
        for(Cooldown cooldown : REVERT_QUEUE)
            if (cooldown.getPlayer().getUniqueId().equals(player.getUniqueId()) && cooldown.getSpell().equals(spell))
                return cooldown;
        return null;
    }

    public void setRevertTime(long revertTime) {
        if (this.inRevertQueue) REVERT_QUEUE.remove(this);
        this.inRevertQueue = true;
        this.revertTime = (revertTime + System.currentTimeMillis());
        REVERT_QUEUE.add(this);
    }

    public void removeCooldown() {
        cooldowns.get(this.player.getUniqueId()).remove(this.spell);
    }

    public static void startCheck() {
        new BukkitRunnable() {
            public void run() {
                long currentTime = System.currentTimeMillis();
                while (!Cooldown.REVERT_QUEUE.isEmpty()) {
                    Cooldown cooldown = Cooldown.REVERT_QUEUE.peek();
                    if (currentTime < cooldown.revertTime) break;
                    Cooldown.REVERT_QUEUE.poll();
                    cooldown.removeCooldown();
                }
            }
        }.runTaskTimer(PrivateProject.getInstance(), 0L, 1L);
    }
}
