package cinister.massiverpg;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;


import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import cinister.massiverpg.Data.GeneralData;
import cinister.massiverpg.Data.MItem;
import cinister.massiverpg.Data.PlayerProfile;
import cinister.massiverpg.Data.Shrine;
import cinister.massiverpg.Data.Skill;
import cinister.massiverpg.Data.Spells.Spell;
import cinister.massiverpg.Listeners.MMBlockListener;
import cinister.massiverpg.Listeners.MMEntityListener;
import cinister.massiverpg.Listeners.MMPlayerListener;
import cinister.massiverpg.Utils.Utils;
import cinister.massiverpg.Utils.Utils.SpellActions.SpellType;

/*
 * Main class for the entire plugin
 * Contains initialization and de-initialization of all things
 * Contains all configuration loading and unloading
 */
public class MassiveRPG extends JavaPlugin {
	public int numBlock = 96;
	//To see which worlds MassiveRPG is enabled on
	public ArrayList<String> worlds = new ArrayList<String>();
	Logger log = Logger.getLogger("Minecraft");
	public static Database database = null;
	
	//Initialization of the plugin
	public void onEnable(){
		//Initialize the database
		database = new Database(this);
		//Initialize the timer for spells and other stuff
		Timer timer = new Timer();
		timer.schedule(new MassiveTimer(), 1000, 1000);
		//Load the configuration files
		this.loadConfiguration();
		//Initialize the listeners for player and entity
		PluginManager pm = this.getServer().getPluginManager();
		MMPlayerListener pl = new MMPlayerListener(this);
		MMEntityListener el = new MMEntityListener(this);
		MMBlockListener bl = new MMBlockListener(this);
		pm.registerEvent(Event.Type.ENTITY_DAMAGE, el, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.ENTITY_DEATH, el, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.BLOCK_PLACE, bl, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.BLOCK_BREAK, bl, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_INTERACT, pl, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_INTERACT_ENTITY, pl, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_MOVE, pl, Event.Priority.Normal, this);
		log.info("MassiveRPG Ready.");
	}
	
	// Parses the command in CommandParser.java
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		return CommandParser.parseCommand(sender, cmd, commandLabel, args);
	}
	
	public void loadConfiguration() {
		log.info("Loading configurations...");
		loadGeneralWorldConfiguration();
		for (String world : this.worlds) {
			log.info("Loading configuration for world " + world + " ...");
			loadBlockConfiguration(world);
			loadDatabaseConfiguration();
			loadGeneralConfiguration(world);
			loadSpellConfiguration(world);
			loadSkillConfiguration(world);
			loadShrineConfiguration(world);
			log.info("Loading complete for world " + world + "!");
		}
	}
	
	// Gets the data file for the specific world
	public String getDataWorldFile(String world) {
		String path = this.getDataFolder() + "/" + world + "/";
		if (!(new File(path)).exists()) {
			(new File(path)).mkdir();
		}
		return path;
	}
	
	// We iterate through the parents until we find a folder that exists, so we don't have to make TONS of lines just for a single file
	public File getFile(String stringFolder, String stringFile) {
		File file = new File(stringFolder, stringFile);
		File folder = new File(stringFolder);
		while (!folder.exists()) {
			folder.mkdir();
			folder = folder.getParentFile();
		}
		return file;
	}
	
	
	// Load general configuration for the world
	public void loadGeneralConfiguration(String dataWorld) {
		Properties props = new Properties();
		try {
			File configFile = this.getFile(this.getDataWorldFile(dataWorld), "Massive" + dataWorld + "Properties.ini");
			if (!configFile.exists()) {
				configFile.createNewFile();
				props.load(new FileReader(configFile));
				props.setProperty("exp_rate", "1");
				props.setProperty("fire_arrow_delay", "1");
				props.setProperty("default_player_health", "100");
				props.setProperty("default_player_mana", "50");
				props.setProperty("default_mana_regain", "10");
				props.setProperty("default_health_regen", "10");
				props.setProperty("default_max_level", "999");
				props.setProperty("notification_message_color", "YELLOW");
				props.setProperty("error_message_color", "RED");
				props.setProperty("warning_message_color", "GOLD");
				props.setProperty("success_message_color", "GREEN");
				props.setProperty("fall_damage", "1");
				props.setProperty("suffocation_damage", "1");
				props.setProperty("default_attack_damage", "10");
				props.setProperty("default_defense", "2");
				props.setProperty("message_tag", "[MRPG]");
				props.store(new FileWriter(configFile), "General Configuration for world '" + dataWorld + "'");
			} else {
				props.load(new FileReader(configFile));
			}
			GeneralData.EXP_RATE = Double.parseDouble(props.getProperty("exp_rate", "1"));
			GeneralData.ARROW_SHOOT_DELAY = Integer.parseInt(props.getProperty("fire_arrow_delay", "1"));
			GeneralData.DEFAULT_PLAYER_HEALTH = Integer.parseInt(props.getProperty("default_player_health", "100"));
			GeneralData.DEFAULT_PLAYER_MANA = Integer.parseInt(props.getProperty("default_player_mana", "50"));
			GeneralData.DEFAULT_MANA_REGEN = Integer.parseInt(props.getProperty("default_mana_regen", "10"));
			GeneralData.DEFAULT_HEALTH_REGEN = Integer.parseInt(props.getProperty("default_health_regen", "10"));
			GeneralData.MAX_LEVEL = Integer.parseInt(props.getProperty("default_max_level", "999"));
			GeneralData.NOTIFICATION_COLOR = Utils.getColor(props.getProperty("notification_message_color", "YELLOW"));
			GeneralData.ERROR_COLOR = Utils.getColor(props.getProperty("error_message_color", "RED"));
			GeneralData.WARNING_COLOR = Utils.getColor(props.getProperty("warning_message_color", "GOLD"));
			GeneralData.SUCCESS_COLOR = Utils.getColor(props.getProperty("success_message_color", "GREEN"));
			GeneralData.SUFFOCATION_DAMAGE = Integer.parseInt(props.getProperty("suffocation_damage", "1"));
			GeneralData.FALL_DAMAGE = Integer.parseInt(props.getProperty("fall_damage", "1"));
			GeneralData.DEFAULT_ATTACK = Integer.parseInt(props.getProperty("default_attack_damage", "10"));
			GeneralData.DEFAULT_DEFENSE = Integer.parseInt(props.getProperty("default_defense", "2"));
			GeneralData.MESSAGE_TAG = props.getProperty("message_tag", "[MRPG]");
		} catch (IOException ex) {
			log.info("Unable to load the general configurations for world '" + dataWorld + "'");
		}
	}
	
	public void loadBlockConfiguration(String dataWorld) {
		Properties props = new Properties();
		try {
			// Create the file if it doesn't exist.
			File configFile = this.getFile(this.getDataWorldFile(dataWorld) + "/blockData/", "MassiveBlockEXP.ini");
			if (!configFile.exists()) {
				configFile.createNewFile();
				props.load(new FileReader(configFile));
				props.setProperty("blocks_num", "96");
				for (int i = 0; i < 96; i++) {
					props.setProperty(Material.getMaterial(i).name(), "0");
				}
				props.store(new FileWriter(configFile), "Block Configuration for world " + dataWorld);
			} else {
				props.load(new FileReader(configFile));
			}
			// Load the configuration.
			numBlock = Integer.parseInt(props.getProperty("blocks_num", "96"));
			for (int i = 0; i < numBlock; i++) {
				GeneralData.blockEXP.add(Integer.parseInt(props.getProperty(Integer.toString(i), "0")));
			}
		} catch (Exception ex) {
			log.log(Level.WARNING, "Unable to load MassiveBlockEXP.ini");
		}
	}
	
	// Loads the custom configuration for items
	public void loadItemConfiguration(String dataWorld) {
		Properties props = new Properties();
		try {
			File configFile = this.getFile(this.getDataWorldFile(dataWorld) + "/itemData/", "MassiveItemDamages.ini");
			if (!configFile.exists()) {
				configFile.createNewFile();
				props.load(new FileReader(configFile));
				props.setProperty("default_weapon_damage", Integer.toString(GeneralData.DEFAULT_WEAPON_DAMAGE));
				props.setProperty("default_defense", Integer.toString(GeneralData.DEFAULT_DEFENSE));
				props.setProperty("default_spell_resistance", Integer.toString(GeneralData.DEFAULT_SPELL_RESISTANCE));
				props.setProperty("default_attack_speed", Integer.toString(GeneralData.DEFAULT_ATTACK_SPEED));
				for (int i = 256; i <= 351; i++) {
					props.setProperty(Material.getMaterial(i).name(), Integer.toString(GeneralData.DEFAULT_WEAPON_DAMAGE) + "," + Integer.toString(GeneralData.DEFAULT_ATTACK_SPEED) + ",0,0," + Integer.toString(GeneralData.DEFAULT_SPELL_RESISTANCE) + "," + Integer.toString(GeneralData.DEFAULT_DEFENSE));
				}
				props.store(new FileWriter(configFile), "Item Configuration File");
			} else {
				props.load(new FileReader(configFile));
			}
			GeneralData.DEFAULT_WEAPON_DAMAGE = Integer.parseInt(props.getProperty("default_weapon_damage", Integer.toString(GeneralData.DEFAULT_WEAPON_DAMAGE)));
			GeneralData.DEFAULT_DEFENSE = Integer.parseInt(props.getProperty("default_defense", Integer.toString(GeneralData.DEFAULT_DEFENSE)));
			GeneralData.DEFAULT_SPELL_RESISTANCE = Integer.parseInt(props.getProperty("default_spell_resistance", Integer.toString(GeneralData.DEFAULT_SPELL_RESISTANCE)));
			GeneralData.DEFAULT_ATTACK_SPEED = Integer.parseInt(props.getProperty("default_attack_speed", Integer.toString(GeneralData.DEFAULT_ATTACK_SPEED)));
			for (int i = 256; i <= 351; i++) {
				String[] itemSplitted = props.getProperty(Material.getMaterial(i).name(), Integer.toString(GeneralData.DEFAULT_WEAPON_DAMAGE) + "," + Integer.toString(GeneralData.DEFAULT_ATTACK_SPEED) + ",0,0," + Integer.toString(GeneralData.DEFAULT_SPELL_RESISTANCE) + "," + Integer.toString(GeneralData.DEFAULT_DEFENSE)).split(",");
				GeneralData.items.put(i, new MItem(Double.parseDouble(itemSplitted[0]), Integer.parseInt(itemSplitted[1]), Double.parseDouble(itemSplitted[2]), Integer.parseInt(itemSplitted[3]), Integer.parseInt(itemSplitted[4]), Integer.parseInt(itemSplitted[5])));
			}
		} catch (IOException ex) {
			log.info("Unable to read item configuration file.");
		}
	}
	
	//This code is absolutely disgraceful, but unfortunately it needs to be done this way.
	//This loads the configurations for Mobs primarily their XPs when a player kills them(Listener at MMEntityListener)
	public void loadMobConfiguration(String dataWorld) {
		Properties props = new Properties();
		try {
			File configFile = this.getFile(this.getDataWorldFile(dataWorld) + "/mobData/", "MassiveMobEXP.ini");
			if (!configFile.exists()) {
				configFile.createNewFile();
				BufferedWriter bw = new BufferedWriter(new FileWriter(configFile));
				bw.write("wolf=0");
				bw.write("pig=0");
				bw.write("sheep=0");
				bw.write("cow=0");
				bw.write("chicken=0");
				bw.write("squid=0");
				bw.write("zombie_pigman=0");
				bw.write("zombie=0");
				bw.write("skeleton=0");
				bw.write("spider=0");
				bw.write("spider_jockey=0");
				bw.write("creeper=0");
				bw.write("slime=0");
				bw.write("ghast=0");
				bw.close();
			}
			
			props.load(new FileReader(configFile));
			GeneralData.mobEXP.add(Integer.parseInt(props.getProperty("wolf")));
			GeneralData.mobEXP.add(Integer.parseInt(props.getProperty("pig")));
			GeneralData.mobEXP.add(Integer.parseInt(props.getProperty("sheep")));
			GeneralData.mobEXP.add(Integer.parseInt(props.getProperty("cow")));
			GeneralData.mobEXP.add(Integer.parseInt(props.getProperty("chicken")));
			GeneralData.mobEXP.add(Integer.parseInt(props.getProperty("squid")));
			GeneralData.mobEXP.add(Integer.parseInt(props.getProperty("zombie_pigman")));
			GeneralData.mobEXP.add(Integer.parseInt(props.getProperty("zombie")));
			GeneralData.mobEXP.add(Integer.parseInt(props.getProperty("skeleton")));
			GeneralData.mobEXP.add(Integer.parseInt(props.getProperty("spider")));
			GeneralData.mobEXP.add(Integer.parseInt(props.getProperty("spider_jockey")));
			GeneralData.mobEXP.add(Integer.parseInt(props.getProperty("creeper")));
			GeneralData.mobEXP.add(Integer.parseInt(props.getProperty("slime")));
			GeneralData.mobEXP.add(Integer.parseInt(props.getProperty("ghast")));
		} catch (IOException ex) {
			log.log(Level.WARNING, "Unable to read the propeties file MassiveMobEXP.ini");
		}
	}
	
	public void loadGeneralWorldConfiguration() {
		Properties props = new Properties();
		try {
			File configFile = new File(this.getDataFolder(), "MassiveWorldProperties.ini");
			if (!configFile.exists()) {
				configFile.createNewFile();
				props.load(new FileReader(configFile));
				props.setProperty("worlds_enabled", "world,nether");
				props.store(new FileWriter(configFile), "General Server Configurations");
			} else {
				props.load(new FileReader(configFile));
			}
			//I know, not very effective way of removing white space characters, but this allows for more dynamic specifications
			String[] worlds = props.getProperty("worlds_enabled", "world,nether").replace(" ", "").split(",");
			for (String world : worlds) {
				if (this.getServer().getWorld(world) != null) {
					this.worlds.add(world);
				}
			}
		} catch (Exception ex) {
			log.log(Level.WARNING, "Unable to read MassiveProperties.ini");
		}
	}
	
	public void loadSpellConfiguration(String dataWorld) {
		File file = this.getFile(this.getDataWorldFile(dataWorld) + "/spells/", "massivespells.config");
		try {
			if (!file.exists()) {
				file.createNewFile();
				BufferedWriter bw = new BufferedWriter(new FileWriter(file));
				bw.write("// The format for creating configurations for a new spell");
				bw.write("// [spellname],[manarequired],[cooldown],[lengthofspell(0 for default)]");
				bw.write("// Once configured, you must create .class file in the /plugins/spells/classes/ folder with the name of the spell as the name of the file, with the specified format to create a custom spell.");
				bw.close();
			}
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = "";
			String[] splitted = null;
			while ((line = br.readLine()) != null) {
				if (!line.replaceAll(" ", "").substring(0, 2).equals("//")) { // Allows for some comments ^_^
					if (line.contains(",")) {
						splitted = line.split(",");
						String spellName = splitted[0];
						int manaRequired = Integer.parseInt(splitted[1]);
						long cooldown = Long.parseLong(splitted[2]);
						long length = Long.parseLong(splitted[3]);
						SpellType spellType = null;
						for (SpellType spelltype : SpellType.values()) {
							if (spelltype.getName().equalsIgnoreCase(splitted[4])) {
								spellType = spelltype;
							}
						}
						ArrayList<String> bonuses = new ArrayList<String>();
						for (int i = 5; i < splitted.length; i++) {
							bonuses.add(splitted[i]);
						}
						Spell spell = new Spell(spellName, cooldown, manaRequired, length, spellType, bonuses);
						GeneralData.spells.add(spell);
					} else {
						log.info("Invalid syntax for spells configuration file");
					}
				}
			}
			br.close();
		} catch (IOException ex) {
			log.info("Unable to read spell configuration file, check for errors.");
		}
	}
	
	// Configuration of the shrines, looking into maybe adding a flag for world guard and perhaps implementing it into this.
	public void loadShrineConfiguration(String dataWorld) {
		File file = this.getFile(this.getDataWorldFile(dataWorld) + "/shrines/", "massiveshrines.config");
		try {
			if (!file.exists()) {
				file.createNewFile();
				BufferedWriter bw = new BufferedWriter(new FileWriter(file));
				bw.write("// The format for create a new shrine (It is advised that you do not do it manually but rather use the internal command.");
				bw.write("// [shrinename],[shrinex],[shriney],[shrinez],[toshrinex],[toshriney],[toshrinez],[world]");
				bw.write("// The to x and from x are the from x is smaller than the to x, f.e: Health Shrine,0,0,0,10,15,10");
				bw.write("// That would create a shrine 15 tall, and 10x10 wide, again it is suggest that you use the built in command.");
				bw.close();
			}
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = "";
			String[] splitted = null;
			while ((line = br.readLine())  != null) {
				if (!line.replaceAll(" ", "").substring(0, 2).equals("//")) {
					if (line.contains(",")) {
						splitted = line.replaceAll(" ", "").split(",");
						String name = splitted[0];
						int fromX = (int) Math.floor(Double.parseDouble(splitted[1]));
						int fromY = (int) Math.floor(Double.parseDouble(splitted[2]));
						int fromZ = (int) Math.floor(Double.parseDouble(splitted[3]));
						int toX = (int) Math.floor(Double.parseDouble(splitted[4]));
						int toY = (int) Math.floor(Double.parseDouble(splitted[5]));
						int toZ = (int) Math.floor(Double.parseDouble(splitted[6]));
						String world = splitted[7];
						Location from = this.getServer().getWorld(world).getBlockAt(fromX, fromY, fromZ).getLocation();
						Location to = this.getServer().getWorld(world).getBlockAt(toX, toY, toZ).getLocation();
						GeneralData.shrines.add(new Shrine(name, from, to, world));
					}
				}
			}
			br.close();
		} catch (IOException ex) {
			log.info("An error occured in reading the shrines.config file");
		}
	}
	
	//Configuration of the skills that can be learned
	public void loadSkillConfiguration(String dataWorld) {
		File file = this.getFile(this.getDataWorldFile(dataWorld) + "/skills/", "massiveskills.config");
		try {
			if (!file.exists()) {
				file.createNewFile();
				BufferedWriter bw = new BufferedWriter(new FileWriter(file));
				bw.write("// The format for creating a new skill");
				bw.write("// [skillname],[cost-in-LPs],[shrinename],[skill-bonus-type:amount],...,...");
				bw.write("// For every bonus for that skill you want, create a new section encapsulated by commas, and use the colon to seperate the type of the bonus and the amount that it provides");
				bw.write("// Bonus Skill Types include the following: MININGSPEED(-1 for instant break), HEALTH, MANA, ATTACKDAMAGE, ATTACKSPEED, WOODCUTTINGSPEED, MOVEMENTSPEED(Experimental), DEFENSE, SPELLDAMAGE, SPELLCOOLDOWN.");
				bw.write("// More can, and will be added hopefully.");
				bw.close();
			}
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = "";
			String[] splitted = null;
			while ((line = br.readLine()) != null) {
				if (!line.replaceAll(" ", "").substring(0, 2).equals("//")) { // Allows for some comments ^_^
					if (line.contains(",")) {
						splitted = line.replaceAll(" ", "").split(",");
						String skillName = splitted[0];
						int cost = Integer.parseInt(splitted[1]);
						String shrineName = splitted[2];
						int maxLevel = Integer.parseInt(splitted[3]);
						ArrayList<String> bonuses = new ArrayList<String>();;
						for (int i = 4; i < splitted.length; i++) {
							bonuses.add(splitted[i]);
						}
						Skill skill = new Skill(maxLevel, skillName, cost, shrineName, bonuses);
						GeneralData.skills.add(skill);
					} else {
						log.log(Level.WARNING, "Invalid syntax, use commas to seperate data points.");
					}
				}
			}
			br.close();
		} catch (IOException ex) {
			log.log(Level.WARNING, "Unable to load massiveskills.config, please check file.");
		}
	}
	
	public void loadDatabaseConfiguration() {
		Properties props = new Properties();
		try {
			File configFile = this.getFile(this.getDataFolder() + "/database/", "MassiveDatabase.ini");
			if (!configFile.exists()) {
				configFile.createNewFile();
				props.load(new FileReader(configFile));
				props.setProperty("mysql_server_name", "localhost");
				props.setProperty("mysql_port", "");
				props.setProperty("mysql_database_name", "massive_database");
				props.setProperty("mysql_user_name", "root");
				props.setProperty("mysql_password", "");
				props.setProperty("mysql_table_prefix", "mrpg");
				props.store(new FileWriter(configFile), "Database Configurations");
			} else {
				props.load(new FileReader(configFile));
			}
			GeneralData.MySQLServerName = props.getProperty("mysql_server_name", "localhost");
			GeneralData.MySQLPort = props.getProperty("mysql_port", "");
			GeneralData.MySQLDBName = props.getProperty("mysql_database_name", "massive_database");
			GeneralData.MySQLUserName = props.getProperty("mysql_user_name", "root");
			GeneralData.MySQLPassword = props.getProperty("mysql_password", "");
			GeneralData.MySQLPrefix = props.getProperty("mysql_table_prefix", "mrpg");
		} catch (Exception ex) {
			log.log(Level.WARNING, "Unable to load MassiveDatabase.ini");
		}
	}
	
	//On the shutdown of the plugin/on a restart.
	public void onDisable() {
		for (Player player : this.getServer().getOnlinePlayers()) {
			PlayerProfile profile = Users.getUser(player);
			profile.save();
		}
		log.info("MassiveRPG Disabled.");
	}
}
