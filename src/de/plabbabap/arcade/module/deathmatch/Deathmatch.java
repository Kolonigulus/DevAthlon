package de.plabbabap.arcade.module.deathmatch;

import java.util.ArrayList;
import java.util.HashMap;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import de.plabbabap.arcade.Plugin;
import de.plabbabap.arcade.module.Module;
import de.plabbabap.arcade.module.ModuleManager;

public class Deathmatch extends Module implements Listener {
	
	private ArrayList<Player> alive;
	private HashMap<Player, Integer> kills;

	public Deathmatch(Plugin plugin, ModuleManager modulemanager) {
		super(plugin, modulemanager, "Deathmatch");
		
		alive = new ArrayList<>();
		kills = new HashMap<>();
		
		
		
	}
	
	
	
	@Override
	public void start(){
		for(Player p : this.getPlugin().getModuleManager().getPlayers()){
			alive.add(p);
			kills.put(p, 0);
			p.getInventory().setItem(0, new ItemStack(Material.WOOD_SWORD));
		}
	}
	
	
	
	@EventHandler
	public void onDamage(EntityDamageEvent event){
		if(event.getEntity() instanceof Player){
			Player p = (Player) event.getEntity();
			
			if(event.getDamage() >= p.getHealth()){
				event.setCancelled(true);
				p.setHealth(20);
				p.teleport(this.getPlugin().getModuleManager().getLobbyspawn());
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7>> &cDu bist auschgeschieden!"));
				alive.remove(p);
				if(alive.size() == 1){
					this.getPlugin().getModuleManager().addPoints(alive.get(0), 10);
					Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7>> &6" + alive.get(0).getName() + " &ahat gewonnen!"));
					this.getPlugin().getModuleManager().finish(this);
				}
			}
			
		}
	}
	
	@EventHandler
	public void block(BlockBreakEvent e){
		e.setCancelled(true);
	}

}
