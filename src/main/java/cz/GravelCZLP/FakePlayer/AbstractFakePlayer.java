
package cz.GravelCZLP.FakePlayer;

import com.comphenix.protocol.reflect.accessors.Accessors;
import com.comphenix.protocol.reflect.accessors.FieldAccessor;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import cz.GravelCZLP.TracerBlocker.RandomNameGenerator;
import cz.GravelCZLP.TracerBlocker.Settings;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public abstract class AbstractFakePlayer {
	/**
	 * Retrieve the entity counter field used to generate a unique entity ID.
	 */
	protected static final FieldAccessor ENTITY_ID = Accessors.getFieldAccessor(
			MinecraftReflection.getEntityClass(),
			"entityCount", true);
	protected final Vector vector;

	/**
	 * List of Players that will see the entity
	 */
	public List<Player> observers = Lists.newArrayList();

	/**
	 * Client (Player) location
	 */
	protected Location clientLocation;
	/**
	 * Location of Client-Side Player
	 */
	protected Location serverLocation;
	/**
	 * id for this entity
	 */
	protected int entityId;
	/**
	 * Name of this Entity
	 */
	protected String name;
	/**
	 * UUID of this entity
	 */
	protected UUID uuid;
	/**
	 * if name has changed
	 */
	protected boolean changed;

	// Update task
	private BukkitTask task;

	public AbstractFakePlayer(final Plugin plugin, Location location) {
		this.clientLocation = Preconditions.checkNotNull(location, "Location cannot be NULL");
		this.serverLocation = clientLocation.clone();
		Vector v = Vector.getRandom(); // This makes a random vector but it
										// doesn't shoot all ways //
		v.setX(v.getX() - 0.5f);
		v.setZ(v.getZ() - 0.5f); // Now it does //
		v.setY(v.getY() / 5);
		vector = v.clone();
		this.name = RandomNameGenerator.getRandomName();
		this.uuid = UUID.randomUUID();
		this.entityId = (Integer) ENTITY_ID.get(null);
		// Increment next entity ID
		ENTITY_ID.set(null, entityId + 1);
		// Background worker
		task = new BukkitRunnable() {
			int i = 0;

			@Override
			public void run() {
				if (i > Settings.FakePlayers.secondsAlive * (20 / Settings.FakePlayers.speed)) {
					destroy();
					return;
				}
				if (Settings.FakePlayers.moving) {
					moveEntity();
					updateEntity();
				}
				maybeDestroyEntity();
				i++;
			}
		}.runTaskTimer(plugin, 1, Settings.FakePlayers.speed);
	}

	private void maybeDestroyEntity() {
		for (Player player : observers) {
			if (!player.getLocation().getWorld().equals(serverLocation.getWorld())) {
				continue;
			}
			if (player.getLocation().distance(serverLocation) < 16) {
				destroy();
				return;
			}
		}
	}

	private void moveEntity() {
		serverLocation.add(vector.getX() / 100, vector.getY() / 100, vector.getZ() / 100);
	}

	public void addObserver(Player player) {
		notifySpawnEntity(player);
		observers.add(player);
	}

	private void updateEntity() {
		// Detect changes
		if (changed) {
			for (Player player : observers) {
				notifySpawnEntity(player);
			}
			changed = false;

			// Update location
		}
		else if (!serverLocation.equals(clientLocation)) {
			broadcastMoveEntity();
			clientLocation = serverLocation.clone();
		}
	}

	protected abstract void notifySpawnEntity(Player player);

	protected abstract void sendAddPlayerTab(Player player);

	protected abstract void sendRemovePlayerTab(Player player);

	protected abstract void broadcastMoveEntity();

	protected abstract void removeObserver(Player player);

	/**
	 * Destroy the current entity.
	 */
	public void destroy() {
		task.cancel();

		for (Player player : Lists.newArrayList(observers)) {
			removeObserver(player);
		}
	}

	public int getEntityId() {
		return entityId;
	}

	/**
	 * Retrieve an immutable view of every player observing this entity.
	 *
	 * @return Every observer.
	 */
	public List<Player> getObservers() {
		return Collections.unmodifiableList(observers);
	}

	public Location getLocation() {
		return serverLocation;
	}

	public void setLocation(Location location) {
		this.serverLocation = location;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		this.changed = true;
	}

	protected int getRandomYaw() {
		return new Random().nextInt(360);
	}

	protected int getRandomPitch() {
		return new Random().nextInt(180) - 90;
	}

	protected float randomHealth() {
		return 0.1F;
	}

	protected int randomPing() {
		return new Random().nextInt(1000);
	}
	
	public void stopRunnble() {
		task.cancel();
	}
}
