
package cz.GravelCZLP.TracerBlocker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;

import cz.GravelCZLP.TracerBlocker.Common.Loader;

public class TracerBlocker extends JavaPlugin {

	private Loader currentLoader;
	
	private static TracerBlocker instance;
	
	@Override
	public void onEnable() {
		Plugin pl = getServer().getPluginManager().getPlugin("ProtocolLib");
		if (pl == null) {
			getLogger().severe("TracerBlocker depends on ProtocolLib");
			this.getServer().getPluginManager().disablePlugin(this);
			return;
		}
		
		loadConfig();
		
		if (Settings.Test.debug) {
			System.out.println("##############################");
			System.out.println("#Tracer Blocker Debug Enabled#");
			System.out.println("##############################");
			System.out.println("Server Version:" + Bukkit.getVersion());
			System.out.println("Bukkit version: " + Bukkit.getBukkitVersion());	
			System.out.println("Maximum supported version " + Version.getMaximumSupported());
			System.out.println("Minumum supported version " + Version.getMinimumSupported());
			System.out.println("ProtocolLib version: " + ProtocolLibrary.getPlugin().getDescription().getVersion());
			System.out.println("Maximum supported version (ProtocolLib) " + ProtocolLibrary.MAXIMUM_MINECRAFT_VERSION);
			System.out.println("Minimum supported version (ProtocolLib) " + ProtocolLibrary.MINIMUM_MINECRAFT_VERSION);
		}
		
		instance = this;
		
		String ver = Bukkit.getBukkitVersion();
		ver = ver.substring(0, ver.indexOf("-"));
		if (Settings.Test.debug) {
			System.out.println("Split version: " + ver);
		}
		
		boolean versionb = Version.isVersionSupported(ver);
		if(!versionb) {
			getServer().getLogger().warning("TracerBlocker does not support version " + ver);
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		currentLoader = Version.getLoaderByVersion(ver, this);
		
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
		//saveConfig();
	}

	private void loadConfig() {
		saveDefaultConfig();
		Settings.PlayerHider.enabled = getConfig().getBoolean("playerhider.enabled", true);
		Settings.PlayerHider.everyTicks = getConfig().getInt("playerhider.every-ticks", 2);
		Settings.PlayerHider.ignoreDistance = getConfig().getInt("playerhider.ignore-distance", 8);
		Settings.PlayerHider.maxDistance = getConfig().getInt("playerhider.max-distance", 50);
		Settings.PlayerHider.disabledWorlds = getConfig().getStringList("playerhider.disabledWorlds");
		Settings.PlayerHider.calulatef5 = getConfig().getBoolean("playerhider.calculatef5", false);
		Settings.PlayerHider.rtDist = getConfig().getDouble("playerhider.rtDist", 0.1);
		
		Settings.ChestHider.enabled = getConfig().getBoolean("chesthider.enabled", true);
		Settings.ChestHider.everyTicks = getConfig().getInt("chesthider.every-ticks", 5);
		Settings.ChestHider.ignoreDistance = getConfig().getInt("chesthider.ignore-distance", 8);
		Settings.ChestHider.maxDistance = getConfig().getInt("chesthider.max-distance", 32);
		Settings.ChestHider.disabledWorlds = getConfig().getStringList("chesthider.disabledWorlds");
		Settings.ChestHider.calulatef5 = getConfig().getBoolean("chesthider.calculatef5", false);
		Settings.ChestHider.rtDist = getConfig().getDouble("chesthider.rtDist", 0.5);
		
		Settings.FakePlayers.enabled = getConfig().getBoolean("fakeplayers.enabled", true);
		Settings.FakePlayers.moving = getConfig().getBoolean("fakeplayers.moving", true);
		Settings.FakePlayers.everyTicks = getConfig().getInt("fakeplayers.every-ticks", 40);
		Settings.FakePlayers.secondsAlive = getConfig().getInt("fakeplayers.seconds-alive", 5);
		Settings.FakePlayers.speed = getConfig().getInt("fakeplayers.speed", 3);
		Settings.FakePlayers.disabledWorlds = getConfig().getStringList("fakeplayers.disabledWorlds");
		Settings.FakePlayers.showArrows = getConfig().getBoolean("fakeplayers.showArrows", true);
		Settings.FakePlayers.maxDistance = getConfig().getDouble("fakeplayers.maxDistance", 16);
		
		Settings.Test.antiHealthTags = getConfig().getBoolean("antihealthTags", false);
		Settings.Test.packetAntiChestEsp = getConfig().getBoolean("packetAntiChestEsp", false);
		Settings.Test.debug = getConfig().getBoolean("debug", false);
	}

	/*public void saveConfig() {
		FileConfiguration config = getConfig();

		// Player hider
		config.set("playerhider.enabled", Settings.PlayerHider.enabled);
		config.set("playerhider.every-ticks", Settings.PlayerHider.everyTicks);
		config.set("playerhider.ignore-distance", Settings.PlayerHider.ignoreDistance);
		config.set("playerhider.max-distance", Settings.PlayerHider.maxDistance);
		config.set("playerhider.disabledWorlds", Settings.PlayerHider.disabledWorlds);

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
		config.set("fakeplayers.maxDistance", Settings.FakePlayers.maxDistance);

		config.set("debug", Settings.Test.debug);
		
		File configFile = new File(getDataFolder() + "/config.yml");
		try {
			config.save(configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/
	
	@Override
	public void saveDefaultConfig()
	{
		try
		{
			File folder = getDataFolder();
			if (!folder.exists()) {
				folder.mkdirs();
			}
			InputStream is = getResource("config.yml");
			File cfg = new File(getDataFolder() + "/config.yml");
			if (cfg.exists()) {
				return;
			}
			cfg.createNewFile();
			FileOutputStream fos = new FileOutputStream(cfg);
			IOUtils.copy(is, fos);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
	public static TracerBlocker getInstance() {
		return instance;
	}
}
