package de.plababap.arcade.module;

import org.bukkit.event.Listener;

public class Module implements Listener {

	// Autor: Leo (Zortax)
	
	// Dies ist die Superclass, die die einzelnen Minispiele dann erweitern
	
	private String name;
	boolean ingame;
	
	
	public Module(String name){
		
		this.name = name;
		this.ingame = false;
		
		
		this.setup();
				
	}
	
	public final String getName(){
		return name;
	}
	
	
	
	
	
	public void setup(){
		// Kann später vom entpsrechenden Module überschrieben werden, falls Minispielspezifisches irgendetwas passieren muss
		
	}
	
	
	
	
	
}
