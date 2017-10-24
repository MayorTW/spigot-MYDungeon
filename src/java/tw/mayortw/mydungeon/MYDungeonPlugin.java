package tw.mayortw.mydungeon;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginLogger;
import org.bukkit.plugin.java.JavaPlugin;

import tw.mayortw.mydungeon.init.MYDungeon;

public class MYDungeonPlugin extends JavaPlugin {
	private final PluginLogger LOG = (PluginLogger) this.getLogger();
	
	@Override
	public void onEnable() {
		this.initConfig();
		
		MYDungeon.init(this);
	}
	
	@Override
	public void onDisable() {
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(args[0].equals("create")){
			ArrayList<Player> partyPlayers = new ArrayList<Player>();
			partyPlayers.add(this.getServer().getPlayer((sender.getName())));
			for(int i = 1; i < args.length; i++){
				partyPlayers.add(this.getServer().getPlayer(args[i]));
			}
			MYDungeon.manager.createDungeon(partyPlayers.toArray(new Player[]{}));
		}
		return true;
	}
	
	public void initConfig(){
		LOG.fine("Initializing config file");
		
		File configFile = new File(this.getDataFolder(), "config.yml");
		
		if(!configFile.getParentFile().exists()){
			configFile.getParentFile().mkdirs();
		}
		
		if(!configFile.exists()){
			this.saveDefaultConfig();
		}
	}
}
