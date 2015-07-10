package de.plababap.arcade.module;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import de.plabbabab.arcade.Plugin;
import de.plabbabab.arcade.data.CustomConfig;

public class Module implements Listener {

	// Autor: Leo (Zortax)
	
	// Dies ist die Superclass, die die einzelnen Minispiele dann erweitern
	
	private String name;
	private boolean ingame;
	private ArrayList<Location> spawns;	
	
	private Plugin plugin;
	private ModuleManager modulemanager;
	private boolean respawn;
	private CustomConfig ccfg;
	
	public Module(Plugin plugin, ModuleManager modulemanager, String name){
		
		this.plugin = plugin;
		this.modulemanager = modulemanager;
		this.name = name;
		ccfg = new CustomConfig(name, this.plugin);
		
		
		this.ingame = false;
		this.spawns = new ArrayList<>();
		
		this.setup();
				
	}
	
	public final String getName(){
		return name;
	}
	
	public final void setSpawns(ArrayList<Location> locs){
		spawns = locs;
	}
	
	public final void addSpawn(Location loc){
		
		
		spawns.add(loc);
		
		
		// Die 
		for (int i = spawns.size(); i >= 0; i++){
			if(ccfg.getConfig().getConfigurationSection("spawns").getConfigurationSection("spawn" + i) == null){
				
				ccfg.getConfig().getConfigurationSection("spawns").createSection("spawn" + i);
				ccfg.getConfig().getConfigurationSection("spawns").getConfigurationSection("spawn" + i).set("world", loc.getWorld());
				ccfg.getConfig().getConfigurationSection("spawns").getConfigurationSection("spawn" + i).set("X", loc.getX());
				ccfg.getConfig().getConfigurationSection("spawns").getConfigurationSection("spawn" + i).set("Y", loc.getY());
				ccfg.getConfig().getConfigurationSection("spawns").getConfigurationSection("spawn" + i).set("Z", loc.getZ());
				ccfg.getConfig().getConfigurationSection("spawns").getConfigurationSection("spawn" + i).set("YAW", loc.getYaw());
				ccfg.getConfig().getConfigurationSection("spawns").getConfigurationSection("spawn" + i).set("PITCH", loc.getPitch());
				
				ccfg.saveConfig();
				
				
				break;
			}
		}
		
		
	}
	
	
	public final void loadSpawns(){
		
		// Alle Spawns aus der Config des Minigames laden und Location Objekte erzeugen
		
		for(int i = 0; i >= 0; i ++){
			if(ccfg.getConfig().getConfigurationSection("spawns").getConfigurationSection("spawn" + i) != null){
				Location loc = new Location(null, 0, 0, 0);
				loc.setWorld(Bukkit.getWorld(ccfg.getConfig().getConfigurationSection("spawn").getConfigurationSection("spanw" + i).getString("world")));
				loc.setX(ccfg.getConfig().getConfigurationSection("spawn").getConfigurationSection("spanw" + i).getDouble(("X")));
				loc.setY(ccfg.getConfig().getConfigurationSection("spawn").getConfigurationSection("spanw" + i).getDouble(("Y")));
				loc.setZ(ccfg.getConfig().getConfigurationSection("spawn").getConfigurationSection("spanw" + i).getDouble(("Z")));
				loc.setPitch((float) ccfg.getConfig().getConfigurationSection("spawn").getConfigurationSection("spanw" + i).getDouble(("PITCH")));
				loc.setYaw((float) ccfg.getConfig().getConfigurationSection("spawn").getConfigurationSection("spanw" + i).getDouble(("YAW")));
				this.spawns.add(loc);
			}else{
				break;
			}
		}
	}
	
	public final void teleport(){
		
		// Wenn nicht genügend Spawns vorhanden sind, alle Spieler zum ersten teleportierem (könnte von Module gewollt sein...)
		
		if(modulemanager.getPlayers().size() < spawns.size()){
			for(Player c : modulemanager.getPlayers()){
				c.teleport(spawns.get(1));
			}
		}else{
			for(int i = 0; i < modulemanager.getPlayers().size(); i ++){
				modulemanager.getPlayers().get(i).teleport(spawns.get(i));
			}
		}
		
		
	}
	
	public void start(){
		// Muss vom entsprechenden Module überschrieben werden
	}
	
	
	
	
	
	public void setup(){
		// Kann später vom entpsrechenden Module überschrieben werden, falls Minispielspezifisches irgendetwas passieren muss
		
	}
	public void respawn(Player player) {
		Random rdm = new Random();
		int randomint = rdm.nextInt(spawns.size() - 1);
		player.teleport(spawns.get(randomint), TeleportCause.PLUGIN);
	}
	/** @author Leonhard
	 * @param event
	 */
	@EventHandler
	public void block(Cancellable event) {
		if (!ingame) {
			event.setCancelled(true);
		}
	}
	/** @author Leonhard
	 * @param event
	 */
	@EventHandler
	public void PlayerDeath(EntityDamageEvent event) {
		if (respawn) {
			if (event.getEntity() instanceof Player) {
				Player player = (Player) event.getEntity();
				if ((((Damageable) event.getEntity()).getHealth() - event
						.getDamage()) < 0) {
					respawn(player);
				}
				

				
			}
		}
	}

	
	
	
	
	
}
