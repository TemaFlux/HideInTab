package me.temaflux.hit;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import me.temaflux.hit.nte.NametagManager;

public class Main
extends JavaPlugin
implements Listener {
	private static Main plugin = null;
	private Listeners Listeners = null;
//	private NTEListeners ntelisteners = null;
	public NametagManager nmanager = null;
	
	@Override
    public void onLoad() {
		plugin = this;
	}
	
	@Override
    public void onEnable() {
    	if (Bukkit.getPluginManager().getPlugin("ProtocolLib") != null) {
    		this.nmanager = new NametagManager();
    		this.Listeners = new Listeners(this);
//    		this.ntelisteners = new NTEListeners(this.nmanager);
//    		Bukkit.getPluginManager().registerEvents(this.ntelisteners, this);
    		Bukkit.getPluginManager().registerEvents(this.Listeners, this);
    	}
    	else getLogger().warning("Плагин не будет работать если не установлен ProtocolLib!");
    }

	@Override
    public void onDisable() {
		if (this.Listeners != null) HandlerList.unregisterAll(Listeners);
		nmanager.reset();
    }
	
	public static Main getPlugin() {
		return plugin;
	}

	public void debug(String x) {
		this.getLogger().info("[DEBUG] " + x);
	}
}
