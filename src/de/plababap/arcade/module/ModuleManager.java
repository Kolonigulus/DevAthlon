package de.plababap.arcade.module;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import de.plabbabap.arcade.Plugin;

public class ModuleManager {
	
	
	
	// Diese Klasse organisiert die einzelnen Minispiel-Module und ruft deren enstprechende Methoden auf
	
	private ArrayList<Player> players;
	private ArrayList<Module> modules;
	private int index;
	private boolean game_starting;
	
	private Location lobbyspawn;
	
	
	private boolean game_started;
	private int max_players;
	
	private Configuration cfg;
	
	private Plugin plugin;
	
	public ModuleManager(Configuration cfg, Plugin plugin){
		
		this.plugin = plugin;
		
		this.cfg = cfg;
		
		players = new ArrayList<>();
		modules = new ArrayList<>();
		index = 0;
		game_starting = false;
		game_started = false;
		max_players = this.plugin.getConfig().getInt("max_player");		
		
		
		
		if(plugin.getConfig().getConfigurationSection("lobbyspawn") != null){
			lobbyspawn = new Location(null, 0, 0, 0);
			lobbyspawn.setWorld(Bukkit.getWorld(plugin.getConfig().getConfigurationSection("lobbyspawn").getString("world")));
			lobbyspawn.setX(plugin.getConfig().getConfigurationSection("lobbyspawn").getDouble("X"));
			lobbyspawn.setY(plugin.getConfig().getConfigurationSection("lobbyspawn").getDouble("Y"));
			lobbyspawn.setZ(plugin.getConfig().getConfigurationSection("lobbyspawn").getDouble("Z"));
			lobbyspawn.setYaw((float) plugin.getConfig().getConfigurationSection("lobbyspawn").getDouble("YAW"));
			lobbyspawn.setPitch((float) plugin.getConfig().getConfigurationSection("lobbyspawn").getDouble("PITCH"));
		}
		
	}
	
	public void setLobbyspawn(Location loc){
		if(plugin.getConfig().getConfigurationSection("lobbyspawn") == null)
			plugin.getConfig().createSection("lobbyspawn");
		
		plugin.getConfig().getConfigurationSection("lobbyspawn").set("world", loc.getWorld().getName());
		plugin.getConfig().getConfigurationSection("lobbyspawn").set("X", loc.getX());
		plugin.getConfig().getConfigurationSection("lobbyspawn").set("Y", loc.getY());
		plugin.getConfig().getConfigurationSection("lobbyspawn").set("Z", loc.getZ());
		plugin.getConfig().getConfigurationSection("lobbyspawn").set("YAW", loc.getYaw());
		plugin.getConfig().getConfigurationSection("lobbyspawn").set("PITCH", loc.getPitch());
		
	}
	
	public void registerModule(Module module){
		
		
		// Ein Minispiel registrieren, muss bei Pluginstart f�r jedes Minispiel aufgerufen werden
		
		modules.add(module);
	}
	
	
	public void nextGame(){
		
		if(index >= modules.size()){
			endOfRound();
		}else{
			
			modules.get(index).teleport();
			modules.get(index).setIngame(true);
			modules.get(index).start();
			index++;
		}
		
		
		
	}
	
	public void endOfRound(){
		// wird aufgerufen, wenn ALLE Minispiele beendet sind
		
		
		
		
	}
	
	
	public boolean join(Player p){
		
		
		if(players.size() >= max_players){
			return false;
		}else{
			players.add(p);
			broadcast(ChatColor.translateAlternateColorCodes('&', cfg.getString("prefix") + " " + cfg.getString("player_join").replace("%player%", p.getName()).replace("%online%", ""+players.size()).replace("%max%", ""+max_players)));

		}
		
		
		if((players.size() >= plugin.getConfig().getInt("players_to_join")) && !game_starting){
			this.startTimer();
		}
		
		return true;
		
	}
	
	public void leave(Player p){
		players.remove(p);
		broadcast(ChatColor.translateAlternateColorCodes('&', cfg.getString("prefix") + " " + cfg.getString("player_leave").replace("%player%", p.getName()).replace("%online%", ""+players.size()).replace("%max%", ""+max_players)));
	}
	
	public void startTimer(){
		
		if(game_starting || game_started){
			return;
		}
		
		game_starting = true;
		game_started = false;
		
		StartTimer t = new StartTimer();
		t.start();
		
		
		
		
	}
	
	public ArrayList<Player> getPlayers(){
		return players;
	}
	
	public boolean isIngame(){
		return game_started;
	}
	
	public ArrayList<Module> getModules(){
		return modules;
	}
	
	public Location getLobbyspawn(){
		return lobbyspawn;
	}
	
	
	
	
	
	private class StartTimer extends Thread{

		@Override
		public void run() {
			
			for(int i = 30; i > 0; i--){
			
				for(Player c : players){
					c.setLevel(i);
					c.setExp(0);
				}
				
				if(i == 30){
					broadcast(cfg.getString("prefix") + " " + cfg.getString("game_starting_in").replace("%sec%", i + ""));
				}
				
				if(i == 15){
					broadcast(cfg.getString("prefix") + " " + cfg.getString("game_starting_in").replace("%sec%", i + ""));
				}
				
				if(i <= 10){
					broadcast(cfg.getString("prefix") + " " + cfg.getString("game_starting_in").replace("%sec%", i + ""));
				}
				
				try {
					this.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				
				
			}
			
			
			for(Player c : players){
				c.setLevel(0);
				c.setExp(1);
			}
			
			game_started = true;
			
			nextGame();
			
			
			
			
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	public void broadcast(String msg){
		Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg));
	}
	
	
}
