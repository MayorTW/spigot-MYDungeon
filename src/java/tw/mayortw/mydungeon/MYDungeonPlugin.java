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
import org.bukkit.plugin.PluginLogger;
import org.bukkit.plugin.java.JavaPlugin;

import me.momocow.common.utils.JarUtils;
import tw.mayortw.mydungeon.i18n.I18n;
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
//		if(args[0].equals("create")){
//			ArrayList<Player> partyPlayers = new ArrayList<Player>();
//			partyPlayers.add(this.getServer().getPlayer((sender.getName())));
//			for(int i = 1; i < args.length; i++){
//				partyPlayers.add(this.getServer().getPlayer(args[i]));
//			}
//			MYDungeon.dungeonManager.createDungeon(partyPlayers.toArray(new Player[]{}));
//		}
		return true;
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
