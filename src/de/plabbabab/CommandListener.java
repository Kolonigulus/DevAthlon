package de.plabbabab;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * @author Leonhard(Grundgerüst)
 *
 */
public class CommandListener implements CommandExecutor {
String Command;
PluginName p;
	public CommandListener(PluginName p){
		Command = null;	//TODO
		this.p = p;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label,
			String[] args) {
		if(command.getName().equals(Command)){
			
		}
		return false;
	}

}
