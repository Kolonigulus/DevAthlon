package de.plababap.arcade.module.oitc;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;

/**
 * @author Leonhard
 *
 */
public class IngameOITCListener implements Listener {
	Oitc oitc;

	public IngameOITCListener(Oitc oitc) {
		this.oitc = oitc;
	}

	@EventHandler
	public void onDeath(EntityDamageEvent event) {
		if (oitc.isIngame()) {
			if (event.getEntity() instanceof Player) {
				Player player = (Player) event.getEntity();
				if (((Damageable) player).getHealth() < event.getDamage()) {
					oitc.getPlugin().getServer().getPluginManager()
							.callEvent(new PlayerKilledEvent(event));
				}
			}
		}

	}

	@EventHandler
	public void onCustomDeath(PlayerKilledEvent event) {
		oitc.deaths.replace((Player) event.getCause().getEntity(),
				oitc.deaths.get(event.getCause().getEntity()) + 1);
		if (event.killedByPlayer()) {
			oitc.kills.replace(event.getKiller(),
					oitc.kills.get(event.getKiller()) + 1);
		}
	}
	@EventHandler(priority = EventPriority.HIGH)
	public void onArrow(EntityDamageByEntityEvent event){
		if(oitc.isIngame()){
			if(event.getDamager().getType().equals(EntityType.ARROW)){
				event.setDamage(50);
			}
		}
	}

}
