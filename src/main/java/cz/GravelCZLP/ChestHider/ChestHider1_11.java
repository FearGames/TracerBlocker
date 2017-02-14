package cz.GravelCZLP.ChestHider;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.comphenix.packetwrapper.WrapperPlayServerBlockChange;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedBlockData;

import cz.GravelCZLP.TracerBlocker.MathUtils;

public class ChestHider1_11 extends AbstractChestHider {

	public ChestHider1_11(MathUtils mathUtils) {
		super(mathUtils); 
	}

	@Override
	public void changeBlock(Player player, Location location, Material type, byte data) {
		WrapperPlayServerBlockChange packet = new WrapperPlayServerBlockChange();
		packet.setLocation(new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
		if (location.getBlock().getType() == Material.ENDER_CHEST) {
			type = Material.REDSTONE_TORCH_ON;
		}
		packet.setBlockData(WrappedBlockData.createData(type, data));
		packet.sendPacket(player);
	}
}
