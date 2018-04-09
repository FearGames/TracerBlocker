package cz.GravelCZLP.TracerBlocker.v1_9;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;

import cz.GravelCZLP.TracerBlocker.MathUtils;
import cz.GravelCZLP.TracerBlocker.Settings;
import cz.GravelCZLP.TracerBlocker.TracerBlocker;
import cz.GravelCZLP.TracerBlocker.Common.Loader;
import cz.GravelCZLP.TracerBlocker.Common.ChestHider.AbstractChestHider;
import cz.GravelCZLP.TracerBlocker.Common.FakePlayer.AbstractFakePlayer;
import cz.GravelCZLP.TracerBlocker.Common.PlayerHider.AbstractPlayerHider;
import cz.GravelCZLP.TracerBlocker.v1_9.ChestHider.ChestHider1_9;
import cz.GravelCZLP.TracerBlocker.v1_9.ChestHider.PacketChestHider1_9;
import cz.GravelCZLP.TracerBlocker.v1_9.FakePlayer.FakePlayer1_9;
import cz.GravelCZLP.TracerBlocker.v1_9.Packets.WrapperPlayServerEntityMetadata;
import cz.GravelCZLP.TracerBlocker.v1_9.PlayerHider.PlayerHider1_9;

/**
 * Created by GravelCZLP on 4.7.17.
 */
public class Loader_v1_9 extends Loader {

	private TracerBlocker tracerBlocker;
	private ProtocolManager protocolManager;
	private MathUtils mathUtils;
	private PacketChestHider1_9 packetChestHider;
	private AbstractPlayerHider playerHider;
	private AbstractChestHider chestHider;

	public Loader_v1_9(TracerBlocker tracerBlocker, ProtocolManager protocolManager) {
		this.tracerBlocker = tracerBlocker;
		this.protocolManager = protocolManager;
	}
	
	@Override
	public void onEnable() {
		if(Settings.Test.antiHealthTags) {
			setupProtocol();
		}

		mathUtils = new MathUtils();

		if(Settings.PlayerHider.enabled) {
			playerHider = new PlayerHider1_9(tracerBlocker);
			tracerBlocker.getServer().getScheduler().runTaskTimer(tracerBlocker, new Runnable() {
				@Override
				public void run() {
					checkVisibility();
				}
			}, 1, Settings.PlayerHider.everyTicks);
		}
		if(Settings.ChestHider.enabled) {
			chestHider = new ChestHider1_9(mathUtils);
			if(Settings.Test.packetAntiChestEsp) {
				packetChestHider = new PacketChestHider1_9(protocolManager, tracerBlocker);
				packetChestHider.setup();
			}
			tracerBlocker.getServer().getScheduler().runTaskTimer(tracerBlocker, new Runnable() {
				@Override
				public void run() {
					chestHider.checkChestVisibility();
				}
			}, 1, Settings.ChestHider.everyTicks);
		}
		if(Settings.FakePlayers.enabled) {
			tracerBlocker.getServer().getScheduler().runTaskTimer(tracerBlocker, new Runnable() {
				@Override
				public void run() {
					spawnFakePlayers();
				}
			}, 1, Settings.FakePlayers.everyTicks);
		}
	}

	public void setupProtocol() {
		PacketAdapter adapter = new PacketAdapter(tracerBlocker, ListenerPriority.HIGHEST, PacketType.Play.Server.ENTITY_METADATA) {

			@Override
			public void onPacketSending(PacketEvent event) {
				WrapperPlayServerEntityMetadata metadata = new WrapperPlayServerEntityMetadata(event.getPacket());

				if(!(metadata.getEntity(event) instanceof Player)) {
					return;
				}

				int eid = metadata.getEntityID();
				Player reciever = event.getPlayer();
				if(eid == reciever.getEntityId()) {
					return;
				}

				WrappedDataWatcher watcher = new WrappedDataWatcher();
				watcher.setObject(7, WrappedDataWatcher.Registry.get(Float.class), 0.1F);
				metadata.setMetadata(watcher.getWatchableObjects());
				event.setPacket(metadata.getHandle());
				return;
			}
		};
		protocolManager.addPacketListener(adapter);
	}

	@Override
	public void onDisable() {

	}

	public void spawnFakePlayers() {
		for(Player player : Bukkit.getOnlinePlayers()) {
			if(Settings.FakePlayers.disabledWorlds.contains(player.getLocation().getWorld().getName())) {
				continue;
			}
			Location fakeLocation;
			do {
				int x = 0, y = 0, z = 0;
				while(x < 16 && x > -16) {
					x = rand(-40, 40);
				}
				while(y < 10 && y > -10) {
					y = rand(-30, 20);
				}
				while(z < 16 && z > -16) {
					z = rand(-40, 40);
				}
				fakeLocation = player.getLocation().clone().add(x, y, z);
			} while(fakeLocation.distance(player.getLocation()) < 16);
			newFakePlayer(fakeLocation, player);
		}
	}

	public AbstractFakePlayer newFakePlayer(Location fakeLocation, Player player) {
		AbstractFakePlayer afp = new FakePlayer1_9(tracerBlocker, fakeLocation);
		afp.addObserver(player);
		return afp;
	}

	public void checkVisibility() {
		for(Player a : Bukkit.getOnlinePlayers()) {
			for(Player b : Bukkit.getOnlinePlayers()) {
				if(a.equals(b)) {
					continue;
				}
				if(a.getWorld().equals(b.getWorld())) {
					if(Settings.PlayerHider.disabledWorlds.contains(a.getWorld().getName())) {
						continue;
					}
					double width = 0.48;
					Location targetAA = b.getLocation().clone().add(-width, 0, -width);
					Location targetBB = b.getLocation().clone().add(width, 1.9, width);
					Location targetCC = b.getLocation().clone().add(0, 1.1, 0);
					int distance = (int) a.getLocation().distance(targetAA);

					if(distance > Settings.PlayerHider.maxDistance) {
						continue;
					}
					if(!a.canSee(b)) {
						continue;
					}

					if(distance <= Settings.PlayerHider.ignoreDistance) {
						playerHider.showPlayer(a, b);
						continue;
					}
					if(a.isGlowing() || b.isGlowing()) {
						playerHider.showPlayer(a, b);
						continue;
					}
					try {

						if(mathUtils.getTargetBlock(mathUtils.lookAt(a.getEyeLocation(), targetAA), distance) == null || mathUtils.getTargetBlock(mathUtils.lookAt(a.getEyeLocation(), targetBB), distance) == null || mathUtils.getTargetBlock(mathUtils.lookAt(a.getEyeLocation(), targetCC), distance) == null) {
							playerHider.showPlayer(a, b);
						} else {
							playerHider.hidePlayer(a, b);
						}
					} catch(IllegalStateException ignored) {

					}
				}
			}
		}
	}
}
