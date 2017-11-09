package tw.mayortw.mydungeon.portal;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import tw.mayortw.mydungeon.MYDungeon;
import tw.mayortw.mydungeon.exception.PortalTagException;
import tw.mayortw.mydungeon.i18n.I18n;

public class Portal {	
	private Location src;
	private Location dst;
	
	// dungeon meta
	private String dgName;
	private String [] dgPlayerRules, dgTeamRules;
	
	/**
	 * @throws PortalTagException
	 */
	public Portal (Location portallocation,
			Location dungeonLocation,
			String dungeonName,
			String [] dungeonPlayerRules,
			String [] dungeonTeamRules) {
		
		this.src = portallocation;
		this.dst = dungeonLocation;
		
		this.dgName = dungeonName;
		this.dgPlayerRules = dungeonPlayerRules;
		this.dgTeamRules = dungeonTeamRules;
	}
	
	public boolean isPlayerAllowed (Player playerIn) {
		String name = playerIn.getName();
		boolean isAllowed = false;
		
		for (String teamRule : this.dgTeamRules) {
			teamRule = teamRule.trim();
			String teamName = teamRule.replaceAll("^[+-]", "");
			Scoreboard scoreboard = MYDungeon.scoreboardManager.getMainScoreboard();
			
			if (teamRule.equals("*") || 
					(scoreboard.getTeam(teamName) != null && scoreboard.getTeam(teamName).hasEntry(name))) {
				if (teamRule.startsWith("-")) {
					isAllowed = false;
				} else {
					isAllowed = true;
				}
			}
		}
		
		for (String playerRule : this.dgPlayerRules) {
			playerRule = playerRule.trim().replaceAll("^[+-]", "");
			
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
		
		I18n i18n = I18n.get(playerIn.getLocale());
		playerIn.sendTitle(this.dgName, i18n.translate("dungeon.subtitle"), 15, 30, 15);
	}
	
	public Location getSrcLocation () {
		return this.src;
	}
	
	public Location getDstLocation () {
		return this.dst;
	}
	
	public String getDungeonName () {
		return this.dgName;
	}
	
	public String[] getPlayerRules () {
		return this.dgPlayerRules;
	}
	
	public String[] getTeamRules () {
		return this.dgTeamRules;
	}
	
	public static String getIndex (Portal portalIn) {
		return getIndex(portalIn.src);
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
