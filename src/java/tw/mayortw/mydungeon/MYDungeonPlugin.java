package tw.mayortw.mydungeon;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginLogger;
import org.bukkit.plugin.java.JavaPlugin;

import me.momocow.common.util.JarUtils;
import tw.mayortw.mydungeon.i18n.I18n;
import tw.mayortw.mydungeon.portal.Portal;
import tw.mayortw.mydungeon.portal.PortalHandler;

public class MYDungeonPlugin extends JavaPlugin {
	private final PluginLogger LOG = (PluginLogger) this.getLogger();
	
	@Override
	public void onEnable () {
		this.initConfig();
		this.initLang();
		
		MYDungeon.init(this);
		
		this.getServer().getPluginManager().registerEvents(new PortalHandler(), this);
	}
	
	@Override
	public void onDisable () {
		MYDungeon.dungeonManager.saveAll();
	}
	
	@Override
	public boolean onCommand (CommandSender sender, Command command, String label, String[] args) {
		if(args.length >= 2 && args[0].equals("portal")){
			switch (args[1]) {
				case "list":
					List<String> portals = new ArrayList<>();
					MYDungeon.dungeonManager.listPortals().forEach(portal -> {
						portals.add(MYDungeon.parser.stringify(portal));
					});
					if (portals.size() > 0){
						sender.sendMessage(portals.toArray(new String[portals.size()]));
					} else {
						I18n i18n = sender instanceof Player ? I18n.get(((Player) sender).getLocale()) : I18n.get();
						sender.sendMessage(i18n.translate("command.reply.portal-list-empty"));
					}
					return true;
				default:
					Portal portal = MYDungeon.dungeonManager.getPortal(args[1]);
					if (portal != null) {
						sender.sendMessage(MYDungeon.parser.stringify(portal));
						return true;
					}
			}
		}
		return false;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> candidates = new ArrayList<>();
		if (args.length == 1) {
			if ("portal".startsWith(args[0])) candidates.add("portal");
		} else if (args.length == 2) {
			if (args[0].equals("portal")) {
				if ("list".startsWith(args[1])) {
					candidates.add("list");
				}
				
				if (this.getConfig().getBoolean("enable-tabComplete-portalIndex", false)) {
					MYDungeon.dungeonManager.listIndices().forEach(index -> {
						if (index.startsWith(args[1])) {
							candidates.add(index);
						}
					});
				}
			}
		}
		
		return candidates;
	}
	
	public void initConfig () {
		LOG.fine("Initializing config file");
		
		File configFile = new File(this.getDataFolder(), "config.yml");
		
		if(!configFile.getParentFile().exists()){
			configFile.getParentFile().mkdirs();
		}
		
		if(!configFile.exists()){
			this.saveDefaultConfig();
		}
	}
	
	public void initLang () {
		
		File thisJarFile = null;
		try {
			thisJarFile = JarUtils.getJarFile(this.getClass());
		} catch (URISyntaxException e) {
			this.LOG.warning("Failed to get the path of the running jar.");
			this.LOG.warning(e.toString());
		}
		
		List<String> langFiles = new ArrayList<>();
		try {
			langFiles = JarUtils.listFiles(thisJarFile, "lang");
		} catch (IOException e) {
			this.LOG.warning("Failed to list the lang files in the jar.");
			this.LOG.warning(e.toString());
		}

		Map<String, InputStream> forI18n = new HashMap<>();
		for (String langFile : langFiles) {
			forI18n.put(langFile, this.getResource(langFile));
		}
		
		I18n.init(forI18n);
	}
}
