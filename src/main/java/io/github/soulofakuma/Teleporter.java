package io.github.soulofakuma;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.plugin.Plugin;

public class Teleporter implements CommandExecutor, Listener {

	private final Plugin plugin;
	private final DataModerator moderator;
	private int taskID;
	
	public Teleporter(Plugin plugin, DataModerator moderator) {
		this.plugin = plugin;
		this.moderator = moderator;
	}
	
	public void startTrace() {
		this.moderator.setup(this.plugin.getServer().getOnlinePlayers());
		this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.taskID = this.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(this.plugin, this.moderator, 0, 1);
	}
	
	public void stopTrace() {
		this.plugin.getServer().getScheduler().cancelTask(this.taskID);
	}
	
	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		if (event.getCause() == TeleportCause.UNKNOWN) return;
		if (!this.moderator.isTriggering(event.getPlayer())) {
			this.moderator.addLocation(event.getPlayer(), this.moderator.getPreviousLocation(event.getPlayer()));
		}
		//event.getPlayer().sendMessage("You are at x:" + String.valueOf(prev.getX()) + " y:" + String.valueOf(prev.getY()) + " and are going to x:" + String.valueOf(event.getTo().getX()) + " y:" + String.valueOf(event.getTo().getY()));
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		this.moderator.setup(event.getPlayer());
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		this.moderator.remove(event.getPlayer());
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		this.moderator.addLocation(event.getEntity().getPlayer(), this.moderator.getPreviousLocation(event.getEntity().getPlayer()));
		TPTrace.sendMessage(event.getEntity().getPlayer(), ChatColor.DARK_GREEN + "Your death location has been saved and can be reached with /back!");
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length == 0) {
				teleport(player, this.moderator.removeLocations(player, 1));
			} else if (args.length == 1 && Teleporter.isInt(args[0])) {
				int steps = Integer.parseInt(args[0]);
				if (steps < 1) {
					TPTrace.sendMessage(player, ChatColor.RED + " How is that supposed to work? You can't go " + String.valueOf(steps) + " steps back!");
					return true;
				}
				teleport(player, this.moderator.removeLocations(player, steps));
			} else if (args.length == 1 && !Teleporter.isInt(args[0])){
				TPTrace.sendMessage(player, ChatColor.RED + args[0] + " is not a number!");
				return true;
			}
			return true;
		} else {
			TPTrace.sendMessage(sender, "This is a player only command!");
			return true;
		}
	}
	
	private void teleport(Player player, Location loc) {
		if (loc == null) {
			TPTrace.sendMessage(player, ChatColor.DARK_RED + "There is nowhere to go back to!");
		} else {
			this.moderator.disableTrigger(player);
			player.teleport(loc);
			TPTrace.sendMessage(player, ChatColor.GOLD + "Teleported back!");
			final Player playerWrapper = player;
			this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
				
				public void run() {
					moderator.enableTrigger(playerWrapper);
					
				}
			}, 4);
		}
	}
	
	public static boolean isInt(String str) {
	    if(str.isEmpty()) return false;
	    for(int i = 0; i < str.length(); i++) {
	        if(i == 0 && str.charAt(i) == '-') {
	            if(str.length() == 1) return false;
	            else continue;
	        }
	        if(Character.digit(str.charAt(i),10) < 0) return false;
	    }
	    return true;
	}
}
