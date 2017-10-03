package tw.mayortw.mydungeon;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginLogger;
import org.bukkit.plugin.java.JavaPlugin;

public class MYDungeon extends JavaPlugin {
	private final PluginLogger LOG = (PluginLogger) this.getLogger();
	
	@Override
	public void onEnable() {
	}
	
	@Override
	public void onDisable() {
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Bukkit.broadcastMessage("Sender: "+sender.getName()+
				"\nCommand: "+command.toString()+"\nLabel: "+label+"\nArgs: "+String.join(", ", args));
		return true;
	}
}
