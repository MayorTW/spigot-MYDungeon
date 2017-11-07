package tw.mayortw.mydungeon;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.bukkit.plugin.PluginLogger;
import org.bukkit.scoreboard.ScoreboardManager;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

import tw.mayortw.mydungeon.i18n.I18n;

public final class MYDungeon {
	public static MYDungeonPlugin plugin;
	public static DungeonManager dungeonManager;
	public static ScoreboardManager scoreboardManager;
	public static ProtocolManager protocolManager;
	public static Map<String, String> serverProperties;
	public static I18n i18n;
	public static PluginLogger LOG;
	
	private static boolean _isReady = false;
	
	public static void init (MYDungeonPlugin pluginIn) {
		plugin = pluginIn;
		LOG = (PluginLogger) pluginIn.getLogger();
		
		dungeonManager = new DungeonManager(plugin);
		protocolManager = ProtocolLibrary.getProtocolManager();
		scoreboardManager = plugin.getServer().getScoreboardManager();
		serverProperties = loadServerProperties();
		i18n = I18n.get();
		
		initPacketListeners();
		
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
	
	private static void initPacketListeners () {
//		protocolManager.addPacketListener(new PacketAdapter (
//				plugin, 
//				ListenerPriority.NORMAL, 
//				PacketType.Play.Server.OPEN_SIGN_EDITOR) {
//			@Override
//			public void onPacketSending(PacketEvent event) {
//				System.out.println(event.getPacket().getStrings());
//			}
//		});
	}
}
