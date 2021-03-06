package cz.GravelCZLP.TracerBlocker;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.material.PistonBaseMaterial;

import com.google.common.collect.ImmutableList;

import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;

public class Utils {

	private static List<Material> transparentMaterials = new ArrayList<>();

	static {
		transparentMaterials.add(Material.CHEST);
		transparentMaterials.add(Material.TRAPPED_CHEST);
		transparentMaterials.add(Material.ENDER_CHEST);

		transparentMaterials.add(Material.AIR);
		transparentMaterials.add(Material.BEACON);

		transparentMaterials.add(Material.GLASS);
		transparentMaterials.add(Material.THIN_GLASS);
		transparentMaterials.add(Material.STAINED_GLASS);
		transparentMaterials.add(Material.STAINED_GLASS_PANE);

		transparentMaterials.add(Material.STATIONARY_WATER);
		transparentMaterials.add(Material.WATER);

		transparentMaterials.add(Material.BED_BLOCK);
		transparentMaterials.add(Material.CARPET);
		transparentMaterials.add(Material.BARRIER);

		transparentMaterials.add(Material.STONE_BUTTON);
		transparentMaterials.add(Material.WOOD_BUTTON);
		transparentMaterials.add(Material.LEVER);
		transparentMaterials.add(Material.TORCH);
		transparentMaterials.add(Material.REDSTONE_TORCH_OFF);
		transparentMaterials.add(Material.REDSTONE_TORCH_ON);

		transparentMaterials.add(Material.CACTUS);
		transparentMaterials.add(Material.LONG_GRASS);
//		transparentMaterials.add(Material.GRASS_PATH);
		transparentMaterials.add(Material.DOUBLE_PLANT);
//		transparentMaterials.add(Material.CHORUS_PLANT);
//		transparentMaterials.add(Material.CHORUS_FLOWER);
		transparentMaterials.add(Material.LEAVES);
		transparentMaterials.add(Material.LEAVES_2);
		transparentMaterials.add(Material.YELLOW_FLOWER);
		transparentMaterials.add(Material.FLOWER_POT);
		transparentMaterials.add(Material.SAPLING);
		transparentMaterials.add(Material.ICE);
		transparentMaterials.add(Material.PACKED_ICE);
		// transparentMaterials.add(Material.FROSTED_ICE);
		transparentMaterials.add(Material.WEB);
		transparentMaterials.add(Material.SLIME_BLOCK);
		transparentMaterials.add(Material.IRON_BARDING);
		transparentMaterials.add(Material.COBBLE_WALL);
		transparentMaterials.add(Material.SIGN_POST);
		transparentMaterials.add(Material.WALL_SIGN);
		transparentMaterials.add(Material.MOB_SPAWNER);
		transparentMaterials.add(Material.REDSTONE_WIRE);
		transparentMaterials.add(Material.REDSTONE_COMPARATOR_OFF);
		transparentMaterials.add(Material.REDSTONE_COMPARATOR_ON);
		transparentMaterials.add(Material.DIODE_BLOCK_OFF);
		transparentMaterials.add(Material.DIODE_BLOCK_ON);
		transparentMaterials.add(Material.RED_ROSE);
		transparentMaterials.add(Material.ENDER_PORTAL_FRAME);
		transparentMaterials.add(Material.DEAD_BUSH);
//		transparentMaterials.add(Material.END_ROD);
		transparentMaterials.add(Material.GOLD_PLATE);
		transparentMaterials.add(Material.IRON_PLATE);
		transparentMaterials.add(Material.WOOD_PLATE);
		transparentMaterials.add(Material.STONE_PLATE);
		transparentMaterials.add(Material.VINE);
		transparentMaterials.add(Material.HOPPER);
		transparentMaterials.add(Material.DAYLIGHT_DETECTOR);
		transparentMaterials.add(Material.DAYLIGHT_DETECTOR_INVERTED);
		transparentMaterials.add(Material.ENCHANTMENT_TABLE);
		transparentMaterials.add(Material.STANDING_BANNER);
		transparentMaterials.add(Material.WALL_BANNER);
		transparentMaterials.add(Material.SKULL);
		transparentMaterials.add(Material.SEEDS);
//		transparentMaterials.add(Material.BEETROOT_SEEDS);
		transparentMaterials.add(Material.MELON_SEEDS);
		transparentMaterials.add(Material.PUMPKIN_SEEDS);
		transparentMaterials.add(Material.MELON_STEM);
		transparentMaterials.add(Material.PUMPKIN_STEM);
		transparentMaterials.add(Material.SUGAR_CANE_BLOCK);
		
		transparentMaterials.add(Material.STRING);
		transparentMaterials.add(Material.STEP);
		transparentMaterials.add(Material.STONE_SLAB2);
		transparentMaterials.add(Material.TRIPWIRE_HOOK);
		transparentMaterials.add(Material.TRIPWIRE);
		transparentMaterials.add(Material.BROWN_MUSHROOM);
		transparentMaterials.add(Material.RED_MUSHROOM);
		transparentMaterials.add(Material.LADDER);
		transparentMaterials.add(Material.WATER_LILY);
		transparentMaterials.add(Material.PISTON_EXTENSION);
		
		transparentMaterials = ImmutableList.copyOf(transparentMaterials);
	}

	public static boolean checkChest(Vector3D from, Location chestLoc) {
		double size = 0.90;

		Location targetAA = chestLoc.clone().add(0, 0, 0);
		Location targetBB = chestLoc.clone().add(size, 0, 0);
		Location targetCC = chestLoc.clone().add(size, 0, size);
		Location targetDD = chestLoc.clone().add(0, 0, size);
		Location targetEE = chestLoc.clone().add(0, size, 0);
		Location targetFF = chestLoc.clone().add(size, size, 0);
		Location targetGG = chestLoc.clone().add(size, size, size);
		Location targetHH = chestLoc.clone().add(0, size, size);

		RayTrace rt = new RayTrace(from, Vector3D.fromLocation(targetAA));
		RayTrace rt1 = new RayTrace(from, Vector3D.fromLocation(targetBB));
		RayTrace rt2 = new RayTrace(from, Vector3D.fromLocation(targetCC));
		RayTrace rt3 = new RayTrace(from, Vector3D.fromLocation(targetDD));
		RayTrace rt4 = new RayTrace(from, Vector3D.fromLocation(targetEE));
		RayTrace rt5 = new RayTrace(from, Vector3D.fromLocation(targetFF));
		RayTrace rt6 = new RayTrace(from, Vector3D.fromLocation(targetGG));
		RayTrace rt7 = new RayTrace(from, Vector3D.fromLocation(targetHH));
		
		boolean result1 = Utils.rayTractResult(rt.raytrace(Settings.ChestHider.rtDist), chestLoc.getWorld());
		boolean result2 = Utils.rayTractResult(rt1.raytrace(Settings.ChestHider.rtDist), chestLoc.getWorld());
		boolean result3 = Utils.rayTractResult(rt2.raytrace(Settings.ChestHider.rtDist), chestLoc.getWorld());
		boolean result4 = Utils.rayTractResult(rt3.raytrace(Settings.ChestHider.rtDist), chestLoc.getWorld());
		boolean result5 = Utils.rayTractResult(rt4.raytrace(Settings.ChestHider.rtDist), chestLoc.getWorld());
		boolean result6 = Utils.rayTractResult(rt5.raytrace(Settings.ChestHider.rtDist), chestLoc.getWorld());
		boolean result7 = Utils.rayTractResult(rt6.raytrace(Settings.ChestHider.rtDist), chestLoc.getWorld());
		boolean result8 = Utils.rayTractResult(rt7.raytrace(Settings.ChestHider.rtDist), chestLoc.getWorld());

		return sumBooleans(new boolean[] { result1, result2, result3, result4, result5, result6, result7, result8 });
	}

	public static boolean[] checkPlayer(Vector3D from, Location playerPos, double h) {
		double width = 0.48;
		Location targetAA = playerPos.clone().add(-width, 0, -width);
		Location targetBB = playerPos.clone().add(-width, 0, width);
		Location targetCC = playerPos.clone().add(width, 0, -width);
		Location targetDD = playerPos.clone().add(width, 0, width);
		
		Location targetEE = playerPos.clone().add(-width, 1.9, -width);
		Location targetFF = playerPos.clone().add(-width, 1.9, width);
		Location targetGG = playerPos.clone().add(width, 1.9, -width);
		Location targetHH = playerPos.clone().add(width, 1.9, width);
		
		Location targetII = playerPos.clone().add(0, h / 2, 0);
		
		Vector3D start = MathUtils.toUnitVector(from.add(new Vector3D(0, h, 0)), 0.2,  playerPos.getYaw(),  playerPos.getPitch());
		
		RayTrace rt1 = new RayTrace(start, Vector3D.fromLocation(targetAA));
		RayTrace rt2 = new RayTrace(start, Vector3D.fromLocation(targetBB));
		RayTrace rt3 = new RayTrace(start, Vector3D.fromLocation(targetCC));
		RayTrace rt4 = new RayTrace(start, Vector3D.fromLocation(targetDD));
		RayTrace rt5 = new RayTrace(start, Vector3D.fromLocation(targetEE));
		RayTrace rt6 = new RayTrace(start, Vector3D.fromLocation(targetFF));
		RayTrace rt7 = new RayTrace(start, Vector3D.fromLocation(targetGG));
		RayTrace rt8 = new RayTrace(start, Vector3D.fromLocation(targetHH));
		RayTrace rt9 = new RayTrace(start, Vector3D.fromLocation(targetII));
		
		boolean result1 = Utils.rayTractResult(rt1.raytrace(Settings.PlayerHider.rtDist), playerPos.getWorld());
		boolean result2 = Utils.rayTractResult(rt2.raytrace(Settings.PlayerHider.rtDist), playerPos.getWorld());
		boolean result3 = Utils.rayTractResult(rt3.raytrace(Settings.PlayerHider.rtDist), playerPos.getWorld());
		boolean result4 = Utils.rayTractResult(rt4.raytrace(Settings.PlayerHider.rtDist), playerPos.getWorld());
		boolean result5 = Utils.rayTractResult(rt5.raytrace(Settings.PlayerHider.rtDist), playerPos.getWorld());
		boolean result6 = Utils.rayTractResult(rt6.raytrace(Settings.PlayerHider.rtDist), playerPos.getWorld());
		boolean result7 = Utils.rayTractResult(rt7.raytrace(Settings.PlayerHider.rtDist), playerPos.getWorld());
		boolean result8 = Utils.rayTractResult(rt8.raytrace(Settings.PlayerHider.rtDist), playerPos.getWorld());
		boolean result9 = Utils.rayTractResult(rt9.raytrace(Settings.PlayerHider.rtDist), playerPos.getWorld());
		
		return new boolean[] { result1, result2, result3, result4, result5, result6, result7, result8, result9 };
	}
	
	public static boolean sumBooleans(boolean[] in) {
		if (in.length <= 0) {
			return false;
		}
		boolean b = in[0];
		for (int i = 1; i < in.length; i++) {
			b |= in[i];
		}
		return b;
	}
	
	public static void showParticle(List<Vector3D> list, float r, float g, float b) {
		for (Vector3D vec : list) {
			PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.REDSTONE, true,
						(float) vec.getX(), (float) vec.getY(), (float) vec.getZ(), r, g, b, 1F, 0, 0);
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (Vector3D.fromLocation(p.getLocation()).distance(vec) < 20) {
					((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);	
				}	
			}
		}
	}

	public static void showParticle(Vector3D vec, float r, float g, float b) {
		PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.REDSTONE, true,
					(float) vec.getX(), (float) vec.getY(), (float) vec.getZ(), r, g, b, 1F, 0, 0);
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (Vector3D.fromLocation(p.getLocation()).distance(vec) < 20) {
				((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);	
			}	
		}
	}
	
	/**
	 * @return true if the whole ray is transparent, false othervise
	 */
	public static boolean rayTractResult(List<Vector3D> list, World w) {
		for (Vector3D vec : list) {
			if (!Utils.isTransparent(vec.toLocation(w).getBlock())) {
				return false;
			}
		}
		return true;
	}

	public static boolean isTransparent(Block b) {
		Material mat = b.getType();
		if (mat.toString().contains("FENCE") || mat.toString().contains("STAIRS")
				|| mat.toString().contains("RAIL") || mat.toString().contains("DOOR")
				|| mat.toString().contains("WALL") || mat.toString().contains("ANVIL")) {
			return true;
		}
		
		//checks for extended pistons
		if (mat == Material.PISTON_BASE || mat == Material.PISTON_STICKY_BASE) {
			PistonBaseMaterial piston = (PistonBaseMaterial) b.getState().getData();
			if (piston.isPowered()) {
				return true;
			}
		}
		
		if (mat == Material.STATIONARY_LAVA || mat == Material.LAVA) { // this checks if the lava has another lava above it, if it does it is not transparent, othervise it is
			Block up = b.getLocation().clone().add(0, 1, 0).getBlock();
			if (up.getType() == Material.STATIONARY_LAVA || up.getType() == Material.LAVA) {
				return false;
			}
			
			return true;
		}
		
		if (transparentMaterials.contains(mat)) {
			return true;
		}
		return false;
	}

}
