package cz.GravelCZLP.TracerBlocker.v1_8;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;

import cz.GravelCZLP.TracerBlocker.Settings;
import cz.GravelCZLP.TracerBlocker.TracerBlocker;
import cz.GravelCZLP.TracerBlocker.Common.Loader;
import cz.GravelCZLP.TracerBlocker.Common.ChestHider.AbstractChestHider;
import cz.GravelCZLP.TracerBlocker.Common.ChestHider.AbstractPacketChestHider;
import cz.GravelCZLP.TracerBlocker.Common.FakePlayer.AbstractFakePlayer;
import cz.GravelCZLP.TracerBlocker.Common.PlayerHider.AbstractPlayerHider;
import cz.GravelCZLP.TracerBlocker.v1_8.ChestHider.ChestHider1_8;
import cz.GravelCZLP.TracerBlocker.v1_8.ChestHider.PacketChestHider1_8;
import cz.GravelCZLP.TracerBlocker.v1_8.FakePlayer.FakePlayer1_8;
import cz.GravelCZLP.TracerBlocker.v1_8.Packets.WrapperPlayServerEntityMetadata;
import cz.GravelCZLP.TracerBlocker.v1_8.PlayerHider.PlayerHider1_8;

/**
 * Created by GravelCZLP on 4.7.17.
 */
public class Loader_v1_8 extends Loader {

	private TracerBlocker tracerBlocker;
	
	private AbstractPacketChestHider packetChestHider;
	private AbstractPlayerHider playerHider;
	private AbstractChestHider chestHider;

	public Loader_v1_8(TracerBlocker tracerBlocker) {
		this.tracerBlocker = tracerBlocker;
	}

	@Override
	public void onEnable() {
		setupProtocol();

		if (Settings.PlayerHider.enabled) {
			playerHider = new PlayerHider1_8();
			
			Bukkit.getServer().getPluginManager().registerEvents(playerHider.constructBukkit(), tracerBlocker);
			ProtocolLibrary.getProtocolManager().addPacketListener(playerHider.constructProtocol(tracerBlocker));
			
			tracerBlocker.getServer().getScheduler().runTaskTimerAsynchronously(tracerBlocker, new Runnable() {
				@Override
				public void run() {
					playerHider.checkVisibility();
				}
			}, 0, Settings.PlayerHider.everyTicks);
			
		}
		if (Settings.ChestHider.enabled) {
			chestHider = new ChestHider1_8();
			
			Bukkit.getPluginManager().registerEvents(chestHider.initBukkit(), tracerBlocker);
			
			if (Settings.Test.packetAntiChestEsp) {
				packetChestHider = new PacketChestHider1_8(chestHider);
				packetChestHider.setup();
			}
			
			tracerBlocker.getServer().getScheduler().runTaskTimerAsynchronously(tracerBlocker, new Runnable() {
				@Override
				public void run() {
					chestHider.checkChestVisibility();
				}
			}, 0, Settings.ChestHider.everyTicks);
		}
		if (Settings.FakePlayers.enabled) {
			
			tracerBlocker.getServer().getScheduler().runTaskTimerAsynchronously(tracerBlocker, new Runnable() {
				@Override
				public void run() {
					spawnFakePlayers();
				}
			}, 0, Settings.FakePlayers.everyTicks);
			
		}
	}

	@Override
	public void setupProtocol() {
		if (Settings.Test.antiHealthTags) {
			PacketAdapter adapter = new PacketAdapter(tracerBlocker, ListenerPriority.HIGHEST,
						PacketType.Play.Server.ENTITY_METADATA) {

				@Override
				public void onPacketSending(PacketEvent event) {
					WrapperPlayServerEntityMetadata metadata = new WrapperPlayServerEntityMetadata(event.getPacket());

					if (!(metadata.getEntity(event) instanceof Player)) {
						return;
					}
					
					int eid = metadata.getEntityID();
					Player reciever = event.getPlayer();
					
					if (eid == reciever.getEntityId()) {
						event.setCancelled(true);
						return;
					}
					
					WrappedDataWatcher watcher = new WrappedDataWatcher(metadata.getMetadata());
					
					watcher.setObject(6, (float) 20F);
					
					if (watcher.hasIndex(17) && watcher.getFloat(17) != 0.0F) {
						watcher.setObject(17, (float) 0.0F);
					}
					
					metadata.setMetadata(watcher.getWatchableObjects());
					
					event.setPacket(metadata.getHandle());
				}
			};
				
			ProtocolLibrary.getProtocolManager().addPacketListener(adapter);	
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
		AbstractFakePlayer afp = new FakePlayer1_8(tracerBlocker, fakeLocation);
		afp.setObserver(player);
		return afp;
	}
}
