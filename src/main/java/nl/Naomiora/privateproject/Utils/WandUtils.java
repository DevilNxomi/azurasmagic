package nl.Naomiora.privateproject.Utils;

import nl.Naomiora.privateproject.PrivateProject;
import nl.Naomiora.privateproject.Utils.reflection.NMSHelper;
import nl.Naomiora.privateproject.wandsmodule.WandsModule;
import nl.Naomiora.privateproject.wandsmodule.wands.interfaces.SomeWand;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.*;

public class WandUtils {
    private static final Material[] unbreakables = {Material.BEDROCK, Material.BARRIER, Material.NETHER_PORTAL, Material.END_PORTAL, Material.END_PORTAL_FRAME};


    public static Integer getIdFromItemStack(ItemStack stack) {
        ItemMeta itemMeta = stack.getItemMeta();
        assert itemMeta != null;
        if (itemMeta.hasLore()) for (String lore : Objects.requireNonNull(itemMeta.getLore()))
            if (lore.startsWith("id: ")) try {
                return Integer.valueOf(lore.replace("id: ", ""));
            } catch (Exception e) {
                return -1;
            }
        return -1;
    }

    public static Integer getArrayKey(ArrayList<Object> arrayList, Object object) {
        for (int i = 0; i < arrayList.size(); i++) if (arrayList.get(i) == object) return i;
        return -1;
    }

    public static List<Location> GenerateSphere(Location Center, int Radius, boolean hollow) {
        List<Location> CircleBlocks = new ArrayList<Location>();
        int Bx = Center.getBlockX();
        int By = Center.getBlockY();
        int Bz = Center.getBlockZ();
        for (int x = Bx - Radius; x <= Bx + Radius; x++)
            for (int y = By - Radius; y <= By + Radius; y++)
                for (int z = Bz - Radius; z <= Bz + Radius; z++) {
                    double distance = (Bx - x) * (Bx - x) + (Bz - z) * (Bz - z) + (By - y) * (By - y);
                    if ((distance < Radius * Radius) && ((!hollow) || (distance >= (Radius - 1) * (Radius - 1)))) {
                        Location l = new Location(Center.getWorld(), x, y, z);
                        CircleBlocks.add(l);
                    }
                }
        return CircleBlocks;
    }

    public static List<Location> getCircle(Location loc, int radius, int height, boolean hollow, boolean sphere, double plusY) {
        List<Location> circleblocks = new ArrayList();
        int cx = loc.getBlockX();
        int cy = loc.getBlockY();
        int cz = loc.getBlockZ();
        for (int x = cx - radius; x <= cx + radius; x++)
            for (int z = cz - radius; z <= cz + radius; z++)
                for (int y = sphere ? cy - radius : cy; y < (sphere ? cy + radius : cy + height); y++) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if ((dist < radius * radius) && ((!hollow) || (dist >= (radius - 1) * (radius - 1)))) {
                        Location l = new Location(loc.getWorld(), x, y + plusY, z);
                        circleblocks.add(l);
                    }
                }
        return circleblocks;
    }


    public static boolean isWand(Player player, ItemStack itemStack) {
        for (SomeWand wand : WandsModule.getInstance().getWandsLoader().getAllWands())
            if (wand.getWandName().replace("&", "§").equalsIgnoreCase(Objects.requireNonNull(itemStack.getItemMeta()).getDisplayName()))
                if (getIdFromItemStack(itemStack) != -1) return true;
        return false;
    }

    public static void registerNewWand(Player player, ItemStack itemStack) {
        for (SomeWand wand : WandsModule.getInstance().getWandsLoader().getAllWands())
            if (wand.getWandName().replace("&", "§").equalsIgnoreCase(Objects.requireNonNull(itemStack.getItemMeta()).getDisplayName()))
                if (getIdFromItemStack(itemStack) == -2) {
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    WandsModule.getInstance().setLastId(WandsModule.getInstance().getLastId() + 1);
                    itemMeta.setLore(Collections.singletonList("id: " + WandsModule.getInstance().getLastId()));
                    itemStack.setItemMeta(itemMeta);
                    player.updateInventory();
                    wand.registerNewWand(player, getIdFromItemStack(itemStack));
                } else if (getIdFromItemStack(itemStack) != -1)
                    wand.registerNewWand(player, getIdFromItemStack(itemStack));
    }

    public static SomeWand getWandObjectFromItemStack(Player player, ItemStack itemStack) {
        for (SomeWand wand : WandsModule.getInstance().getWandsLoader().getAllWands())
            if (wand.getWandName().replace("&", "§").equalsIgnoreCase(Objects.requireNonNull(itemStack.getItemMeta()).getDisplayName()))
                if (getIdFromItemStack(itemStack) != -1)
                    return WandsModule.getInstance().getAllUsingWands().get(getIdFromItemStack(itemStack));
        return null;
    }

    public static boolean isWandInMainHand(Player player) {
        if (player.getInventory().getItemInMainHand() == null) {
            player.sendMessage((PrivateProject.getInstance().getPrefix() + "Please hold an azurasmagic wand in your maind-hand.").replace("&", "§"));
            return false;
        }
        if (!player.getInventory().getItemInMainHand().hasItemMeta()) {
            player.sendMessage((PrivateProject.getInstance().getPrefix() + "Please hold an azurasmagic wand in your maind-hand.").replace("&", "§"));
            return false;
        }
        if (!Objects.requireNonNull(player.getInventory().getItemInMainHand().getItemMeta()).hasDisplayName() || !player.getInventory().getItemInMainHand().getItemMeta().hasLore()) {
            player.sendMessage((PrivateProject.getInstance().getPrefix() + "Please hold an azurasmagic wand in your maind-hand.").replace("&", "§"));
            return false;
        }
        if (!isWand(player, player.getInventory().getItemInMainHand())) {
            player.sendMessage((PrivateProject.getInstance().getPrefix() + "Please hold an azurasmagic wand in your maind-hand.").replace("&", "§"));
            return false;
        }
        return true;
    }

    public static boolean isUnbreakable(Block block) {
        return Arrays.asList(unbreakables).contains(block.getType());
    }

    public static Block getTopBlock(Location loc, int range) {
        return getTopBlock(loc, range, range);
    }

    public static Block getTopBlock(Location loc, int positiveY, int negativeY) {
        Block blockHolder = loc.getBlock();
        int y = 0;
        while ((blockHolder.getType() != Material.AIR) && (Math.abs(y) < Math.abs(positiveY))) {
            y++;
            Block tempBlock = loc.clone().add(0.0D, y, 0.0D).getBlock();
            if (tempBlock.getType() == Material.AIR || tempBlock.isPassable()) {
                return blockHolder;
            }
            blockHolder = tempBlock;
        }
        while ((blockHolder.getType() == Material.AIR) && (Math.abs(y) < Math.abs(negativeY))) {
            y--;
            blockHolder = loc.clone().add(0.0D, y, 0.0D).getBlock();
            if (blockHolder.getType() != Material.AIR && !blockHolder.isPassable()) {
                return blockHolder;
            }
        }
        return blockHolder;
    }

    public static Collection<Chunk> getChunksAroundPlayer(Player player) {
        int[] offset = {-2, -1, 0, 1, 2};

        World world = player.getWorld();
        int baseX = player.getLocation().getChunk().getX();
        int baseZ = player.getLocation().getChunk().getZ();

        Collection<Chunk> chunksAroundPlayer = new HashSet<>();
        for (int x : offset) {
            for (int z : offset) {
                Chunk chunk = world.getChunkAt(baseX + x, baseZ + z);
                chunksAroundPlayer.add(chunk);
            }
        }
        return chunksAroundPlayer;
    }

    public static boolean hasBlocksAbove(LivingEntity player) {
        return player.getLocation().getBlockY() + 1 < player.getWorld().getHighestBlockYAt(player.getLocation());
    }

    public static void setChunkBiome(Chunk chunk, Biome biome) {
        for (int x = 0; x < 16; x++)
            for (int z = 0; z < 16; z++) {
                final Block block = chunk.getBlock(x, 0, z);
                block.setBiome(biome);
            }
    }

    public static void updateChunk(Player player, Chunk chunk) {
        try {
            NMSHelper.sendPacket(player, NMSHelper.getNMSClass("PacketPlayOutUnloadChunk").getConstructor(int.class, int.class).newInstance(chunk.getX(), chunk.getZ()));
            NMSHelper.sendPacket(player, NMSHelper.getNMSClass("PacketPlayOutMapChunk").getConstructor(NMSHelper.getNMSClass("Chunk"), int.class).newInstance(chunk.getClass().getMethod("getHandle").invoke(chunk), 65535));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createBoom(Location loc, int regentime, int radius, double damage) {
        for (Location loca : getCircle(loc, radius, 0, false, true, -1)) {
            if (!isUnbreakable(loca.getBlock())) {
                new TempBlockCreation(loca.getBlock(), Material.AIR, Material.AIR.createBlockData(), regentime, true);
            }
        }

        for (Entity e : GetTargets.getEntitiesAroundPoint(loc, radius)) {
            if (e instanceof LivingEntity) ((LivingEntity) e).damage(damage);
        }

        loc.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, loc, 1, 0D, 0D, 0D, 0.01);
    }

    public static void removeItems(Inventory inventory, Material type, int amount) {
        if (amount <= 0) return;
        int size = inventory.getSize();
        for (int slot = 0; slot < size; slot++) {
            ItemStack is = inventory.getItem(slot);
            if (is == null) continue;
            if (type == is.getType()) {
                int newAmount = is.getAmount() - amount;
                if (newAmount > 0) {
                    is.setAmount(newAmount);
                    break;
                } else {
                    inventory.clear(slot);
                    amount = -newAmount;
                    if (amount == 0) break;
                }
            }
        }
    }

    public static Vector getOrthogonalVector(Vector axis, double degrees, double length) {
        Vector ortho = new Vector(axis.getY(), -axis.getX(), 0.0D);
        ortho = ortho.normalize();
        ortho = ortho.multiply(length);

        return rotateVectorAroundVector(axis, ortho, degrees);
    }

    public static Vector rotateVectorAroundVector(Vector axis, Vector rotator, double degrees) {
        double angle = Math.toRadians(degrees);
        Vector rotation = axis.clone();
        Vector rotate = rotator.clone();
        rotation = rotation.normalize();

        Vector thirdaxis = rotation.crossProduct(rotate).normalize().multiply(rotate.length());

        return rotate.multiply(Math.cos(angle)).add(thirdaxis.multiply(Math.sin(angle)));
    }

    public static ArrayList<Block> getBlocksAroundCenter(Location loc, int radius) {
        ArrayList<Block> blocks = new ArrayList<Block>();

        for (int x = (loc.getBlockX() - radius); x <= (loc.getBlockX() + radius); x++) {
            for (int y = (loc.getBlockY() - radius); y <= (loc.getBlockY() + radius); y++) {
                for (int z = (loc.getBlockZ() - radius); z <= (loc.getBlockZ() + radius); z++) {
                    Location l = new Location(loc.getWorld(), x, y, z);
                    if (l.distance(loc) <= radius) {
                        blocks.add(l.getBlock());
                    }
                }
            }
        }
        return blocks;
    }

    public static void ParticleField(Location loc, int xRadius, int zRadius, Particle particle) {
        int yRadius = 1;
        for (int x = loc.getBlockX() - xRadius; x <= loc.getBlockX() + xRadius; x++) {
            for (int y = loc.getBlockY(); y <= loc.getBlockY() + yRadius; y++) {
                for (int z = loc.getBlockZ() - zRadius; z <= loc.getBlockZ() + zRadius; z++) {
                    Location currentLoc = loc.getWorld().getBlockAt(x, y, z).getLocation();
                    currentLoc.getWorld().spawnParticle(particle, currentLoc, 5, 0.3F, 0.3F, 0.3F, 0.03F);
                }
            }
        }
    }
}