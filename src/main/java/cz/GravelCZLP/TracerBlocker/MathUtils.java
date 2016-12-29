package cz.GravelCZLP.TracerBlocker;

import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.util.BlockIterator;

@SuppressWarnings("deprecation")
public class MathUtils {

	
	/**
	 * Retrieve the look at vector.
	 *
	 * @param loc
	 *            - initial position. This vector will be modified.
	 * @param lookat
	 *            - the position to look at.
	 * @return The look at vector.
	 */
	public Location lookAt(Location loc, Location lookat) {
		double dx = lookat.getX() - loc.getX();
		double dy = lookat.getY() - loc.getY();
		double dz = lookat.getZ() - loc.getZ();

		double dxz = Math.sqrt(dx * dx + dz * dz);
		double pitch = Math.atan(dy / dxz);
		double yaw = 0;

		if (dx != 0) {
			if (dx < 0) {
				yaw = 1.5 * Math.PI;
			}
			else {
				yaw = 0.5 * Math.PI;
			}
			yaw -= Math.atan(dz / dx);
		}
		else if (dz < 0) {
			yaw = Math.PI;
		}

		loc.setYaw((float) Math.toDegrees(-yaw));
		loc.setPitch((float) Math.toDegrees(-pitch));
		return loc;
	}
	
	public Block getTargetBlock(Location direction, int maxDistance) throws IllegalStateException {
		direction = direction.clone();
		if (direction.getY() > direction.getWorld().getMaxHeight()) {
			direction.setY(direction.getWorld().getMaxHeight());
		}
		if (direction.getY() <= 0) {
			direction.setY(0);
		}
		for (Iterator<Block> it = new BlockIterator(direction, 0, maxDistance); it.hasNext();) {
			Block block = it.next();

			// Determine if this is a non-air block
			if (!block.isEmpty() && !block.isLiquid()
					&& !TRANSPARENT_MATERIALS.contains(block.getType().getId())) { return block; }
		}
		// No target block found
		return null;
	}
	
	private static final HashSet<Integer> TRANSPARENT_MATERIALS = new HashSet<>();

	static {
		TRANSPARENT_MATERIALS.add(Material.AIR.getId());
		TRANSPARENT_MATERIALS.add(Material.SAPLING.getId());
		TRANSPARENT_MATERIALS.add(Material.POWERED_RAIL.getId());
		TRANSPARENT_MATERIALS.add(Material.DETECTOR_RAIL.getId());
		TRANSPARENT_MATERIALS.add(Material.LONG_GRASS.getId());
		TRANSPARENT_MATERIALS.add(Material.DEAD_BUSH.getId());
		TRANSPARENT_MATERIALS.add(Material.YELLOW_FLOWER.getId());
		TRANSPARENT_MATERIALS.add(Material.RED_ROSE.getId());
		TRANSPARENT_MATERIALS.add(Material.BROWN_MUSHROOM.getId());
		TRANSPARENT_MATERIALS.add(Material.RED_MUSHROOM.getId());
		TRANSPARENT_MATERIALS.add(Material.TORCH.getId());
		TRANSPARENT_MATERIALS.add(Material.REDSTONE_WIRE.getId());
		TRANSPARENT_MATERIALS.add(Material.SEEDS.getId());
		TRANSPARENT_MATERIALS.add(Material.SIGN_POST.getId());
		TRANSPARENT_MATERIALS.add(Material.WOODEN_DOOR.getId());
		TRANSPARENT_MATERIALS.add(Material.LADDER.getId());
		TRANSPARENT_MATERIALS.add(Material.RAILS.getId());
		TRANSPARENT_MATERIALS.add(Material.WALL_SIGN.getId());
		TRANSPARENT_MATERIALS.add(Material.LEVER.getId());
		TRANSPARENT_MATERIALS.add(Material.STONE_PLATE.getId());
		TRANSPARENT_MATERIALS.add(Material.IRON_DOOR_BLOCK.getId());
		TRANSPARENT_MATERIALS.add(Material.WOOD_PLATE.getId());
		TRANSPARENT_MATERIALS.add(Material.REDSTONE_TORCH_OFF.getId());
		TRANSPARENT_MATERIALS.add(Material.REDSTONE_TORCH_ON.getId());
		TRANSPARENT_MATERIALS.add(Material.STONE_BUTTON.getId());
		TRANSPARENT_MATERIALS.add(Material.SNOW.getId());
		TRANSPARENT_MATERIALS.add(Material.SUGAR_CANE_BLOCK.getId());
		TRANSPARENT_MATERIALS.add(Material.DIODE_BLOCK_OFF.getId());
		TRANSPARENT_MATERIALS.add(Material.DIODE_BLOCK_ON.getId());
		TRANSPARENT_MATERIALS.add(Material.PUMPKIN_STEM.getId());
		TRANSPARENT_MATERIALS.add(Material.MELON_STEM.getId());
		TRANSPARENT_MATERIALS.add(Material.VINE.getId());
		TRANSPARENT_MATERIALS.add(Material.FENCE_GATE.getId());
		TRANSPARENT_MATERIALS.add(Material.WATER_LILY.getId());
		TRANSPARENT_MATERIALS.add(Material.NETHER_WARTS.getId());
		TRANSPARENT_MATERIALS.add(Material.CARPET.getId());
		TRANSPARENT_MATERIALS.add(Material.WATER.getId());
		TRANSPARENT_MATERIALS.add(Material.STATIONARY_WATER.getId());
		TRANSPARENT_MATERIALS.add(Material.GLASS.getId());
		TRANSPARENT_MATERIALS.add(Material.STAINED_GLASS.getId());
		TRANSPARENT_MATERIALS.add(Material.THIN_GLASS.getId());
		TRANSPARENT_MATERIALS.add(Material.STAINED_GLASS_PANE.getId());
		TRANSPARENT_MATERIALS.add(Material.CHEST.getId());
		TRANSPARENT_MATERIALS.add(Material.ENDER_CHEST.getId());
		TRANSPARENT_MATERIALS.add(Material.TRAPPED_CHEST.getId());
		TRANSPARENT_MATERIALS.add(Material.LEAVES.getId());
		TRANSPARENT_MATERIALS.add(Material.LEAVES_2.getId());
	}
}
