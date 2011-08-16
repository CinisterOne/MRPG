package cinister.massiverpg.Data;

import java.util.ArrayList;
import java.util.HashMap;

public class GeneralData {
	public static ArrayList<Integer> blockEXP = new ArrayList<Integer>();
	public static ArrayList<Skill> skills = new ArrayList<Skill>();
	public static ArrayList<Spell> spells = new ArrayList<Spell>();
	public static ArrayList<Shrine> shrines = new ArrayList<Shrine>();
	public static HashMap<Integer, Integer> mobXP = new HashMap<Integer, Integer>();
	public static double EXP_RATE = 1.0D;
	public static int MAX_LEVEL = 100;
	public static String MySQLServerName = "";
	public static String MySQLPort = "";
	public static String MySQLDBName = "";
	public static String MySQLUserName = "";
	public static String MySQLPassword = "";
	public static String MySQLPrefix = "";
}
