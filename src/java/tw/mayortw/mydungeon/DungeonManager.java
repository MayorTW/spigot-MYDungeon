package tw.mayortw.mydungeon;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import tw.mayortw.mydungeon.dungeon.Dungeon;
import tw.mayortw.mydungeon.portal.Portal;

public final class DungeonManager {
	private int MAX_DUNGEONS;
	private File PORTALS_FILE;
	
	private Plugin plugin;
	
	private Map<String, Dungeon> dungeons = new HashMap<String, Dungeon>();
	private Map<String, Portal> portals = new HashMap<String, Portal>();
	
	public DungeonManager (Plugin pluginIn) {
		this.plugin = pluginIn;
		
		FileConfiguration config = this.plugin.getConfig();
		this.MAX_DUNGEONS = config.getInt("max-dungeons", 10);
		this.PORTALS_FILE = new File(this.plugin.getDataFolder().getAbsolutePath(), "portals.dat");
	}
	
	public String addPortal (Portal portalIn) {
		String index = null;
		if (portalIn != null) {
			index = Portal.getIndex(portalIn);
			this.portals.put(index, portalIn);
			this.savePortals();
		}
		
		return index;
	}

	public void removePortal (Location locationIn) {
		this.removePortal(Portal.getIndex(locationIn));
	}
	
	public void removePortal (String index) {
		this.portals.remove(index);
		this.savePortals();
	}
	
	public Portal getPortal (Location locationIn) {
		return this.getPortal(Portal.getIndex(locationIn));
	}
	
	public Portal getPortal (String index) {
		return this.portals.get(index);
	}
	
	public void savePortals () {
		if (this.portals.size() == 0) return;
		
		StringBuilder builder = new StringBuilder();
		for (Portal portal : this.portals.values()) {
			builder.append(portal.toString() + "\n");
		}
		
		try {
			Files.write(builder.toString(), this.PORTALS_FILE, Charsets.UTF_8);
		} catch (IOException e) {
			MYDungeon.LOG.warning("Failed to save portals to the disk.");
			MYDungeon.LOG.warning(e.toString());
		}
	}
	
	public void saveAll () {
		this.savePortals();
	}
	
	public boolean createDungeon (Player ...playersIn) {
		// no resources available for a new dungeon
//		if (this.dungeons.size() >= MAX_DUNGEONS) {
//			MYDungeon.LOG.warning("Reject to create a new dungeon since there have already been too much dungeons");
//			return false;
//		}
//		
//		List<Player> playerlist = Arrays.asList(playersIn);
//		
//		// filter those players who have already been in another dungeon
//		for (Dungeon currentDungeon: this.dungeons) {
//			for (Player dungeonPlayer: playerlist) {
//				if (currentDungeon.hasMember(dungeonPlayer)) {
//					playerlist.remove(dungeonPlayer);
//				}
//			}
//		}
//		
//		// register new dungeon
//		Dungeon newDungeon = new Dungeon(playerlist.toArray(new Player[]{}));
//		this.dungeons.add(newDungeon);
//		
//		// hide players from anyone else outside the dungeon
//		List<? extends Player> onlinePlayers = 
//				Lists.newArrayList(MYDungeon.plugin.getServer().getOnlinePlayers());
//		onlinePlayers.removeAll(playerlist);
//		for (Player otherPlayer: onlinePlayers) {
//			for (Player dungeonPlayer: playerlist) {
//				otherPlayer.hidePlayer(dungeonPlayer);
//				dungeonPlayer.hidePlayer(otherPlayer);
//			}
//		}
//		
//		for (Player dungeonPlayer: playerlist) {
//			dungeonPlayer.sendMessage("Have a nice dungeon game.");
//		}
//		
//		MYDungeon.LOG.info("A new dungeon is created with id=" + newDungeon.getId());
//		
		return true;
	}
}
