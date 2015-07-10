package de.plabbabab.arcade.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import de.plabbabab.arcade.Plugin;

/** f�ngt alle events ab die f�r alle Spiele gleich sind
 * @author Leonhard(Grundger�st)
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
