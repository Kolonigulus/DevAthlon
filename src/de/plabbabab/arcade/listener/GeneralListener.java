package de.plabbabab.arcade.listener;


import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import de.plabbabab.arcade.Plugin;

/** fängt alle events ab die für alle Spiele gleich sind
 * @author Leonhard(Grundgerüst) / Leo (Zortax)
 *	
 */
public class GeneralListener implements Listener{
	Plugin plugin;
	public GeneralListener(Plugin p){
		this.plugin = p;
	}
	@EventHandler
	public void catcher(PlayerJoinEvent event){
		event.setJoinMessage(null);
		if(plugin.getModuleManager().isIngame()){
			event.getPlayer().kickPlayer(ChatColor.translateAlternateColorCodes('&', plugin.getMessageConfig().getString("allready_ingame")));
		}else{
			if(plugin.getModuleManager().join(event.getPlayer()) == false){
				event.getPlayer().kickPlayer(ChatColor.translateAlternateColorCodes('&', plugin.getMessageConfig().getString("game_full")));
			}
		}
	}
}
