package de.plabbabap.arcade.data;

import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import de.plabbabap.arcade.Plugin;


public class CustomConfig {

	// Autor: Leo (Zortax)
	
	private Plugin plugin;
	private String name;
	
	
	private FileConfiguration customConfig = null;
	private File customConfigFile = null;
	
	public CustomConfig(String name, Plugin plugin){
		
		this.plugin = plugin;
		this.name = name;
		
		this.reloadConfig();
	}
	
	
	
	
	public void reloadConfig() {
	    if (customConfigFile == null) {
	    customConfigFile = new File(plugin.getDataFolder(), name);
	    }
	    customConfig = YamlConfiguration.loadConfiguration(customConfigFile);
	 
	   
	    // testen, ob eine DefaultConfig in der JAR liegt...
	    Reader defConfigStream;
		try {
			defConfigStream = new InputStreamReader(plugin.getResource(name), "UTF8");
			
			if (defConfigStream != null) {
		        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
		        customConfig.setDefaults(defConfig);
		    }
			
		} catch (UnsupportedEncodingException e) {
			plugin.getLogger().log(Level.WARNING, "Fehler beim laden von " + name + "!");
			e.printStackTrace();
		}
	    
	}
	
	public FileConfiguration getConfig() {
	    if (customConfig == null) {
	        reloadConfig();
	    }
	    return customConfig;
	}
	
	public void saveConfig() {
	    if (customConfigFile == null) {
	        customConfigFile = new File(plugin.getDataFolder(), name);
	    }
	    if (!customConfigFile.exists()) {            
	         plugin.saveResource(name, false);
	    }
	}
	
	
}
