package cz.GravelCZLP.TracerBlocker.v1_12;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;

import cz.GravelCZLP.TracerBlocker.Settings;
import cz.GravelCZLP.TracerBlocker.TracerBlocker;
import cz.GravelCZLP.TracerBlocker.Common.Loader;
import cz.GravelCZLP.TracerBlocker.Common.ChestHider.AbstractChestHider;
import cz.GravelCZLP.TracerBlocker.Common.FakePlayer.AbstractFakePlayer;
import cz.GravelCZLP.TracerBlocker.Common.PlayerHider.AbstractPlayerHider;
import cz.GravelCZLP.TracerBlocker.v1_12.ChestHider.ChestHider1_12;
import cz.GravelCZLP.TracerBlocker.v1_12.ChestHider.PacketChestHider1_12;
import cz.GravelCZLP.TracerBlocker.v1_12.FakePlayer.FakePlayer1_12;
import cz.GravelCZLP.TracerBlocker.v1_12.Packets.WrapperPlayServerEntityMetadata;
import cz.GravelCZLP.TracerBlocker.v1_12.PlayerHider.PlayerHider1_12;

/**
 * Created by GravelCZLP on 4.7.17.
 */
public class Loader_v1_12 extends Loader {

	private TracerBlocker tracerBlocker;
	private ProtocolManager protocolManager;
	private PacketChestHider1_12 packetChestHider;
	private AbstractPlayerHider playerHider;
	private AbstractChestHider chestHider;

	public Loader_v1_12(TracerBlocker tracerBlocker, ProtocolManager protocolManager) {
		this.tracerBlocker = tracerBlocker;
		this.protocolManager = protocolManager;
	}

	@Override
	public void onEnable() {
		setupProtocol();

		if (Settings.PlayerHider.enabled) {
			playerHider = new PlayerHider1_12(tracerBlocker);
			
			tracerBlocker.getServer().getScheduler().runTaskTimer(tracerBlocker, new Runnable() {
				@Override
				public void run() {
					playerHider.checkVisibility();
				}
			}, 1, Settings.PlayerHider.everyTicks);
			
		}
		if (Settings.ChestHider.enabled) {
			chestHider = new ChestHider1_12();
			
			if (Settings.Test.packetAntiChestEsp) {
				packetChestHider = new PacketChestHider1_12(protocolManager);
				packetChestHider.setup();
			}
			
			tracerBlocker.getServer().getScheduler().runTaskTimer(tracerBlocker, new Runnable() {
				@Override
				public void run() {
					chestHider.checkChestVisibility();
				}
			}, 1, Settings.ChestHider.everyTicks);
		}
		if (Settings.FakePlayers.enabled) {
			
			tracerBlocker.getServer().getScheduler().runTaskTimer(tracerBlocker, new Runnable() {
				@Override
				public void run() {
					spawnFakePlayers();
				}
			}, 1, Settings.FakePlayers.everyTicks);
			
		}
	}

	@Override
	public void setupProtocol() {
		PacketAdapter adapter = new PacketAdapter(tracerBlocker, ListenerPriority.HIGHEST,
				PacketType.Play.Server.ENTITY_METADATA) {

			@Override
			public void onPacketSending(PacketEvent event) {
				WrapperPlayServerEntityMetadata metadata = new WrapperPlayServerEntityMetadata(event.getPacket());

				int eid = metadata.getEntityID();
				
				List<Entity> ents = event.getPlayer().getWorld().getEntities();
				
				for (Entity ent : ents) {
					if (ent.getEntityId() == eid) {
						if (ent.getType() != EntityType.PLAYER) {
							return; // Not a player, ignore the data.
						}
					}
				}
				
				if (event.getPlayer().getEntityId() != eid) {	
					WrappedDataWatcher watcher = new WrappedDataWatcher(metadata.getMetadata());
					
					if (watcher.hasIndex(7)) {
						watcher.setObject(7, WrappedDataWatcher.Registry.get(Float.class), 0.1F);	
					}
					
					if (watcher.hasIndex(11)) {
						if (watcher.getFloat(11) != 0F) {
							watcher.setObject(11, WrappedDataWatcher.Registry.get(Float.class), 0.0F);
						}	
					}
					
					metadata.setMetadata(watcher.getWatchableObjects());
					
					event.setPacket(metadata.getHandle());
				}
			}
		};
		if (Settings.Test.antiHealthTags) {
			protocolManager.addPacketListener(adapter);	
		}
	}

	@Override
	public void onDisable() {

	}

	@Override
	public void spawnFakePlayers() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (Settings.FakePlayers.disabledWorlds.contains(player.getLocation().getWorld().getName())) {
				continue;
			}
			Location fakeLocation;
			do {
				int x = 0, y = 0, z = 0;
				while (x < 16 && x > -16) {
					x = rand(-40, 40);
				}
				while (y < 10 && y > -10) {
					y = rand(-30, 20);
				}
				while (z < 16 && z > -16) {
					z = rand(-40, 40);
				}
				fakeLocation = player.getLocation().clone().add(x, y, z);
			} while (fakeLocation.distance(player.getLocation()) < Settings.FakePlayers.maxDistance);
			newFakePlayer(fakeLocation, player);
		}
	}

	@Override
	public AbstractFakePlayer newFakePlayer(Location fakeLocation, Player player) {
		if (player == null) {
			return null;
		}
		AbstractFakePlayer afp = new FakePlayer1_12(tracerBlocker, fakeLocation);
		afp.addObserver(player);
		return afp;
	}
}
