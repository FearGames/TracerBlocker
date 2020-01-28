package cz.GravelCZLP.TracerBlocker.Common.FakePlayer;

import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import com.comphenix.protocol.reflect.accessors.Accessors;
import com.comphenix.protocol.reflect.accessors.FieldAccessor;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.google.common.base.Preconditions;

import cz.GravelCZLP.TracerBlocker.RandomNameGenerator;
import cz.GravelCZLP.TracerBlocker.Settings;

public abstract class AbstractFakePlayer {
	/**
	 * Retrieve the entity counter field used to generate a unique entity ID.
	 */
	protected static final FieldAccessor ENTITY_ID = Accessors.getFieldAccessor(
			MinecraftReflection.getEntityClass(),
			"entityCount", true);
	
	/**
	 * Player that will see this entity.
	 */
	public Player observer;

	protected Location clientLocation;
	
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
	private Runnable task;
	// Update task id
	private final int taskId;
	
	protected Vector v;
	
	public AbstractFakePlayer(final Plugin plugin, Location location) {
		this.clientLocation = Preconditions.checkNotNull(location, "Location cannot be null");
		this.serverLocation = clientLocation.clone();
		
		v = Vector.getRandom();
		v.setX(v.getX() - 0.5f);
		v.setZ(v.getZ() - 0.5f);
		v.setY(v.getY() / 5);
		
		this.name = RandomNameGenerator.getRandomName();
		this.uuid = UUID.randomUUID();
		this.entityId = (Integer) ENTITY_ID.get(null);
		// Increment next entity ID
		ENTITY_ID.set(null, entityId + 1);
		// Background worker
		task = new Runnable() {
			int i = 0;

			@Override
			public void run() {
				if (i > Settings.FakePlayers.secondsAlive * (20 / Settings.FakePlayers.speed)) {
					destroy();
					return;
				}
				maybeDestroyEntity();
				if (Settings.FakePlayers.moving) {
					moveEntity();
					updateEntity();
				}
				i++;
			}
		};
		BukkitTask bt = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, task, 0, Settings.FakePlayers.speed);
		taskId = bt.getTaskId();
	}

	private void maybeDestroyEntity() {
		if (observer == null || !observer.isOnline()) {
			destroy();
			return;
		}
		if (observer.getLocation().getWorld().equals(serverLocation.getWorld())) {
			if (serverLocation.getY() < 0) {
				destroy();
			}
			if (serverLocation.getY() > 250) {
				destroy();
			}
			/*if (observer.getLocation().distance(serverLocation) < Settings.FakePlayers.maxDistance) {
				destroy();
				observer.sendMessage("Deleted..");
			}*/
		}
	}

	private void moveEntity() {		
		serverLocation.add(v.getX() / 100, v.getY() / 100, v.getZ() / 100);
	}

	public void setObserver(Player player) {
		observer = player;
		notifySpawnEntity(player);
		changed = true;
	}

	private void updateEntity() {
		// Detect changes
		if (changed) {
			notifySpawnEntity(observer);
			changed = false;
		} else if (!serverLocation.equals(clientLocation)) {
			broadcastMoveEntity(observer);
			clientLocation = serverLocation.clone();
		}
	}

	protected abstract void notifySpawnEntity(Player player);

	protected abstract void sendAddPlayerTab(Player player);

	protected abstract void sendRemovePlayerTab(Player player);

	protected abstract void broadcastMoveEntity(Player player);

	protected abstract void removeObserver(Player player);

	/**
	 * Destroy the current entity.
	 */
	public void destroy() {
		Bukkit.getScheduler().cancelTask(taskId);

		removeObserver(observer);
	}

	public int getEntityId() {
		return entityId;
	}

	public Player getObserver() {
		return observer;
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
	
	protected float randomHealth() {
		return new Random().nextFloat();
	}

	protected int randomPing() {
		return new Random().nextInt(1000);
	}
	protected int randomArrows() {
		return new Random().nextInt(5);
	}
}
