package de.plabbabap.arcade.module.parcour;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import de.plabbabap.arcade.Plugin;
import de.plabbabap.arcade.module.Module;
import de.plabbabap.arcade.module.ModuleManager;

public class Parcour extends Module implements Listener{

	
	
	private boolean started;
	private ArrayList<Location> checkpoints;
	private ArrayList<User> users;
	private boolean in_game;
	private Scoreboard board;
	private Objective obj;
	private int seconds_left;
	
	
	public Parcour(Plugin plugin, ModuleManager modulemanager) {
		super(plugin, modulemanager, "Parcour");
		
		checkpoints = new ArrayList<>();
		users = new ArrayList<>();
		in_game = false;
		started = false;
		seconds_left = 300;
		
		
		
		
		board = Bukkit.getScoreboardManager().getNewScoreboard();
		obj = board.registerNewObjective("distance", "dummy");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.setDisplayName(ChatColor.GREEN + "Distanz  " + ChatColor.WHITE + seconds_left + "");
		
	}
	
	
	
	
	
	@Override
	public void setup(){
		
		
		for(Player p : this.getPlugin().getModuleManager().getPlayers()){
			users.add(new User(p, this));
			this.getUser(p).addPoint(p.getLocation());
		}
		
		
		
		
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
			
			for(Location c : checkpoints){
			
				Location tmp = new Location(null, 0, 0, 0);
				tmp.setWorld(c.getWorld());
				tmp.setX(c.getX());
				tmp.setY(c.getY());
				tmp.setZ(c.getZ() + i * 28);
				tmp.setYaw(c.getYaw());
				tmp.setPitch(c.getPitch());
				users.get(i).addPoint(tmp);
			
			}
		}
		
		
		
		
		
		
		
		
		
		
		//started = true;
		
		
		
	}
	
	
	public void updateScoreboard(){
		
		for(User c : users){
			obj.getScore(c.getPlayer()).setScore((int) (c.getPlayer().getLocation().getBlockX() - c.getCheckpoint(0).getX()));
		}
		
		obj.setDisplayName(ChatColor.GREEN + "Distanz  " + ChatColor.WHITE + seconds_left + "");
		
	}
	
	@Override
	public void start(){
		
		Bukkit.broadcastMessage("Starting the game...");
		
		for(User c : users){
			c.respawn();
		}
		in_game = true;
		started = true;
		
		
		for(User c : users){
			c.getPlayer().setScoreboard(board);
		}
		
		updateScoreboard();
		EndTimer t = new EndTimer();
		t.start();
		
		
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
		
		
		if(this.getPlugin().getModuleManager().getActualModule().getName().equalsIgnoreCase(this.getName()) && (this.getPlugin().getModuleManager().isInLobby() == false)){
			for(User c : users){
				c.onMove(event.getPlayer());
			}
		}
		
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
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event){
		if(this.getPlugin().getModuleManager().getActualModule().getName().equalsIgnoreCase(this.getName()) && (this.getPlugin().getModuleManager().isInLobby() == false)){
			event.setCancelled(true);
		}
	}
	
	
	
	private class EndTimer extends Thread{
		
		public EndTimer(){
			
		}
		
		
		@Override
		public void run(){
			while(true){
				try {
					this.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				seconds_left--;
				updateScoreboard();
				
				if(seconds_left <= 0){
					break;
				}
				
				for(User c : users){
					c.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', getPlugin().getMessageConfig().getString("prefix") + " " + getPlugin().getMessageConfig().getString("points_added").replace("%points%", (obj.getScore(c.getPlayer()).getScore() / 10) + "")));
					getPlugin().getModuleManager().addPoints(c.getPlayer(), (obj.getScore(c.getPlayer()).getScore() / 10));
				}
			}
			
			
		}
		
	}

}
