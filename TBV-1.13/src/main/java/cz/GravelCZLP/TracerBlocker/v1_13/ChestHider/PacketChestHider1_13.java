package cz.GravelCZLP.TracerBlocker.v1_13.ChestHider;

import java.util.Iterator;
import java.util.List;

import org.bukkit.Location;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.nbt.NbtBase;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;

import cz.GravelCZLP.TracerBlocker.Settings;
import cz.GravelCZLP.TracerBlocker.TracerBlocker;
import cz.GravelCZLP.TracerBlocker.Common.ChestHider.AbstractPacketChestHider;

public class PacketChestHider1_13 extends AbstractPacketChestHider {

	private ProtocolManager manager;

	public PacketChestHider1_13(ProtocolManager manager) {
		this.manager = manager;
	}

	@Override
	public void setup() {
		PacketAdapter adapter = new PacketAdapter(TracerBlocker.getInstance(), ListenerPriority.HIGHEST, PacketType.
				Play.Server.MAP_CHUNK) {

			@Override
			public void onPacketSending(PacketEvent event) {//FIXME: this causes client side chest bug. TODO: how to fix: Remove the chest from the chunk data as well. The problem is parsing byte[] to usable data.
				List<NbtBase<?>> tileEnts = event.getPacket().getListNbtModifier().read(0);
				
				Iterator<NbtBase<?>> iter = tileEnts.iterator();
				while (iter.hasNext()) {
					NbtCompound compound = (NbtCompound) iter.next();
					int x = compound.getInteger("x");
					int y = compound.getInteger("x");
					int z = compound.getInteger("x");
					Location ploc = event.getPlayer().getLocation();
					double distanceSqrt = ploc.distanceSquared(new Location(ploc.getWorld(), x, y, z));
					
					String id = compound.getString("id");
					String name = id.split(":")[1];
					if (name.equalsIgnoreCase("chest") || name.equalsIgnoreCase("ender_chest") && distanceSqrt > Settings.ChestHider.maxDistance * 2) {
						iter.remove();
					}
				}
				
				event.getPacket().getListNbtModifier().write(0, tileEnts);
			}
		};
		manager.addPacketListener(adapter);
	}
	
}
