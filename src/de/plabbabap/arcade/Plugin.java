package de.plabbabap.arcade;

import java.sql.SQLException;
import java.util.ArrayList;

import mainSSQL.SQLTable;
import mainSSQL.SSQLO;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import de.plabbabap.arcade.data.CustomConfig;
import de.plabbabap.arcade.listener.GeneralListener;
import de.plabbabap.arcade.module.Module;
import de.plabbabap.arcade.module.ModuleManager;
import de.plabbabap.arcade.module.deathmatch.Deathmatch;
import de.plabbabap.arcade.module.oitc.IngameOITCListener;
import de.plabbabap.arcade.module.oitc.Oitc;
import de.plabbabap.arcade.module.parcour.Parcour;

public class Plugin extends JavaPlugin {
	public boolean ingame;
	SSQLO SQL;
	String PluginName;
	ArrayList<SQLTable> TableList;

	ModuleManager modulemanager;
	CustomConfig messages;

	GeneralListener elisten;
	Parcour parc;
	Deathmatch dm;
	@Override
	public void onEnable() {

		this.saveDefaultConfig();
		messages = new CustomConfig("messages.yml", this);
		messages.saveConfig();

		

		
		modulemanager = new ModuleManager(messages.getConfig(), this);
		
		parc = new Parcour(this, this.getModuleManager());
		dm = new Deathmatch(this, this.getModuleManager());
	//	Oitc Oitc = new Oitc(this,modulemanager);
		//modulemanager.registerModule(Oitc);
		modulemanager.registerModule(parc);
		modulemanager.registerModule(dm);
		
		//getServer().getPluginManager()
		//.registerEvents(new IngameOITCListener(Oitc), this);
		//modulemanager.registerModule(new Oitc(this, modulemanager));
		 try {
		 setUpSQL();

		 } catch (ClassNotFoundException | SQLException e) {
		 
			 	getLogger().info("Verbindung konnte nicht aufgebaut werden:");
				getLogger().info("SQLException: " + e.getMessage());
				getLogger().info("SQLState: " + ((SQLException) e).getSQLState());
				getLogger().info("VendorError: " + ((SQLException) e).getErrorCode());
		 }
		registerEvents();

	}

	@Override
	public void onDisable() {

	}

	public ModuleManager getModuleManager() {
		return modulemanager;
	}

	public Configuration getMessageConfig() {
		return messages.getConfig();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {

		if (cmd.getName().equalsIgnoreCase("addspawn")) {
			if (!sender.isOp()) {
				sender.sendMessage(ChatColor.RED + "Das darfst du nicht!");
			} else {
				if (this.getModuleManager().isIngame()) {
					sender.sendMessage(ChatColor.RED
							+ "Das kannst du nur machen, wenn keine Runde läuft!");
				} else {
					if (args.length < 1) {
						sender.sendMessage(ChatColor.RED
								+ "Du musst einen Spielmodus als Argument eingeben: /addspawn <Minispiel>");
					} else {
						for (Module c : this.getModuleManager().getModules()) {
							if (c.getName().equalsIgnoreCase(args[0])) {
								c.addSpawn(((Player) sender).getLocation());
								sender.sendMessage(ChatColor.GREEN
										+ "Spawn für " + c.getName()
										+ " hinzugefügt!");
								return true;
							}
						}
						sender.sendMessage(ChatColor.RED
								+ "Es existiert kein Minispiel mit diesem Namen!");
					}
				}
			}
		}
		if(cmd.getName().equalsIgnoreCase("setlobby")){
			if (!sender.isOp()) {
				sender.sendMessage(ChatColor.RED + "Das darfst du nicht!");
				return true;
			} else {
				if (this.getModuleManager().isIngame()) {
					sender.sendMessage(ChatColor.RED + "Das kannst du nur machen, wenn keine Runde läuft!");
					return true;
				} else {
					this.getModuleManager().setLobbyspawn(((Player) sender).getLocation());
					sender.sendMessage(ChatColor.GREEN + "Lobbyspawn gesetzt!");
				}
			}
		}
		
	if(cmd.getName().equalsIgnoreCase("checkpoint")){
			if (!sender.isOp()) {
				sender.sendMessage(ChatColor.RED + "Das darfst du nicht!");
				return true;
			} else {
				if (this.getModuleManager().isIngame()) {
					sender.sendMessage(ChatColor.RED + "Das kannst du nur machen, wenn keine Runde läuft!");
					return true;
				} else {
					
					for(Module c : this.getModuleManager().getModules()){
						if(c.getName().equalsIgnoreCase("Parcour")){
							((Parcour) c).addCheckPoint(((Player) sender).getLocation());
							sender.sendMessage(ChatColor.GREEN + "Lobbyspawn hinzugefügt!");
						}
					}
					
				}
			}
		}

		return true;
	}

	public SSQLO getSQLO() {
		return SQL;
	}

	/**
	 * @author Leonhard
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	private void setUpSQL() throws ClassNotFoundException, SQLException {
		SQL = new SSQLO(this.getConfig().getString("MySQL.url"), this
				.getConfig().getString("MySQL.port"), this.getConfig()
				.getString("MySQL.database"), this.getConfig().getString(
				"MySQL.user"), this.getConfig().getString("MySQL.passwd"));
		getLogger().info("MySQLVerbindung wurde hergestellt");
	}

	/**
	 * @author Leonhard
	 */
	private void registerEvents() {
		elisten = new GeneralListener(this);
		this.getServer().getPluginManager().registerEvents(elisten, this);
		this.getServer().getPluginManager().registerEvents(parc, this);
		this.getServer().getPluginManager().registerEvents(dm, this);
		getLogger()
				.info("CommandListener und EventListener wurden hinzugefügt");
	}

}
