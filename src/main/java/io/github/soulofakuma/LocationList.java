package io.github.soulofakuma;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class LocationList implements CommandExecutor {
	
	private final Plugin plugin;
	private final DataModerator moderator;
	
	public LocationList(Plugin plugin, DataModerator moderator) {
		this.plugin = plugin;
		this.moderator = moderator;
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			ArrayList<Location> locations = this.moderator.getLocationList(player);
			int steps = (locations.size() > 10) ? 10 : locations.size();
			if (args.length == 1 && Teleporter.isInt(args[0])) {
				steps = Integer.parseInt(args[0]);
			} else if (args.length != 0) {
				return false;
			}
			if (locations.size() == 0) {
				TPTrace.sendMessage(player, ChatColor.RED + "Your backlist is empty!");
				return true;
			}
			if (steps > locations.size()) {
				TPTrace.sendMessage(player, ChatColor.RED + "Your list of previous locations is smaller than the number you've provided. Displaying the full list of " + String.valueOf(locations.size()) + " elements.");
				steps = locations.size();
			}
			TPTrace.sendMessage(player, "Here is your list of locations you can go back to:");
			player.sendMessage(ChatColor.YELLOW + "-----------------------------------------------------");
			ChatColor c = ChatColor.GRAY;
			for (int i = 0; i < steps; i++) {
				Location loc = locations.get(locations.size() - 1 - i);
				player.sendMessage(c +
						String.valueOf(i + 1) + ". Step: Coordinates: " + 
						String.valueOf(Math.round(loc.getX())) + ", " + String.valueOf(Math.round(loc.getY())) + ", " + String.valueOf(Math.round(loc.getZ())) +
						" | World: " + loc.getWorld().getName() +
						" | Distance: " + Math.round(loc.distance(player.getLocation())));
				c = (c == ChatColor.GRAY) ? ChatColor.DARK_GRAY : ChatColor.GRAY;
			}
			player.sendMessage(ChatColor.YELLOW + "-----------------------------------------------------");
			return true;
		} else {
			TPTrace.sendMessage(sender, "This is a player only command!");
			return true;
		}
		
	}

}
