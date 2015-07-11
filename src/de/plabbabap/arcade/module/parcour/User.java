package de.plabbabap.arcade.module.parcour;

import java.util.ArrayList;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class User {
	
	private Player player;
	
	private int last_checkpoint;
	private ArrayList<Location> checkpoints;
	Parcour parcour;
	
	
	public User(Player p, Parcour parcour){
		
		this.parcour = parcour;
		last_checkpoint = 0;
		player = p;
		checkpoints = new ArrayList<>();
	}
	
	public Location getLastCheckpoint(){
		return checkpoints.get(last_checkpoint);
	}
	
	public void addPoint(Location loc){
		checkpoints.add(loc);
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public void respawn(){
		player.teleport(checkpoints.get(last_checkpoint));
	}
	
	public void onMove(Player p){
		
	
		
		if(p.getName().equalsIgnoreCase(player.getName())){
			if(last_checkpoint + 1 < checkpoints.size()){
				
				//Bukkit.broadcastMessage("onMove (User)");
				if(p.getLocation().getX() >= checkpoints.get(last_checkpoint + 1).getX()){
					last_checkpoint++;
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', parcour.getConfig().getString("reached_checkpoint").replace("%point%", last_checkpoint + "")));
				}
			}
		}
	}
	
}
