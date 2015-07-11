package de.plabbabap.arcade.module.parcour;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import de.plababbap.arcade.module.Module;
import de.plababbap.arcade.module.ModuleManager;
import de.plabbabap.arcade.Plugin;

public class Parcour extends Module implements Listener{

	
	
	private boolean started;
	private ArrayList<Location> checkpoints;
	private ArrayList<User> users;
	private boolean in_game;
	
	
	public Parcour(Plugin plugin, ModuleManager modulemanager) {
		super(plugin, modulemanager, "Parcour");
		
		checkpoints = new ArrayList<>();
		users = new ArrayList<>();
		in_game = false;
		started = false;
		
	}
	
	
	
	
	
	@Override
	public void setup(){
		
		
		for(Player p : this.getPlugin().getModuleManager().getPlayers()){
			users.add(new User(p, this));
			this.getUser(p).addPoint(p.getLocation());
		}
		
		
		Bukkit.broadcastMessage("Loading Checkpoints...");
		
		if(this.getConfig().getConfigurationSection("checkpoints") != null){
			for(int i = 0; i >= 0; i++){
				if(this.getConfig().getConfigurationSection("checkpoints").getConfigurationSection("point" + i) != null){
					Location loc = new Location(null, 0, 0, 0);
					loc.setWorld(Bukkit.getWorld(this.getConfig().getConfigurationSection("checkpoints").getConfigurationSection("point" + i).getString("world")));
					loc.setX(this.getConfig().getConfigurationSection("checkpoints").getConfigurationSection("point" + i).getDouble("X"));
					loc.setY(this.getConfig().getConfigurationSection("checkpoints").getConfigurationSection("point" + i).getDouble("Y"));
					loc.setZ(this.getConfig().getConfigurationSection("checkpoints").getConfigurationSection("point" + i).getDouble("Z"));
					loc.setYaw((float) this.getConfig().getConfigurationSection("checkpoints").getConfigurationSection("point" + i).getDouble("YAW"));
					loc.setPitch((float) this.getConfig().getConfigurationSection("checkpoints").getConfigurationSection("point" + i).getDouble("PITCH"));
					
					checkpoints.add(loc);
					
				}else{
					break;
				}
			}
		}
		
		for(int i = 0; i < users.size(); i++){
			Location tmp = new Location(null, 0, 0, 0);
			tmp.setWorld(checkpoints.get(i).getWorld());
			tmp.setX(checkpoints.get(i).getX());
			tmp.setY(checkpoints.get(i).getY());
			tmp.setZ(checkpoints.get(i).getZ() + i * 28);
			tmp.setYaw(checkpoints.get(i).getYaw());
			tmp.setPitch(checkpoints.get(i).getPitch());
			users.get(i).addPoint(tmp);
		}
		
		
		
		
		
		
		
		//started = true;
		
		
		
	}
	
	@Override
	public void start(){
		
		Bukkit.broadcastMessage("Starting the game...");
		
		for(User c : users){
			c.respawn();
		}
		in_game = true;
		started = true;
		
	}
	
	public void addCheckPoint(Location loc){
		
		
		
		if(this.getConfig().getConfigurationSection("checkpoints") == null)
			this.getConfig().createSection("checkpoints");
		
		for (int i = checkpoints.size() - 1; i >= 0; i++){
			if(this.getConfig().getConfigurationSection("checkpoints").getConfigurationSection("point" + i) == null){
				
				this.getConfig().getConfigurationSection("checkpoints").createSection("point" + i);
				this.getConfig().getConfigurationSection("checkpoints").getConfigurationSection("point" + i).set("world", loc.getWorld().getName());
				this.getConfig().getConfigurationSection("checkpoints").getConfigurationSection("point" + i).set("X", loc.getX());
				this.getConfig().getConfigurationSection("checkpoints").getConfigurationSection("point" + i).set("Y", loc.getY());
				this.getConfig().getConfigurationSection("checkpoints").getConfigurationSection("point" + i).set("Z", loc.getZ());
				this.getConfig().getConfigurationSection("checkpoints").getConfigurationSection("point" + i).set("YAW", loc.getYaw());
				this.getConfig().getConfigurationSection("checkpoints").getConfigurationSection("point" + i).set("PITCH", loc.getPitch());
				
				this.getCustomConfig().forceSave();
				
				
				break;
			}
		}
		
		
		
		
		
	}
	
	public User getUser(Player p){
		for(User c : users){
			if(c.getPlayer().getName().equalsIgnoreCase(p.getName())){
				return c;
			}
		}
		
		return null;
	}
	
	
	
	
	
	
	
	
	
	@EventHandler
	public void onMove(PlayerMoveEvent event){
		
		if(this.getPlugin().getModuleManager().getActualModule() == null)
			return;
		
		if(this.isIngame()){
		
			if(started == false){
				event.setCancelled(true);
				return;
			}
		
			
		
			
		}
		
		
		
		if(this.getPlugin().getModuleManager().getActualModule().getName().equalsIgnoreCase(this.getName()) && (this.getPlugin().getModuleManager().isInLobby() == false)){
			if(event.getPlayer().getLocation().getY() < 10){
				this.getUser(event.getPlayer()).respawn();
			}
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent event){
		
		if(this.getPlugin().getModuleManager().getActualModule() == null)
			return;
		
		//Bukkit.broadcastMessage("Ingame: " + this.isIngame());
		if(!(event.getEntity() instanceof Player))
			return;
			
		if(this.getPlugin().getModuleManager().getActualModule().getName().equalsIgnoreCase(this.getName()) && (this.getPlugin().getModuleManager().isInLobby() == false)){
			event.setCancelled(true);
			
			if(event.getEntity().getLocation().getY() < 10){
				this.getUser((Player) event.getEntity()).respawn();
			}
		}
	}

}
