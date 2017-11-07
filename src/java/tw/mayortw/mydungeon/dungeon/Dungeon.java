package tw.mayortw.mydungeon.dungeon;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;

public class Dungeon {
	protected final UUID _id = UUID.randomUUID();
	protected Set<String> players = new HashSet<String>();
	
	public Dungeon (Player ...playersIn) {
		this.addPlayer(playersIn);
	}
	
	public void addPlayer (Player ...playersIn) {
		for(Player player: playersIn){
			this.players.add(player.getName());
		}
	}
	
	public boolean hasMember (Player playerIn) {
		return this.hasMember(playerIn.getName());
	}
	
	public boolean hasMember (String nameIn) {
		return this.players.contains(nameIn);
	}
	
	public UUID getId () {
		return this._id;
	}
	
	@Override
	public boolean equals (Object o) {
		return (o instanceof Dungeon)? ((Dungeon) o).getId().equals(this._id): false;
	}
}
