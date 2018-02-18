package cz.GravelCZLP.TracerBlocker.v1_10.ChestHider;

import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import cz.GravelCZLP.TracerBlocker.Common.ChestHider.AbstractChestHider;
import cz.GravelCZLP.TracerBlocker.MathUtils;
import cz.GravelCZLP.TracerBlocker.v1_10.Packets.WrapperPlayServerBlockChange;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ChestHider1_10 extends AbstractChestHider {

	public ChestHider1_10(MathUtils mathUtils) {
		super(mathUtils); 
	}

	@Override
	public void changeBlock(Player player, Location location, Material type, byte data) {
		WrapperPlayServerBlockChange packet = new WrapperPlayServerBlockChange();
		packet.setLocation(new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
		packet.setBlockData(WrappedBlockData.createData(type, data));
		packet.sendPacket(player);
	}
}
