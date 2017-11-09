package tw.mayortw.mydungeon;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import tw.mayortw.mydungeon.dungeon.Dungeon;
import tw.mayortw.mydungeon.exception.PortalCountExceedException;
import tw.mayortw.mydungeon.portal.Portal;

public final class DungeonManager {
	private int MAX_DUNGEONS;
	private int MAX_PORTALS;
	private File PORTALS_FILE;
	
	private Plugin plugin;

	private Map<String, Dungeon> dungeons = new HashMap<String, Dungeon>();
	private Map<String, Portal> portals = new HashMap<String, Portal>();
	
	public DungeonManager (Plugin pluginIn) {
		this.plugin = pluginIn;
		
		FileConfiguration config = this.plugin.getConfig();
		this.MAX_DUNGEONS = config.getInt("max-dungeons", 10);
		this.MAX_PORTALS = config.getInt("max-portals", 10);
		this.PORTALS_FILE = new File(this.plugin.getDataFolder().getAbsolutePath(), "portals.dat");
		
		this.loadAll();
	}
	
	/**
	 * add or edit a portal config
	 * @param portalIn
	 * @return
	 * @throws PortalCountExceedException
	 */
	public String addPortal (Portal portalIn) throws PortalCountExceedException {
		String index = "";
		if (portalIn != null) {
			index = Portal.getIndex(portalIn);
			if (this.portals.containsKey(index) || this.portals.size() < this.MAX_PORTALS) {
				this.portals.put(index, portalIn);
				this.savePortals();
			} else {
				throw new PortalCountExceedException(this.portals.size());
			}
		}
		
		return index;
	}

	public String removePortal (Location locationIn) {
		return this.removePortal(Portal.getIndex(locationIn));
	}
	
	public String removePortal (String index) {
		Portal portal = this.portals.remove(index);
		this.savePortals();
		return portal == null ? "" : index;
	}
	
	public Portal getPortal (Location locationIn) {
		return this.getPortal(Portal.getIndex(locationIn));
	}
	
	public Portal getPortal (String index) {
		return this.portals.get(index);
	}
	
	public List<Portal> listPortals () {
		return new ArrayList<>(this.portals.values());
	}
	
	public List<String> listIndices () {
		return new ArrayList<>(this.portals.keySet());
	}
	
	public void loadPortals () {
		try (Stream<String> stream = Files.lines(this.PORTALS_FILE.toPath())) {
			stream.forEach(line -> {
				this.addPortal(MYDungeon.parser.parse(line));
			});
		} catch (IOException e) {
			MYDungeon.LOG.warning("Failed to load portals from the disk.");
			MYDungeon.LOG.warning(e.toString());
		} catch (PortalCountExceedException e) {
			MYDungeon.LOG.warning("Failed to add portals to the server.");
			MYDungeon.LOG.warning(e.toString());
		}
	}
	
	public void savePortals () {
		if (this.portals.size() == 0) return;
		
		List<String> content = new ArrayList<>();
		for (Portal portal : this.portals.values()) {
			content.add(MYDungeon.parser.stringify(portal));
		}
		
		try {
			Files.write(this.PORTALS_FILE.toPath(), content);
		} catch (IOException e) {
			MYDungeon.LOG.warning("Failed to save portals to the disk.");
			MYDungeon.LOG.warning(e.toString());
		}
	}
	
	public void loadAll () {
		this.loadPortals();
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
