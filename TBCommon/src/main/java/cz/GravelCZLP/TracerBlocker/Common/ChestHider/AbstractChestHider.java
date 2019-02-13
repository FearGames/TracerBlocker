package cz.GravelCZLP.TracerBlocker.Common.ChestHider;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import cz.GravelCZLP.TracerBlocker.MathUtils;
import cz.GravelCZLP.TracerBlocker.RayTrace;
import cz.GravelCZLP.TracerBlocker.Settings;
import cz.GravelCZLP.TracerBlocker.Utils;
import cz.GravelCZLP.TracerBlocker.Vector3D;

public abstract class AbstractChestHider {

	public void checkChestVisibility() {
		for (Player a : Bukkit.getOnlinePlayers()) {

			if (a.getGameMode() == GameMode.SPECTATOR) {
				continue;
			}

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

							int distance = (int) a.getLocation().distance(state.getLocation());

							// No need to check this
							if (distance > Settings.ChestHider.maxDistance) {
								hideBlock(a, state.getLocation());
								continue;
							}

							if (distance < Settings.ChestHider.ignoreDistance) {
								showBlock(a, state.getLocation());
								continue;
							}

							boolean backResult = false;
							boolean frontResult = false;

							if (Settings.ChestHider.calulatef5) {
								Location eyeLoc = a.getEyeLocation();

								RayTrace front = new RayTrace(Vector3D.fromLocation(eyeLoc), eyeLoc.getYaw(), eyeLoc.getPitch(), 4.1);
								RayTrace back = new RayTrace(Vector3D.fromLocation(eyeLoc), eyeLoc.getYaw() + 180, -eyeLoc.getPitch(), 4.1);

								Vector3D endFront = front.getEnd();
								Vector3D endBack = back.getEnd();

								for (Vector3D vec : front.raytrace(0.1)) {
									Block b = vec.toLocation(world).getBlock();
									world.spawnParticle(Particle.REDSTONE, vec.toLocation(world), 0, 1, 1, 0);
									if (!Utils.isTransparent(b)) {
										endFront = vec;
										break;
									}
								}
								for (Vector3D vec : back.raytrace(0.1)) {
									Block b = vec.toLocation(world).getBlock();
									world.spawnParticle(Particle.REDSTONE, vec.toLocation(world), 0, 1, 1, 0);
									if (!Utils.isTransparent(b)) {
										endBack = vec;
										break;
									}
								}

								if (Settings.Test.debug) {
									MathUtils.renderAxisHelper(endFront.toLocation(world), 1);
									MathUtils.renderAxisHelper(endBack.toLocation(world), 1);
								}

								backResult = Utils.chestCheck(endBack, state.getLocation());
								frontResult = Utils.chestCheck(endFront, state.getLocation());
							}

							Vector3D acualEye = MathUtils.toUnitVector(
									Vector3D.fromLocation(a.getLocation().add(0, a.getEyeHeight(), 0)), 0.2,
									a.getLocation().getYaw(), a.getLocation().getPitch());

							boolean normalReult = Utils.chestCheck(acualEye, state.getLocation());

							if (!(normalReult || backResult || frontResult)) {
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

	@SuppressWarnings("deprecation")
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
