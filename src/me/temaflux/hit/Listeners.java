package me.temaflux.hit;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers.PlayerInfoAction;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedGameProfile;

import me.temaflux.hit.nte.FakeTeam;
import me.temaflux.hit.nte.PacketAccessor;

public class Listeners
implements Listener {
	public final Main plugin;
	public final ProtocolManager protocolmanager = ProtocolLibrary.getProtocolManager();
	public final PacketAdapter packetadapter;
	
	public Listeners(Main plugin) {
		this.plugin = plugin;
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
        	PacketContainer team = protocolmanager.createPacket(PacketType.Play.Server.SCOREBOARD_TEAM);
        	team.getStrings().write(4, "never");
           	ArrayList<String> playerToAdd = new ArrayList<>();
           	playerToAdd.add(x.getProfile().getName());
        	team.getSpecificModifier(Collection.class).write(0, playerToAdd);
        	//
        	try {
				protocolmanager.sendServerPacket(e.getPlayer(), team);
			} catch (InvocationTargetException e1) {
				e1.printStackTrace();
			}
        	//remove the player
        	WrappedGameProfile nev = new WrappedGameProfile(x.getProfile().getUUID(), "");
        	nev.getProperties().putAll(x.getProfile().getProperties());
        	newPlayerInfoDataList.add(new PlayerInfoData(nev, x.getLatency(), x.getGameMode(), null));
        }
        e.getPacket().getPlayerInfoDataLists().write(0, newPlayerInfoDataList);
	}
	
    public void onPacketReceiving(PacketEvent event) {
		
	}
    
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
    	List<String> players = new ArrayList<String>();
    	for (Player p : Utils.getOnline())
    		players.add(p.getName());
    	PacketContainer packet = createTeamPacket(new FakeTeam("", "", 0, false), 0, players);
    	try {
			this.protocolmanager.sendServerPacket(e.getPlayer(), packet);
		} catch (InvocationTargetException e1) {
			e1.printStackTrace();
		}
    }
    
    //
    
    public void hidePlayer(Player p) {
    	if (plugin == null && plugin.nmanager == null) return;
        FakeTeam tm = plugin.nmanager.getFakeTeam(p);
        ToggleVisibility("never", tm.getName(), tm.getPrefix(), tm.getSuffix(), tm.getMembers());
    }
    
    public void showPlayer(Player p) {
    	if (plugin == null && plugin.nmanager == null) return;
        FakeTeam tm = plugin.nmanager.getFakeTeam(p);
        ToggleVisibility("always", tm.getName(), tm.getPrefix(), tm.getSuffix(), tm.getMembers());
    }
    
    private static void setField(Object edit, String fieldName, Object value) {
        try {
            Field field = edit.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(edit, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    
    public static Class<?> getNMSClass(String name) {
        try {
            return Class.forName("net.minecraft.server."
                    + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public void ToggleVisibility(String visible, String name, String prefix, String suffix, Collection<?> players) {
//        Constructor<?> ChatComponentText;
//        Class<?> typeChatComponentText = getNMSClass("ChatComponentText");
        Object teamPacket = null;
     
        try {
            teamPacket = getNMSClass("PacketPlayOutScoreboardTeam").getDeclaredConstructor().newInstance();
//            ChatComponentText = typeChatComponentText.getConstructor(String.class);
            setField(teamPacket, "h", players); //members
            setField(teamPacket, "c", prefix); //prefix
            setField(teamPacket, "d", suffix); //suffix
            setField(teamPacket, "a", name); //teamName
            setField(teamPacket, "i", 2); //paramInt
            setField(teamPacket, "j", 1); //packOption
            setField(teamPacket, "b", name); //displayName
            setField(teamPacket, "f", "never"); //push
            setField(teamPacket, "e", visible); //visibility
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
        
        for (Player x : Bukkit.getOnlinePlayers()) {
        	if (x.getName() == name) continue;
        	PacketAccessor.sendPacket(x, teamPacket);
        }
    }
}
