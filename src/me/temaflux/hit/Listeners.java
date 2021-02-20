package me.temaflux.hit;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers.PlayerInfoAction;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedGameProfile;

public class Listeners
implements Listener {
	public final ProtocolManager protocolmanager = ProtocolLibrary.getProtocolManager();
	public final PacketAdapter packetadapter;
	
	public Listeners(JavaPlugin plugin) {
    	this.packetadapter = new PacketAdapter(plugin, new PacketType[] {PacketType.Play.Server.PLAYER_INFO}) {
    		// Redirect
    		@Override
    	    public void onPacketSending(PacketEvent event) {
    			Listeners.this.onPacketSending(event);
    	    }
    		// Redirect
    		@Override
    	    public void onPacketReceiving(PacketEvent event) {
    			Listeners.this.onPacketReceiving(event);
    		}
    	};
    	protocolmanager.addPacketListener(this.packetadapter);
	}
	
	public void onPacketSending(PacketEvent e) {
		try {
			if (e.getPacket().getPlayerInfoAction().read(0) != PlayerInfoAction.ADD_PLAYER) return;
		}
		catch (Exception ex) {
			return;
		}
        
        List<PlayerInfoData> newPlayerInfoDataList = new ArrayList<PlayerInfoData>();
        for (PlayerInfoData x : e.getPacket().getPlayerInfoDataLists().getValues().get(0)) {
        	if (x.getProfile().getUUID() == e.getPlayer().getUniqueId()) continue;
        	
        	//remove the player
//        	(CraftPlayer) pl).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, ((CraftPlayer) p).getHandle()));
        	PacketContainer packet = new PacketContainer(PacketType.Play.Server.PLAYER_INFO);
        	packet.getPlayerInfoAction().write(0, PlayerInfoAction.REMOVE_PLAYER);
        	try {
				this.protocolmanager.sendServerPacket(e.getPlayer(), packet);
			} catch (InvocationTargetException e1) {
				e1.printStackTrace();
			}
        	
        	WrappedGameProfile nev = new WrappedGameProfile(x.getProfile().getUUID(), "");
        	nev.getProperties().putAll(x.getProfile().getProperties());
        	newPlayerInfoDataList.add(new PlayerInfoData(nev, x.getLatency(), x.getGameMode(), null));
        }
        e.getPacket().getPlayerInfoDataLists().write(0, newPlayerInfoDataList);
	}
	
    public void onPacketReceiving(PacketEvent event) {
		
	}
}
