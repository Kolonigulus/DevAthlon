package de.plababap.arcade.module;

import java.util.ArrayList;
import java.util.HashMap;

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
	private HashMap<Player, Integer> points;
	private int index;
	private boolean game_starting;
	
	private Location lobbyspawn;
	
	
	private boolean game_started;
	private int max_players;
	
	private Configuration cfg;
	
	private Plugin plugin;
	private boolean lobby;
	
	public ModuleManager(Configuration cfg, Plugin plugin){
		
		this.plugin = plugin;
		
		this.cfg = cfg;
		
		players = new ArrayList<>();
		modules = new ArrayList<>();
		index = -1;
		game_starting = false;
		game_started = false;
		max_players = this.plugin.getConfig().getInt("max_player");		
		
		points = new HashMap<>();
		
		lobby = true;
		
		
		
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
		
		plugin.saveConfig();
		
	}
	
	public void registerModule(Module module){
		
		
		// Ein Minispiel registrieren, muss bei Pluginstart für jedes Minispiel aufgerufen werden
		
		modules.add(module);
	}
	
	
	public void nextGame(){
		
		index++;
		
		if(index >= modules.size()){
			endOfRound();
		}else{
			
			modules.get(index).teleport();
			modules.get(index).setup();
			modules.get(index).setIngame(true);
			modules.get(index).start();
			
			lobby = false;
			
		}
		
		
		
	}
	
	public HashMap<Player, Integer> getPoints(){
		return points;
	}
	
	
	public void finish(Player winner, Module module){
		
		lobby = true;
		
		for(Player c : this.getPlayers()){
			c.teleport(this.getLobbyspawn());
			c.getInventory().clear();
			c.getActivePotionEffects().clear();
			c.getEquipment().clear();
			c.getInventory().setHelmet(null);
			c.getInventory().setChestplate(null);
			c.getInventory().setLeggings(null);
			c.getInventory().setBoots(null);
			c.setHealth(20);
			c.setFoodLevel(20);
			c.setLevel(0);
			c.setExp(0);
		}
		
		this.broadcast(plugin.getMessageConfig().getString("prefix") + " " + plugin.getMessageConfig().getString("minigame_ended").replace("%game%", module.getName()).replace("%player%", winner.getName()));
		StartTimer t = new StartTimer();
		t.start();
	}
	
	public void endOfRound(){
		// wird aufgerufen, wenn ALLE Minispiele beendet sind
		
		
		
		
	}
	
	public void setPoints(Player p, int points){
		this.points.put(p, points);
	}
	
	
	public boolean join(Player p){
		
		
		if(players.size() >= max_players){
			return false;
		}else{
			players.add(p);
			broadcast(ChatColor.translateAlternateColorCodes('&', cfg.getString("prefix") + " " + cfg.getString("player_join").replace("%player%", p.getName()).replace("%online%", ""+players.size()).replace("%max%", ""+max_players)));

		}
		
		
		if(players.size() >= plugin.getConfig().getInt("players_to_start")){
			if(game_starting == false)
				this.startTimer();
		}
		
		return true;
		
	}
	
	public void leave(Player p){
		players.remove(p);
		broadcast(ChatColor.translateAlternateColorCodes('&', cfg.getString("prefix") + " " + cfg.getString("player_leave").replace("%player%", p.getName()).replace("%online%", ""+players.size()).replace("%max%", ""+max_players)));
	}
	
	public Module getActualModule(){
		return modules.get(index);
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
	
	public boolean isInLobby(){
		return lobby;
	}
	
	
	
	
	
	private class StartTimer extends Thread{

		@SuppressWarnings("static-access")
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
