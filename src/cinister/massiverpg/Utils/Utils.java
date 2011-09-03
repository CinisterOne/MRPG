package cinister.massiverpg.Utils;


import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;

import cinister.massiverpg.Data.GeneralData;



/*
 *This class deals with all miscellaneous tasks, f.e sending a message to a player in a standardized format.
 * *Note: This class is also where all non-related methods that have any need of a class for themselves go. 
 */
public class Utils {
	
	public enum CommandLevel {
		WARNING, ERROR, NOTIFICATION, REWARD;
	}
	
	public enum SpellActions {
		LMB(0), RMB(1), sLMB(2), sRMB(3);
		
		private final int index;
		
		private SpellActions(int index) {
			this.index = index;
		}
		
		public int getIndex() {
			return this.index;
		}
		
		public enum SpellType {
			ALCHEMY("alchemy"), BLACK_MAGIC("black_magic"), WHITE_MAGIC("white_magic");
			
			String name = "";
			
			private SpellType(String name) {
				this.name = name;
			}
			
			public String getName() {
				return this.name;
			}
		}
	}
	// Sends a personal-message to the play that only they may hear.
	public static void sendPrivateMessage(CommandLevel level, String message, Player player) {
		String tag = GeneralData.MESSAGE_TAG;
		ChatColor color = ChatColor.WHITE;
		if (level == CommandLevel.WARNING) {
			color = GeneralData.WARNING_COLOR;
		} else if (level == CommandLevel.ERROR) {
			color = GeneralData.ERROR_COLOR;
		} else if (level == CommandLevel.NOTIFICATION) {
			color = GeneralData.NOTIFICATION_COLOR;
		} else if (level == CommandLevel.REWARD) {
			color = GeneralData.SUCCESS_COLOR;
		}
		String[] splitted = message.split("\n");
		for (String newMessage : splitted) {
			player.sendMessage(color + tag + newMessage);
		}
	}
	
	public static ChatColor getColor(String color) {
		if (color.equalsIgnoreCase("gold")) {
			return ChatColor.GOLD;
		} else if (color.equalsIgnoreCase("red")) {
			return ChatColor.RED;
		} else if (color.equalsIgnoreCase("yellow")) {
			return ChatColor.YELLOW;
		} else if (color.equalsIgnoreCase("blue")) {
			return ChatColor.BLUE;
		} else if (color.equalsIgnoreCase("dark_blue")) {
			return ChatColor.DARK_BLUE;
		} else if (color.equalsIgnoreCase("green")) {
			return ChatColor.GREEN;
		} else if (color.equalsIgnoreCase("dark_red")) {
			return ChatColor.DARK_RED;
		} else if (color.equalsIgnoreCase("black")) {
			return ChatColor.BLACK;
		} else if (color.equalsIgnoreCase("dark_green")) {
			return ChatColor.DARK_GREEN;
		} else if (color.equalsIgnoreCase("dark_purple")) {
			return ChatColor.DARK_PURPLE;
		} else if (color.equalsIgnoreCase("dark_aqua")) {
			return ChatColor.DARK_AQUA;
		} else if (color.equalsIgnoreCase("aqua")) {
			return ChatColor.AQUA;
		} else if (color.equalsIgnoreCase("gray")) {
			return ChatColor.GRAY;
		} else if (color.equalsIgnoreCase("dark_gray")) {
			return ChatColor.DARK_GRAY;
		} else if (color.equalsIgnoreCase("light_purple")) {
			return ChatColor.LIGHT_PURPLE;
		} else if (color.equalsIgnoreCase("white")) {
			return ChatColor.WHITE;
		} else {
			return ChatColor.WHITE;
		}
	}
	
	public static String concatStringArray(String[] array, String seperator) {
		StringBuffer buffer = new StringBuffer();
		if (array.length > 0) {
			buffer.append(array[0]);
			for (int i = 1; i < array.length; i++) {
				buffer.append(seperator + array[i]);
			}
		}
		return buffer.toString();
	}
	
	public static String getTime(long timeMillis) {
		long time = timeMillis / 1000;
		String seconds = Integer.toString((int)(time % 60));
		String minutes = Integer.toString((int) (time % 3600) / 60);
		String hours = Integer.toString((int) (time / 3600));
		return hours + " hours " + minutes + " minutes and " + seconds + " seconds";
	}
	
	public static int getTypeId(Projectile projectile) {
		int id = 0;
		if (projectile instanceof Arrow) {
			id = Material.ARROW.getId();
		} else if (projectile instanceof Egg) {
			id = Material.EGG.getId();
		} else if (projectile instanceof Snowball) {
			id = Material.SNOW_BALL.getId();
		}
		return id;
	}
}

