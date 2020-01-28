package cz.GravelCZLP.TracerBlocker.Common.ChestHider;

import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;

import cz.GravelCZLP.TracerBlocker.MathUtils;
import cz.GravelCZLP.TracerBlocker.RayTrace;
import cz.GravelCZLP.TracerBlocker.Settings;
import cz.GravelCZLP.TracerBlocker.Utils;
import cz.GravelCZLP.TracerBlocker.Vector3D;

public abstract class AbstractChestHider {

	private Table<Integer, Location, Boolean> chestMemberships = HashBasedTable.create();
	 
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

			int chunkRadius = Settings.ChestHider.maxDistance / 16 + 1;
			
			int minX = loc.getChunk().getX() - chunkRadius;
			int maxX = loc.getChunk().getX() + chunkRadius + 1;
			int minZ = loc.getChunk().getZ() - chunkRadius;
			int maxZ = loc.getChunk().getZ() + chunkRadius + 1;
			
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
									if (Settings.Test.debug) {
										//world.spawnParticle(Particle.REDSTONE, vec.toLocation(world), 0, 1, 1, 0);	
										Utils.showParticle(Vector3D.fromLocation(vec.toLocation(world)), 1f, 1f, 0f);
									}
									if (!Utils.isTransparent(b)) {
										endFront = vec;
										break;
									}
								}
								
								for (Vector3D vec : back.raytrace(0.1)) {
									Block b = vec.toLocation(world).getBlock();
									if (Settings.Test.debug) {
										//world.spawnParticle(Particle.REDSTONE, vec.toLocation(world), 0, 1, 1, 0);	
										Utils.showParticle(Vector3D.fromLocation(vec.toLocation(world)), 1f, 1f, 0f);
									}
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
		boolean hiddenBefore = setMembership(player.getEntityId(), location, false);
		if (hiddenBefore) {
			changeBlock(player, location, location.getBlock().getType(), location.getBlock().getData());	
		}
	}
	
	
	public Table<Integer, Location, Boolean> getChestMembership() {
		return chestMemberships;
	}

	private void hideBlock(Player player, Location location) {
		boolean visibleBefore = !setMembership(player.getEntityId(), location, true);
		if (visibleBefore) {
			changeBlock(player, location, Material.AIR, (byte) 0);	
		}
	}

	private boolean setMembership(int entityId, Location location, boolean member)
	{
		if (member) {
			return chestMemberships.put(entityId, location, true) != null;
		} else {
			return chestMemberships.remove(entityId, location) != null;
		}
	}
	
	public abstract void changeBlock(Player player, Location location, Material type, byte data);
	
	public Listener initBukkit() {
		return new Listener() {
			
			@EventHandler
			public void onBlockBreak(BlockBreakEvent e) {
				Block b = e.getBlock();
				Material mat = b.getType();
				Location loc = b.getLocation();
				if (mat == Material.CHEST || mat == Material.TRAPPED_CHEST || mat == Material.ENDER_CHEST) {
					Iterator<Cell<Integer, Location, Boolean>> iter = chestMemberships.cellSet().iterator();
					while (iter.hasNext()) {
						Location l = iter.next().getColumnKey();
						if (loc.getBlockX() == l.getBlockX() 
									|| loc.getBlockY() == l.getBlockY()
									|| loc.getBlockZ() == l.getBlockZ() || l.getWorld().equals(loc.getWorld())) {
							iter.remove();
						}
					}
				}
			}
			
			@EventHandler
			public void onBlockPlace(BlockPlaceEvent e) {
				Block b = e.getBlock();
				Material mat = b.getType();
				if (mat == Material.CHEST || mat == Material.TRAPPED_CHEST || mat == Material.ENDER_CHEST) {
					checkChestVisibility();
				}
			}
			
		};
	}
}
