package cz.GravelCZLP.TracerBlocker.v1_12;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import cz.GravelCZLP.TracerBlocker.Common.ChestHider.AbstractChestHider;
import cz.GravelCZLP.TracerBlocker.Common.PlayerHider.AbstractPlayerHider;
import cz.GravelCZLP.TracerBlocker.Loader;
import cz.GravelCZLP.TracerBlocker.MathUtils;
import cz.GravelCZLP.TracerBlocker.Settings;
import cz.GravelCZLP.TracerBlocker.TracerBlocker;
import cz.GravelCZLP.TracerBlocker.v1_12.ChestHider.ChestHider1_12;
import cz.GravelCZLP.TracerBlocker.v1_12.ChestHider.PacketChestHider1_12;
import cz.GravelCZLP.TracerBlocker.v1_12.FakePlayer.FakePlayer1_12;
import cz.GravelCZLP.TracerBlocker.v1_12.Packets.WrapperPlayServerEntityMetadata;
import cz.GravelCZLP.TracerBlocker.v1_12.PlayerHider.PlayerHider1_12;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Random;

/**
 * Created by GravelCZLP on 4.7.17.
 */
public class Loader_v1_12 extends Loader {

	private static final Random rand = new Random();
	private TracerBlocker tracerBlocker;
	private ProtocolManager protocolManager;
	private MathUtils mathUtils;
	private PacketChestHider1_12 packetChestHider;
	private AbstractPlayerHider playerHider;
	private AbstractChestHider chestHider;

	public Loader_v1_12(TracerBlocker tracerBlocker, ProtocolManager protocolManager) {
		this.tracerBlocker = tracerBlocker;
		this.protocolManager = protocolManager;
	}

	public static int rand(int min, int max) {
		return min + (rand).nextInt(max - min);
	}

	@Override
	public void onEnable() {
		if(Settings.Test.antiHealthTags) {
			setupProtocol();
		}

		mathUtils = new MathUtils();

		if(Settings.PlayerHider.enabled) {
			playerHider = new PlayerHider1_12(tracerBlocker);
			tracerBlocker.getServer().getScheduler().runTaskTimer(tracerBlocker, new Runnable() {
				@Override
				public void run() {
					checkVisibility();
				}
			}, 1, Settings.PlayerHider.everyTicks);
		}
		if(Settings.ChestHider.enabled) {
			chestHider = new ChestHider1_12(mathUtils);
			if(Settings.Test.packetAntiChestEsp) {
				packetChestHider = new PacketChestHider1_12(protocolManager, tracerBlocker);
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
				watcher.setObject(2, WrappedDataWatcher.Registry.get(Float.class), 0.1F);
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

	private void spawnFakePlayers() {
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

	private void newFakePlayer(Location fakeLocation, Player player) {
		new FakePlayer1_12(tracerBlocker, fakeLocation).addObserver(player);
	}

	private void checkVisibility() {
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
