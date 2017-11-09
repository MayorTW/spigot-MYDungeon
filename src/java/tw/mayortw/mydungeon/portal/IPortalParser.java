package tw.mayortw.mydungeon.portal;

import org.bukkit.Location;

public interface IPortalParser {
	/**
	 * Called by PortalHandler when a portal is configured by players
	 * @param portalLocation
	 * @param portalConfig
	 * @return
	 */
	public Portal createPortal (Location portalLocation, String portalConfig);
	
	/**
	 * Called when a portal is about to be loaded from the disk
	 * @param portalConfig
	 * @return
	 */
	public Portal parse (String portalConfig);
	
	/**
	 * Called when a portal is about to be saved to the disk
	 * @param portalConfig
	 * @return
	 */
	public String stringify (Portal portal);
	
	/**
	 * Called when a portal is about to show on the configuration book
	 * @param portalConfig
	 * @return
	 */
	public String getDungeonMeta (Portal portal);
	
	public String getPortalMeta (Portal portal);
}
