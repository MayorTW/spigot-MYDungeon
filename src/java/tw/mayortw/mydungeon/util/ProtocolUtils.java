package tw.mayortw.mydungeon.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import org.bukkit.entity.Player;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;

public class ProtocolUtils {
	public static ProtocolManager manager = ProtocolLibrary.getProtocolManager();
	
	public static void sendServerPacket (Collection<Player> players, PacketContainer packet) throws InvocationTargetException {
		for (Player receiver : players) {
			manager.sendServerPacket(receiver, packet);
		}
	}
	
	public static void sendServerPacket (Collection<Player> players, Collection<PacketContainer> packets) throws InvocationTargetException {
		for (Player receiver : players) {
			sendServerPacket(receiver, packets);
		}
	}
	
	public static void sendServerPacket (Player receiver, Collection<PacketContainer> packets) throws InvocationTargetException {
		for (PacketContainer packet : packets) {
			manager.sendServerPacket(receiver, packet);
		}
	}
}
