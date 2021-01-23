package io.github.soulofakuma;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class DataModerator implements Runnable {

	private final Plugin plugin;
	private final HashMap<Player, ArrayList<Location>> traces;
	private final HashMap<Player, Location> prevs;
	private final HashMap<Player, Boolean> triggering;
	private final HashMap<Player, Long> lastTp;
	
	public DataModerator(Plugin plugin) {
		this.plugin = plugin;
		this.traces = new HashMap<Player, ArrayList<Location>>();
		this.prevs = new HashMap<Player, Location>();
		this.triggering = new HashMap<Player, Boolean>();
		this.lastTp = new HashMap<Player, Long>();
	}
	
	public void addLocation(Player player, Location loc) {
		if (!traces.containsKey(player)) setup(player);
		ArrayList<Location> locList = this.traces.get(player);
		if (this.lastTp.get(player) + 100 > System.currentTimeMillis() && locList.size() > 0 && locationIsEqual(locList.get(locList.size() - 1), loc)) return; //Avoid double entries when some plugins accidently trigger the event multiple times
		locList.add(loc);
		this.lastTp.put(player, System.currentTimeMillis());
		TPTrace.sendMessage(player, ChatColor.DARK_GREEN + "Your teleport has been recorded!");
	}
	
	private boolean locationIsEqual(Location loc1, Location loc2) {
		return Math.round(loc1.getX()) == Math.round(loc2.getX()) && 
				Math.round(loc1.getY()) == Math.round(loc2.getY()) &&
				Math.round(loc1.getZ()) == Math.round(loc2.getZ()) &&
				loc1.getWorld().equals(loc2.getWorld());
	}
	
	public Location removeLocations(Player player, int count) {
		if (!traces.containsKey(player)) setup(player);
		Location loc = null;
		ArrayList<Location> locations = this.traces.get(player);
		if (count <= locations.size()) {
			for (int i = 0; i < count; i++) {
				loc = locations.remove(locations.size() - 1);
			}
		}
		return loc;
	}
	
	public ArrayList<Location> getLocationList(Player player) {
		if (traces.containsKey(player)) return this.traces.get(player);
		this.setup(player);
		return null;
	}
	
	public void setup(Player player) {
		this.traces.put(player, new ArrayList<Location>());
		this.triggering.put(player, false);
		this.lastTp.put(player, 0L);
	}

	public void setup(Collection<? extends Player> players) {
		for (Player player : players) {
			this.traces.put(player, new ArrayList<Location>());
			this.triggering.put(player, false);
			this.lastTp.put(player, 0L);
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
