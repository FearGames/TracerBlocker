package cz.GravelCZLP.TracerBlocker.Common.ChestHider;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import cz.GravelCZLP.TracerBlocker.MathUtils;
import cz.GravelCZLP.TracerBlocker.RayTrace;
import cz.GravelCZLP.TracerBlocker.Settings;
import cz.GravelCZLP.TracerBlocker.Utils;
import cz.GravelCZLP.TracerBlocker.Vector3D;

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
								hideBlock(a, state.getLocation());
								continue;
							}

							if (distance < Settings.ChestHider.ignoreDistance) {
								showBlock(a, state.getLocation());
								continue;
							}

							RayTrace rt = new RayTrace(Vector3D.fromLocation(a.getEyeLocation()), Vector3D.fromLocation(targetAA));
							RayTrace rt1 = new RayTrace(Vector3D.fromLocation(a.getEyeLocation()), Vector3D.fromLocation(targetBB));
							RayTrace rt2 = new RayTrace(Vector3D.fromLocation(a.getEyeLocation()), Vector3D.fromLocation(targetCC));
							RayTrace rt3 = new RayTrace(Vector3D.fromLocation(a.getEyeLocation()), Vector3D.fromLocation(targetDD));
							RayTrace rt4 = new RayTrace(Vector3D.fromLocation(a.getEyeLocation()), Vector3D.fromLocation(targetEE));
							RayTrace rt5 = new RayTrace(Vector3D.fromLocation(a.getEyeLocation()), Vector3D.fromLocation(targetFF));
							RayTrace rt6 = new RayTrace(Vector3D.fromLocation(a.getEyeLocation()), Vector3D.fromLocation(targetGG));
							RayTrace rt7 = new RayTrace(Vector3D.fromLocation(a.getEyeLocation()), Vector3D.fromLocation(targetHH));
							
							boolean result1 = Utils.rayTractResult(rt.raytrace(0.5), a.getWorld());
							boolean result2 = Utils.rayTractResult(rt1.raytrace(0.5), a.getWorld());
							boolean result3 = Utils.rayTractResult(rt2.raytrace(0.5), a.getWorld());
							boolean result4 = Utils.rayTractResult(rt3.raytrace(0.5), a.getWorld());
							boolean result5 = Utils.rayTractResult(rt4.raytrace(0.5), a.getWorld());
							boolean result6 = Utils.rayTractResult(rt5.raytrace(0.5), a.getWorld());
							boolean result7 = Utils.rayTractResult(rt6.raytrace(0.5), a.getWorld());
							boolean result8 = Utils.rayTractResult(rt7.raytrace(0.5), a.getWorld());
							
							if (!(result1 || result2 || result3 || result4 || result5 || result6 || result7 || result8)) {
								hideBlock(a, state.getLocation());
							} else {
								showBlock(a, state.getLocation());
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
