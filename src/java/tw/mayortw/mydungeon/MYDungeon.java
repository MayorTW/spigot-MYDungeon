package tw.mayortw.mydungeon;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.bukkit.plugin.PluginLogger;
import org.bukkit.scoreboard.ScoreboardManager;

import tw.mayortw.mydungeon.portal.IPortalParser;
import tw.mayortw.mydungeon.portal.PortalTagParser;

public final class MYDungeon {
	public static MYDungeonPlugin plugin;
	public static DungeonManager dungeonManager;
	public static IPortalParser parser;
	public static ScoreboardManager scoreboardManager;
	public static Map<String, String> serverProperties;
	public static PluginLogger LOG;
	
	private static boolean _isReady = false;
	
	public static void init (MYDungeonPlugin pluginIn) {
		plugin = pluginIn;
		LOG = (PluginLogger) pluginIn.getLogger();
		parser = new PortalTagParser();
		
		dungeonManager = new DungeonManager(plugin);
		scoreboardManager = plugin.getServer().getScoreboardManager();
		serverProperties = loadServerProperties();
				
		_isReady = true;
	}
	
	public static boolean isReady () {
		return _isReady;
	}
	
	private static Map<String, String> loadServerProperties () {
		Map<String, String> ret = new HashMap<> ();
		File serverPropFile = new File(plugin.getServer().getWorldContainer().getAbsolutePath(), "server.properties");
		
		try (Stream<String> stream = Files.lines(serverPropFile.toPath())) {
	        stream.forEach(line -> {
	        	String[] propPair = line.split("=", 2);
				if (propPair.length == 1 || propPair[0].isEmpty()) return;
				
				ret.put(propPair[0], propPair[1]);
	        });
		} catch (IOException e) {
			LOG.warning("Failed to read the server properties file at " + serverPropFile.getAbsolutePath());
			e.printStackTrace();
		} 
		
		return ret;
	}
}
