package cinister.massiverpg.Listeners;

import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Squid;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
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
				int xp = this.getExpByMob(entity);
				//We don't want an arrow to be the thing dead eh?
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
	
	public int getExpByMob(Entity entity) {
		int xp = 0;
		if (entity instanceof Wolf) {
			xp = GeneralData.mobEXP.get(0);
		} else if (entity instanceof Pig) {
			xp = GeneralData.mobEXP.get(1);
		} else if (entity instanceof Sheep) {
			xp = GeneralData.mobEXP.get(2);
		} else if (entity instanceof Cow) {
			xp = GeneralData.mobEXP.get(3);
		} else if (entity instanceof Chicken) {
			xp = GeneralData.mobEXP.get(4);
		} else if (entity instanceof Squid) {
			xp = GeneralData.mobEXP.get(5);
		} else if (entity instanceof PigZombie) {
			xp = GeneralData.mobEXP.get(6);
		} else if (entity instanceof Zombie) {
			xp = GeneralData.mobEXP.get(7);
		} else if (entity instanceof Skeleton) {
			xp = GeneralData.mobEXP.get(8);
		} else if (entity instanceof Spider) {
			xp = GeneralData.mobEXP.get(9);
		} else if (entity.getPassenger() instanceof Skeleton) {
			xp = GeneralData.mobEXP.get(10);
		} else if (entity instanceof Creeper) {
			xp = GeneralData.mobEXP.get(11);
		} else if (entity instanceof Slime) {
			xp = GeneralData.mobEXP.get(12);
		} else if (entity instanceof Ghast) {
			xp = GeneralData.mobEXP.get(13);
		}
		return xp;
	}
}
