package de.plabbabap.arcade.module.oitc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import mainSSQL.SQLTable;
import mainSSQL.types.SQLType;
import mainSSQL.types.SQLTypesEnum;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.plabbabap.arcade.Plugin;
import de.plabbabap.arcade.module.Module;
import de.plabbabap.arcade.module.ModuleManager;

/**
 * @author Leonhard
 *
 */
public class Oitc extends Module {
	protected SQLTable table;
	protected HashMap<Player, Integer> kills;
	protected HashMap<Player, Integer> deaths;
	protected ArrayList<Player> isOut;

	public Oitc(Plugin plugin, ModuleManager modulemanager) {
		super(plugin, modulemanager, "OITC");

	}

	@Override
	public void start() {
		this.getPlugin().getServer().getPluginManager()
				.registerEvents(new IngameOITCListener(this), this.getPlugin());
		for(Player c : getPlugin().getModuleManager().getPlayers()){
			kills.put(c, 0);
			deaths.put(c, 0);
			c.getInventory().setItem(0, new ItemStack(Material.WOOD_SWORD));
			c.getInventory().setItem(2, new ItemStack(Material.BOW));
			c.getInventory().setItem(1, new ItemStack(Material.ARROW, 1));
		}
		}

	/**
	 * @author Leonhard
	 * @see de.plabbabap.arcade.module.Module#setup()
	 */
	@Override
	public void setup() {
		// HashMap und SQLTable für Oitc erzeugen
		kills = new HashMap<Player, Integer>();
		deaths = new HashMap<Player, Integer>();
		isOut = new ArrayList<Player>();
		HashMap<String, SQLType> header = new HashMap<String, SQLType>();
		header.put("UUID", new SQLType(SQLTypesEnum.TINYTEXT));
		header.put("Wins", new SQLType(SQLTypesEnum.INT));
		header.put("Kills", new SQLType(SQLTypesEnum.INT));
		header.put("Deaths", new SQLType(SQLTypesEnum.INT));
	}

}
