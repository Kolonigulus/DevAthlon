package de.plabbabap.arcade;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import mainSSQL.SQLTable;
import mainSSQL.SSQLO;
import mainSSQL.types.SQLType;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import de.plababap.arcade.module.Module;
import de.plababap.arcade.module.ModuleManager;
import de.plababap.arcade.module.oitc.Oitc;
import de.plabbabap.arcade.data.CustomConfig;
import de.plabbabap.arcade.listener.GeneralListener;

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
	
	
	
	
	
	
	
	
	
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		
		
		if(cmd.getName().equalsIgnoreCase("addspawn")){
			if(!sender.isOp()){
				sender.sendMessage(ChatColor.RED + "Das darfst du nicht!");
			}else{
				if(this.getModuleManager().isIngame()){
					sender.sendMessage(ChatColor.RED + "Das kannst du nur machen, wenn keine Runde läuft!");
				}else{
					if(args.length < 1){
						sender.sendMessage(ChatColor.RED + "Du musst einen Spielmodus als Argument eingeben: /addspawn <Minispiel>");
					}else{
						for(Module c : this.getModuleManager().getModules()){
							if(c.getName().equalsIgnoreCase(args[0])){
								c.addSpawn(((Player) sender).getLocation());
								sender.sendMessage(ChatColor.GREEN + "Spawn für " + c.getName() + " hinzugefügt!");
								return true;
							}
						}
						sender.sendMessage(ChatColor.RED + "Es existiert kein Minispiel mit diesem Namen!");
					}
				}
			}
		}
		
		
		
		return false;
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
