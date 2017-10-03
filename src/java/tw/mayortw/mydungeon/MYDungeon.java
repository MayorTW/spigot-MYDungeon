package tw.mayortw.mydungeon;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.PluginLogger;
import org.bukkit.plugin.java.JavaPlugin;

public class MYDungeon extends JavaPlugin {
	private final PluginLogger LOG = (PluginLogger) this.getLogger();
	
	@Override
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(new TriggerPoint(), this);
	}
	
	@Override
	public void onDisable() {
		
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		return super.onTabComplete(sender, command, alias, args);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
//		Bukkit.broadcastMessage("Sender: "+sender.getName()+
//				"\nCommand: "+command.toString()+"\nLabel: "+label+"\nArgs: "+String.join(", ", args));
		return true;
	}
	
	public static final class TriggerPoint implements Listener{
		@EventHandler
		public void check(PlayerInteractEvent event){
			if(this.isTime(event)){
				//TODO Create dungeon!!!
			}
		}
		
		private boolean isTime(PlayerInteractEvent event){
			return event.getHand() == EquipmentSlot.HAND &&
					event.getPlayer().getInventory().getItemInMainHand().getType() == Material.DIAMOND;
		}
	}
}
