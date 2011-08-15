package cinister.massiverpg.Listeners;

import org.bukkit.event.player.PlayerListener;

import cinister.massiverpg.MassiveRPG;

public class MMPlayerListener extends PlayerListener {
	public MassiveRPG plugin;
	
	public MMPlayerListener(MassiveRPG plugin) {
		this.plugin = plugin;
	}
}
