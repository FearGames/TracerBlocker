package cz.GravelCZLP.TracerBlocker.v1_9.ChestHider;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedBlockData;

import cz.GravelCZLP.TracerBlocker.MathUtils;
import cz.GravelCZLP.TracerBlocker.Common.ChestHider.AbstractChestHider;
import cz.GravelCZLP.TracerBlocker.v1_9.Packets.WrapperPlayServerBlockChange;

public class ChestHider1_9 extends AbstractChestHider {

	public ChestHider1_9(MathUtils mathUtils) {
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
