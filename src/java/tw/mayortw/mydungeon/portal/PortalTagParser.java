package tw.mayortw.mydungeon.portal;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;

import me.momocow.common.util.StringUtils;
import tw.mayortw.mydungeon.MYDungeon;
import tw.mayortw.mydungeon.exception.PortalTagException;

public class PortalTagParser implements IPortalParser{

	@Override
	public Portal parse (String content) {
		Location portalLocation = null;
		try {
			portalLocation = this.parseSrcTagString(content);
		} catch (PortalTagException e) {
			return null;
		}
		
		if (portalLocation != null) {
			return this.parseDgTagString(portalLocation, content);
		}

		return null;
	}

	@Override
	public String stringify (Portal portalIn) {
		return this.getPortalMeta(portalIn) + this.getDungeonMeta(portalIn);
	}

	@Override
	public Portal createPortal (Location portalLocation, String portalConfig) {
		try {
			return this.parseDgTagString(portalLocation, portalConfig);
		} catch (PortalTagException e) {
			MYDungeon.LOG.warning("Failed to create a portal.");
			MYDungeon.LOG.warning(e.toString());
			return null;
		}
	}
	
	@Override
	public String getDungeonMeta(Portal portalIn) {
		Location dungeonLocation = portalIn.getDstLocation();
		return String.format("<dg f=%f n=\"%s\" p=\"%s\" t=\"%s\" w=\"%s\" x=%d y=%d z=%d>",
				dungeonLocation.getYaw(),
				portalIn.getDungeonName(), 
				String.join(",", portalIn.getPlayerRules()), 
				String.join(",", portalIn.getTeamRules()), 
				dungeonLocation.getWorld().getName(), 
				dungeonLocation.getBlockX(), dungeonLocation.getBlockY(), dungeonLocation.getBlockZ());
	}

	@Override
	public String getPortalMeta(Portal portalIn) {
		Location portalLocation = portalIn.getSrcLocation();
		return String.format("<src w=\"%s\" x=%d y=%d z=%d />", 
				portalLocation.getWorld().getName(),
				portalLocation.getBlockX(), portalLocation.getBlockY(), portalLocation.getBlockZ());
	}
	
	private Location parseSrcTagString (String portalStr) throws PortalTagException {
		String worldname = "";
		World srcWorld = null;
		int srcX = 0, srcY = 0, srcZ = 0;
		boolean hasWorld = false, hasX = false, hasY = false, hasZ = false;
		
		List<List<String>> picked = StringUtils.pickFrom(portalStr, "< *src +(?:(?:(w=.+?)|(x=.+?)|(y=.+?)|(z=.+?)) +)+ */>");
		for (int i = 1; i < picked.get(0).size(); i++) {
			String[] attrPair = picked.get(0).get(i).split("=");
			switch (attrPair[0]) {
				case "w":
					worldname = StringUtils.replaceQuotes(attrPair[1], "");
					srcWorld = MYDungeon.plugin.getServer().getWorld(worldname);
					hasWorld = srcWorld != null;
					break;
				case "x":
					try {
						srcX = Integer.parseInt(attrPair[1]);
						hasX = true;
					} catch (NumberFormatException err) {
						throw new PortalTagException("NumberFormatException at portal X-position.", err);
					}
					break;
				case "y":
					try {
						srcY = Integer.parseInt(attrPair[1]);
						hasY = true;
					} catch (NumberFormatException err) {
						throw new PortalTagException("NumberFormatException at portal X-position.", err);
					}
					break;
				case "z":
					try {
						srcZ = Integer.parseInt(attrPair[1]);
						hasZ = true;
					} catch (NumberFormatException err) {
						throw new PortalTagException("NumberFormatException at portal X-position.", err);
					}
			}
		}
		
		if (hasWorld && hasX && hasY && hasZ) {
			return new Location(srcWorld, srcX, srcY, srcZ);
		}
		
		return null;
	}

	private Portal parseDgTagString (Location portalLocation, String content) throws PortalTagException {
		World dstWorld = null;
		int dstX = 0, dstY = 0, dstZ = 0;
		float facing = 0f;
		
		String dgName = "";
		String [] dgPlayerRules = new String[0], dgTeamRules = new String[0];
		
		// extract the first tag, RegExp = "^.*?(?:< *dungeon (.*?) */?>.*)*$"
		// extract the last tag, RegExp = "^.*?(?:< *dungeon (.*?) */?>.*?)*$"
		// if no tag is found, an empty string is returned
		String attrStr = content.replaceAll("\n", "").replaceAll("^.*?(?:< *(?:dungeon|dg) +(.*?) */?>.*?)*$", "$1");
		
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
				case "f":
				case "facing":
					switch (attrValue) {
						case "s":
						case "south":
							facing = 0f;
							break;
						case "w":
						case "west":
							facing = 90f;
							break;
						case "n":
						case "north":
							facing = 180f;
							break;
						case "e":
						case "east":
							facing = 270f;
							break;
						default:
							try {
								facing = Float.parseFloat(attrValue);
							} catch (NumberFormatException err) {
								throw new PortalTagException("NumberFormatException at Dungeon facing attribute.", err);
							}
					}
					
					break;
				case "n":
				case "name":
					dgName = attrValue;
					break;
				case "p":
				case "players":
					dgPlayerRules = attrValue.split(", *(?=-|\\+)");
					for (int ii = 0; ii < dgPlayerRules.length; ii++) {
						dgPlayerRules[ii] = dgPlayerRules[ii].trim();
					}
					break;
				case "t":
				case "teams":
					dgTeamRules = attrValue.split(", *(?=-|\\+)");
					for (int ii = 0; ii < dgTeamRules.length; ii++) {
						dgTeamRules[ii] = dgTeamRules[ii].trim();
					}
					break;
				case "w":
				case "world":
					dstWorld = MYDungeon.plugin.getServer().getWorld(attrValue);
					break;
				case "x":
					try {
						dstX = Integer.parseInt(attrValue);
					} catch (NumberFormatException err) {
						throw new PortalTagException("NumberFormatException at Dungeon X-position.", err);
					}
					break;
				case "y":
					try {
						dstY = Integer.parseInt(attrValue);
					} catch (NumberFormatException err) {
						throw new PortalTagException("NumberFormatException at Dungeon Y-position.", err);
					}
					break;
				case "z":
					try {
						dstZ = Integer.parseInt(attrValue);
					} catch (NumberFormatException err) {
						throw new PortalTagException("NumberFormatException at Dungeon Z-position.", err);
					}
			}
		}

		if (dstWorld == null) {
			throw new PortalTagException("Dungeon world is null, maybe it is not initialized.");
		}
		
		Location dungeonLocation = new Location(dstWorld, dstX, dstY, dstZ, facing, 0f);
		return new Portal(portalLocation, dungeonLocation, dgName, dgPlayerRules, dgTeamRules);
	}
}
