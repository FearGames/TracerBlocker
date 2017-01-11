
package cz.GravelCZLP.FakePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.comphenix.packetwrapper.WrapperPlayServerAnimation;
import com.comphenix.packetwrapper.WrapperPlayServerEntityDestroy;
import com.comphenix.packetwrapper.WrapperPlayServerEntityMetadata;
import com.comphenix.packetwrapper.WrapperPlayServerNamedEntitySpawn;
import com.comphenix.packetwrapper.WrapperPlayServerPlayerInfo;
import com.comphenix.packetwrapper.WrapperPlayServerRelEntityMoveLook;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedGameProfile;

public class FakePlayer1_11 extends AbstractFakePlayer {
	
	public FakePlayer1_11(Plugin plugin, Location location) {
		super(plugin, location);
	}

	protected WrappedDataWatcher getWatcher() {
		WrappedDataWatcher watcher = new WrappedDataWatcher();

		watcher.setObject(0, WrappedDataWatcher.Registry.get(Byte.class), (byte) 0x20);
		watcher.setObject(1, WrappedDataWatcher.Registry.get(Integer.class), 300);
		watcher.setObject(3, WrappedDataWatcher.Registry.get(String.class), name);
		watcher.setObject(7, WrappedDataWatcher.Registry.get(Float.class), 0.1F);
		return watcher;
	}

	@Override
	protected void removeObserver(Player player) {
		sendRemovePlayerTab(player);
		WrapperPlayServerEntityDestroy destroy = new WrapperPlayServerEntityDestroy();
		destroy.setEntityIds(new int[] { entityId });
		destroy.sendPacket(player);
		observers.remove(player);
	}

	protected void notifySpawnEntity(Player player) {
		sendAddPlayerTab(player);
		WrapperPlayServerNamedEntitySpawn spawned = new WrapperPlayServerNamedEntitySpawn();
		spawned.setEntityID(entityId);
		spawned.setPosition(serverLocation.toVector());
		spawned.setPlayerUUID(uuid);
		spawned.setYaw(serverLocation.getYaw());
		spawned.setPitch(serverLocation.getPitch());
		spawned.setMetadata(getWatcher());
		spawned.sendPacket(player);
		sendRemovePlayerTab(player);
	}

	protected void sendAddPlayerTab(Player player) {
		WrapperPlayServerPlayerInfo info = new WrapperPlayServerPlayerInfo();
		info.setAction(EnumWrappers.PlayerInfoAction.ADD_PLAYER);
		List<PlayerInfoData> dataList = new ArrayList<>();
		WrappedGameProfile profile = new WrappedGameProfile(uuid, name);
		WrappedChatComponent displayName = WrappedChatComponent.fromText(name);

		PlayerInfoData data = new PlayerInfoData(profile, 0, EnumWrappers.NativeGameMode.SURVIVAL, displayName);
		Object generic = PlayerInfoData.getConverter().getGeneric(MinecraftReflection.getPlayerInfoDataClass(), data);
		PlayerInfoData back = PlayerInfoData.getConverter().getSpecific(generic);
		dataList.add(back);
		info.setData(dataList);
		info.sendPacket(player);
	}

	protected void sendRemovePlayerTab(Player player) {
		WrapperPlayServerPlayerInfo info = new WrapperPlayServerPlayerInfo();
		info.setAction(EnumWrappers.PlayerInfoAction.REMOVE_PLAYER);
		List<PlayerInfoData> dataList = new ArrayList<>();
		WrappedGameProfile profile = new WrappedGameProfile(uuid, name);
		WrappedChatComponent displayName = WrappedChatComponent.fromText(name);

		PlayerInfoData data = new PlayerInfoData(profile, randomPing(), EnumWrappers.NativeGameMode.SURVIVAL, displayName);
		Object generic = PlayerInfoData.getConverter().getGeneric(MinecraftReflection.getPlayerInfoDataClass(), data);
		PlayerInfoData back = PlayerInfoData.getConverter().getSpecific(generic);
		dataList.add(back);
		info.setData(dataList);
		info.sendPacket(player);
	}

	protected void broadcastMoveEntity() {
		WrapperPlayServerRelEntityMoveLook move = new WrapperPlayServerRelEntityMoveLook();
		move.setEntityID(entityId);
		move.setDx(serverLocation.getX() - clientLocation.getX());
		move.setDy(serverLocation.getY() - clientLocation.getY());
		move.setDz(serverLocation.getZ() - clientLocation.getZ());

		Location loc = new Location(observers.get(0).getWorld(), move.getDx(), move.getDy(), move.getDz());
		Block b = loc.getWorld().getBlockAt(loc);
		boolean onGround = b.getRelative(BlockFace.DOWN).getType() != Material.AIR;

		move.setOnGround(onGround);

		move.setYaw(getRandomYaw());
		move.setPitch(getRandomPitch());

		WrapperPlayServerAnimation animation = new WrapperPlayServerAnimation();
		animation.setEntityID(entityId);
		animation.setAnimation(getRandomAnimation());

		WrapperPlayServerEntityMetadata metadata = new WrapperPlayServerEntityMetadata();
		WrappedDataWatcher watcher = new WrappedDataWatcher();
		if (new Random().nextInt(10) == 0) {
			watcher.setObject(0, WrappedDataWatcher.Registry.get(Byte.class), (byte) 0x02);
		}
		if (new Random().nextInt(10) == 0) {
			
		}
		watcher.setObject(7, WrappedDataWatcher.Registry.get(Float.class), (float) randomHealth());
		watcher.setObject(10, WrappedDataWatcher.Registry.get(Integer.class), randomArrows());
		metadata.setEntityID(entityId);
		metadata.setMetadata(watcher.getWatchableObjects());

		for (Player player : observers) {
			if (move != null) {
				move.sendPacket(player);
			}
			animation.sendPacket(player);
			metadata.sendPacket(player);
		}
	}

	protected int getRandomAnimation() {
		Random r = new Random();
		int i = r.nextInt(2);
		switch (i) {
		case 0:
			return 0;
		case 1:
			return 1;
		case 2:
			return 3;
		default:
			return 0;
		}
	}

	public void printInfo(Player toWho) {
		toWho.sendMessage("Entity Spawned !");
		toWho.sendMessage("Name: " + name);
		toWho.sendMessage("UUID: " + uuid);
		toWho.sendMessage("Entity ID: " + entityId);
	}

	public int randomArrows() {
		return new Random().nextInt(20);
	}
}
