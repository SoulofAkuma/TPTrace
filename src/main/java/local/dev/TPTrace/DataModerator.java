package local.dev.TPTrace;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class DataModerator implements Runnable {

	private final Plugin plugin;
	private final HashMap<Player, ArrayList<Location>> traces;
	private final HashMap<Player, Location> prevs;
	private final HashMap<Player, Boolean> triggering;
	
	public DataModerator(Plugin plugin) {
		this.plugin = plugin;
		this.traces = new HashMap<Player, ArrayList<Location>>();
		this.prevs = new HashMap<Player, Location>();
		this.triggering = new HashMap<Player, Boolean>();
	}
	
	public void addLocation(Player player, Location loc) {
		if (!traces.containsKey(player)) setup(player);
		this.traces.get(player).add(loc);
	}
	
	public Location removeLocations(Player player, int count) {
		if (!traces.containsKey(player)) setup(player);
		Location loc = null;
		ArrayList<Location> locations = this.traces.get(player);
		if (count <= locations.size()) {
			for (int i = 0; i < count; i++) {
				loc = locations.remove(locations.size() - 1 - i);
			}
		}
		return loc;
	}
	
	public void setup(Player player) {
		this.traces.put(player, new ArrayList<Location>());
		this.triggering.put(player, false);
	}

	public void setup(Collection<? extends Player> players) {
		for (Player player : players) {
			this.traces.put(player, new ArrayList<Location>());
			this.triggering.put(player, false);
		}
	}
	
	public void remove(Player player) {
		this.traces.remove(player);
		this.triggering.remove(player);
	}
	
	public void run() {
		for (Player player : this.triggering.keySet()) {
			this.prevs.put(player, player.getLocation());
		}
	}
	
	public Location getPreviousLocation(Player player) {
		if (this.prevs.containsKey(player)) return this.prevs.get(player);
		return null;
	}
	
	public void changeTriggerState(Player player) {
		this.triggering.put(player, !this.triggering.get(player));
	}
	
	public boolean isTriggering(Player player) {
		if (this.triggering.get(player)) {
			return true;
		}
		return false;
	}
}
