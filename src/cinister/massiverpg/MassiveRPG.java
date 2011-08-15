package cinister.massiverpg;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import cinister.massiverpg.Data.GeneralData;
import cinister.massiverpg.Data.Shrine;
import cinister.massiverpg.Data.Skill;
import cinister.massiverpg.Data.Spell;
import cinister.massiverpg.Listeners.MMBlockListener;
import cinister.massiverpg.Listeners.MMEntityListener;
import cinister.massiverpg.Listeners.MMPlayerListener;

public class MassiveRPG extends JavaPlugin {
	public int numBlock = 96;
	//To see which worlds MassiveRPG is enabled on
	public ArrayList<String> worlds = new ArrayList<String>();
	Logger log = Logger.getLogger("Minecraft");
	Database database = null;
	
	//Initialization of the plugin
	public void onEnable(){
		//Initialize the database
		database = new Database(this);
		//Initialize the listeners for player and entity
		PluginManager pm = this.getServer().getPluginManager();
		MMPlayerListener pl = new MMPlayerListener(this);
		MMEntityListener el = new MMEntityListener(this);
		MMBlockListener bl = new MMBlockListener(this);
		pm.registerEvent(Event.Type.ENTITY_DAMAGE, el, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.BLOCK_BREAK, bl, Event.Priority.Normal, this);
		log.info("MassiveRPG Ready.");
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
	
	public void loadGeneralConfiguration(String dataWorld) {
		Properties props = new Properties();
		try {
			File configFile = this.getFile(this.getDataWorldFile(dataWorld), "Massive" + dataWorld + "Properties.ini");
			if (!configFile.exists()) {
				configFile.createNewFile();
			}
			props.load(new FileReader(configFile));
			GeneralData.EXP_RATE = Double.parseDouble(props.getProperty("exp_rate", "1"));
			GeneralData.ARROW_SHOOT_DELAY = Integer.parseInt(props.getProperty("fire_arrow_delay", "1"));
			GeneralData.DEFAULT_PLAYER_HEALTH = Integer.parseInt(props.getProperty("default_player_health", "100"));
			GeneralData.DEFAULT_PLAYER_MANA = Integer.parseInt(props.getProperty("default_player_mana", "50"));
			GeneralData.DEFAULT_MANA_REGEN = Integer.parseInt(props.getProperty("default_mana_regen", "10"));
			GeneralData.DEFAULT_HEALTH_REGEN = Integer.parseInt(props.getProperty("default_health_regen", "1"));
			GeneralData.MAX_LEVEL = Integer.parseInt(props.getProperty("default_max_level"));
		}
	}
	
	public void loadBlockConfiguration(String dataWorld) {
		Properties props = new Properties();
		try {
			// Create the file if it doesn't exist.
			File configFile = this.getFile(this.getDataWorldFile(dataWorld) + "/blockData/", "MassiveBlockEXP.ini");
			if (!configFile.exists()) {
				configFile.createNewFile();
			}

			// Load the configuration.
			props.load(new FileReader(configFile));
			numBlock = Integer.parseInt(props.getProperty("blocks_num", "96"));
			for (int i = 0; i < numBlock; i++) {
				GeneralData.blockEXP.add(Integer.parseInt(props.getProperty(Integer.toString(i), "0")));
			}
		} catch (Exception ex) {
			log.log(Level.WARNING, "Unable to load MassiveBlockEXP.ini");
		}
	}
	
	public void loadGeneralWorldConfiguration() {
		Properties props = new Properties();
		try {
			File configFile = new File(this.getDataFolder(), "MassiveWorldProperties.ini");
			if (!configFile.exists()) {
				configFile.createNewFile();
			}
			props.load(new FileReader(configFile));
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
				bw.write("// [spellname],[manarequired],[cooldown]");
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
						Spell spell = new Spell(spellName, cooldown, manaRequired);
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
			}
			
			props.load(new FileReader(configFile));
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
		log.info("MassiveRPG Disabled.");
	}
}
