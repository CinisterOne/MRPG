package cinister.massiverpg.Listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;

import cinister.massiverpg.MassiveRPG;
import cinister.massiverpg.Users;
import cinister.massiverpg.Data.GeneralData;
import cinister.massiverpg.Data.PlayerProfile;

 /* ******* MMEntityListener *********
 *  This entire thing is almost exclusively dealing with the gaining of xp when something is killed 
 *  and it gives XP to the player
 *  
 */

public class MMEntityListener extends EntityListener {
	public MassiveRPG plugin;
	
	public MMEntityListener(MassiveRPG plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void onEntityDeath(EntityDeathEvent event) {
		Entity entity = event.getEntity();
		int damage = 0;
		//All the EntityDamageEvent stuff is to see if an entity caused this and the player just didn't fall, and to get data pertaining to that specific event.
		EntityDamageEvent cause = event.getEntity().getLastDamageCause();
		if (cause instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent newEvent = (EntityDamageByEntityEvent) cause;
			Entity attacker = newEvent.getDamager();
			if (attacker instanceof Player) {
				Player player = (Player) attacker;
				PlayerProfile profile = Users.getUser(player);
				//Retrieves the pre-defined MobXPs defined in MassiveRPG under loadMobConfiguration class.
				int xp = GeneralData.mobXP.get(new Integer(entity.getEntityId()));
				//We don't want an arrow to be killing us eh?
				if (entity instanceof LivingEntity) {
					//See if it's even worth using all the memory just to do nothing.
					if (xp > 0) {
						//Adds the XP to the player profile and does all the checking and magic like that for us.
						profile.addXP(xp);
					}
				}
			}
		}
	}

}
