package local.dev.TPTrace;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class TPTrace extends JavaPlugin {
	
	private DataModerator moderator;
	private Teleporter teleporter;
	
	@Override
	public void onEnable() {
		getLogger().info("Loading TPTrace Plugin");
		this.moderator = new DataModerator(this);
		this.teleporter = new Teleporter(this, this.moderator);
		this.getCommand("back").setExecutor(this.teleporter);
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
}
