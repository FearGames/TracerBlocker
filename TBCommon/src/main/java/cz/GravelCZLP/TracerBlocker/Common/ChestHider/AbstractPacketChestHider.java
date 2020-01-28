package cz.GravelCZLP.TracerBlocker.Common.ChestHider;

/**
 * Created by GravelCZLP on 4.7.17.
 */
public abstract class AbstractPacketChestHider {

	protected AbstractChestHider ach;
	
	public AbstractPacketChestHider(AbstractChestHider ach)
	{
		this.ach = ach;
	}
	
	public abstract void setup();

}
