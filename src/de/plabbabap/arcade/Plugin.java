package de.plabbabap.arcade;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import mainSSQL.SQLTable;
import mainSSQL.SSQLO;
import mainSSQL.types.SQLType;

import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;

import de.plababap.arcade.module.ModuleManager;
import de.plababap.arcade.module.oitc.Oitc;
import de.plabbabab.arcade.listener.GeneralListener;
import de.plabbabap.arcade.data.CustomConfig;

public class Plugin extends JavaPlugin {
	public boolean ingame;
	SSQLO SQL;
	String PluginName;
	ArrayList<SQLTable> TableList;
	
	ModuleManager modulemanager;
	CustomConfig messages;
	
	GeneralListener elisten;

	
	@Override
	public void onEnable() {
		
		this.saveDefaultConfig();
		messages = new CustomConfig("messages.yml", this);
		messages.saveConfig();
		
		modulemanager = new ModuleManager(messages.getConfig(), this);
		
		modulemanager.registerModule(new Oitc(this, modulemanager));
		
		//try {
			//setUpSQL();

		//} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		//}
		registerEvents();

	}

	@Override
	public void onDisable() {

	}
	
	
	
	public ModuleManager getModuleManager(){
		return modulemanager;
	}
	
	public Configuration getMessageConfig(){
		return messages.getConfig();
	}

	/**
	 * @author Leonhard
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	private void setUpSQL() throws ClassNotFoundException, SQLException {
		SQL = new SSQLO("localhost", "3306", PluginName, "testuser", "passwd");
		HashMap<String, SQLType> header = new HashMap<String, SQLType>();
		// TODO
		TableList.add(SQL.CreateTable(header, "name"));
		getLogger().info("MySQLVerbindung wurde hergestellt");
	}

	/**
	 * @author Leonhard
	 */
	private void registerEvents() {
		elisten = new GeneralListener(this);
		this.getServer().getPluginManager().registerEvents(elisten, this);
		getLogger()
				.info("CommandListener und EventListener wurden hinzugefügt");
	}

}
