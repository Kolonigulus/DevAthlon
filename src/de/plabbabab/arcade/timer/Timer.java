package de.plabbabab.arcade.timer;

import org.bukkit.configuration.Configuration;

public class Timer implements Runnable{

	// Autor: Leo (Zortax)
	
	private String msg;
	
	public Timer(int seconds, String msg_key, Configuration cfg){
		
		msg = cfg.getString(msg_key);
			
	}
	
	@Override
	public void run() {
		
		
		
		
	}
	
	

}
