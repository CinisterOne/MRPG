package cinister.massiverpg.Data;

import java.util.ArrayList;
import java.util.HashMap;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

import cinister.massiverpg.MassiveRPG;
import cinister.massiverpg.Users;
import cinister.massiverpg.Data.Spells.Spell;
import cinister.massiverpg.Utils.Utils;
import cinister.massiverpg.Utils.Utils.CommandLevel;
import cinister.massiverpg.Utils.Utils.SpellActions;
import cinister.massiverpg.Utils.Utils.SpellActions.SpellType;


public class PlayerProfile {
	//Players name
	private String name;
	//The string is the name of the skill, the integer is the level of it 0-maxLevel, it's a reference for how many bonuses to add
	private HashMap<String, Integer> playerSkills = new HashMap<String, Integer>();
	//The string is the name of the spell, and the long is the cooldown in milliseconds
	private HashMap<String, Long> playerSpells = new HashMap<String, Long>();
	private String castedSpell;
	//This is the unique reference id in the database table
	private long userid;
	private int mana;
	private int maxMana;
	private int maxHealth;
	private int health;
	private int mainLevel;
	private int mainXP;
	private long AttackDUS;
	private long ArrowDUS;
	// 4 slots for the 4 quick spells
	private String quickSpell = ",,,";
	//Their Learning Points
	private int LP;
	//The world in which he plays, perhaps unnecessary, but hey.
	private String world;
	//The string location is this, X,Y,Z separated by commas and in string form.
	
	public PlayerProfile(Player player) {
		this(player.getName(), player.getWorld().getName());
	}

	public PlayerProfile(String player, String world) {
		//Loads up the skills
		for (Skill skill : GeneralData.skills) {
			this.playerSkills.put(skill.getName(), 0);
		}
		//Loads up the spells
		for (Spell spell : GeneralData.spells) {
			this.playerSpells.put(spell.getName(), 0L);
		}
		
		
	}
	
	public boolean loadMySQL(String player, String world) {
		int id = 0;
		id = MassiveRPG.database.getInt("SELECT id FROM " + GeneralData.MySQLPrefix + world + "users WHERE user = '" + player + "'");
		if (id == 0) return false;
		this.userid = id;
		if (id > 0) {
			//Loads the sql statement for skills
			String skillStatement = "";
			for (int i = 0; i < GeneralData.skills.size(); i++) {
				skillStatement += GeneralData.skills.get(i) + ", ";
			}
			//Loads the sql statement for spells
			String spellStatement = "";
			for (int i = 0; i < GeneralData.spells.size(); i++) {
				spellStatement += GeneralData.spells.get(i) + ", ";
			}
			//Loads the user data
			HashMap<Integer, ArrayList<String>> users = MassiveRPG.database.read("SELECT world, level, experience, maxhp, hp, maxmana, mana, learnpoints FROM " + GeneralData.MySQLPrefix + world + "users WHERE id = " + id);
				this.world = users.get(1).get(0);
				this.mainLevel = Integer.parseInt(users.get(1).get(1));
				this.mainXP = Integer.parseInt(users.get(1).get(2));
				this.maxHealth = Integer.parseInt(users.get(1).get(3));
				this.health = Integer.parseInt(users.get(1).get(4));
				this.maxMana = Integer.parseInt(users.get(1).get(5));
				this.mana = Integer.parseInt(users.get(1).get(6));
				this.LP = Integer.parseInt(users.get(1).get(7));
				this.quickSpell = users.get(1).get(8);
				this.castedSpell = users.get(1).get(9);
			if (skillStatement != "") {
				HashMap<Integer, ArrayList<String>> skills = MassiveRPG.database.read("SELECT " + skillStatement + "FROM " + GeneralData.MySQLPrefix + world + "skills WHERE user_id = " + id);
					//Populates the player's skills and their corresponding level.
					String[] skillNames = skillStatement.replaceAll(" ", "").split(",");
					for (int i = 0; i < skills.get(1).size(); i++) {
						this.playerSkills.put(skillNames[i], Integer.parseInt(skills.get(1).get(i)));
					}
			}
			if (spellStatement != "") {
				HashMap<Integer, ArrayList<String>> spells = MassiveRPG.database.read("SELECT " + skillStatement + "FROM " + GeneralData.MySQLPrefix + world + "spells WHERE user_id = " + id);
					//Populates the players spells and the remaining cooldown, if any.
					String[] spellName = spellStatement.replaceAll(" ", "").split(",");
					for (int i = 0; i < spells.get(1).size(); i++) {
						this.playerSpells.put(spellName[i], Long.parseLong(spells.get(1).get(i)));
					}
			}
			return true;
		} else {
			return false;
		}
	}
	
	public void addMySQLPlayer(String player, String world) {
		int id = 0;
		MassiveRPG.database.write("INSERT INTO " + GeneralData.MySQLPrefix + world + "users (user) VALUES ('" + player + "')");
		id = MassiveRPG.database.getInt("SELECT id FROM " + GeneralData.MySQLPrefix + world + "users WHERE user = " + player);
		MassiveRPG.database.write("INSERT INTO " + GeneralData.MySQLPrefix + world + "skills (user_id) VALUES (" + id + ")");
		MassiveRPG.database.write("INSERT INTO " + GeneralData.MySQLPrefix + world + "spells (user_id) VALUES (" + id + ")");
	}
	
	public void save() {
		MassiveRPG.database.write("UPDATE " + GeneralData.MySQLPrefix + world + "users SET"
				+ " world = " + this.world + ","
				+ " level = " + this.mainLevel + ","
				+ " experience = " + this.mainXP + ","
				+ " maxhp = " + this.maxHealth + ","
				+ " hp = " + this.health + ","
				+ " maxmana = " + this.maxMana + ","
				+ " mana = " + this.mana + ","
				+ " learnpoints = " + this.LP + ","
				+ " quickspell = " + this.quickSpell + ","
				+ " castedspell = " + this.castedSpell
				+ " WHERE id = " + this.userid);
		String skillValues = "";
		for (Skill skill : GeneralData.skills) {
			skillValues += skill.getName() + " = " + this.playerSkills.get(skill.getName()) + ", ";
		}
		skillValues = skillValues.substring(0, skillValues.lastIndexOf(","));
		MassiveRPG.database.write("UPDATE " + GeneralData.MySQLPrefix + world + "users SET "
				+ skillValues
				+ " WHERE user_id = " + userid);
		String spellValues = "";
		for (Spell spell : GeneralData.spells) {
			skillValues += spell.getName() + " = " + this.playerSkills.get(spell.getName()) + ", ";
		}
		spellValues = spellValues.substring(0, spellValues.lastIndexOf(","));
		MassiveRPG.database.write("UPDATE " + GeneralData.MySQLPrefix + world + "users SET "
				+ spellValues
				+ " WHERE user_id = " + userid);
	}
	
	//Sends the player the amount of learn points he has.
	public void updateLearningPoints() {
		Utils.sendPrivateMessage(CommandLevel.NOTIFICATION, "You currently have " + this.getLearningPoints() + " learning points.", Bukkit.getServer().getPlayer(this.name));
	}
	
	//Sets the spell that's cast when it is right clicked.
	public void setQuickSpell(String quickSpell, SpellActions action) {
		//Checks to see if it exists
		for (Spell spell : GeneralData.spells) {
			if (spell.getName().equalsIgnoreCase(quickSpell)) {
				String[] splitted = this.quickSpell.split(",");
				splitted[action.getIndex()] = quickSpell;
				this.quickSpell = Utils.concatStringArray(splitted, ",");
			}
		}
	}
	
	public String getQuickSpell(SpellActions action) {
		return quickSpell.split(",")[action.getIndex()];
	}
	
	public void addSkillPoints(String name, int amount) {
		amount += playerSkills.get(name);
		playerSkills.put(name, amount);
	}
	
	public void subtractSkillPoints(String name, int amount) {
		amount = playerSkills.get(name) - amount;
		playerSkills.put(name, amount);
	}
	
	//Casting from Integer to int JUST to be safe.
	public int getSkillPoints(String name) {
		return (int) playerSkills.get(name);
	}
	
	public int getLearningPoints() {
		return this.LP;
	}
	
	public void subtractLearningPoints(int subtract, boolean display) {
		this.LP -= subtract;
		if (display) {
			Utils.sendPrivateMessage(CommandLevel.WARNING, "You have lost " + subtract + " learning points.", Bukkit.getServer().getPlayer(this.name));
			this.updateLearningPoints();
		}
	}
	
	public void addLearningPoints(int newAmount, boolean display) {
		this.LP += newAmount;
		if (display) {
			Utils.sendPrivateMessage(CommandLevel.REWARD, "You have gained " + newAmount + " learning points.", Bukkit.getServer().getPlayer(this.name));
			this.updateLearningPoints();
		}
	}
	
	public void setLearningPoints(int amount) {
		this.LP = amount;
	}
	
	public int getLevel() {
		return this.mainLevel;
	}
	
	public void setLevel(int level) {
		this.mainLevel = level;
	}
	
	public long getAttackDUS() {
		return this.AttackDUS;
	}
	
	public void setAttackDUS(long delay) {
		this.AttackDUS = delay;
	}
	
	public long getArrowDUS() {
		return this.ArrowDUS;
	}
	
	public void setArrowDUS(long delay) {
		this.ArrowDUS = delay;
	}
	
	public int getExperience() {
		return this.mainXP;
	}
	
	public void levelUp() {
		this.setLevel(this.getLevel() + 1);
		Bukkit.getServer().getPlayer(this.name).sendMessage(ChatColor.GREEN + "Congratulations! You have leveled up to level " + this.mainLevel + "!");
	}
	
	public int getRemainingXP() {
		return (mainLevel * 250 * (mainLevel + 1) + (mainLevel * 250)) - mainXP;
	}
	
	public boolean addXP(int amount) {
		this.mainXP += amount * GeneralData.EXP_RATE;
		//Checks to see if the player meets the EXP requirements.
		if (this.getRemainingXP() <= 0) {
			//If he isn't above the max level
			if (this.mainLevel + 1 <= GeneralData.MAX_LEVEL) {
				this.levelUp();
				return true;
			}
		} else {
			return true;
		}
		return false;
	}
	
	/*
	 * Deals damage to a player, based on what happened and what is happening
	 */
	public void damage(Entity attacker, DamageCause cause, int damage, boolean isMagic) {
		Player player = Bukkit.getServer().getPlayer(this.name);
		int playerDamage = 0;
		if (attacker != null) {
			if (attacker instanceof Player) {
				Player playerAttacker = (Player) attacker;
				PlayerProfile profile = Users.getUser(playerAttacker);
				playerDamage = this.getHealth() - ((int) Math.floor((GeneralData.DEFAULT_ATTACK + (GeneralData.DEFAULT_ATTACK * profile.getSkillPoints("ATTACKDAMAGE"))) - (GeneralData.DEFAULT_DEFENSE + (GeneralData.DEFAULT_DEFENSE * this.getSkillPoints("DEFENSE")))));
				this.setHealth(playerDamage);
			} else if (attacker instanceof Projectile) {
				Projectile projectile = (Projectile) attacker;
				LivingEntity shooter = projectile.getShooter();
				if (shooter != null) {
					if (attacker != null) {
						if (shooter instanceof Player) {
							Player damager = (Player) shooter;
							PlayerProfile shooterProfile = Users.getUser(damager);
							if (isMagic) {
								if (shooterProfile.getCastedSpell() != "") {
									double spellDamage = 0;
									// Get the last spell the player casted
									Spell spell = Spell.getSpell(shooterProfile.getCastedSpell());
									// Casting a deadly spell
									if (spell.getSpellType() == SpellType.BLACK_MAGIC) {
										spellDamage = (this.getSkillPoints("SPELLRESISTANCE") * this.getArmorProtections("SPELLRESISTANCE")) - (damage + (GeneralData.items.get(Utils.getTypeId(projectile)).getAttackDamage() + (GeneralData.items.get(Utils.getTypeId(projectile)).getAttackDamage() * (shooterProfile.getSkillPoints("SPELLATTACK") + shooterProfile.getArmorProtections("SPELLATTACK")))));
										this.setHealth((int) (this.getHealth() + spellDamage));
									// Healing for the the player
									} else if (spell.getSpellType() == SpellType.WHITE_MAGIC) {
										this.setHealth((int) (this.getHealth() + (damage + shooterProfile.getSkillPoints("CHARM"))));
									}
								}
							}
						}
					}
				}
			}
		} else {
			if (cause == DamageCause.SUFFOCATION) {
				playerDamage = this.getHealth() - GeneralData.SUFFOCATION_DAMAGE;
				this.setHealth(playerDamage);
				player.damage(getConvertedDamage(playerDamage));
			} else if (cause == DamageCause.FALL) {
				playerDamage = (this.getHealth() + (damage * this.getSkillPoints("ACROBATICS")) - ((int) Math.floor(GeneralData.FALL_DAMAGE * damage/4)));
				this.setHealth(playerDamage);
				player.damage(getConvertedDamage(playerDamage));
			} else {
				this.setHealth(this.getHealth() - ((damage * this.getMaxHealth()) / 20));
			}
		}
	}
	
	public double getArmorProtections(String skill) {
		Player player = getPlayer();
		ItemStack[] inventory = player.getInventory().getArmorContents();;
		double protection = 0.0D;
		if (skill.equalsIgnoreCase("SPELLRESISTANCE")) {
			for (ItemStack item : inventory) {
				protection += GeneralData.items.get(item.getTypeId()).getSpellResistance();
			}
		} else if (skill.equalsIgnoreCase("SPELLDAMAGE")) {
			for (ItemStack item : inventory) {
				protection += GeneralData.items.get(item.getTypeId()).getSpellDamage();
			}
		} else if (skill.equalsIgnoreCase("ATTACKSPEED")) {
			for (ItemStack item : inventory) {
				protection += GeneralData.items.get(item.getTypeId()).getAttackSpeed();
			}
		} else if (skill.equalsIgnoreCase("ATTACKDAMAGE")) {
			for (ItemStack item : inventory) {
				protection += GeneralData.items.get(item.getTypeId()).getAttackDamage();
			}
		} else if (skill.equalsIgnoreCase("CHARM")) {
			for (ItemStack item : inventory) {
				protection += GeneralData.items.get(item.getTypeId()).getCharm();
			}
		} else if (skill.equalsIgnoreCase("ATTACKDEFENSE")) {
			for (ItemStack item : inventory) {
				protection += GeneralData.items.get(item.getTypeId()).getAttackDefense();
			}
		}
		return protection;
	}
		
	public String getCastedSpell() {
		return this.castedSpell;
	}
	
	public void setCastedSpell(String spell) {
		this.castedSpell = spell;
	}
	
	public int getConvertedDamage(int damage) {
		return (int) Math.floor((damage * 20) / this.getMaxHealth());
	}
	
	public Player getPlayer() {
		return Bukkit.getServer().getPlayer(this.name);
	}
	
	public int getHealth() {
		return this.health;
	}
	
	public void setHealth(int amount) {
		if (amount > this.getMaxHealth()) {
			amount = this.getMaxHealth();
			getPlayer().damage(this.getConvertedDamage(amount));
		}
		this.health = amount;
	}
	
	public int getMana() {
		return this.mana;
	}
	
	public void setMana(int amount) {
		if (amount > this.getMaxMana()) {
			amount = this.getMaxMana();
		}
		this.mana = amount;
	}
	
	public int getMaxMana() {
		return this.maxMana;
	}
	
	public int getMaxHealth() {
		return this.maxHealth;
	}
	
	public double getSkillValue(String aBonusType) {
		double bonusSum = 0D;
		for (Skill skill : GeneralData.skills) {
			for (String bonus : skill.getBonuses()) {
				String[] splitted = bonus.split(":");
				String bonusType = splitted[0];
				double bonusAmount = Double.parseDouble(splitted[1]);
				if (bonusType.equalsIgnoreCase(aBonusType)) {
					bonusSum += bonusAmount * playerSkills.get(skill.getName());
				}
			}
		}
		return bonusSum;
	}
	
	public void setMaxMana(int amount) {
		this.maxMana = amount;
	}
	
	public void setMaxHealth(int amount) {
		this.maxHealth = amount;
	}
	
	public Long getSpellTime(String spell) {
		return this.playerSpells.get(spell);
	}
	
	public void tickSpellTime(String spell) {
		this.playerSpells.put(spell, this.playerSpells.get(spell) - 1000L);
	}
}
