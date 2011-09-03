package cinister.massiverpg.Data;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;

import cinister.massiverpg.Data.Spells.Spell;

public class GeneralData {
	public static ArrayList<Integer> blockEXP = new ArrayList<Integer>();
	public static ArrayList<Skill> skills = new ArrayList<Skill>();
	public static ArrayList<Spell> spells = new ArrayList<Spell>();
	public static ArrayList<Shrine> shrines = new ArrayList<Shrine>();
	public static ArrayList<Integer> mobEXP = new ArrayList<Integer>();
	public static HashMap<Integer, MItem> items = new HashMap<Integer, MItem>();
	public static int DEFAULT_DEFENSE = 1;
	public static int DEFAULT_ATTACK = 1;
	public static int DEFAULT_ATTACK_SPEED = 2;
	public static int DEFAULT_SPELL_RESISTANCE = 10;
	public static int SUFFOCATION_DAMAGE;
	public static int FALL_DAMAGE;
	public static int DEFAULT_WEAPON_DAMAGE = 2;
	public static int ARROW_SHOOT_DELAY;
	public static int DEFAULT_PLAYER_HEALTH;
	public static int DEFAULT_PLAYER_MANA;
	public static int DEFAULT_MANA_REGEN;
	public static int DEFAULT_HEALTH_REGEN;
	public static ChatColor NOTIFICATION_COLOR;
	public static ChatColor ERROR_COLOR;
	public static ChatColor WARNING_COLOR;
	public static ChatColor SUCCESS_COLOR;
	public static String MESSAGE_TAG;
	public static double EXP_RATE = 1.0D;
	public static int MAX_LEVEL = 100;
	public static String MySQLServerName = "";
	public static String MySQLPort = "";
	public static String MySQLDBName = "";
	public static String MySQLUserName = "";
	public static String MySQLPassword = "";
	public static String MySQLPrefix = "";
}
