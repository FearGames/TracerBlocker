package cz.GravelCZLP.TracerBlocker.v1_12.ChestHider;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedBlockData;

import cz.GravelCZLP.TracerBlocker.TracerBlocker;
import cz.GravelCZLP.TracerBlocker.Common.ChestHider.AbstractPacketChestHider;
import cz.GravelCZLP.TracerBlocker.v1_12.Packets.WrapperPlayServerBlockChange;
import cz.GravelCZLP.TracerBlocker.v1_12.Packets.WrapperPlayServerMapChunk;

public class PacketChestHider1_12 extends AbstractPacketChestHider {

	private ProtocolManager manager;
	private JavaPlugin pl;

	public PacketChestHider1_12(ProtocolManager manager, JavaPlugin pl) {
		this.manager = manager;
		this.pl = pl;
	}

	@Override
	public void setup() {
		PacketAdapter adapter = new PacketAdapter(pl, ListenerPriority.HIGHEST, PacketType.
				Play.Server.MAP_CHUNK) {

			@Override
			public void onPacketSending(PacketEvent event) {
				boolean debug = false;
				if (pl instanceof TracerBlocker) {
					TracerBlocker tb = (TracerBlocker) pl;
					debug = tb.debug;
				}
				if (debug) {
					System.out.println("Sending Chunk packet.");
				}
				WrapperPlayServerMapChunk chunk = new WrapperPlayServerMapChunk(event.getPacket());
				World w = event.getPlayer().getWorld();
				if(debug) {
					System.out.println(w.getName());
				}
				Chunk c = w.getChunkAt(chunk.getChunkX(), chunk.getChunkZ());
				if(debug) {
					System.out.println("Chunk X: " + chunk.getChunkX() + " Chunk Z: " + chunk.getChunkZ());
				}
				BlockState[] tileEnts = c.getTileEntities();
				List<Location> locs = new ArrayList<>();
				for(BlockState bs : tileEnts) {
					if(bs.getType() == Material.CHEST || bs.getType() == Material.ENDER_CHEST || bs.getType() == Material.TRAPPED_CHEST) {
						locs.add(bs.getLocation());
					}
				}
				if(debug) {
					System.out.println("Is variable locs empty ? : " + locs.isEmpty());
				}
				for(Location loc : locs) {
					WrapperPlayServerBlockChange change = new WrapperPlayServerBlockChange();
					BlockPosition pos = new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
					change.setLocation(pos);
					change.setBlockData(WrappedBlockData.createData(Material.AIR));
					try {
						manager.sendServerPacket(event.getPlayer(), change.getHandle());
					} catch(InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			}
		};
		manager.addPacketListener(adapter);
	}
}
