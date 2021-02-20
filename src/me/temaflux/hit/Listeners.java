package me.temaflux.hit;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers.PlayerInfoAction;
import com.comphenix.protocol.wrappers.PlayerInfoData;

public class Listeners
implements Listener {
	public Listeners(JavaPlugin plugin) {
		ProtocolLibrary.getProtocolManager().addPacketListener(
    		new PacketAdapter(plugin, new PacketType[] {
                PacketType.Play.Server.PLAYER_INFO
            }) {
    		
    		@Override
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
    	});
	}
}
