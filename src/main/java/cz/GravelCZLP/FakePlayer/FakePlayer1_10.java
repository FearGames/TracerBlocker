package cz.GravelCZLP.FakePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.EnumWrappers.ItemSlot;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedGameProfile;

import cz.GravelCZLP.PacketWrapper.v1_10.WrapperPlayServerEntityDestroy;
import cz.GravelCZLP.PacketWrapper.v1_10.WrapperPlayServerEntityEquipment;
import cz.GravelCZLP.PacketWrapper.v1_10.WrapperPlayServerNamedEntitySpawn;
import cz.GravelCZLP.PacketWrapper.v1_10.WrapperPlayServerPlayerInfo;
import cz.GravelCZLP.PacketWrapper.v1_10.WrapperPlayServerRelEntityMoveLook;

public class FakePlayer1_10 extends AbstractFakePlayer
{

    public FakePlayer1_10( Plugin plugin, Location location )
    {
        super( plugin, location );
    }

    protected WrappedDataWatcher getWatcher( Player player, WrapperPlayServerNamedEntitySpawn packet )
    {
        WrappedDataWatcher watcher = new WrappedDataWatcher();

        watcher.setObject( 0, WrappedDataWatcher.Registry.get( Byte.class ), (byte) 0x20 );
        watcher.setObject( 1, WrappedDataWatcher.Registry.get( Integer.class ), 300 );
        watcher.setObject( 3, WrappedDataWatcher.Registry.get( String.class ), name );
        watcher.setObject( 2, WrappedDataWatcher.Registry.get( Float.class ), (float) randomHealth() );
        return watcher;
    }

    @Override
    protected void removeObserver( Player player )
    {
        sendRemovePlayerTab( player );
        WrapperPlayServerEntityDestroy destroy = new WrapperPlayServerEntityDestroy();
        destroy.setEntityIds( new int[]{ entityId } );
        destroy.sendPacket( player );
        observers.remove( player );
    }

    protected void notifySpawnEntity( Player player )
    {
        sendAddPlayerTab( player );
        WrapperPlayServerNamedEntitySpawn spawned = new WrapperPlayServerNamedEntitySpawn();
        spawned.setEntityID( entityId );
        spawned.setPosition( serverLocation.toVector() );
        spawned.setPlayerUUID( uuid );
        spawned.setYaw( serverLocation.getYaw() );
        spawned.setPitch( serverLocation.getPitch() );
        spawned.setMetadata( getWatcher( player, spawned ) );
        spawned.sendPacket( player );
        sendRemovePlayerTab( player );
    }

    protected void sendAddPlayerTab( Player player )
    {
        WrapperPlayServerPlayerInfo info = new WrapperPlayServerPlayerInfo();
        info.setAction( EnumWrappers.PlayerInfoAction.ADD_PLAYER );
        List<PlayerInfoData> dataList = new ArrayList<>();
        WrappedGameProfile profile = new WrappedGameProfile( uuid, name );
        WrappedChatComponent displayName = WrappedChatComponent.fromText( name );

        PlayerInfoData data = new PlayerInfoData( profile, 0, EnumWrappers.NativeGameMode.SURVIVAL, displayName );
        Object generic = PlayerInfoData.getConverter().getGeneric( MinecraftReflection.getPlayerInfoDataClass(), data );
        PlayerInfoData back = PlayerInfoData.getConverter().getSpecific( generic );
        dataList.add( back );
        info.setData( dataList );
        info.sendPacket( player );
    }

    protected void sendRemovePlayerTab( Player player )
    {
        WrapperPlayServerPlayerInfo info = new WrapperPlayServerPlayerInfo();
        info.setAction( EnumWrappers.PlayerInfoAction.REMOVE_PLAYER );
        List<PlayerInfoData> dataList = new ArrayList<>();
        WrappedGameProfile profile = new WrappedGameProfile( uuid, name );
        WrappedChatComponent displayName = WrappedChatComponent.fromText( name );

        PlayerInfoData data = new PlayerInfoData( profile, 0, EnumWrappers.NativeGameMode.SURVIVAL, displayName );
        Object generic = PlayerInfoData.getConverter().getGeneric( MinecraftReflection.getPlayerInfoDataClass(), data );
        PlayerInfoData back = PlayerInfoData.getConverter().getSpecific( generic );
        dataList.add( back );
        info.setData( dataList );
        info.sendPacket( player );
    }

    protected void broadcastMoveEntity()
    {
        WrapperPlayServerRelEntityMoveLook move = new WrapperPlayServerRelEntityMoveLook();
        move.setEntityID( entityId );
        move.setDx( serverLocation.getX() - clientLocation.getX() );
        move.setDy( serverLocation.getY() - clientLocation.getY() );
        move.setDz( serverLocation.getZ() - clientLocation.getZ() );
        
        Location loc = new Location(Bukkit.getWorld("world"), move.getDx(), move.getDy(), move.getDz());
        Block b = loc.getWorld().getBlockAt(loc);
        boolean onGround = b.getRelative(BlockFace.DOWN).getType() != Material.AIR;
        
        move.setOnGround(onGround);
        
        move.setYaw( getRandomYaw() );
        move.setPitch( getRandomPitch() );  
        
        WrapperPlayServerEntityEquipment equipment = new WrapperPlayServerEntityEquipment();
        
        equipment.setEntityID(entityId);
        
        Random r = new Random();
        
        Material[] swords = new Material[] { Material.WOOD_SWORD, Material.STONE_SWORD, Material.IRON_SWORD, Material.GOLD_SWORD, Material.DIAMOND_SWORD };
        
        Material[] helmets = new Material[] { Material.LEATHER_HELMET, Material.CHAINMAIL_HELMET, Material.GOLD_HELMET, Material.IRON_HELMET, Material.DIAMOND_HELMET };
        Material[] chestplates = new Material[] { Material.LEATHER_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE, Material.GOLD_CHESTPLATE, Material.IRON_CHESTPLATE, Material.DIAMOND_CHESTPLATE };
        Material[] leggings = new Material[] { Material.LEATHER_LEGGINGS, Material.CHAINMAIL_LEGGINGS, Material.GOLD_LEGGINGS, Material.IRON_LEGGINGS, Material.DIAMOND_LEGGINGS };
        Material[] boots = new Material[] { Material.LEATHER_BOOTS, Material.CHAINMAIL_BOOTS, Material.GOLD_BOOTS, Material.IRON_BOOTS, Material.DIAMOND_BOOTS };
        
        equipment.setSlot(ItemSlot.FEET);
        equipment.setItem(new ItemStack(boots[r.nextInt(boots.length)]));
        
        equipment.setSlot(ItemSlot.LEGS);
        equipment.setItem(new ItemStack(leggings[r.nextInt(leggings.length)]));
        
        equipment.setSlot(ItemSlot.CHEST);
        equipment.setItem(new ItemStack(chestplates[r.nextInt(chestplates.length)]));
        
        equipment.setSlot(ItemSlot.HEAD);
        equipment.setItem(new ItemStack(helmets[r.nextInt(helmets.length)]));
        
        equipment.setSlot(ItemSlot.MAINHAND);
        equipment.setItem(new ItemStack(swords[r.nextInt(swords.length)]));
        
        for ( Player player : observers )
        {
            move.sendPacket( player );
            equipment.sendPacket( player );
        }
    }
}