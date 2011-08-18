package cinister.massiverpg.Data;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class PlayerProfile {
	//Players name
	private String name;
	//The string is the name of the skill, the integer is the level of it 0-maxLevel, it's a reference for how many bonuses to add
	private HashMap<String, Integer> playerSkills = new HashMap<String, Integer>();
	//The string is the name of the spell, and the long is the cooldown in milliseconds
	private HashMap<String, Long> playerSpells = new HashMap<String, Long>();
	//This is the unique reference id in the database table
	private long userid;
	private int mana;
	private int maxMana;
	private int maxHealth;
	private int health;
	private int mainLevel;
	private int mainXP;
	private String quickSpell = "";
	//Their Learning Points
	private int LP;
	//The world in which he plays, perhaps unnecessary, but hey.
	private String world;
	//The string location is this, X,Y,Z seperated by commas and in string form.
	private String spawnLocation;
	
	public PlayerProfile(Player player) {
		this(player.getName());
	}
	
	//TODO Load all PlayerProfile data from MySQL, use database.java as a reference.
	public PlayerProfile(String player) {
		
	}
	
	//Sets the spell that's cast when it is right clicked.
	public void setQuickSpell(String quickSpell) {
		//Checks to see if it exists
		for (Spell spell : GeneralData.spells) {
			if (spell.getName().equalsIgnoreCase(quickSpell)) {
				this.quickSpell = quickSpell;
			}
		}
	}
	
	public boolean addSkillPoints(String name, int amount) {
		if (playerSkills.containsKey(name)) {
			amount += playerSkills.get(name);
			playerSkills.remove(name);
			playerSkills.put(name, amount);
			return true;
		}
		return false;
	}
	
	public boolean subtractSkillPoints(String name, int amount) {
		if (playerSkills.containsKey(name)) {
			amount -= playerSkills.get(name);
			playerSkills.remove(name);
			playerSkills.put(name, amount);
			return true;
		}
		return false;
	}
	
	//Casting from Integer to int JUST to be safe.
	public int getSkillPoints(String name) {
		return (int) playerSkills.get(name);
	}
	
	public int getLearningPoints() {
		return this.LP;
	}
	
	public void addLearningPoints(int newAmount) {
		this.LP += newAmount;
	}
	
	public int getLevel() {
		return this.mainLevel;
	}
	
	public void setLevel(int level) {
		this.mainLevel = level;
	}
	
	public int getExperience() {
		return this.mainXP;
	}
	
	public void levelUp() {
		this.mainLevel++;
		Bukkit.getServer().getPlayer(this.name).sendMessage(ChatColor.GREEN + "Congratulations! You have leveled up to level " + this.mainLevel + "!");
	}
	
	public int getRemainingXP() {
		return (mainLevel * 250 * (mainLevel + 1) + (mainLevel * 250)) - mainXP;
	}
	
	public boolean addXP(int amount) {
		this.mainXP += amount * GeneralData.EXP_RATE;
		if (this.getRemainingXP() <= 0) {
			if (this.mainLevel + 1 <= GeneralData.MAX_LEVEL) {
				this.levelUp();
				return true;
			}
		}
		return false;
	}
	
	public int getHealth() {
		return this.health;
	}
	
	public void setHealth(int amount) {
		this.health = amount;
	}
}
