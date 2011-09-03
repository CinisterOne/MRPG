package cinister.massiverpg;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;


import org.bukkit.entity.Player;

import cinister.massiverpg.Data.GeneralData;
import cinister.massiverpg.Data.Skill;
import cinister.massiverpg.Data.Spells.Spell;

//Got all this out of Java's official online website on MySQL databases, A.K.A JDBC.
/*
 * Special thanks to McMMO which heavily influenced this code, mostly is there's, I just tweaked it for the specific database
 */
public class Database {
	private Connection conn;
	private MassiveRPG plugin;
	
	public Database(MassiveRPG plugin) {
		this.plugin = plugin;
		//Load instance of the driver
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception ex) {
			System.out.println("Unable to initialize JDBC Driver");
		}
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + GeneralData.MySQLServerName + ":" + GeneralData.MySQLPort + "/" + GeneralData.MySQLDBName + "?user=" + GeneralData.MySQLUserName + "&password=" + GeneralData.MySQLPassword);
		} catch (SQLException ex1) {
			System.out.println("SQLException: " + ex1.getMessage());
			System.out.println("SQLState: " + ex1.getSQLState());
			System.out.println("VendorError: " + ex1.getErrorCode());
		}
	}
	
	public void createStructure() {
		for (String world : plugin.worlds) {
			//Take the skills  and spells data and put it into a usable format for MySQL
			String sqlStatement = "";
			String cooldownStatement = "";
			// The ALTER TABLE is for when updates occur, and there isn't a column for it, and we don't erase the data already present.
			for (Skill skill : GeneralData.skills) {
				sqlStatement += "'" + skill.getName() + "' int(10) NOT NULL,";
				write("ALTER TABLE IF EXISTS '" + GeneralData.MySQLPrefix + world + "skills' ADD IF NOT EXISTS '" + skill.getName() + "' int(10) NOT NULL;");
			}
			for (Spell spell : GeneralData.spells) {
				cooldownStatement += "'" + spell.getName() + "' int(10) NOT NULL,";
				write("ALTER TABLE IF EXISTS '" + GeneralData.MySQLPrefix + world + "spells' ADD IF NOT EXISTS '" + spell.getName() + "' int(20) NOT NULL;");
			}
			//Base player data
			write("CREATE TABLE IF NOT EXISTS '" + GeneralData.MySQLPrefix + world + "users' ('id' int(10) unsigned NOT NULL AUTO_INCREMENT," + 
					"'user' varchar(40) NOT NULL," + 
					"'world' varchar(40) NOT NULL," + 
					"'level' int(10) NOT NULL," + 
					"'experience' int(10) NOT NULL," +
					"'maxhp' int(10) NOT NULL," +
					"'hp' int(10) NOT NULL," +
					"'maxmana' int(10) NOT NULL," +
					"'mana' int(10) NOT NULL," + 
					"'learnpoints' int(10) NOT NULL," +
					"'quickspell' varchar(40) NOT NULL," +
					"'castedspell' varchar(40) NOT NULL," +
					"PRIMARY KEY ('id')," + 
					"UNIQUE KEY 'user' ('user')) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1;"
			);
			//Skills
			write("CREATE TABLE IF NOT EXISTS '" + GeneralData.MySQLPrefix + world + "skills' ('user_id' int(10) unsigned NOT NULL," +
					sqlStatement + 
					"PRIMARY KEY ('user_id') ENGINE=MyISAM DEFAULT CHARSET=latin1;"
			);
			//Spells
			write("CREATE TABLE IF NOT EXISTS '" + GeneralData.MySQLPrefix + world + "spells' ('user_id' int(10) unsigned NOT NULL," +
					cooldownStatement + 
					"PRIMARY KEY ('user_id') ENGINE=MyISAM DEFAULT CHARSET=latin1;"
			);
		}
	}
	
	private void reconnect() {
		System.out.println("[Massive] MassiveDatabase is reconnecting...");
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + GeneralData.MySQLServerName + ":" + GeneralData.MySQLPort + "/" + GeneralData.MySQLDBName + "?user=" + GeneralData.MySQLUserName + "&password=" + GeneralData.MySQLPassword);
			System.out.println("[Massive] Reconnection successful!");
		} catch (SQLException ex) {
			System.out.println("[Massive] Unable to reconnect");
		}
		//Reinitialize the players online, in case of a plugin reload.
		try {
			if (conn.isValid(5)) {
				Users.clearUsers();
				for (String world : plugin.worlds) {
					for (Player players : plugin.getServer().getWorld(world).getPlayers()) {
						Users.addUser(players);
					}
				}
			}
		} catch (SQLException ex) {
			//log.info("A database is required in storing MySQL.");
		}
	}
	
	public boolean write(String sql) {
		try {
			if (!conn.isValid(5)) {
				reconnect();
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		try {
			PreparedStatement statement = null;
			statement = this.conn.prepareStatement(sql);
			statement.executeUpdate();
			return true;
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			return false;
		}
	}
	
	//Used for retrieving the id of the user and/or future data
	public Integer getInt(String sql) {
		PreparedStatement statement = null;
		ResultSet set = null;
		Integer result = 0;
		try {
			if (!conn.isValid(5)) {
				reconnect();
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		try {
			statement = this.conn.prepareStatement(sql);
			if (statement.executeQuery() != null) {
				statement.executeQuery();
				set = statement.getResultSet();
				if (set.next()) {
					result = set.getInt(1);
				}
				
			}
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		return result;
	}
	
	public HashMap<Integer, ArrayList<String>> read(String sql) {
		try {
			if (!conn.isValid(5)) {
				reconnect();
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		
		PreparedStatement statement = null;
		ResultSet set = null;
		HashMap<Integer, ArrayList<String>> Rows = new HashMap<Integer, ArrayList<String>>();
		try {
			statement = this.conn.prepareStatement(sql);
			if (statement.executeQuery() != null) {
				statement.executeQuery();
				set = statement.getResultSet();
				while (set.next()) {
					ArrayList<String> Col = new ArrayList<String>();
					for (int i = 1; i < set.getMetaData().getColumnCount(); i++) { 
						Col.add(set.getString(i));
					}
					Rows.put(set.getRow(), Col);
				}
			}
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("Vendor Error: " + ex.getErrorCode());
		}
		if (set != null) {
			try {
				set.close();
			} catch (SQLException ex) {
				set = null;
			}
		}
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException ex) {
				statement = null;
			}
		}
		return Rows;
	}
}
