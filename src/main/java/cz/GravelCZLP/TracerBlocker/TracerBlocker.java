
package cz.GravelCZLP.TracerBlocker;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.packetwrapper.WrapperPlayServerEntityMetadata;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;

import cz.GravelCZLP.ChestHider.AbstractChestHider;
import cz.GravelCZLP.ChestHider.ChestHider1_11;
import cz.GravelCZLP.ChestHider.PacketChestHider;
import cz.GravelCZLP.FakePlayer.FakePlayer1_11;
import cz.GravelCZLP.PlayerHider.AbstractPlayerHider;
import cz.GravelCZLP.PlayerHider.PlayerHider1_11;
import cz.GravelCZLP.TracerBlocker.commands.TracerBlockerCommand;

public class TracerBlocker extends JavaPlugin {
	private static final Random rand = new Random();
	private AbstractPlayerHider playerHider;
	private AbstractChestHider chestHider;
	private ProtocolManager manager;
	private MathUtils mathUtils;
	private PacketChestHider packetChestHider;
	
	@Override
	public void onEnable() {
		if (!getServer().getVersion().contains("1.11")) {
			getLogger().warning("PrimoTracerBlocker currently only supports 1.11.x");
			this.getServer().getPluginManager().disablePlugin(this);
			return;
		}
		if (getServer().getPluginManager().getPlugin("PacketWrapper") == null) {
			getLogger().warning("TracerBlocker depends on PacketWrapper.");
			this.getServer().getPluginManager().disablePlugin(this);
			return;
		}
		if (getServer().getPluginManager().getPlugin("ProtocolLib") == null) {
			getLogger().warning("TracerBlocker depends on ProtocolLib");
			this.getServer().getPluginManager().disablePlugin(this);
			return;
		}
		mathUtils = new MathUtils();
		manager = ProtocolLibrary.getProtocolManager();
		loadConfig();

		Server server = getServer();

		server.getPluginCommand("tracerblocker").setExecutor(new TracerBlockerCommand(this));
		if (Settings.Test.antiHealthTags) {
			setupProtocol();	
		}

		getLogger().info(server.getVersion());
		if (Settings.PlayerHider.enabled) {
			if (getServer().getVersion().contains("1.11")) {
				playerHider = new PlayerHider1_11(this);
			}
			server.getScheduler().runTaskTimer(this, new Runnable() {
				@Override
				public void run() {
					checkVisibility();
				}
			}, 1, Settings.PlayerHider.everyTicks);
		}
		if (Settings.ChestHider.enabled ) {
			if (server.getVersion().contains("1.11")) {
				chestHider = new ChestHider1_11(mathUtils);
				if (Settings.Test.packetAntiChestEsp) {
					packetChestHider = new PacketChestHider(manager, this);
					packetChestHider.setup();	
				}
			}
			server.getScheduler().runTaskTimer(this, new Runnable() {
				@Override
				public void run() {
					chestHider.checkChestVisibility();
				}
			}, 1, Settings.ChestHider.everyTicks);
		}
		if (Settings.FakePlayers.enabled) {
			server.getScheduler().runTaskTimer(this, new Runnable() {
				@Override
				public void run() {
					spawnFakePlayers();
				}
			}, 1, Settings.FakePlayers.everyTicks);
		}
	}

	public void setupProtocol() {
		PacketAdapter adapter = new PacketAdapter(this, ListenerPriority.HIGHEST,
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
					return;
				}
				
				WrappedDataWatcher watcher = new WrappedDataWatcher();
				watcher.setObject(7, WrappedDataWatcher.Registry.get(Float.class), 0.1F);
				metadata.setMetadata(watcher.getWatchableObjects());
				event.setPacket(metadata.getHandle());
				return;
			}
		};
		manager.addPacketListener(adapter);
	}

	public void onDisable() {
		ProtocolLibrary.getProtocolManager().removePacketListeners(this);
		saveConfig();
	}

	public void loadConfig() {
		saveDefaultConfig();
		Settings.PlayerHider.enabled = getConfig().getBoolean("playerhider.enabled");
		Settings.PlayerHider.everyTicks = getConfig().getInt("playerhider.every-ticks");
		Settings.PlayerHider.ignoreDistance = getConfig().getInt("playerhider.ignore-distance");
		Settings.PlayerHider.maxDistance = getConfig().getInt("playerhider.max-distance");
		Settings.PlayerHider.disabledWorlds = getConfig().getStringList("playerhider.disabledWorlds");

		Settings.ChestHider.enabled = getConfig().getBoolean("chesthider.enabled");
		Settings.ChestHider.everyTicks = getConfig().getInt("chesthider.every-ticks");
		Settings.ChestHider.ignoreDistance = getConfig().getInt("chesthider.ignore-distance");
		Settings.ChestHider.maxDistance = getConfig().getInt("chesthider.max-distance");
		Settings.ChestHider.disabledWorlds = getConfig().getStringList("chesthider.disabledWorlds");

		Settings.FakePlayers.enabled = getConfig().getBoolean("fakeplayers.enabled");
		Settings.FakePlayers.moving = getConfig().getBoolean("fakeplayers.moving");
		Settings.FakePlayers.everyTicks = getConfig().getInt("fakeplayers.every-ticks");
		Settings.FakePlayers.secondsAlive = getConfig().getInt("fakeplayers.seconds-alive");
		Settings.FakePlayers.speed = getConfig().getInt("fakeplayers.speed");
		Settings.FakePlayers.disabledWorlds = getConfig().getStringList("fakeplayers.disabledWorlds");
		
		Settings.Test.antiHealthTags = getConfig().getBoolean("antihealthTags", false);
		Settings.Test.packetAntiChestEsp = getConfig().getBoolean("packetAntiChestEsp", false);
	}

	public void saveConfig() {
		FileConfiguration config = getConfig();

		// Player hider
		config.set("playerhider.enabled", Settings.PlayerHider.enabled);
		config.set("playerhider.every-ticks", Settings.PlayerHider.everyTicks);
		config.set("playerhider.ignore-distance", Settings.PlayerHider.ignoreDistance);
		config.set("playerhider.max-distance", Settings.PlayerHider.maxDistance);
		config.set("playerhider.disabledworlds", Settings.PlayerHider.disabledWorlds);

		// Chest hider
		config.set("chesthider.enabled", Settings.ChestHider.enabled);
		config.set("chesthider.every-ticks", Settings.ChestHider.everyTicks);
		config.set("chesthider.ignore-distance", Settings.ChestHider.ignoreDistance);
		config.set("chesthider.max-distance", Settings.ChestHider.maxDistance);
		config.set("chesthider.disabledWorlds", Settings.ChestHider.disabledWorlds);

		// Fake Players
		config.set("fakeplayers.enabled", Settings.FakePlayers.enabled);
		config.set("fakeplayers.moving", Settings.FakePlayers.moving);
		config.set("fakeplayers.every-ticks", Settings.FakePlayers.everyTicks);
		config.set("fakeplayers.seconds-alive", Settings.FakePlayers.secondsAlive);
		config.set("fakeplayers.speed", Settings.FakePlayers.speed);
		config.set("fakeplayers.disabledWorlds", Settings.FakePlayers.disabledWorlds);

		File configFile = new File(getDataFolder() + "/config.yml");
		try {
			config.save(configFile);
		}
		catch (IOException e) {
			e.printStackTrace();
			System.err.println(
					"Error occuren when tying to save config for Tracer Blocker, all settings set in game have been not saved ?:(");
		}
	}

	private void spawnFakePlayers() {
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
			}
			while (fakeLocation.distance(player.getLocation()) < 16);
			newFakePlayer(fakeLocation, player);
		}
	}

	private void newFakePlayer(Location fakeLocation, Player player) {
		if (getServer().getVersion().contains("1.11")) {
			new FakePlayer1_11(this, fakeLocation).addObserver(player);
		}
	}

	public static int rand(int min, int max) {
		return min + (rand).nextInt(max - min);
	}

	private void checkVisibility() {
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
					Location targetBB = b.getLocation().clone().add(width, 1.9, width);
					Location targetCC = b.getLocation().clone().add(0, 1.1, 0);
					int distance = (int) a.getLocation().distance(targetAA);

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
					if (a.isGlowing() || b.isGlowing()) {
						playerHider.showPlayer(a, b);
						continue;
					}
					try {

						if (mathUtils.getTargetBlock(mathUtils.lookAt(a.getEyeLocation(), targetAA), distance) == null
								|| mathUtils.getTargetBlock(mathUtils.lookAt(a.getEyeLocation(), targetBB), distance) == null
								|| mathUtils.getTargetBlock(mathUtils.lookAt(a.getEyeLocation(), targetCC), distance) == null) {
							playerHider.showPlayer(a, b);
						}
						else {
							playerHider.hidePlayer(a, b);
						}
					} catch (IllegalStateException ignored) {

					}
				}
			}
		}
	}
}
