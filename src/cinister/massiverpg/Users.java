package cinister.massiverpg;

import java.util.HashMap;


import org.bukkit.entity.Player;

import cinister.massiverpg.Data.PlayerProfile;


public class Users {
	public static HashMap<String, PlayerProfile> users = new HashMap<String, PlayerProfile>();
	
	public static PlayerProfile getUser(Player player) {
		PlayerProfile profile = null;
		if (!(users.containsKey(player.getName()))) {
			Users.addUser(player);
		} else {
			profile = users.get(player.getName());
		}
		return profile;
	}
	
	//Just adding a bit of options
	public static PlayerProfile addUser(Player player) {
		return Users.addUser(player.getName(), player.getWorld().getName());
	}
	
	public static PlayerProfile addUser(String player, String world) {
		PlayerProfile pp = null;
		if (!users.containsKey(player)) {
			pp = new PlayerProfile(player, world);
			users.put(player, pp);
		}
		return pp;
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
