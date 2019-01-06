package cz.GravelCZLP.TracerBlocker;

import java.util.ArrayList;
import java.util.List;

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
		//transparentMaterials.add(Material.FROSTED_ICE);
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
	}
	
	public static boolean rayTractResult(List<Vector3D> list, World w) {
		for (Vector3D vec : list) {
			w.spawnParticle(Particle.REDSTONE, vec.toLocation(w), 1);
			if (!Utils.isTransparent(vec.toLocation(w).getBlock())) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean isTransparent(Block b) {
		Material mat = b.getType();
		if (mat.toString().contains("FENCE") 
				|| mat.toString().contains("STAIRS") 
				|| mat.toString().contains("SLAB")
				|| mat.toString().contains("RAIL")
				|| mat.toString().contains("DOOR")
				|| mat.toString().contains("WALL")) {
			return true;
		}
		if (transparentMaterials.contains(mat)) {
			return true;
		}
		return false;
	}
	
}
