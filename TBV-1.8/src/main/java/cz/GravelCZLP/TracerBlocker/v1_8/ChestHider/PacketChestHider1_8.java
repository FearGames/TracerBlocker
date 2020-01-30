package cz.GravelCZLP.TracerBlocker.v1_8.ChestHider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;

import cz.GravelCZLP.TracerBlocker.MathUtils;
import cz.GravelCZLP.TracerBlocker.TracerBlocker;
import cz.GravelCZLP.TracerBlocker.Utils;
import cz.GravelCZLP.TracerBlocker.Vector3D;
import cz.GravelCZLP.TracerBlocker.Common.ChestHider.AbstractChestHider;
import cz.GravelCZLP.TracerBlocker.Common.ChestHider.AbstractPacketChestHider;
import net.minecraft.server.v1_8_R3.PacketPlayOutMapChunk.ChunkMap;

public class PacketChestHider1_8 extends AbstractPacketChestHider {

	public PacketChestHider1_8(AbstractChestHider ach)
	{
		super(ach);
	}

	@Override
	public void setup() {
		PacketAdapter chunkBulk = new PacketAdapter(TracerBlocker.getInstance(), ListenerPriority.HIGHEST, PacketType.
				Play.Server.MAP_CHUNK_BULK) {

			@Override
			public void onPacketSending(PacketEvent event) {	
				PacketContainer pc = event.getPacket();
				//boolean groundUp = pc.getBooleans().read(0);
				int[] chunkXA = pc.getIntegerArrays().read(0);
				int[] chunkZA = pc.getIntegerArrays().read(1);
				
				ChunkMap[] cm = (ChunkMap[]) pc.getModifier().read(2);
				
				Player p = event.getPlayer();
				
				for (int i = 0; i < cm.length; i++)
				{
					Map<Integer, List<GBlock>> m = new HashMap<>();
					int chunkX = chunkXA[i];
					int chunkZ = chunkZA[i];
					
					Vector3D acualEye = MathUtils.toUnitVector(
								Vector3D.fromLocation(p.getLocation().add(0, p.getEyeHeight(), 0)), 0.2,
								p.getLocation().getYaw(), p.getLocation().getPitch());
					
					BlockState[] tileEntites = event.getPlayer().getWorld().getChunkAt(chunkX, chunkZ).getTileEntities();
					for (BlockState bs : tileEntites) 
					{
						if (bs.getType() == Material.CHEST 
									|| bs.getType() == Material.TRAPPED_CHEST 
									|| bs.getType() == Material.ENDER_CHEST) 
						{
							double dist = Vector3D.fromLocation(bs.getLocation()).distance(acualEye);
							
							boolean hide = false;
							
							if (dist > 64) {
								hide = true;
							} else {
								hide = !Utils.checkChest(acualEye, bs.getLocation());
							}
							
							if (hide) {
								int s = bs.getY() / 16;
								if (m.get(s) != null)
								{
									m.get(s).add(new GBlock(bs.getX(), bs.getY(), bs.getZ()));
								} else {
									m.put(s, new ArrayList<>(Arrays.asList(new GBlock(bs.getX(), bs.getY(), bs.getZ()))));
								}	
							}
						}
					}
										
					cm[i] = modify(cm[i], m);
				}
				
				pc.getModifier().write(2, cm);
				
				event.setPacket(pc);
			}
		};
		
		PacketAdapter chunk = new PacketAdapter(TracerBlocker.getInstance(), ListenerPriority.HIGHEST, PacketType.
					Play.Server.MAP_CHUNK) {

			@Override
			public void onPacketSending(PacketEvent event) {	
				PacketContainer pc = event.getPacket();
				//boolean groundUp = pc.getBooleans().read(0);
				int chunkX = pc.getIntegers().read(0);
				int chunkZ = pc.getIntegers().read(1);
				
				ChunkMap cm = (ChunkMap) pc.getModifier().read(2);
				
				Map<Integer, List<GBlock>> m = new HashMap<>();
				
				Player p = event.getPlayer();
				
				Vector3D acualEye = MathUtils.toUnitVector(
							Vector3D.fromLocation(p.getLocation().add(0, p.getEyeHeight(), 0)), 0.2,
							p.getLocation().getYaw(), p.getLocation().getPitch());
				
				BlockState[] tileEntites = event.getPlayer().getWorld().getChunkAt(chunkX, chunkZ).getTileEntities();
				for (BlockState bs : tileEntites) 
				{
					if (bs.getType() == Material.CHEST 
								|| bs.getType() == Material.TRAPPED_CHEST 
								|| bs.getType() == Material.ENDER_CHEST) 
					{
						double dist = Vector3D.fromLocation(bs.getLocation()).distance(acualEye);
						
						boolean hide = false;
						
						if (dist > 64) {
							hide = true;
						} else {
							hide = !Utils.checkChest(acualEye, bs.getLocation());
						}
						
						if (hide) {
							int s = bs.getY() / 16;
							if (m.get(s) != null)
							{
								m.get(s).add(new GBlock(bs.getX(), bs.getY(), bs.getZ()));
							} else {
								m.put(s, new ArrayList<>(Arrays.asList(new GBlock(bs.getX(), bs.getY(), bs.getZ()))));
							}	
						}
					}
				}
									
				cm = modify(cm, m);
				
				pc.getModifier().write(2, cm);
				
				event.setPacket(pc);
			}
		};
		PacketAdapter blockAction = new PacketAdapter(TracerBlocker.getInstance(), ListenerPriority.HIGHEST, PacketType.Play.Server.BLOCK_ACTION)
		{
			
			@Override
			public void onPacketSending(PacketEvent event)
			{
				PacketContainer handle = event.getPacket();
				BlockPosition pos = handle.getBlockPositionModifier().read(0);
				Material mat = handle.getBlocks().read(0);
				if (mat == Material.ENDER_CHEST || mat == Material.CHEST || mat == Material.TRAPPED_CHEST) {
					Player p = event.getPlayer();
					Location loc = new Location(p.getWorld(), pos.getX(), pos.getY(), pos.getZ());
					Block b = loc.getWorld().getBlockAt(loc);
					Boolean obj = ach.getChestMembership().get(p.getEntityId(), b.getLocation());
					if (obj == null) {
						Vector3D acualEye = MathUtils.toUnitVector(
									Vector3D.fromLocation(p.getLocation().add(0, p.getEyeHeight(), 0)), 0.2,
									p.getLocation().getYaw(), p.getLocation().getPitch());
						obj = Boolean.valueOf(!Utils.checkChest(acualEye, loc));
					}
					boolean hide = obj.booleanValue();
					if (hide) {
						event.setCancelled(true);
					}
				}
			}
			
		};
		
		PacketAdapter breakAnimation = new PacketAdapter(TracerBlocker.getInstance(), ListenerPriority.HIGHEST, PacketType.Play.Server.BLOCK_BREAK_ANIMATION)
		{
			@Override
			public void onPacketSending(PacketEvent event)
			{
				PacketContainer packet = event.getPacket();
				BlockPosition pos = packet.getBlockPositionModifier().read(0);
				Player p = event.getPlayer();
				Location loc = new Location(p.getWorld(), pos.getX(), pos.getY(), pos.getZ());
				Block b = p.getWorld().getBlockAt(loc);
				if (b.getType() == Material.CHEST || b.getType() == Material.ENDER_CHEST || b.getType() == Material.TRAPPED_CHEST) {
					Boolean obj = ach.getChestMembership().get(p.getEntityId(), b.getLocation());
					if (obj == null) {
						Vector3D acualEye = MathUtils.toUnitVector(
									Vector3D.fromLocation(p.getLocation().add(0, p.getEyeHeight(), 0)), 0.2,
									p.getLocation().getYaw(), p.getLocation().getPitch());
						obj = Boolean.valueOf(!Utils.checkChest(acualEye, loc));
					}
					boolean hide = obj.booleanValue();
					if (hide) {
						event.setCancelled(true);
					}
				}
			}
		};
		
		/*PacketAdapter blockChange = new PacketAdapter(TracerBlocker.getInstance(), ListenerPriority.HIGHEST, PacketType.Play.Server.BLOCK_CHANGE) 
		{
			@Override
			public void onPacketSending(PacketEvent event)
			{
				PacketContainer handle = event.getPacket();
				BlockPosition pos = handle.getBlockPositionModifier().read(0);
				WrappedBlockData data = handle.getBlockData().read(0);
				if (data.getType() == Material.CHEST || data.getType() == Material.ENDER_CHEST || data.getType() == Material.TRAPPED_CHEST) {
					Player p = event.getPlayer();
					Location loc = pos.toLocation(p.getWorld());
					Boolean obj = ach.getChestMembership().get(p.getEntityId(), loc);
					if (obj == null) {
						Vector3D acualEye = MathUtils.toUnitVector(
									Vector3D.fromLocation(p.getLocation().add(0, p.getEyeHeight(), 0)), 0.2,
									p.getLocation().getYaw(), p.getLocation().getPitch());
						obj = Boolean.valueOf(Utils.checkChest(acualEye, loc));
					}
					boolean hidden = obj.booleanValue();
					if (hidden) {
						event.setCancelled(true);	
					}
				}
			}
		};
		
		PacketAdapter multiBlockChange = new PacketAdapter(TracerBlocker.getInstance(), ListenerPriority.HIGHEST, PacketType.Play.Server.MULTI_BLOCK_CHANGE) 
		{
			@Override
			public void onPacketSending(PacketEvent event)
			{
				PacketContainer handle = event.getPacket();
				MultiBlockChangeInfo[] records = handle.getMultiBlockChangeInfoArrays().read(0);
				Player p = event.getPlayer();
				for (MultiBlockChangeInfo info : records) {
					Location loc = info.getLocation(p.getWorld());
					Material mat = loc.getBlock().getType();
					if (mat == Material.CHEST || mat == Material.ENDER_CHEST || mat == Material.TRAPPED_CHEST) {
						Boolean obj = ach.getChestMembership().get(p.getEntityId(), loc);
						if (obj == null) {
							Vector3D acualEye = MathUtils.toUnitVector(
										Vector3D.fromLocation(p.getLocation().add(0, p.getEyeHeight(), 0)), 0.2,
										p.getLocation().getYaw(), p.getLocation().getPitch());
							obj = Boolean.valueOf(Utils.checkChest(acualEye, loc));
							
						}
						boolean hidden = obj.booleanValue();
						if (hidden) {
							event.setCancelled(true);	
						}
					}
				}
			}
		};*/
		
		//TODO: tyto dva nefungují :/ záhí NPE
		ProtocolLibrary.getProtocolManager().addPacketListener(breakAnimation);
		ProtocolLibrary.getProtocolManager().addPacketListener(blockAction);
		
//		ProtocolLibrary.getProtocolManager().addPacketListener(blockChange);
//		ProtocolLibrary.getProtocolManager().addPacketListener(multiBlockChange);
		
		ProtocolLibrary.getProtocolManager().addPacketListener(chunkBulk);
		ProtocolLibrary.getProtocolManager().addPacketListener(chunk);
	}

	private static ChunkMap modify(ChunkMap map, Map<Integer, List<GBlock>> toChange) {
        int changedSections = createBitmask(toChange.keySet());
        int newSections = changedSections & ~map.b;
        int fullBitmask = map.b | changedSections;
        boolean biome = map.a.length % 10240 != 0;
        boolean skylight = map.a.length > bitCount(map.b) * 10240 + (biome ? 256 : 0);
        //Adding new sections (if necessary)
        if (newSections != 0) {
            byte[] newBytes = new byte[map.a.length + (10240 + (skylight ? 2048 : 0)) * bitCount(newSections)];
            System.arraycopy(map.a, 0, newBytes, 0, map.a.length);
            Arrays.fill(newBytes, 8192 * bitCount(fullBitmask), newBytes.length, (byte)255); 
            int sectionStart = 0;
            int lightStart = bitCount(fullBitmask) * 8192;
            int skylightStart = lightStart + bitCount(fullBitmask) * 2048;
            for (int section = 0; section < 16; section++) {
                if ((fullBitmask & 1 << section) != 0) {
                    if ((newSections & 1 << section) != 0) {
                        System.arraycopy(newBytes, sectionStart, newBytes, sectionStart + 8192, newBytes.length - (sectionStart + 8192));
                        Arrays.fill(newBytes, sectionStart, sectionStart + 8192, (byte)0);
                     
                        System.arraycopy(newBytes, lightStart, newBytes, lightStart + 2048, newBytes.length - (lightStart + 2048));
                        if (skylight) {
                            System.arraycopy(newBytes, skylightStart, newBytes, skylightStart + 2048, newBytes.length - (skylightStart + 2048));
                        }
                    }
                    sectionStart += 8192;
                    lightStart += 2048;
                    skylightStart += 2048;
                }
            }
            map.a = newBytes;
        }
        //Writing blocks to section
        int sstart = 0;
        for (int s = 0; s < 16; s++) {
            if ((fullBitmask & 1 << s) != 0) {
                if ((changedSections & 1 << s) != 0) {
                    for (GBlock fb : toChange.get(s)) {
                        char blockId = (char) (fb.getType().getId() << 4);
                        int index = sstart + ((fb.getX() & 15) + 16 * ((fb.getZ() & 15) + 16 * (fb.getY() & 15))) * 2;
                        map.a[index] = (byte) (blockId & 255);
                        map.a[index + 1] = (byte) (blockId >> 8 & 255);
                    }
                }
                sstart += 8192;
            }
        }
        map.b = fullBitmask;
        
        return map;
    }

    private static int bitCount(int i) {
        return Integer.bitCount(i);
    }

    private static int createBitmask(Collection<Integer> sections) {
        int i = 0;
        for (int section : sections) {
            i |= 1 << section;
        }
        return i;
    }
    
    private class GBlock {

    	private int x, y, z;
    	
    	public GBlock(int x, int y, int z)
		{
    		this.x = x;
    		this.y = y;
    		this.z = z;
		}
    	
		public Material getType()
		{
			return Material.AIR;
		}

		public int getY()
		{
			return y;
		}

		public int getZ()
		{
			return z;
		}

		public int getX()
		{
			return x;
		}
    	
    }
}
