package me.temaflux.hit;

import java.util.ArrayList;
import org.bukkit.event.Listener;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers.PlayerInfoAction;
import com.comphenix.protocol.wrappers.PlayerInfoData;

public class Listeners
implements Listener {
	public final Main plugin;
	public final ProtocolManager protocolmanager = ProtocolLibrary.getProtocolManager();
	public final PacketAdapter packetadapter;
	
	public Listeners(Main plugin) {
		this.plugin = plugin;
    	this.packetadapter = new PacketAdapter(plugin, new PacketType[] {PacketType.Play.Server.PLAYER_INFO}) {
    		@Override
    	    public void onPacketSending(PacketEvent event) {
    			Listeners.this.onPacketSending(event);
    	    }
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
        e.getPacket().getPlayerInfoDataLists().write(0, new ArrayList<PlayerInfoData>());
	}
	
    public void onPacketReceiving(PacketEvent event) {}
}
