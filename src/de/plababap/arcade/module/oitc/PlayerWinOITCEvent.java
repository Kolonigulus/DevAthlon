package de.plababap.arcade.module.oitc;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author Leonhard
 *
 */
public class PlayerWinOITCEvent extends Event {
private Player player;
	@Override
	public HandlerList getHandlers() {
		// TODO Auto-generated method stub
		return null;
	}
	public PlayerWinOITCEvent(Oitc oitc, Player player){
		this.player = player;
	}
	public Player getPlayer(){
		return player;
	}

}
