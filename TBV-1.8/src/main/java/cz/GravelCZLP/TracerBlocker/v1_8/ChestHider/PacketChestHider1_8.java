package cz.GravelCZLP.TracerBlocker.v1_8.ChestHider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

import cz.GravelCZLP.TracerBlocker.MathUtils;
import cz.GravelCZLP.TracerBlocker.TracerBlocker;
import cz.GravelCZLP.TracerBlocker.Utils;
import cz.GravelCZLP.TracerBlocker.Vector3D;
import cz.GravelCZLP.TracerBlocker.Common.ChestHider.AbstractChestHider;
import cz.GravelCZLP.TracerBlocker.Common.ChestHider.AbstractPacketChestHider;
import net.minecraft.server.v1_8_R3.PacketPlayOutMapChunk.ChunkMap;

public class PacketChestHider1_8 extends AbstractPacketChestHider
{

	public PacketChestHider1_8(AbstractChestHider ach)
	{
		super(ach);
	}

	@Override
	public void setup()
	{
		PacketAdapter chunkBulk = new PacketAdapter(TracerBlocker.getInstance(), ListenerPriority.HIGHEST, PacketType.Play.Server.MAP_CHUNK_BULK)
		{

			@Override
			public void onPacketSending(PacketEvent event)
			{
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

							if (dist > 64)
							{
								hide = true;
							} else
							{
								hide = !Utils.checkChest(acualEye, bs.getLocation());
							}

							if (hide)
							{
								int s = bs.getY() / 16;
								if (m.get(s) != null)
								{
									m.get(s).add(new GBlock(bs.getX(), bs.getY(), bs.getZ()));
								} else
								{
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

		PacketAdapter chunk = new PacketAdapter(TracerBlocker.getInstance(), ListenerPriority.HIGHEST, PacketType.Play.Server.MAP_CHUNK)
		{

			@Override
			public void onPacketSending(PacketEvent event)
			{
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

						if (dist > 64)
						{
							hide = true;
						} else
						{
							hide = !Utils.checkChest(acualEye, bs.getLocation());
						}

						if (hide)
						{
							int s = bs.getY() / 16;
							if (m.get(s) != null)
							{
								m.get(s).add(new GBlock(bs.getX(), bs.getY(), bs.getZ()));
							} else
							{
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

		ProtocolLibrary.getProtocolManager().addPacketListener(chunkBulk);
		ProtocolLibrary.getProtocolManager().addPacketListener(chunk);
	}

	private static ChunkMap modify(ChunkMap map, Map<Integer, List<GBlock>> toChange)
	{
		int changedSections = createBitmask(toChange.keySet());
		int fullBitmask = map.b | changedSections;
		//Writing blocks to section
		int sstart = 0;
		for (int s = 0; s < 16; s++)
		{
			if ((fullBitmask & 1 << s) != 0)
			{
				if ((changedSections & 1 << s) != 0)
				{
					for (GBlock fb : toChange.get(s))
					{
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

	private static int createBitmask(Collection<Integer> sections)
	{
		int i = 0;
		for (int section : sections)
		{
			i |= 1 << section;
		}
		return i;
	}

	private class GBlock
	{

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
