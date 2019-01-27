package cz.GravelCZLP.TracerBlocker.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerShowEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	
	private boolean canelled = false;
	
	private Player observer, entity;
	private boolean[] rayTraceResults;
	private ShowReason reason;
	
	public PlayerShowEvent(Player observer, Player entity, ShowReason res, boolean[] rayTraceResults) {
		this.observer = observer;
		this.entity = entity;
		this.reason = res;
		this.rayTraceResults = rayTraceResults;
	}
	
	/**
	 * The player that will have the entity shown
	 * @return - The observer 
	 */
	public Player getObserver() {
		return observer;
	}
	/**
	 * The entity to be shown
	 * @return - The entity (Player)
	 */
	public Player getEntity() {
		return entity;
	}
	
	/**
	 * Returns an array of booleans that contain the ray trace results
	 * will be an empty array if the reason is not RAY_TRACE_RESULTS
	 * @return - array of ray trace results
	 */
	public boolean[] getRayTraceResults() {
		return rayTraceResults;
	}
	/**
	 * Why was the player shown
	 * @return - The reason
	 */
	public ShowReason getResult() {
		return reason;
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
	
	public static enum ShowReason {
		IGNORE_DISTANCE, GLOWING, RAY_TRACE_RESULTS;
	}
}
