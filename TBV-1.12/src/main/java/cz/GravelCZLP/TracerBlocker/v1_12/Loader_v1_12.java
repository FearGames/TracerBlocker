package cz.GravelCZLP.TracerBlocker.v1_12;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;

import cz.GravelCZLP.TracerBlocker.RayTrace;
import cz.GravelCZLP.TracerBlocker.Settings;
import cz.GravelCZLP.TracerBlocker.TracerBlocker;
import cz.GravelCZLP.TracerBlocker.Utils;
import cz.GravelCZLP.TracerBlocker.Vector3D;
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
		if (Settings.Test.antiHealthTags) {
			setupProtocol();
		}

		if (Settings.PlayerHider.enabled) {
			playerHider = new PlayerHider1_12(tracerBlocker);
			tracerBlocker.getServer().getScheduler().runTaskTimer(tracerBlocker, new Runnable() {
				@Override
				public void run() {
					checkVisibility();
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

				if (event.getPlayer().getEntityId() == metadata.getEntityID()) {
					event.setCancelled(true);
					return;
				}

				WrappedDataWatcher watcher = new WrappedDataWatcher(metadata.getMetadata());
				
				watcher.setObject(7, WrappedDataWatcher.Registry.get(Float.class), 0.1F);
				
				metadata.setMetadata(watcher.getWatchableObjects());
				
				event.setPacket(metadata.getHandle());
			}
		};
		protocolManager.addPacketListener(adapter);
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
			System.out.println("Player cannot be null.");
		}
		AbstractFakePlayer afp = new FakePlayer1_12(tracerBlocker, fakeLocation);
		afp.addObserver(player);
		return afp;
	}

	@Override
	public void checkVisibility() {
		for (Player a : Bukkit.getOnlinePlayers()) {
			for (Player b : Bukkit.getOnlinePlayers()) {
				if (a.equals(b)) {
					continue;
				}
				if (a.getWorld().equals(b.getWorld())) {
					if (Settings.PlayerHider.disabledWorlds.contains(a.getWorld().getName())) {
						continue;
					}
					double width = 0.48;
					Location targetAA = b.getLocation().clone().add(-width, 0, -width);
					Location targetBB = b.getLocation().clone().add(-width, 0, width);
					Location targetCC = b.getLocation().clone().add(width, 0, -width);
					Location targetDD = b.getLocation().clone().add(width, 0, width);
					
					Location targetEE = b.getLocation().clone().add(-width, 1.9, -width);
					Location targetFF = b.getLocation().clone().add(-width, 1.9, width);
					Location targetGG = b.getLocation().clone().add(width, 1.9, -width);
					Location targetHH = b.getLocation().clone().add(width, 1.9, width);
					
					Location targetII = b.getLocation().clone().add(0, 1.1, 0);
					
					double distance = a.getLocation().distance(targetAA);
					
					if (distance > Settings.PlayerHider.maxDistance) {
						continue;
					}
					if (!a.canSee(b)) {
						continue;
					}

					if (distance <= Settings.PlayerHider.ignoreDistance) {
						playerHider.showPlayer(a, b);
						continue;
					}
					
					if (a.hasPotionEffect(PotionEffectType.GLOWING) || b.hasPotionEffect(PotionEffectType.GLOWING)) {
						playerHider.showPlayer(a, b);
						continue;
					}
					
					RayTrace rt1 = new RayTrace(Vector3D.fromLocation(a.getEyeLocation()), Vector3D.fromLocation(targetAA));
					RayTrace rt2 = new RayTrace(Vector3D.fromLocation(a.getEyeLocation()), Vector3D.fromLocation(targetBB));
					RayTrace rt3 = new RayTrace(Vector3D.fromLocation(a.getEyeLocation()), Vector3D.fromLocation(targetCC));
					RayTrace rt4 = new RayTrace(Vector3D.fromLocation(a.getEyeLocation()), Vector3D.fromLocation(targetDD));
					RayTrace rt5 = new RayTrace(Vector3D.fromLocation(a.getEyeLocation()), Vector3D.fromLocation(targetEE));
					RayTrace rt6 = new RayTrace(Vector3D.fromLocation(a.getEyeLocation()), Vector3D.fromLocation(targetFF));
					RayTrace rt7 = new RayTrace(Vector3D.fromLocation(a.getEyeLocation()), Vector3D.fromLocation(targetGG));
					RayTrace rt8 = new RayTrace(Vector3D.fromLocation(a.getEyeLocation()), Vector3D.fromLocation(targetHH));
					RayTrace rt9 = new RayTrace(Vector3D.fromLocation(a.getEyeLocation()), Vector3D.fromLocation(targetII));

					boolean result1 = Utils.rayTractResult(rt1.raytrace(0.5), a.getWorld());
					boolean result2 = Utils.rayTractResult(rt2.raytrace(0.5), a.getWorld());
					boolean result3 = Utils.rayTractResult(rt3.raytrace(0.5), a.getWorld());
					boolean result4 = Utils.rayTractResult(rt4.raytrace(0.5), a.getWorld());
					boolean result5 = Utils.rayTractResult(rt5.raytrace(0.5), a.getWorld());
					boolean result6 = Utils.rayTractResult(rt6.raytrace(0.5), a.getWorld());
					boolean result7 = Utils.rayTractResult(rt7.raytrace(0.5), a.getWorld());
					boolean result8 = Utils.rayTractResult(rt8.raytrace(0.5), a.getWorld());
					boolean result9 = Utils.rayTractResult(rt9.raytrace(0.5), a.getWorld());

					if (!(result1 || result2 || result3 || result4 || result5 || result6 || result7 || result8 || result9)) {
						playerHider.hidePlayer(a, b);
					} else {
						playerHider.showPlayer(a, b);
					}
				}
			}
		}
	}
}
