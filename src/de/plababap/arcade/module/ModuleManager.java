package de.plababap.arcade.module;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

public class ModuleManager {
	
	
	
	// Diese Klasse organisiert die einzelnen Minispiel-Module und ruft deren enstprechende Methoden auf
	
	private ArrayList<Player> players;
	private ArrayList<Module> modules;
	private int index;
	private boolean game_starting;
	
	
	private boolean game_started;
	private int max_players;
	
	private Configuration cfg;
	
	public ModuleManager(Configuration cfg){
		
		this.cfg = cfg;
		
		players = new ArrayList<>();
		modules = new ArrayList<>();
		index = 0;
		game_starting = false;
		game_started = false;
		
	}
	
	public void registerModule(Module module){
		// Ein Minispiel registrieren, muss bei Pluginstart für jedes Minispiel aufgerufen werden
		
		modules.add(module);
	}
	
	
	public void nextGame(){
		
		if(index >= modules.size()){
			endOfRound();
		}else{
			
			modules.get(index).
			
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
			return true;
		}
		
		
		
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
		
		
		
		
	}
	
	public ArrayList<Player> getPlayers(){
		return players;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public void broadcast(String msg){
		Bukkit.broadcastMessage(msg);
	}
	
	
}
