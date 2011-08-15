package cinister.massiverpg.Listeners;

import org.bukkit.event.entity.EntityListener;

import cinister.massiverpg.MassiveRPG;
import cinister.massiverpg.Users;
import cinister.massiverpg.Data.PlayerProfile;

public class MMEntityListener extends EntityListener {
	public MassiveRPG plugin;
	
	public MMEntityListener(MassiveRPG plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void onEntityDamage(EntityDamageEvent event) {
		Entity entity = event.getEntity();
		if (entity instanceof Player) {
			Player player = (Player) entity;
			PlayerProfile profile = Users.getUser(player);
		}
	}

}
