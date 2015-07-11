package de.plababap.arcade.module.oitc;

import java.sql.SQLException;

import mainSSQL.TContent.SQLRow;
import mainSSQL.TContent.fields.SQLField;
import mainSSQL.TContent.fields.SQLValue;

import org.bukkit.GameMode;
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
		if (oitc.deaths.get(event.getCause().getEntity()).equals(
				oitc.getConfig().get("maxDeaths"))) {
			oitc.isOut.add((Player) event.getCause().getEntity());
			((Player) event.getCause().getEntity())
					.setGameMode(GameMode.SPECTATOR);
		}
		if (oitc.kills.get(event.getKiller()).equals(
				oitc.getConfig().get("neededKills"))) {
			oitc.getPlugin().getServer().getPluginManager()
					.callEvent(new PlayerWinOITCEvent(oitc, event.getKiller()));
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onArrow(EntityDamageByEntityEvent event) {
		if (oitc.isIngame()) {
			if (event.getDamager().getType().equals(EntityType.ARROW)) {
				event.setDamage(50);
			}
		}
	}

	public void onWin(PlayerWinOITCEvent event) {
		oitc.getPlugin()
				.getModuleManager()
				.broadcast(
						event.getPlayer().getName()
								+ " hat One in the Chamber gewonnen!");
		//TODO Punkte vergeben
		try {
			for(Player c : oitc.getPlugin().getModuleManager().getPlayers()){
				if(oitc.table.getRowsByValue(oitc.table.getColumn("UUID"), c.getUniqueId().toString()).size() == 0){
					String[] data = new String[4];
					data[0] = c.getUniqueId().toString();
					if(c.getUniqueId().equals(event.getPlayer())){
						data[1] = "1";
					}else{
						data[1] = "0";
					}
					data[2] = oitc.kills.get(c).toString();
					data[3] = oitc.deaths.get(c).toString();
				}else{
					SQLRow row = oitc.table.getRowsByValue(oitc.table.getColumn("UUID"), c.getUniqueId().toString()).get(0);
					SQLField field = row.getField(oitc.table.getColumn("Wins"));
					field.setValue(new SQLValue(field.getValue().getInteger() + 1));
					field = row.getField(oitc.table.getColumn("Kills"));
					field.setValue(new SQLValue(field.getValue().getInteger() + oitc.kills.get(c)));
					field = row.getField(oitc.table.getColumn("Deaths"));
					field.setValue(new SQLValue(field.getValue().getInteger() + oitc.deaths.get(c)));
				}
				
			}
			
			oitc.getPlugin().getModuleManager().finish(event.getPlayer(), oitc);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
