package cinister.massiverpg.Data;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import cinister.massiverpg.MassiveRPG;


public class PlayerProfile {
	private String name;
	private HashMap<String, Integer> playerSkills = new HashMap<String, Integer>();
	private long userid;
	private int mana;
	private int maxMana;
	private int maxHealth;
	private int health;
	private int mainLevel;
	private int mainXP;
	private int LP;
	private String world;
	private String spawnLocation;
	
	public PlayerProfile(Player player) {
		this.health = health;
		this.maxHealth = 100;
		this.LP = LP;
		this.mainXP = mainXP;
		this.mana = mana;
		this.mainLevel = 0;
		this.maxMana = 50;
	}
	
	public PlayerProfile(String player) {
		
	}
	
	public boolean loadMySQL(Player player, MassiveRPG plugin) 
	{
		Integer id = 0;
		id = plugin.database.GetInt("SELECT id FROM "+GeneralData.MySQLPrefix + world + "users WHERE user = '" + player.getName() + "'");
		if(id == 0)
			return false;
		this.userid = id;
		if (id > 0) {
			for (Spell spell : GeneralData.spells) {
				
			}
			HashMap<Integer, ArrayList<String>> spawn = plugin.database.Read("SELECT x, y, z FROM "+LoadProperties.MySQLtablePrefix+"spawn WHERE user_id = " + id);
				spawnLocation = spawn.get(1).get(0) + "," + spawn.get(1).get(1) + "," + spawn.get(1).get(2);				
			HashMap<Integer, ArrayList<String>> cooldowns = mcMMO.database.Read("SELECT mining, woodcutting, unarmed, herbalism, excavation, swords, axes FROM "+LoadProperties.MySQLtablePrefix+"cooldowns WHERE user_id = " + id);
			/*
			 * I'm still learning MySQL, this is a fix for adding a new table
			 * It's not the best method but hey, a man's gotta' do what a man's gotta' do eh?
			 */
			if(cooldowns.get(1) == null)
			{
				plugin.database.Write("INSERT INTO "+GeneralData.MySQLPrefix + world + "cooldowns (user_id) VALUES ("+id+")");
			}
			else
			{
				superBreakerDATS = Integer.valueOf(cooldowns.get(1).get(0));
				treeFellerDATS = Integer.valueOf(cooldowns.get(1).get(1));
				berserkDATS = Integer.valueOf(cooldowns.get(1).get(2));
				greenTerraDATS = Integer.valueOf(cooldowns.get(1).get(3));
				gigaDrillBreakerDATS = Integer.valueOf(cooldowns.get(1).get(4));
				serratedStrikesDATS = Integer.valueOf(cooldowns.get(1).get(5));
				skullSplitterDATS = Integer.valueOf(cooldowns.get(1).get(6));
			}
			HashMap<Integer, ArrayList<String>> stats = mcMMO.database.Read("SELECT taming, mining, repair, woodcutting, unarmed, herbalism, excavation, archery, swords, axes, acrobatics FROM "+LoadProperties.MySQLtablePrefix+"skills WHERE user_id = " + id);
				skills.put(SkillType.TAMING, Integer.valueOf(stats.get(1).get(0)));
				skills.put(SkillType.MINING, Integer.valueOf(stats.get(1).get(1)));
				skills.put(SkillType.REPAIR, Integer.valueOf(stats.get(1).get(2)));
				skills.put(SkillType.WOODCUTTING, Integer.valueOf(stats.get(1).get(3)));
				skills.put(SkillType.UNARMED, Integer.valueOf(stats.get(1).get(4)));
				skills.put(SkillType.HERBALISM, Integer.valueOf(stats.get(1).get(5)));
				skills.put(SkillType.EXCAVATION, Integer.valueOf(stats.get(1).get(6)));
				skills.put(SkillType.ARCHERY, Integer.valueOf(stats.get(1).get(7)));
				skills.put(SkillType.SWORDS, Integer.valueOf(stats.get(1).get(8)));
				skills.put(SkillType.AXES, Integer.valueOf(stats.get(1).get(9)));
				skills.put(SkillType.ACROBATICS, Integer.valueOf(stats.get(1).get(10)));
			HashMap<Integer, ArrayList<String>> experience = mcMMO.database.Read("SELECT taming, mining, repair, woodcutting, unarmed, herbalism, excavation, archery, swords, axes, acrobatics FROM "+LoadProperties.MySQLtablePrefix+"experience WHERE user_id = " + id);
				skillsXp.put(SkillType.TAMING, Integer.valueOf(experience.get(1).get(0)));
				skillsXp.put(SkillType.MINING, Integer.valueOf(experience.get(1).get(1)));
				skillsXp.put(SkillType.REPAIR, Integer.valueOf(experience.get(1).get(2)));
				skillsXp.put(SkillType.WOODCUTTING, Integer.valueOf(experience.get(1).get(3)));
				skillsXp.put(SkillType.UNARMED, Integer.valueOf(experience.get(1).get(4)));
				skillsXp.put(SkillType.HERBALISM, Integer.valueOf(experience.get(1).get(5)));
				skillsXp.put(SkillType.EXCAVATION, Integer.valueOf(experience.get(1).get(6)));
				skillsXp.put(SkillType.ARCHERY, Integer.valueOf(experience.get(1).get(7)));
				skillsXp.put(SkillType.SWORDS, Integer.valueOf(experience.get(1).get(8)));
				skillsXp.put(SkillType.AXES, Integer.valueOf(experience.get(1).get(9)));
				skillsXp.put(SkillType.ACROBATICS, Integer.valueOf(experience.get(1).get(10)));
			return true;
		}
		else {
			return false;
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
			}
		}
	}
	
	public int getHealth() {
		return this.health;
	}
	
	public void setHealth(int amount) {
		this.health = amount;
	}
}
