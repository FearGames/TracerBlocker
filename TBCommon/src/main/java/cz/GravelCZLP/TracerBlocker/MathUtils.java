package cz.GravelCZLP.TracerBlocker;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

@SuppressWarnings("deprecation")
public class MathUtils {
	
	public static Entity getNearestEntityInSight(Player player, int range) {
	    ArrayList<Entity> entities = (ArrayList<Entity>) player.getNearbyEntities(range, range, range);
	    ArrayList<Block> sightBlock = (ArrayList<Block>) player.getLineOfSight( (Set<Material>) null, range);
	    ArrayList<Location> sight = new ArrayList<Location>();
	    for (int i = 0;i<sightBlock.size();i++)
	        sight.add(sightBlock.get(i).getLocation());
	    for (int i = 0;i<sight.size();i++) {
	        for (int k = 0;k<entities.size();k++) {
	            if (Math.abs(entities.get(k).getLocation().getX()-sight.get(i).getX())<1.3) {
	                if (Math.abs(entities.get(k).getLocation().getY()-sight.get(i).getY())<1.5) {
	                    if (Math.abs(entities.get(k).getLocation().getZ()-sight.get(i).getZ())<1.3) {
	                        return entities.get(k);
	                    }
	                }
	            }
	        }
	    }
	    return null; //Return null/nothing if no entity was found
	}
	
	public static boolean canSeeBlock(Player player, Location blockLocation, int range) {
		
		ArrayList<Block> sightBlocks = (ArrayList<Block>) player.getLineOfSight((Set<Material>) null, range); 
		for (Block b : sightBlocks) {
			int bX = b.getX();
			int bY = b.getY();
			int bZ = b.getZ();
			
			int tbX = blockLocation.getBlockX();
			int tbY = blockLocation.getBlockY();
			int tbZ = blockLocation.getBlockZ();
			if (bX == tbX) {
				if (bY == tbY) {
					if (bZ == tbZ) {
						return true;
					}
				}
			}
		}
		return false; //Player cannot see the block
	}
	
	public static double squared(double d) {
		return d * d;
	}
	
	public static final HashSet<Integer> TRANSPARENT_MATERIALS = new HashSet<>();
	
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
