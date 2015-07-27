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
	private Objective obj;
	private org.bukkit.scoreboard.Scoreboard scoreboard;

	public DigCube(Plugin plugin, ModuleManager modulemanager) {
		super(plugin, modulemanager, "digcube");

	}

	@SuppressWarnings("deprecation")
	@Override
	public void setup() {
		seconds_left = 120;
		dias = new HashMap<Player, Integer>();
		for (Location c : getSpawns()) {
			generateCube(c);
		}
		for (Player c : getPlugin().getModuleManager().getPlayers()) {
			dias.put(c, 0);
		}
		seconds_left = getConfig().getInt("time");
		getPlugin().getServer().getScheduler()
				.scheduleSyncDelayedTask(getPlugin(), new Runnable() {
					@Override
					public void run() {
						scoreboard = Bukkit.getScoreboardManager()
								.getNewScoreboard();
						obj = scoreboard.registerNewObjective("dias", "dummy");
						obj.setDisplaySlot(DisplaySlot.SIDEBAR);
						obj.setDisplayName("Zeit übrig: " + seconds_left);
						for (Player c : getPlugin().getModuleManager()
								.getPlayers()) {
							c.setScoreboard(scoreboard);
							updateScoreboard();
						}
					}

				});

		getPlugin().getLogger().info("Scoreboard inititalisiert");

	}

	private void generateCube(Location loc) {
		getPlugin().getServer().getScheduler()
				.scheduleSyncDelayedTask(getPlugin(), new Runnable() {

					@Override
					public void run() {
						Location start = new Location(loc.getWorld(), loc
								.getX(), loc.getY(), loc.getZ() - 20);
						for (int i = 0; i < 20 * 2 + 1; i++) {
							for (int j = 0; j < 20 * 2 + 1; j++) {
								for (int k = 0; k < 20; k++) {
									Location temp = new Location(start
											.getWorld(), start.getX() + i,
											start.getY() + j, start.getZ() + k);
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
									} else if (randi == 80) {
										block.setType(Material.DIAMOND_ORE);
									}
								}
							}
						}
					}

				});

	}

	@Override
	public void start() {
		for (Player c : getPlugin().getModuleManager().getPlayers()) {
			c.getInventory().clear();
			ItemStack stack = new ItemStack(Material.STONE_PICKAXE);
			stack.addEnchantment(Enchantment.DIG_SPEED, 2);
			stack.addEnchantment(Enchantment.DURABILITY, 3);
			c.getInventory().setItem(0, stack);
			stack = new ItemStack(Material.DIAMOND_AXE);
			stack.addEnchantment(Enchantment.DURABILITY, 3);
			c.getInventory().setItem(2, stack);
			stack = new ItemStack(Material.DIAMOND_SPADE);
			stack.addEnchantment(Enchantment.DURABILITY, 3);
			c.getInventory().setItem(4, stack);
			stack = new ItemStack(Material.SHEARS);
			stack.addEnchantment(Enchantment.DURABILITY, 3);
			c.getInventory().setItem(6, stack);
			stack = new ItemStack(Material.IRON_PICKAXE);
			stack.setDurability((short) 40);
			c.getInventory().setItem(8, stack);

		}

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDia(PlayerPickupItemEvent event) {
		if (event.getItem().getItemStack().getType().equals(Material.DIAMOND)
				&& isIngame()) {
			event.getPlayer().getInventory().remove(Material.DIAMOND);
			if (dias.containsKey(event.getPlayer())) {
				dias.replace(event.getPlayer(), dias.get(event.getPlayer()) + 1);
				updateScoreboard();
			}
		}
	}

	private class EndTimer extends Thread {
		public EndTimer() {

		}

		@Override
		public void run() {
			while (true) {
				try {
					this.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				seconds_left--;
				updateScoreboard();

				if (seconds_left <= 0) {
					ende();
					break;
				}

			}

		}

	}

	public void updateScoreboard() {
		for (Player c : this.getPlugin().getModuleManager().getPlayers()) {
			obj.getScore(c).setScore(dias.get(c));
		}
		obj.setDisplayName("Zeit übrig: " + seconds_left);
	}

	protected void ende() {
		Player winner = null;
		for (Player c : getPlugin().getModuleManager().getPlayers()) {
			if (winner != null) {
				if (dias.get(c) > dias.get(winner)) {
					winner = c;
				}
			} else {
				winner = c;
			}
			getPlugin().getModuleManager().addPoints(c, dias.get(c));
		}
		getPlugin().getModuleManager().broadcast(
				"&aDer Gewinner ist " + winner.getName());
	}
}
