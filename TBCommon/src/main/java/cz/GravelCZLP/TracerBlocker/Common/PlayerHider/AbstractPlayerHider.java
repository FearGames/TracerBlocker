package cz.GravelCZLP.TracerBlocker.Common.PlayerHider;

import java.util.Arrays;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.google.common.base.Preconditions;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import cz.GravelCZLP.TracerBlocker.MathUtils;
import cz.GravelCZLP.TracerBlocker.RayTrace;
import cz.GravelCZLP.TracerBlocker.Settings;
import cz.GravelCZLP.TracerBlocker.TracerBlocker;
import cz.GravelCZLP.TracerBlocker.Utils;
import cz.GravelCZLP.TracerBlocker.Vector3D;
import cz.GravelCZLP.TracerBlocker.Events.PlayerHideEvent;
import cz.GravelCZLP.TracerBlocker.Events.PlayerShowEvent;
import cz.GravelCZLP.TracerBlocker.Events.PlayerShowEvent.ShowReason;

/**
 * Copyright ${year} Luuk Jacobs
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

public abstract class AbstractPlayerHider {
	
	protected Table<Integer, Integer, Boolean> observerEntityMap = HashBasedTable.create();
	protected ProtocolManager manager;

	/**
	 * Construct a new entity hider.
	 *
	 * @param plugin
	 *            - the plugin that controls this entity hider.
	 */
	public AbstractPlayerHider() {
		this.manager = ProtocolLibrary.getProtocolManager();
	}

	public void checkVisibility() {
		for (Player a : Bukkit.getOnlinePlayers()) {
			for (Player b : Bukkit.getOnlinePlayers()) {
				if (a.getUniqueId().toString().equals(b.getUniqueId().toString())) {
					continue;
				}
				if (a.getGameMode() == GameMode.SPECTATOR || b.getGameMode() == GameMode.SPECTATOR) {
					continue;
				}
				
				if (a.getWorld().equals(b.getWorld())) {
					if (Settings.PlayerHider.disabledWorlds.contains(a.getWorld().getName())) {
						continue;
					}
					
					Location targetAA = b.getLocation().clone().add(-0.48, 0, -0.48);
					
					double distance = a.getLocation().distance(targetAA);
					
					if (distance > Settings.PlayerHider.maxDistance) {
						continue;
					}

					if (distance <= Settings.PlayerHider.ignoreDistance) {
						PlayerShowEvent e = new PlayerShowEvent(a, b, ShowReason.IGNORE_DISTANCE, new boolean[0][0]);
						Bukkit.getPluginManager().callEvent(e);
						if (!e.isCancelled()) {
							showPlayer(a, b);	
						}
						continue;
					}
					
					/*if (b.hasPotionEffect(PotionEffectType.GLOWING)) {
						PlayerShowEvent e = new PlayerShowEvent(a, b, ShowReason.GLOWING, new boolean[0][0]);
						Bukkit.getPluginManager().callEvent(e);
						if (!e.isCancelled()) {
							showPlayer(a, b);	
						}
						continue;
					}*/
					
					Vector3D acualEye = MathUtils.toUnitVector(
							Vector3D.fromLocation(a.getLocation()), 0.2,
							a.getLocation().getYaw(), a.getLocation().getPitch());
					
					boolean[] resultNormal = Utils.checkPlayer(acualEye, b.getLocation(), a.getEyeHeight());
					boolean[] resultback = new boolean[0];
					boolean[] resultfront = new boolean[0];
					
					if (Settings.PlayerHider.calulatef5) {
						Location eyeLoc = a.getEyeLocation();

						RayTrace front = new RayTrace(Vector3D.fromLocation(eyeLoc), eyeLoc.getYaw(), eyeLoc.getPitch(), 4.1);
						RayTrace back = new RayTrace(Vector3D.fromLocation(eyeLoc), eyeLoc.getYaw() + 180, -eyeLoc.getPitch(), 4.1);

						Vector3D endFront = front.getEnd();
						Vector3D endBack = back.getEnd();

						World world = a.getWorld();
						
						for (Vector3D vec : front.raytrace(0.1)) {
							Block bl = vec.toLocation(world).getBlock();
							if (Settings.Test.debug) {
								//Utils.showParticle(Vector3D.fromLocation(vec.toLocation(world)), 1f, 1f, 0f);
							}
							if (!Utils.isTransparent(bl)) {
								endFront = vec;
								break;
							}
						}
						for (Vector3D vec : back.raytrace(0.1)) {
							Block bl = vec.toLocation(world).getBlock();
							if (Settings.Test.debug) {	
								//Utils.showParticle(Vector3D.fromLocation(vec.toLocation(world)), 1f, 1f, 0f);
							}
							if (!Utils.isTransparent(bl)) {
								endBack = vec;
								break;
							}
						}

						if (Settings.Test.debug) {
							MathUtils.renderAxisHelper(endFront.toLocation(world), 1);
							MathUtils.renderAxisHelper(endBack.toLocation(world), 1);
						}
						
						resultback = Utils.checkPlayer(endBack, b.getLocation(), 0);
						resultfront = Utils.checkPlayer(endFront, b.getLocation(), 0);
					}
					
					if (!(Utils.sumBooleans(resultNormal) || Utils.sumBooleans(resultfront) || Utils.sumBooleans(resultback))) {
						PlayerHideEvent e = new PlayerHideEvent(a, b, new boolean[][] {
							resultNormal,
							resultback,
							resultfront
							});
						Bukkit.getPluginManager().callEvent(e);
						if (!e.isCancelled()) {
							hidePlayer(a, b);
						}
					} else {
						PlayerShowEvent e = new PlayerShowEvent(a, b, ShowReason.GLOWING, new boolean[][] {
							resultNormal,
							resultback,
							resultfront
							}
						);
						Bukkit.getPluginManager().callEvent(e);
						if (!e.isCancelled()) {
							showPlayer(a, b);	
						}
					}
				}
			}
		}
	}
	
	public void hidePlayer(Player a, Player b) {
		hideEntity(a, b);
	}

	// Current policy

	public void showPlayer(Player a, Player b) {
		showEntity(a, b);
	}

	/**
	 * Set the visibility status of a given entity for a particular observer.
	 *
	 * @param observer
	 *            - the observer player.
	 * @param entityID
	 *            - ID of the entity that will be hidden or made visible.
	 * @param visible
	 *            - TRUE if the entity should be made visible, FALSE if not.
	 * @return TRUE if the entity was visible before this method call, FALSE
	 *         otherwise.
	 */
	protected boolean setVisibility(Player observer, int entityID, boolean visible) {
		return !setMembership(observer, entityID, !visible);
	}

	/**
	 * Add or remove the given entity and observer entry from the table.
	 *
	 * @param observer
	 *            - the player observer.
	 * @param entityID
	 *            - ID of the entity.
	 * @param member
	 *            - TRUE if they should be present in the table, FALSE
	 *            otherwise.
	 * @return TRUE if they already were present, FALSE otherwise.
	 */
	// Helper method
	protected boolean setMembership(Player observer, int entityID, boolean member) {
		if (member) {
			return observerEntityMap.put(observer.getEntityId(), entityID, true) != null;
		}
		else {
			return observerEntityMap.remove(observer.getEntityId(), entityID) != null;
		}
	}

	/**
	 * Determine if the given entity and observer is present in the table.
	 *
	 * @param observer
	 *            - the player observer.
	 * @param entityID
	 *            - ID of the entity.
	 * @return TRUE if they are present, FALSE otherwise.
	 */
	protected boolean getMembership(Player observer, int entityID) {
		return observerEntityMap.contains(observer.getEntityId(), entityID);
	}

	/**
	 * Construct the Bukkit event listener.
	 *
	 * @return Our listener.
	 */
	public Listener constructBukkit() {
		return new Listener() {

			@EventHandler
			public void onPlayerQuit(PlayerQuitEvent e) {
				removePlayer(e.getPlayer());
			}
		};
	}

	/**
	 * Determine if a given entity is visible for a particular observer.
	 *
	 * @param observer
	 *            - the observer player.
	 * @param entityID
	 *            - ID of the entity that we are testing for visibility.
	 * @return TRUE if the entity is visible, FALSE otherwise.
	 */
	protected boolean isVisible(Player observer, int entityID) {
		// If we are using a whitelist, presence means visibility - if not, the
		// opposite is the case

		return !getMembership(observer, entityID);
	}

	/**
	 * Invoked when a player logs out.
	 *
	 * @param player
	 *            - the player that jused logged out.
	 */
	synchronized protected void removePlayer(Player player) {
		Map<Integer, Boolean> row = observerEntityMap.row(player.getEntityId());
		for (int entityID : row.keySet()) {
			observerEntityMap.remove(player.getEntityId(), entityID);
		}
	}

	/**
	 * Construct the packet listener that will be used to intercept every
	 * entity-related packet.
	 *
	 * @param plugin
	 *            - the parent plugin.
	 * @return The packet listener.
	 */
	public abstract PacketAdapter constructProtocol(Plugin plugin);

	/**
	 * Allow the observer to see an entity that was previously hidden.
	 *
	 * @param observer
	 *            - the observer.
	 * @param entity
	 *            - the entity to show.
	 * @return TRUE if the entity was hidden before, FALSE otherwise.
	 */
	public final boolean showEntity(Player observer, Entity entity) {
		validate(observer, entity);
		boolean hiddenBefore = !setVisibility(observer, entity.getEntityId(), true);

		// Resend packets
		if (manager != null && hiddenBefore) {
			Bukkit.getScheduler().runTask(TracerBlocker.getInstance(), () -> {
				manager.updateEntity(entity, Arrays.asList(observer));
			});
		}
		return hiddenBefore;
	}

	/**
	 * Prevent the observer from seeing a given entity.
	 *
	 * @param observer
	 *            - the player observer.
	 * @param entity
	 *            - the entity to hide.
	 * @return TRUE if the entity was previously visible, FALSE otherwise.
	 */
	public abstract boolean hideEntity(Player observer, Entity entity);

	/**
	 * Determine if the given entity has been hidden from an observer.
	 * <p/>
	 * Note that the entity may very well be occluded or out of range from the
	 * perspective of the observer. This method simply checks if an entity has
	 * been completely hidden for that observer.
	 *
	 * @param observer
	 *            - the observer.
	 * @param entity
	 *            - the entity that may be hidden.
	 * @return TRUE if the player may see the entity, FALSE if the entity has
	 *         been hidden.
	 */
	public boolean canSee(Player observer, Entity entity) {
		validate(observer, entity);

		return isVisible(observer, entity.getEntityId());
	}

	// For valdiating the input parameters
	protected void validate(Player observer, Entity entity) {
		Preconditions.checkNotNull(observer, "observer cannot be NULL.");
		Preconditions.checkNotNull(entity, "entity cannot be NULL.");
	}

}
