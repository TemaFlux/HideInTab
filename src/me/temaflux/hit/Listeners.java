package me.temaflux.hit;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers.PlayerInfoAction;
import com.comphenix.protocol.wrappers.PlayerInfoData;

public class Listeners
implements Listener {
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
	}
	
	public void onPacketSending(PacketEvent event) {
		try {
			if (event.getPacket().getPlayerInfoAction().read(0) != PlayerInfoAction.ADD_PLAYER) return;
		}
		catch (Exception e) {
			return;
		}
        
        List<PlayerInfoData> newPlayerInfoDataList = new ArrayList<PlayerInfoData>();
        event.getPacket().getPlayerInfoDataLists().write(0, newPlayerInfoDataList);
	}
	
    public void onPacketReceiving(PacketEvent event) {
		
	}
}
