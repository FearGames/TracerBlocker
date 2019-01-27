package cz.GravelCZLP.TracerBlocker.Common;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import cz.GravelCZLP.TracerBlocker.Common.FakePlayer.AbstractFakePlayer;

/**
 * Created by GravelCZLP on 4.7.17.
 */
public abstract class Loader {
	
	private static final Random rand = new Random();
	
	public static int rand(int min, int max) {
		return min + (rand).nextInt(max - min);
	}
	
	public abstract void onDisable();

	public abstract void onEnable();

	public abstract void setupProtocol();
	
	public abstract void spawnFakePlayers();
	
	public abstract AbstractFakePlayer newFakePlayer(Location fakeLocation, Player player);
	
}
