package de.plabbabap.arcade.listener;


import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.plabbabap.arcade.Plugin;

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
	public void playerJoin(PlayerJoinEvent event){
		event.setJoinMessage(null);
		if(plugin.getModuleManager().isIngame()){
			event.getPlayer().kickPlayer(ChatColor.translateAlternateColorCodes('&', plugin.getMessageConfig().getString("allready_ingame")));
			return;
		}else{
			if(plugin.getModuleManager().join(event.getPlayer()) == false){
				event.getPlayer().kickPlayer(ChatColor.translateAlternateColorCodes('&', plugin.getMessageConfig().getString("game_full")));
				return;
			}
		}
		
		event.getPlayer().setGameMode(GameMode.ADVENTURE);
		event.getPlayer().setHealth(20);
		event.getPlayer().setFoodLevel(20);
		event.getPlayer().getInventory().clear();
	}
	
	@EventHandler
	public void quit(PlayerQuitEvent event){
		plugin.getModuleManager().leave(event.getPlayer());
	}
	
	
	@EventHandler
	public void hunger(FoodLevelChangeEvent event){
		event.setCancelled(true);
	}
	
	
	
	
	@EventHandler
	public void blockBreak(BlockBreakEvent event){
		
		if(plugin.getModuleManager().isIngame() == false){
			event.setCancelled(true);
		}
		
	}
	
	
	
	
	
}
