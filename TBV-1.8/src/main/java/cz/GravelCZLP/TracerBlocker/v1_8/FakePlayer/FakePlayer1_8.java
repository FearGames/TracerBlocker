package cz.GravelCZLP.TracerBlocker.v1_8.FakePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedGameProfile;

import cz.GravelCZLP.TracerBlocker.MathUtils;
import cz.GravelCZLP.TracerBlocker.Settings;
import cz.GravelCZLP.TracerBlocker.Vector3D;
import cz.GravelCZLP.TracerBlocker.Common.FakePlayer.AbstractFakePlayer;
import cz.GravelCZLP.TracerBlocker.v1_8.Packets.WrapperPlayServerAnimation;
import cz.GravelCZLP.TracerBlocker.v1_8.Packets.WrapperPlayServerEntityDestroy;
import cz.GravelCZLP.TracerBlocker.v1_8.Packets.WrapperPlayServerEntityMetadata;
import cz.GravelCZLP.TracerBlocker.v1_8.Packets.WrapperPlayServerEntityMoveLook;
import cz.GravelCZLP.TracerBlocker.v1_8.Packets.WrapperPlayServerNamedEntitySpawn;
import cz.GravelCZLP.TracerBlocker.v1_8.Packets.WrapperPlayServerPlayerInfo;

public class FakePlayer1_8 extends AbstractFakePlayer {

	public FakePlayer1_8(Plugin plugin, Location location) {
		super(plugin, location);
	}

	protected WrappedDataWatcher getWatcher() {
		WrappedDataWatcher watcher = new WrappedDataWatcher();

		watcher.setObject(0, (byte) 0x20);
		watcher.setObject(1, (int) 300);
		watcher.setObject(2, (String) name);
		watcher.setObject(6, (float) 0.1F);
		return watcher;
	}

	@Override
	protected void removeObserver(Player player) {
		sendRemovePlayerTab(player);
		
		WrapperPlayServerEntityDestroy destroy = new WrapperPlayServerEntityDestroy();
		
		destroy.setEntityIds(new int[]{ entityId });
		
		destroy.sendPacket(player);
		
		observer = null;
	}

	protected void notifySpawnEntity(Player player) {
		if (observer == null) {
			return;
		}
		sendAddPlayerTab(player);
		WrapperPlayServerNamedEntitySpawn spawned = new WrapperPlayServerNamedEntitySpawn();
		spawned.setEntityID(entityId);
		spawned.setX(serverLocation.getX());
		spawned.setY(serverLocation.getY());
		spawned.setZ(serverLocation.getZ());
		spawned.setPlayerUUID(uuid);
		spawned.setYaw(serverLocation.getYaw());
		spawned.setPitch(serverLocation.getPitch());
		spawned.setMetadata(getWatcher());
		spawned.sendPacket(player);
		sendRemovePlayerTab(player);
	}

	protected void sendAddPlayerTab(Player player) {
		if (observer == null) {
			return;
		}
		WrapperPlayServerPlayerInfo info = new WrapperPlayServerPlayerInfo();
		info.setAction(EnumWrappers.PlayerInfoAction.ADD_PLAYER);
		List<PlayerInfoData> dataList = new ArrayList<>();
		WrappedGameProfile profile = new WrappedGameProfile(uuid, name);
		WrappedChatComponent displayName = WrappedChatComponent.fromText(name);

		PlayerInfoData data = new PlayerInfoData(profile, 0, EnumWrappers.NativeGameMode.SURVIVAL, displayName);
		Object generic = PlayerInfoData.getConverter().getGeneric(data);//PlayerInfoData.getConverter().getGeneric(MinecraftReflection.getPlayerInfoDataClass(), data);
		PlayerInfoData back = PlayerInfoData.getConverter().getSpecific(generic);
		dataList.add(back);
		info.setData(dataList);
		info.sendPacket(player);
	}

	protected void sendRemovePlayerTab(Player player) {
		if (observer == null) {
			return;
		}
		WrapperPlayServerPlayerInfo info = new WrapperPlayServerPlayerInfo();
		info.setAction(EnumWrappers.PlayerInfoAction.REMOVE_PLAYER);
		List<PlayerInfoData> dataList = new ArrayList<>();
		WrappedGameProfile profile = new WrappedGameProfile(uuid, name);
		WrappedChatComponent displayName = WrappedChatComponent.fromText(name);

		PlayerInfoData data = new PlayerInfoData(profile, randomPing(), EnumWrappers.NativeGameMode.SURVIVAL, displayName);
		Object generic = PlayerInfoData.getConverter().getGeneric(data);//PlayerInfoData.getConverter().getGeneric(MinecraftReflection.getPlayerInfoDataClass(), data);
		
		PlayerInfoData back = PlayerInfoData.getConverter().getSpecific(generic);
		dataList.add(back);
		info.setData(dataList);
		info.sendPacket(player);
	}

	protected void broadcastMoveEntity(Player player) {
		if (player == null) {
			return;
		}
		WrapperPlayServerEntityMoveLook move = new WrapperPlayServerEntityMoveLook();
		move.setEntityID(entityId);

		double xChange = (serverLocation.getX() * 32 - clientLocation.getX() * 32) * 128;
		double yChange = (serverLocation.getY() * 32 - clientLocation.getY() * 32) * 128;
		double zChange = (serverLocation.getZ() * 32 - clientLocation.getZ() * 32) * 128;
		
		move.setDx(xChange);
		move.setDy(yChange);
		move.setDz(zChange);
		
		float[] angles = MathUtils.getAngles(Vector3D.fromLocation(serverLocation), Vector3D.fromLocation(player.getEyeLocation()));
		
		move.setYaw(angles[0] + 180.0f);
		move.setPitch(angles[1]);
		
		Location loc = new Location(player.getWorld(), serverLocation.getX(), serverLocation.getY(), serverLocation.getZ());
		Block b = loc.getWorld().getBlockAt(loc);
		boolean onGround = b.getRelative(BlockFace.DOWN).getType() != Material.AIR;

		move.setOnGround(onGround);
		
		boolean animate = new Random().nextInt(10) == 5;
		WrapperPlayServerAnimation animation = null;
		if (animate) {
			animation = new WrapperPlayServerAnimation();
			animation.setEntityID(entityId);
			animation.setAnimation(getRandomAnimation());
		}

		WrapperPlayServerEntityMetadata metadata = new WrapperPlayServerEntityMetadata();
		WrappedDataWatcher watcher = new WrappedDataWatcher();	
		
		//http://wiki.vg/Entity_metadata#Entity_Metadata_Format
		byte toSet = 0x20;
		boolean sneaking = new Random().nextInt(5) == 3;
		//boolean visible = new Random().nextInt(50) == 1;
		if (sneaking) {
			toSet += 0x02;
		}
		/*if (visible) {
			toSet -= 0x20;
		}*/
		watcher.setObject(0, (byte) toSet);
		
		watcher.setObject(6, (float) randomHealth()); // not needed when anti health tags are enabled, but still
		
		if (Settings.FakePlayers.showArrows) {
			watcher.setObject(9, (int) randomArrows());	
		} else {
			watcher.setObject(9, (int) 0);
		}
		
		metadata.setEntityID(entityId);
		metadata.setMetadata(watcher.getWatchableObjects());
		
		move.sendPacket(player);
		if (animate) {
			animation.sendPacket(player);
		}
		metadata.sendPacket(player);
	}

	protected int getRandomAnimation() {
		Random r = new Random();
		int i = r.nextInt(3);
		switch(i) {
			case 0:
				return 0;
			case 1:
				return 1;
			case 2:
				return 4;
			default:
				return 0;
		}
	}
}
