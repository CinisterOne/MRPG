package cinister.massiverpg;

import java.util.HashMap;

import org.bukkit.entity.Player;

import cinister.massiverpg.Data.PlayerProfile;

public class Users {
	public static HashMap<String, PlayerProfile> users = new HashMap<String, PlayerProfile>();
	
	public static PlayerProfile getUser(Player player) {
		PlayerProfile profile = null;
		if (!(users.containsKey(player.getName()))) {
			users.put(player.getName(), profile = new PlayerProfile(player));
		} else {
			profile = users.get(player.getName());
		}
		return profile;
	}
	
	//Just adding a bit of options
	public static void addUser(Player player) {
		Users.addUser(player.getName());
	}
	
	public static void addUser(String player) {
		if (!users.containsKey(player)) {
			PlayerProfile pp = new PlayerProfile(player);
			users.put(player, pp);
		}
	}
	
	public static void removeUser(Player player) {
		users.remove(player.getName());
	}
	
	public static void removeUser(String player) {
		users.remove(player);
	}
	
	public static void clearUsers() {
		users.clear();
	}
}
