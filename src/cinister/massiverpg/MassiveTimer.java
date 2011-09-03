package cinister.massiverpg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimerTask;


import org.bukkit.Bukkit;

import cinister.massiverpg.Data.PlayerProfile;
import cinister.massiverpg.Data.Spells.SpellDuration;
import cinister.massiverpg.Utils.Utils;
import cinister.massiverpg.Utils.Utils.CommandLevel;

public class MassiveTimer extends TimerTask {
	
	//Perttttyyy sloppy but oh well, it works, and it saves efficiency rather than iterating through every single player on the server.
	private static ArrayList<String> cooldownPlayers = new ArrayList<String>();
	// Holds all the spells that have been cast and are still in progress, for example, healing for 20 seconds.
	private static ArrayList<SpellDuration> spellBeingCasted = new ArrayList<SpellDuration>();
	//For each player name which is the key there are a list of names of the spells that have cooldowns, they are then referenced to their long values stored in the player and then subtracted there
	private static HashMap<String, ArrayList<String>> cooldowns = new HashMap<String, ArrayList<String>>();
	
	// Add a timer for cooldowns for the specified spell
	public static void addCooldown(String spellName, String playerName) {
		if (cooldowns.containsKey(playerName)) {
			cooldowns.get(playerName).add(spellName);
		} else {
			// Have to do it this way so that Java is happy.
			ArrayList<String> list = new ArrayList<String>();
			list.add(spellName);
			cooldowns.put(playerName, list);
		}
		if (!cooldownPlayers.contains(playerName)) {
			cooldownPlayers.add(playerName);
		}
	}
	
	// Removes the cooldown, it's been spent.
	public static void removeCooldown(String player, String spell) {
		if (cooldowns.get(player).size() > 1) {
			cooldowns.get(player).remove(cooldowns.get(player).indexOf(spell));
		} else {
			cooldowns.remove(player);
			cooldownPlayers.remove(player);
		}
	}
	
	//Checks to see if the cooldown for the spell for the player is finished or not.
	public static boolean isCooldownFinished(String player, String spell) {
		if (!cooldowns.containsKey(player)) {
			return true;
		} else {
			if (cooldowns.get(player).contains(spell)) {
				return true;
			}
		}
		return false;
	}
	
	
	/*
	 * Used to add a player to the list, returns true if the player isn't already in progress of another spell
	 * returns false if the player is.
	 */
	public static boolean addPlayerSpellCast(SpellDuration spell) {
		for (SpellDuration spellDuration : spellBeingCasted) {
			// If the player already has a spell going on then we return false and exit everything
			if (spellDuration.getOwner().getName().equals(spell.getOwner().getName())) {
				return false;
			}
		}
		// Every other case we add it to the list and return true;
		spellBeingCasted.add(spell);
		return true;
	}
	
	/*
	 * Used to remove a player from the active spell record, returns true if the player had a spell and was present, returns false if there's none.
	 */
	public static boolean removePlayerSpellCast(String player) {
		for (SpellDuration spellDuration : spellBeingCasted) {
			if (spellDuration.getOwner().getName().equals(player)) {
				spellBeingCasted.remove(spellDuration);
				return true;
			}
		}
		return false;
	}
	
	public static SpellDuration getSpellBeingCasted(String player) {
		for (SpellDuration spellDuration : spellBeingCasted) {
			if (spellDuration.getOwner().getName().equals(player)) {
				return spellDuration;
			}
		}
		return null;
	}
	
	/*
	 * Checks to see if a player has cast a spell and is currently using it, prevents spell stacking.
	 */
	public static boolean hasPlayerSpell(String player) {
		for (SpellDuration spellDuration : spellBeingCasted) {
			if (spellDuration.getOwner().getName().equals(player)) {
				return true;
			}
		}
		return false;
	}
	
	
	@Override
	public void run() {
		// Goes through every player that has a cooldown and every spell in the players cooldown and decrements them, unless the decrement is less than or equal to zero then it removes the cooldown
		// and notifies the player.
		for (String player : cooldownPlayers) {
			for (String spell : cooldowns.get(player)) {
				PlayerProfile profile = Users.getUser(Bukkit.getServer().getPlayer(player));
				if (profile.getSpellTime(spell) - 1000 <= 0) {
					removeCooldown(player, spell);
					Utils.sendPrivateMessage(CommandLevel.NOTIFICATION, "The spell " + spell + " can now be used again.", Bukkit.getServer().getPlayer(player));
				} else {
					profile.tickSpellTime(spell);
				}
			}
		}
		// Iterates through all the spells active in the server and decrements them all, unless it's less than 0, then it removes it.
		for (SpellDuration spell : spellBeingCasted) {
			//Calls the spell events, respectively
			if (spell.getTime() - 1000 <= 0 && spell.getTime() != -1337) {
				removePlayerSpellCast(spell.getOwner().getName());
				spell.getSpell().getSpellCast().onCastFinish(spell.getOwner());
				Utils.sendPrivateMessage(CommandLevel.NOTIFICATION, "The spell " + spell.getSpell().getName() + " has worn off.", spell.getOwner());
			} else {
				spell.tick();
				spell.getSpell().getSpellCast().onCastTick(spell.getOwner());
			}
		}
	}
}
