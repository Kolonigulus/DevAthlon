package de.plababap.arcade.module.parcour;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import de.plababap.arcade.module.Module;
import de.plababap.arcade.module.ModuleManager;
import de.plabbabap.arcade.Plugin;

public class Parcour extends Module implements Listener{

	
	private HashMap<Integer, Location> checkpoints;
	private HashMap<Player, Integer> playerpoints;
	private HashMap<Player, Double> ydistance;
	private boolean started;
	
	
	public Parcour(Plugin plugin, ModuleManager modulemanager) {
		super(plugin, modulemanager, "Parcour");
		
		checkpoints = new HashMap<>();
		playerpoints = new HashMap<>();
		ydistance = new HashMap<>();
		started = false;
		
	}
	
	
	
	
	
	@Override
	public void setup(){
		
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
					checkpoints.put(i + 1, loc);
				}else{
					break;
				}
			}
		}
		
		
		
		checkpoints.put(0, this.getPlugin().getModuleManager().getPlayers().get(0).getLocation());
		
		for(Player c : this.getPlugin().getModuleManager().getPlayers()){
			playerpoints.put(c, 0);
			ydistance.put(c, checkpoints.get(0).getZ());
		}
		
		//started = true;
		
		
		
	}
	
	public void addCheckPoint(Location loc){
		
		checkpoints.put(checkpoints.size(), loc);
		
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
	
	
	@Override
	public void start(){
		
		Bukkit.broadcastMessage("Starting the game...");
		
		for(int i = 0; i < this.getPlugin().getModuleManager().getPlayers().size(); i++){
			Location tmp = new Location(null, 0, 0, 0);
			tmp.setWorld(checkpoints.get(playerpoints.get(this.getPlugin().getModuleManager().getPlayers().get(i))).getWorld());
			tmp.setX(checkpoints.get(playerpoints.get(this.getPlugin().getModuleManager().getPlayers().get(i))).getX());
			tmp.setY(checkpoints.get(playerpoints.get(this.getPlugin().getModuleManager().getPlayers().get(i))).getY());
			tmp.setZ(checkpoints.get(playerpoints.get(this.getPlugin().getModuleManager().getPlayers().get(i))).getZ() + i * 28);
			tmp.setYaw(checkpoints.get(playerpoints.get(this.getPlugin().getModuleManager().getPlayers().get(i))).getYaw());
			tmp.setPitch(checkpoints.get(playerpoints.get(this.getPlugin().getModuleManager().getPlayers().get(i))).getPitch());
			
			this.getPlugin().getModuleManager().getPlayers().get(i).teleport(tmp);
			
			ydistance.put(this.getPlugin().getModuleManager().getPlayers().get(i), (double) (i * 28));
		}
		
		started = true;
		this.setIngame(true);
		
	}
	
	public void respawn(Player p){
		
		
		Location tmp = new Location(null, 0, 0, 0);
		tmp.setWorld(checkpoints.get(playerpoints.get(p)).getWorld());
		tmp.setX(checkpoints.get(playerpoints.get(p)).getX());
		tmp.setY(checkpoints.get(playerpoints.get(p)).getY());
		tmp.setZ(checkpoints.get(playerpoints.get(p)).getZ() + ydistance.get(p));
		tmp.setYaw(checkpoints.get(playerpoints.get(p)).getYaw());
		tmp.setPitch(checkpoints.get(playerpoints.get(p)).getPitch());
		
		p.teleport(tmp);
		
		
		
	}
	
	
	
	
	
	@EventHandler
	public void onMove(PlayerMoveEvent event){
		
		if(this.getPlugin().getModuleManager().getActualModule().getName().equalsIgnoreCase(this.getName()) && this.getPlugin().getModuleManager().isInLobby() == false){
		
			if(started == false){
				event.setCancelled(true);
				return;
			}
		
			if(event.getPlayer().getLocation().getY() < 10){
				this.respawn(event.getPlayer());
			}
		
			if(event.getPlayer().getLocation().getX() > checkpoints.get(playerpoints.get(event.getPlayer()) + 1).getX()){
				playerpoints.put(event.getPlayer(), playerpoints.get(event.getPlayer()) + 1);
				event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', this.getPlugin().getConfig().getString("prefix") + " " + this.getConfig().getString("checkpoint_reched").replace("%point%", "" + playerpoints.get(event.getPlayer()))));
			}
		}
	}
	
	@EventHandler
	public void onDamager(EntityDamageEvent event){
		
		//Bukkit.broadcastMessage("Ingame: " + this.isIngame());
		
			
		if(this.getPlugin().getModuleManager().getActualModule().getName().equalsIgnoreCase(this.getName()) && this.getPlugin().getModuleManager().isInLobby() == false){
			event.setCancelled(true);
			
			if(event.getEntity().getLocation().getY() < 10){
				this.respawn((Player) event.getEntity());
			}
		}
	}

}
