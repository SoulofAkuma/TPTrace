package io.github.soulofakuma;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class TPTrace extends JavaPlugin {
	
	private DataModerator moderator;
	private Teleporter teleporter;
	private LocationList llist;
	
	@Override
	public void onEnable() {
		getLogger().info("Loading TPTrace Plugin");
		this.moderator = new DataModerator(this);
		this.teleporter = new Teleporter(this, this.moderator);
		this.llist = new LocationList(this, this.moderator);
		this.getCommand("back").setExecutor(this.teleporter);
		this.getCommand("backlist").setExecutor(this.llist);
		this.teleporter.startTrace();
	}
	
	@Override
	public void onDisable() {
		getLogger().info("Unloading TPTrace Plugin");
		this.teleporter.stopTrace();
		//TODO: plugin unloader logic
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		return false;
	}
	
	public static void sendMessage(Player player, String message) {
		player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "TPTrace"+ ChatColor.DARK_GRAY + "] " + ChatColor.WHITE + message);
	}
	
	public static void sendMessage(CommandSender player, String message) {
		player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "TPTrace" + ChatColor.DARK_GRAY + "] " + ChatColor.WHITE + message);
	}
}
