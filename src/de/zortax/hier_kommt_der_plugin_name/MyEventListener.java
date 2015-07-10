package de.zortax.hier_kommt_der_plugin_name;

import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * @author Leonhard(Grundgerüst)
 *
 */
public class MyEventListener implements Listener{
	PluginName p;
	public MyEventListener(PluginName p){
		this.p = p;
	}
	@EventHandler
	public void catcher(Cancellable e){
		e.setCancelled(true);;
	}
}
