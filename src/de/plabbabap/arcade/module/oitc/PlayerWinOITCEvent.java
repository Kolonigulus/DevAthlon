package de.plabbabap.arcade.module.oitc;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author Leonhard
 *
 */
public class PlayerWinOITCEvent extends Event {
private Player player;
private static final HandlerList handlers = new HandlerList();
	@Override
	public HandlerList getHandlers() {
		// TODO Auto-generated method stub
		return handlers;
	}
	public PlayerWinOITCEvent(Oitc oitc, Player player){
		this.player = player;
	}
	public Player getPlayer(){
		return player;
	}

}
