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
	
	public Block getTargetBlock(Location direction, int maxDistance) {
		direction = direction.clone();
		if (direction.getY() > direction.getWorld().getMaxHeight()) {
			direction.setY(direction.getWorld().getMaxHeight());
		}
		if (direction.getY() <= 0) {
			direction.setY(0);
		}
		for (Iterator<Block> it = new BlockIterator(direction, 0, maxDistance + 2); it.hasNext();) {
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
		for (Material m : Material.values()) {
			if (m.isBlock()) {
				if (m.isTransparent()) {
					if (!m.isSolid()) {
						TRANSPARENT_MATERIALS.add(m.getId());
					}
				}
			}
		}
	}
}
