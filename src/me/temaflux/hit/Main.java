package me.temaflux.hit;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main
extends JavaPlugin
implements Listener {
	private static Main plugin = null;
	private Listeners Listeners = null;
	
	@Override
    public void onLoad() {
		plugin = this;
	}
	
	@Override
    public void onEnable() {
    	if (Bukkit.getPluginManager().getPlugin("ProtocolLib") != null) {
    		this.Listeners = new Listeners(this);
    		Bukkit.getPluginManager().registerEvents(this.Listeners, this);
    	}
    	else getLogger().warning("Плагин не будет работать если не установлен ProtocolLib!");
    }

	@Override
    public void onDisable() {
		if (this.Listeners != null) HandlerList.unregisterAll(Listeners);
    }
	
	public static Main getPlugin() {
		return plugin;
	}
}
