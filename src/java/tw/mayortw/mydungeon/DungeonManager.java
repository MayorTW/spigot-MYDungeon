package tw.mayortw.mydungeon;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

import tw.mayortw.mydungeon.init.MYDungeon;

public final class DungeonManager {
	private int MAX_DUNGEONS;
	
	private Set<Dungeon> dungeons = new HashSet<Dungeon>();
	
	public DungeonManager(){
		FileConfiguration config = MYDungeon.plugin.getConfig();
		
		MAX_DUNGEONS = config.getInt("max-dungeons", 10);
	}
	
	public boolean createDungeon(Player ...playersIn){
		// no resources available for a new dungeon
		if(this.dungeons.size() >= MAX_DUNGEONS){
			MYDungeon.LOG.warning("Reject to create a new dungeon since there have already been too much dungeons");
			return false;
		}
		
		List<Player> playerlist = Arrays.asList(playersIn);
		
		// filter those players who have already been in another dungeon
		for(Dungeon currentDungeon: this.dungeons){
			for(Player dungeonPlayer: playerlist){
				if(currentDungeon.hasMember(dungeonPlayer)){
					playerlist.remove(dungeonPlayer);
				}
			}
		}
		
		// register new dungeon
		Dungeon newDungeon = new Dungeon(playerlist.toArray(new Player[]{}));
		this.dungeons.add(newDungeon);
		
		// hide players from anyone else outside the dungeon
		List<? extends Player> onlinePlayers = 
				Lists.newArrayList(MYDungeon.plugin.getServer().getOnlinePlayers());
		onlinePlayers.removeAll(playerlist);
		for(Player otherPlayer: onlinePlayers){
			for(Player dungeonPlayer: playerlist){
				otherPlayer.hidePlayer(dungeonPlayer);
				dungeonPlayer.hidePlayer(otherPlayer);
			}
		}
		
		for(Player dungeonPlayer: playerlist){
			dungeonPlayer.sendMessage("Have a nice dungeon game.");
		}
		
		MYDungeon.LOG.info("A new dungeon is created with id=" + newDungeon.getId());
		
		return true;
	}
	
	public boolean createDungeon(String ...playersIn){
		return true;
	}
}
