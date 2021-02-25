package me.temaflux.hit;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.temaflux.hit.nte.NametagManager;

public class NTEListeners
implements Listener {
	private final NametagManager nametagManager;
	
	public NTEListeners(NametagManager nametagManager) {
		this.nametagManager = nametagManager;
	}
	
    /**
     * Cleans up any nametag data on the server to prevent memory leaks
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event) {
        nametagManager.reset(event.getPlayer().getName());
        System.out.println("Quit player nte");
    }

    /**
     * Applies tags to a player
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        nametagManager.sendTeams(player);
        nametagManager.setNametag(player.getName(), "", "");
        System.out.println("Player join nte");
    }
}
