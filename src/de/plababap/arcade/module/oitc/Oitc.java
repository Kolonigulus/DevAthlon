package de.plababap.arcade.module.oitc;

import java.sql.SQLException;
import java.util.HashMap;

import mainSSQL.SQLTable;
import mainSSQL.types.SQLType;
import mainSSQL.types.SQLTypesEnum;
import de.plababap.arcade.module.Module;
import de.plababap.arcade.module.ModuleManager;
import de.plabbabap.arcade.Plugin;

/**
 * @author Leonhard
 *
 */
public class Oitc extends Module {
	protected SQLTable table;

	public Oitc(Plugin plugin, ModuleManager modulemanager) {
		super(plugin, modulemanager, "OITC");

	}

	@Override
	public void start() {
		this.getPlugin().getServer().getPluginManager()
				.registerEvents(new IngameOITCListener(this), this.getPlugin());
		}

	/**
	 * @author Leonhard
	 * @see de.plababap.arcade.module.Module#setup()
	 */
	@Override
	public void setup() {
		// HashMap und SQLTable für Oitc erzeugen
		HashMap<String, SQLType> header = new HashMap<String, SQLType>();
		header.put("UUID", new SQLType(SQLTypesEnum.TINYTEXT));
		header.put("Wins", new SQLType(SQLTypesEnum.INT));
		header.put("Kills", new SQLType(SQLTypesEnum.INT));
		header.put("Deaths", new SQLType(SQLTypesEnum.INT));
		try {
			table = getPlugin().getSQLO().CreateTable(header,
					this.getConfig().getString("MySQL.table"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
