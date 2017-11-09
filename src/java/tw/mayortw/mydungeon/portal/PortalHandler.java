package tw.mayortw.mydungeon.portal;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import tw.mayortw.mydungeon.MYDungeon;
import tw.mayortw.mydungeon.exception.PortalCountExceedException;
import tw.mayortw.mydungeon.i18n.I18n;

public class PortalHandler implements Listener {
	/**
	 * To stop anyone from breaking the portal, the listen should be the highest priority
	 * @param e
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onSignBroken (BlockBreakEvent e) {
		if (isValidMaterial(e.getBlock().getType())) {
			if (hasPermission(e.getPlayer())) {
				I18n i18n = I18n.get(e.getPlayer().getLocale());
				String invoice = MYDungeon.dungeonManager.removePortal(e.getBlock().getLocation());
				if (!invoice.isEmpty()) {
					e.getPlayer().sendMessage(i18n.translate("config.remove", invoice));
				}
			} else {
				e.setCancelled(true);
			}			
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onSignClicked (PlayerInteractEvent e) {
		if (clickOnBlock(e.getClickedBlock()) && isValidMaterial(e.getClickedBlock().getType())) {
			I18n i18n = I18n.get(e.getPlayer().getLocale());
			if (tryToActivatePortal(e.getAction(), e.getPlayer())) {
				Portal portal = MYDungeon.dungeonManager.getPortal(e.getClickedBlock().getLocation());
				if (portal != null && portal.isPlayerAllowed(e.getPlayer())) {
					// let the player join the dungeon
					portal.teleportPlayer(e.getPlayer());
				}
			} else if (tryToConfigPortal(e.getAction(), e.getPlayer()) && hasPermission(e.getPlayer())) {
				ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
				if (!isConfigBook(item.getType())) {
					String dungeonTag = "<dg w=world x=0 y=0 z=0>";
					Portal existingPortal = null;
					if ((existingPortal = MYDungeon.dungeonManager.getPortal(e.getClickedBlock().getLocation())) != null) {
						dungeonTag = MYDungeon.parser.getDungeonMeta(existingPortal);
					}
					
					ItemStack configBook = new ItemStack(Material.BOOK_AND_QUILL);
					BookMeta meta = (BookMeta) configBook.getItemMeta();
					
					meta.setDisplayName(i18n.translate("configbook.displayname"));
					meta.setAuthor(MYDungeon.plugin.getName());
					meta.setLore(null);
					meta.setUnbreakable(true);
					
					int pageIndex = 1;
					String key = String.format("configbook.content.page%d", pageIndex);
					while (true) {
						meta.addPage("");
						String nextKey = String.format("configbook.content.page%d", pageIndex + 1);
						if (!i18n.has(nextKey)) {
							meta.setPage(pageIndex++, i18n.translate(key) + "\n\n" + dungeonTag);
							break;
						}
						meta.setPage(pageIndex++, i18n.translate(key));
						key = nextKey;
					}

					configBook.setItemMeta(meta);
					e.getPlayer().getInventory().addItem(configBook);
				} else {
					BookMeta meta = (BookMeta) item.getItemMeta();
					Portal portal = MYDungeon.parser.createPortal(e.getClickedBlock().getLocation(), 
							String.join(" ", meta.getPages()));
					try {
						String invoice = MYDungeon.dungeonManager.addPortal(portal);
						item.setAmount(0);
						e.getPlayer().sendMessage(i18n.translate("config.success", invoice));
					} catch (PortalCountExceedException err) {
						e.getPlayer().sendMessage(i18n.translate("config.fail"));
						MYDungeon.LOG.warning("Failed to config or add a portal.");
						MYDungeon.LOG.warning(err.toString());
					}
					
				}
			}
		}
	}
	
	private boolean clickOnBlock (Block blockIn) {
		return blockIn != null;
	}
	
	private boolean hasPermission (Player playerIn) {
		return playerIn.hasPermission("mydungeon.portal.config");
	}
	
	private boolean isValidMaterial (Material materialIn) {
		return materialIn == Material.SIGN_POST || materialIn == Material.WALL_SIGN;
	}
	
	private boolean tryToConfigPortal (Action actionIn, Player playerIn) {
		return actionIn == Action.LEFT_CLICK_BLOCK && playerIn.isSneaking();
	}
	
	private boolean tryToActivatePortal (Action actionIn, Player playerIn) {
		return actionIn == Action.RIGHT_CLICK_BLOCK && !playerIn.isSneaking();
	}
	
	private boolean isConfigBook (Material materialIn) {
		return materialIn == Material.BOOK_AND_QUILL || materialIn == Material.WRITTEN_BOOK;
	}
}
