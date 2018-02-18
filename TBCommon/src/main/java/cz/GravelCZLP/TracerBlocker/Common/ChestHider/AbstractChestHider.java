package cz.GravelCZLP.TracerBlocker.Common.ChestHider;

import cz.GravelCZLP.TracerBlocker.MathUtils;
import cz.GravelCZLP.TracerBlocker.Settings;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

@SuppressWarnings("deprecation")
public abstract class AbstractChestHider {

	protected MathUtils math;
	
	public AbstractChestHider(MathUtils mathUtils) {
		math = mathUtils;
	}

	public void checkChestVisibility() {
		for (Player a : Bukkit.getOnlinePlayers()) {
			Location loc = a.getLocation();
			World world = loc.getWorld();
			if (Settings.ChestHider.disabledWorlds.contains(world.getName())) {
				continue;
			}
			int chunkRadius = Settings.ChestHider.maxDistance / 16;
			int minX = loc.getChunk().getX() - chunkRadius;
			int maxX = loc.getChunk().getX() + chunkRadius;
			int minZ = loc.getChunk().getZ() - chunkRadius;
			int maxZ = loc.getChunk().getZ() + chunkRadius;
			for (int x = minX; x < maxX; x++) {
				for (int z = minZ; z < maxZ; z++) {
					Chunk chunk = world.getChunkAt(x, z);
					for (BlockState state : chunk.getTileEntities()) {
						if (state.getType().equals(Material.CHEST) || state.getType().equals(Material.TRAPPED_CHEST)
								|| state.getType().equals(Material.ENDER_CHEST)) {
							double size = .90;
							Location targetAA = state.getLocation().clone().add(0, 0, 0);
							Location targetBB = state.getLocation().clone().add(size, 0, 0);
							Location targetCC = state.getLocation().clone().add(size, 0, size);
							Location targetDD = state.getLocation().clone().add(0, 0, size);
							Location targetEE = state.getLocation().clone().add(0, size, 0);
							Location targetFF = state.getLocation().clone().add(size, size, 0);
							Location targetGG = state.getLocation().clone().add(size, size, size);
							Location targetHH = state.getLocation().clone().add(0, size, size);

							int distance = (int) a.getLocation().distance(targetAA);

							// No need to check this
							if (distance > Settings.ChestHider.maxDistance) {
								continue;
							}

							if (distance < Settings.ChestHider.ignoreDistance) {
								showBlock(a, state.getLocation());
								continue;
							}

							Block blockAA = math.getTargetBlock(math.lookAt(a.getEyeLocation(), targetAA), distance);
							Block blockBB = math.getTargetBlock(math.lookAt(a.getEyeLocation(), targetBB), distance);
							Block blockCC = math.getTargetBlock(math.lookAt(a.getEyeLocation(), targetCC), distance);
							Block blockDD = math.getTargetBlock(math.lookAt(a.getEyeLocation(), targetDD), distance);
							Block blockEE = math.getTargetBlock(math.lookAt(a.getEyeLocation(), targetEE), distance);
							Block blockFF = math.getTargetBlock(math.lookAt(a.getEyeLocation(), targetFF), distance);
							Block blockGG = math.getTargetBlock(math.lookAt(a.getEyeLocation(), targetGG), distance);
							Block blockHH = math.getTargetBlock(math.lookAt(a.getEyeLocation(), targetHH), distance);

							if (blockAA == null || blockAA.getType().equals(state.getBlock().getType())
									|| blockBB == null || blockBB.getType().equals(state.getBlock().getType())
									|| blockCC == null || blockCC.getType().equals(state.getBlock().getType())
									|| blockDD == null || blockDD.getType().equals(state.getBlock().getType())
									|| blockEE == null || blockEE.getType().equals(state.getBlock().getType())
									|| blockFF == null || blockFF.getType().equals(state.getBlock().getType())
									|| blockGG == null || blockGG.getType().equals(state.getBlock().getType())
									|| blockHH == null || blockHH.getType().equals(state.getBlock().getType())) {
								showBlock(a, state.getLocation());
							} else {
								hideBlock(a, state.getLocation());
							}

						}
					}
				}
			}
		}
	}

	private void showBlock(Player player, Location location) {
		changeBlock(player, location, location.getBlock().getType(), location.getBlock().getData());
	}

	public abstract void changeBlock(Player player, Location location, Material type, byte data);

	private void hideBlock(Player player, Location location) {
		Material mat = Material.AIR;
		if (location.getBlock().getType() == Material.ENDER_CHEST) {
			mat = Material.REDSTONE_TORCH_ON;
		}
		changeBlock(player, location, mat, (byte) 0);
	}
}
