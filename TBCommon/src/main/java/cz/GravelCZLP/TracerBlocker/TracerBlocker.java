
package cz.GravelCZLP.TracerBlocker;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

import cz.GravelCZLP.TracerBlocker.Common.Loader;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class TracerBlocker extends JavaPlugin {
	private ProtocolManager manager;

	private Loader currentLoader;

	public boolean debug = false;
	
	@Override
	public void onEnable() {
		if (getServer().getPluginManager().getPlugin("ProtocolLib") == null) {
			getLogger().warning("TracerBlocker depends on ProtocolLib");
			this.getServer().getPluginManager().disablePlugin(this);
			return;
		}

		debug = true;
		
		if (debug) {
			System.out.println(Bukkit.getVersion());
			System.out.println(Bukkit.getBukkitVersion());	
		}
		
		String ver = Bukkit.getBukkitVersion();
		ver = ver.substring(0, ver.indexOf("-"));
		if (debug) {
			System.out.println(ver);
		}
		
		boolean versionb = Version.isVersionSupported(ver);
		if(!versionb) {
			getServer().getLogger().warning("TracerBlocker does not support version " + ver);
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		manager = ProtocolLibrary.getProtocolManager();

		currentLoader = Version.getLoaderByVersion(ver, this, manager);
		
		loadConfig();
		
		if (currentLoader == null) {
			getServer().getLogger().warning("Tracer blocker did not found any Version Loader for " + ver);
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		currentLoader.onEnable();
	}

	public void onDisable() {
		if(currentLoader != null) {
			currentLoader.onDisable();
		}
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
			System.err.println("Error occured when tying to save config for Tracer Blocker, all settings set in game have been not saved ?:(");
		}
	}
}
