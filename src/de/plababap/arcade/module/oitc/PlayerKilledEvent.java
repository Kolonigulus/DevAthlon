package de.plababap.arcade.module.oitc;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * @author Leonhard
 *
 */
public class PlayerKilledEvent extends Event {
	private HandlerList handlerlist;
	private EntityDamageEvent cause;
	private boolean killedByPlayer;
	private Player killer;

	@Override
	public HandlerList getHandlers() {
		// TODO Auto-generated method stub
		return handlerlist;
	}

	public PlayerKilledEvent(EntityDamageEvent event) {
		cause = event;
		if (event instanceof EntityDamageByEntityEvent) {
			killedByPlayer = true;
			killer = (Player) ((EntityDamageByEntityEvent) event).getDamager();

		}
	}

	public EntityDamageEvent getCause() {
		return cause;
	}

	public boolean killedByPlayer() {
		return killedByPlayer;
	}
	public Player getKiller(){
		return killer;
	}

}
