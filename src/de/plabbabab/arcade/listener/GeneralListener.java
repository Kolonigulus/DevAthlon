package de.plabbabab.arcade.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import de.plabbabab.arcade.Plugin;

/** fängt alle events ab die für alle Spiele gleich sind
 * @author Leonhard(Grundgerüst)
 *	
 */
public class GeneralListener implements Listener{
	Plugin p;
	public GeneralListener(Plugin p){
		this.p = p;
	}
	@EventHandler
	public void catcher(PlayerJoinEvent event){
		if(p.ingame){
			event.getPlayer().kickPlayer("Spiel ist gerade ingame!");
		}
	}
}
