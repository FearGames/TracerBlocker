package cz.GravelCZLP.TracerBlocker;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;

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
		transparentMaterials.add(Material.GRASS_PATH);
		transparentMaterials.add(Material.DOUBLE_PLANT);
		transparentMaterials.add(Material.CHORUS_PLANT);
		transparentMaterials.add(Material.CHORUS_FLOWER);
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
		transparentMaterials.add(Material.END_ROD);
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
		transparentMaterials.add(Material.BEETROOT_SEEDS);
		transparentMaterials.add(Material.MELON_SEEDS);
		transparentMaterials.add(Material.PUMPKIN_SEEDS);
		transparentMaterials.add(Material.MELON_STEM);
		transparentMaterials.add(Material.PUMPKIN_STEM);
		transparentMaterials.add(Material.SUGAR_CANE_BLOCK);
	}

	public static boolean chestCheck(Vector3D from, Location chestLoc) {
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

		boolean result1 = Utils.rayTractResult(rt.raytrace(0.5), chestLoc.getWorld());
		boolean result2 = Utils.rayTractResult(rt1.raytrace(0.5), chestLoc.getWorld());
		boolean result3 = Utils.rayTractResult(rt2.raytrace(0.5), chestLoc.getWorld());
		boolean result4 = Utils.rayTractResult(rt3.raytrace(0.5), chestLoc.getWorld());
		boolean result5 = Utils.rayTractResult(rt4.raytrace(0.5), chestLoc.getWorld());
		boolean result6 = Utils.rayTractResult(rt5.raytrace(0.5), chestLoc.getWorld());
		boolean result7 = Utils.rayTractResult(rt6.raytrace(0.5), chestLoc.getWorld());
		boolean result8 = Utils.rayTractResult(rt7.raytrace(0.5), chestLoc.getWorld());

		return (result1 || result2 || result3 || result4 || result5 || result6 || result7 || result8);
	}

	public static void showParticle(List<Vector3D> list, World w) {
		if (Settings.Test.debug) {
			for (Vector3D vec : list) {
				w.spawnParticle(Particle.REDSTONE, vec.toLocation(w), 1);
			}
		}
	}

	public static boolean rayTractResult(List<Vector3D> list, World w) {
		showParticle(list, w);
		for (Vector3D vec : list) {
			if (!Utils.isTransparent(vec.toLocation(w).getBlock())) {
				return false;
			}
		}
		return true;
	}

	public static boolean isTransparent(Block b) {
		Material mat = b.getType();
		if (mat.toString().contains("FENCE") || mat.toString().contains("STAIRS") || mat.toString().contains("SLAB")
				|| mat.toString().contains("RAIL") || mat.toString().contains("DOOR")
				|| mat.toString().contains("WALL")) {
			return true;
		}
		if (transparentMaterials.contains(mat)) {
			return true;
		}
		return false;
	}

}
