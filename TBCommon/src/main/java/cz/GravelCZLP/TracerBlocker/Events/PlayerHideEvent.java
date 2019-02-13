package cz.GravelCZLP.TracerBlocker.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerHideEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	
	private boolean canelled = false;
	
	private Player observer, entity;
	private boolean[][] rayTraceResults;
	
	public PlayerHideEvent(Player observer, Player entity, boolean[][] rayTraceResults) {
		this.observer = observer;
		this.entity = entity;
	}
	/**
	 * The player that will have the entity hidden
	 * @return - The observer 
	 */
	public Player getObserver() {
		return observer;
	}
	/**
	 * The entity to be hidden
	 * @return - The entity (Player)
	 */
	public Player getEntity() {
		return entity;
	}
	
	/**
	 * Returns an array of booleans that contain the ray trace results
	 * will be an empty array if the reason is not RAY_TRACE_RESULTS
	 * 0 - normal raytrace result (first perspective)
	 * 1 - F5 (3rd person) raytrace result (if enabled in the config)
	 * 2 - F5 (2rd person) raytrace result (if enabled in the config)
	 * @return - array of ray trace results
	 */
	public boolean[][] getRayTraceResults() {
		return rayTraceResults;
	}
	
	@Override
	public boolean isCancelled() {
		return canelled;
	}

	@Override
	public void setCancelled(boolean b) {
		this.canelled = b;
	}
	 
	@Override
	public HandlerList getHandlers() {
	    return handlers;
	}
	 
	public static HandlerList getHandlerList() {
	    return handlers;
	}
}
