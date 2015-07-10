package de.plababap.arcade.module;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.event.Listener;

import de.plabbabab.arcade.Plugin;

public class Module implements Listener {

	// Autor: Leo (Zortax)
	
	// Dies ist die Superclass, die die einzelnen Minispiele dann erweitern
	
	private String name;
	private boolean ingame;
	private ArrayList<Location> spawns;	
	
	private Plugin plugin;
	private ModuleManager modulemanager;
	
	public Module(Plugin plugin, ModuleManager modulemanager, String name){
		
		this.plugin = plugin;
		this.modulemanager = modulemanager;
		this.name = name;
		
		
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
	}
	
	
	
	
	
	public void setup(){
		// Kann später vom entpsrechenden Module überschrieben werden, falls Minispielspezifisches irgendetwas passieren muss
		
	}
	
	
	
	
	
}
