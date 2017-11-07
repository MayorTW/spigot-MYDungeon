package tw.mayortw.mydungeon.portal;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import me.momocow.common.utils.StringUtils;
import tw.mayortw.mydungeon.MYDungeon;
import tw.mayortw.mydungeon.exception.PortalTagException;

public class Portal {	
	private World srcWorld;
	private int srcX;
	private int srcY;
	private int srcZ;
	
	private World dstWorld;
	private int dstX = 0;
	private int dstY = 0;
	private int dstZ = 0;
	
	// dungeon meta
	private String dgName = "";
	private String [] dgPlayers = new String [] {}, dgTeams = new String [] {};
	
	/**
	 * @throws PortalTagException
	 */
	public Portal (Location locationIn, String content) throws PortalTagException {
		this.parseTagString(content);
		
		this.srcWorld = locationIn.getWorld();
		this.srcX = locationIn.getBlockX();
		this.srcY = locationIn.getBlockY();
		this.srcZ = locationIn.getBlockZ();
	}
	
	private void parseTagString (String content) throws PortalTagException {
		// extract the first tag, RegExp = "^.*?(?:< *dungeon (.*?) */?>.*)*$"
		// extract the last tag, RegExp = "^.*?(?:< *dungeon (.*?) */?>.*?)*$"
		// if no tag is found, an empty string is returned
		String attrStr = content.replaceAll("^.*?(?:< *(?:dungeon|dg) +(.*?) */?>.*?)*$", "$1");
		
		if (attrStr.isEmpty()) throw new PortalTagException("No Dungeon tag is found in the provided content.\n" 
				+ "Raw content: \"" + content + "\"");
		
		String[] attributes = StringUtils.preserveQuotedSpaces(attrStr)
				.replaceAll(" +", " ")				// remove redundant spaces
				.split(" ");						// split all attribute tokens

		for (int i = 0; i < attributes.length; i++) {
			String[] attrPair = attributes[i].split("=");
			String attrValue = (attrPair.length < 2) ? "" : 
				attrPair[1].trim().replaceAll("^[\"']", "").replaceAll("[\"']$", "");
			attrValue = StringUtils.recoverPreservedSpaces(attrValue);
			
			// switch statement with a String expression requires Java7+
			switch (attrPair[0]) {
				case "n":
				case "name":
					this.dgName = attrValue;
					break;
				case "p":
				case "players":
					this.dgPlayers = attrValue.split(", *(?=-|\\+)");
					for (int ii = 0; ii < this.dgPlayers.length; ii++) {
						this.dgPlayers[ii] = this.dgPlayers[ii].trim();
					}
					break;
				case "t":
				case "teams":
					this.dgTeams = attrValue.split(", *(?=-|\\+)");
					for (int ii = 0; ii < this.dgTeams.length; ii++) {
						this.dgTeams[ii] = this.dgTeams[ii].trim();
					}
					break;
				case "w":
				case "world":
					this.dstWorld = MYDungeon.plugin.getServer().getWorld(attrValue);
					break;
				case "x":
					try {
						this.dstX = Integer.parseInt(attrValue);
					} catch (NumberFormatException err) {
						throw new PortalTagException("NumberFormatException at Dungeon X-position.", err);
					}
					break;
				case "y":
					try {
						this.dstY = Integer.parseInt(attrValue);
					} catch (NumberFormatException err) {
						throw new PortalTagException("NumberFormatException at Dungeon Y-position.", err);
					}
					break;
				case "z":
					try {
						this.dstZ = Integer.parseInt(attrValue);
					} catch (NumberFormatException err) {
						throw new PortalTagException("NumberFormatException at Dungeon Z-position.", err);
					}
			}
		}

		if (this.dstWorld == null) {
			throw new PortalTagException("Dungeon world is null, maybe it is not initialized.");
		}
	}
	
	public boolean isPlayerAllowed (Player playerIn) {
		String name = playerIn.getName();
		boolean isAllowed = false;
		
		for (String teamRule : this.dgTeams) {
			teamRule = teamRule.trim();
			String teamName = teamRule.substring(1);
			
			if (teamRule.equals("*") || MYDungeon.scoreboardManager.getMainScoreboard()
					.getTeam(teamName).hasEntry(name)) {
				if (teamRule.startsWith("-")) {
					isAllowed = false;
				} else {
					isAllowed = true;
				}
			}
		}
		
		for (String playerRule : this.dgPlayers) {
			playerRule = playerRule.trim();
			
			if (playerRule.equals("*") || name.matches(playerRule)) {
				if (playerRule.startsWith("-")) {
					isAllowed = false;
				} else {
					isAllowed = true;
				}
			}
		}
		
		return isAllowed;
	}
	
	public void teleportPlayer (Player playerIn) {
		playerIn.teleport(this.getDstLocation());
	}
	
	public Location getSrcLocation () {
		return new Location (this.srcWorld, this.srcX, this.srcY, this.srcZ);
	}
	
	public Location getDstLocation () {
		return new Location (this.dstWorld, this.dstX, this.dstY, this.dstZ);
	}
	
	public String getDungeonTag () {
		return String.format("<dg n=\"%s\" p=\"%s\" t=\"%s\" w=\"%s\" x=%d y=%d z=%d>", 
				this.dgName, 
				String.join(",", this.dgPlayers), 
				String.join(",", this.dgTeams), 
				this.dstWorld.getName(), 
				this.dstX, this.dstY, this.dstZ);
	}
	
	@Override
	public String toString () {
		return String.format("<src w=\"%s\" x=%d y=%d z=%d>%s", 
				this.srcWorld.getName(),
				this.srcX, this.srcY, this.srcZ,
				this.getDungeonTag());
	}
	
	public static String getIndex (Portal portalIn) {
		return getIndex(portalIn.srcWorld.getName(), portalIn.srcX, portalIn.srcY, portalIn.srcZ);
	}
	
	public static String getIndex (Location locationIn) {
		return getIndex (locationIn.getWorld().getName(), 
				locationIn.getBlockX(), locationIn.getBlockY(), locationIn.getBlockZ());
	}
	
	public static String getIndex (String worldname, int x, int y, int z) {
		StringBuilder builder = new StringBuilder(worldname);
		builder.append("-");
		builder.append(String.valueOf(x));
		builder.append("-");
		builder.append(String.valueOf(y));
		builder.append("-");
		builder.append(String.valueOf(z));
		return builder.toString();
	}
}
