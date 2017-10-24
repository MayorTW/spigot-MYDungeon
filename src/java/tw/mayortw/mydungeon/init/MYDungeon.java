package tw.mayortw.mydungeon.init;

import org.bukkit.plugin.PluginLogger;

import tw.mayortw.mydungeon.DungeonManager;
import tw.mayortw.mydungeon.MYDungeonPlugin;

public final class MYDungeon {
	public static MYDungeonPlugin plugin;
	public static DungeonManager manager;
	public static PluginLogger LOG;
	
	private static boolean _isReady = false;
	
	public static void init(MYDungeonPlugin pluginIn){
		plugin = pluginIn;
		LOG = (PluginLogger) pluginIn.getLogger();
		
		initManager(new DungeonManager());
		
		_isReady = true;
	}
	
	public static void initManager(DungeonManager managerIn){
		manager = managerIn;
	}
	
	public static boolean isReady(){
		return _isReady;
	}
}
