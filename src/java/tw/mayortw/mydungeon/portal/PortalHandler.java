package tw.mayortw.mydungeon.portal;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import tw.mayortw.mydungeon.MYDungeon;
import tw.mayortw.mydungeon.exception.PortalTagException;
import tw.mayortw.mydungeon.i18n.I18n;

public class PortalHandler implements Listener{
//	private Map<UUID, Block> dirtyPortals = new HashMap<>();
//	
//	@EventHandler(priority = EventPriority.HIGH)
//	public void onSignEdited (SignChangeEvent e) {
//		if (e.getBlock().getX() != 0 || e.getBlock().getY() != -1 || e.getBlock().getZ() != 0) return;
//		
//		e.setCancelled(true);
//		
//		if (!e.getPlayer().hasPermission("mydungeon.portal.config")
//				|| !this.dirtyPortals.containsKey(e.getPlayer().getUniqueId())) return;
//		
//		String content = String.join("\n", e.getLines());
//
//		// extract the first tag, RegExp = "^.*?(?:< *dungeon (.*?) */?>.*)*$"
//		// extract the last tag, RegExp = "^.*?(?:< *dungeon (.*?) */?>.*?)*$"
//		// if no tag is found, an empty string is returned
//		String attrStr = content.replaceAll("^.*?(?:(< *dungeon +.*? */?>).*?)*$", "$1");
//		
//		if (attrStr.isEmpty()) return;
//		
//		PortalTag tagObj;
//		try{
//			tagObj = new PortalTag(attrStr);
//		} catch (PortalTagException err) {
//			MYDungeon.LOG.warning("Failed to initiate a dungeon portal.");
//			MYDungeon.LOG.warning(err.toString());
//			
//			return;
//		}
//		
//		MYDungeon.dungeonManager.addPortal(
//				tagObj.createPortal(this.dirtyPortals.get(e.getPlayer().getUniqueId())));
//		
//		// clear all dungeon tags in the content of a sign
//		String[] contentNoTags = content.replaceAll("< *dungeon +.*? */?>", "").split("\n");
//		for (int i = 0; i < contentNoTags.length; i++) {
//			e.setLine(i, contentNoTags[i]);
//		}
//	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onSignClicked (PlayerInteractEvent e) {
		if (e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.SIGN_POST) {
			if (e.getAction() == Action.RIGHT_CLICK_BLOCK
					&& !e.getPlayer().isSneaking()) {
				Portal portal = MYDungeon.dungeonManager.getPortal(e.getClickedBlock().getLocation());
				if (portal != null && portal.isPlayerAllowed(e.getPlayer())) {
					// let the player join the dungeon
					portal.teleportPlayer(e.getPlayer());
				}
			} else if (e.getAction() == Action.LEFT_CLICK_BLOCK && e.getPlayer().isSneaking()
					&& e.getPlayer().hasPermission("mydungeon.portal.config")) {
				ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
				if (item.getType() != Material.BOOK_AND_QUILL) {
					ItemStack configBook = new ItemStack(Material.BOOK_AND_QUILL);
					BookMeta meta = (BookMeta) configBook.getItemMeta();
					
					I18n i18n = I18n.get(e.getPlayer().getLocale());
					meta.setDisplayName(i18n.translate("configbook.displayname"));
					meta.setAuthor(MYDungeon.plugin.getName());
					meta.setLore(null);
					meta.setUnbreakable(true);
					
					int pageIndex = 1;
					String key = String.format("configbook.content.page%d", pageIndex);
					for (; i18n.has(key); key = String.format("configbook.content.page%d", ++pageIndex)) {
						meta.addPage("");
						meta.setPage(pageIndex, i18n.translate(key));
					}

					configBook.setItemMeta(meta);
					e.getPlayer().getInventory().addItem(configBook);
				} else {
					BookMeta meta = (BookMeta) item.getItemMeta();
					String content = String.join(" ", meta.getPages());
					try {
						Portal portal = new Portal(e.getClickedBlock().getLocation(), content);
						MYDungeon.dungeonManager.addPortal(portal);
					} catch (PortalTagException err) {
						MYDungeon.LOG.warning("Failed to initiate a portal.");
						MYDungeon.LOG.warning(err.toString());
					}
				}
			}
		}
	}
}
