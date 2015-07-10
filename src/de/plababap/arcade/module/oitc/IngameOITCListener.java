package de.plababap.arcade.module.oitc;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * @author Leonhard
 *
 */
public class IngameOITCListener implements Listener {
	Oitc oitc;
	public IngameOITCListener(Oitc oitc){
		this.oitc = oitc;
	}
	@EventHandler
	public void onDeath(EntityDamageEvent event){
		if(event.getEntity() instanceof Player){
			Player player = (Player) event.getEntity();
			if(((Damageable)player).getHealth() < event.getDamage()){
				
			}
		}
	}

}
