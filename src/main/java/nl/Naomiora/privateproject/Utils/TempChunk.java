package nl.Naomiora.privateproject.Utils;

import nl.Naomiora.privateproject.PrivateProject;
import nl.Naomiora.privateproject.wandsmodule.wands.IceSpells.IceInvasion;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.PriorityQueue;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TempChunk {
    public static Map<Chunk, TempChunk> instances = new ConcurrentHashMap<>();
    public static final PriorityQueue<TempChunk> REVERT_QUEUE = new PriorityQueue<>(100,
            (t1, t2) -> (int) (t1.revertTime - t2.revertTime));

    private Chunk chunk;
    private Biome oldBiome;
    private long revertTime;
    private boolean inRevertQueue;

    public TempChunk(Chunk chunk, Biome newBiome, long revertTime) {
        if (isTempChunk(chunk)) {
            TempChunk tempChunk = get(chunk);
            tempChunk.setRevertTime(revertTime);
            WandUtils.setChunkBiome(chunk, newBiome);
        } else {
            this.chunk = chunk;
            this.oldBiome = chunk.getWorld().getBiome(chunk.getX() * 16, chunk.getZ() * 16);
            WandUtils.setChunkBiome(chunk, newBiome);
            this.setRevertTime(revertTime);
        }
    }

    public static TempChunk get(Chunk chunk) {
        if (isTempChunk(chunk)) return instances.get(chunk);
        return null;
    }

    public static boolean isTempChunk(Chunk chunk) {
        return chunk != null && instances.containsKey(chunk);
    }

    public long getRevertTime() {
        return this.revertTime;
    }

    public void setRevertTime(long revertTime) {
        if (this.inRevertQueue) REVERT_QUEUE.remove(this);
        this.inRevertQueue = true;
        this.revertTime = (revertTime + System.currentTimeMillis());
        REVERT_QUEUE.add(this);
    }

    public static void revertAll() {
        for (TempChunk chunk : REVERT_QUEUE) {
            chunk.revertChunk();
        }
    }

    public void revertChunk() {
        WandUtils.setChunkBiome(this.chunk, this.oldBiome);
        for (Player onlinePlayer : Bukkit.getServer().getOnlinePlayers())
            WandUtils.updateChunk(onlinePlayer, this.chunk);

        for (UUID uuid : IceInvasion.affectedChunk.keySet())
            if (IceInvasion.affectedChunk.get(uuid).contains(chunk)) IceInvasion.affectedChunk.remove(uuid);
    }

    public static void startReversion() {
        new BukkitRunnable() {
            public void run() {
                long currentTime = System.currentTimeMillis();
                while (!TempChunk.REVERT_QUEUE.isEmpty()) {
                    TempChunk tempChunk = TempChunk.REVERT_QUEUE.peek();
                    if (currentTime < tempChunk.revertTime) {
                        break;
                    }
                    TempChunk.REVERT_QUEUE.poll();
                    tempChunk.revertChunk();
                }
            }
        }.runTaskTimer(PrivateProject.getInstance(), 0L, 1L);
    }
}