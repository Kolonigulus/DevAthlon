package de.plabbabap.arcade.module.oitc;

import java.sql.SQLException;

import mainSSQL.TContent.SQLRow;
import mainSSQL.TContent.fields.SQLField;
import mainSSQL.TContent.fields.SQLValue;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.inventory.ItemStack;

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
		if (oitc.getPlugin().getModuleManager().getActualModule().getName()
				.equalsIgnoreCase(oitc.getName())
				&& (oitc.getPlugin().getModuleManager().isInLobby() == false)) {
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
		// Pr�ft ob der Spieler von einem anderen get�tet wurde, sendet
		// Chat-Messages und setzt die Stats
		oitc.deaths.replace((Player) event.getCause().getEntity(),
				oitc.deaths.get(event.getCause().getEntity()) + 1);
		if (event.killedByPlayer()) {
			oitc.kills.replace(event.getKiller(),
					oitc.kills.get(event.getKiller()) + 1);
			oitc.getPlugin()
					.getModuleManager()
					.broadcast(
							"&c"
									+ ((Player) event.getCause().getEntity())
											.getName() + "wurde von "
									+ event.getKiller().getName() + "get�tet");
		} else if (!event.killedByPlayer()) {
			oitc.getPlugin()
					.getModuleManager()
					.broadcast(
							"&c"
									+ ((Player) event.getCause().getEntity())
											.getName() + "ist gestorben");
		}
		if (oitc.deaths.get(event.getCause().getEntity()).equals(
				oitc.getConfig().get("maxDeaths"))) {
			oitc.isOut.add((Player) event.getCause().getEntity());
			((Player) event.getCause().getEntity())
					.setGameMode(GameMode.SPECTATOR);
		}// Wenn der Spieler der den anderen get�tet hat die ben�tigt Punktzahl
			// erreicht wird das event geworfen
		if (oitc.kills.get(event.getKiller()).equals(
				oitc.getConfig().get("neededKills"))
				|| event.killedByPlayer()) {
			oitc.getPlugin().getServer().getPluginManager()
					.callEvent(new PlayerWinOITCEvent(oitc, event.getKiller()));
		}
		if (event.killedByPlayer()) { // gibt die Pfeile
			int arrowcount = 1;
			if (event.getKiller().getInventory().getItem(5).getAmount() > 0) {
				event.getKiller().getInventory()
						.setItem(5, new ItemStack(Material.ARROW, 2));
			} else {
				event.getKiller().getInventory()
						.setItem(5, new ItemStack(Material.ARROW, 1));
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onArrow(EntityDamageByEntityEvent event) {
		if (oitc.getPlugin().getModuleManager().getActualModule().getName()
				.equalsIgnoreCase(oitc.getName())
				&& (oitc.getPlugin().getModuleManager().isInLobby() == false)) {
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
		
		
		oitc.getPlugin().getModuleManager().finish(oitc);
			}
			
	}
