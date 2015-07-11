package de.plabbabap.arcade.module.digcube;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import de.plabbabap.arcade.Plugin;
import de.plabbabap.arcade.module.Module;
import de.plabbabap.arcade.module.ModuleManager;

public class DigCube extends Module {
private HashMap<Player, Integer> dias;
public int seconds_left;
private Objective  obj;
private org.bukkit.scoreboard.Scoreboard scoreboard;
private Objective objt;
	public DigCube(Plugin plugin, ModuleManager modulemanager) {
		super(plugin, modulemanager, "digcube");

	}

	@Override
	public void setup() {
		for (Location c : getSpawns()) {
			generateCube(c);
		}
		for(Player c : getPlugin().getModuleManager().getPlayers()){
			dias.put(c, 0);
		}
		seconds_left = getConfig().getInt("time");
	}

	private void generateCube(Location loc) {
		Location start = new Location(loc.getWorld(), loc.getX()
				+ getConfig().getInt("Cubesize.toEndfromMid"), loc.getY(),
				loc.getZ() - getConfig().getInt("Cubesize.toEndfromMid"));
		for (int i = 0; i < getConfig().getInt("Cubesize.toEndfromMid") * 2 + 1; i++) {
			for (int j = 0; i < getConfig().getInt("Cubesize.toEndfromMid") * 2 + 1; j++) {
				for (int k = 0; i < getConfig().getInt("Cubesize.tiefe"); k++) {
					Location temp = new Location(loc.getWorld(), i, k, j);
					Block block = temp.getBlock();
					Random rdm = new Random();
					int randi = rdm.nextInt(81);
					if (randi < 20) {
						block.setType(Material.WOOD);
					} else if (randi < 40) {
						block.setType(Material.STONE);
					} else if (randi < 60) {
						block.setType(Material.SOUL_SAND);
					} else if (randi < 80) {
						block.setType(Material.WOOL);
					}else if(randi == 81){
						block.setType(Material.DIAMOND_ORE);
					}
				}
			}
		}

	}
	@Override
	public void start(){
		for(Player c : getPlugin().getModuleManager().getPlayers()){
			c.getInventory().clear();
			ItemStack stack = new ItemStack(Material.STONE_PICKAXE);
			stack.addEnchantment(Enchantment.DIG_SPEED, 2);
			stack.addEnchantment(Enchantment.DURABILITY, 1000);
			c.getInventory().setItem(0, stack);
			stack = new ItemStack(Material.DIAMOND_AXE);
			stack.addEnchantment(Enchantment.DURABILITY, 1000);
			c.getInventory().setItem(2, stack);
			stack = new ItemStack(Material.DIAMOND_SPADE);
			stack.addEnchantment(Enchantment.DURABILITY, 1000);
			c.getInventory().setItem(4, stack);
			stack = new ItemStack(Material.SHEARS);
			stack.addEnchantment(Enchantment.DURABILITY, 1000);
			c.getInventory().setItem(6, stack);
			stack = new ItemStack(Material.IRON_PICKAXE);
			stack.setDurability((short) 40);
			c.getInventory().setItem(8, stack);
		}
		scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		obj = scoreboard.registerNewObjective("dias", "dummy");
		objt = scoreboard.registerNewObjective("Zeit", "dummy");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.setDisplayName("Zeit übrig");
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDia(PlayerPickupItemEvent event){
		if(event.getItem().getItemStack().getType().equals(Material.DIAMOND) || isIngame()){
			event.getItem().remove();
			if(dias.containsKey(event.getPlayer())){
				dias.replace(event.getPlayer(), dias.get(event.getPlayer()) + 1);
			}
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
			

		}
		
		
	}
	
}
	public void updateScoreboard() {
		
	}
}
