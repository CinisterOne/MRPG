package cinister.massiverpg.Data;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class Spell {
	String name;
	long cooldownTime;
	long startTime = -1;
	int manaCost;
	PlayerSpellCast spellCast;
	
	public Spell(String name, long cooldownTime, int manaCost) {
		this.name = name;
		this.cooldownTime = cooldownTime;
		this.manaCost = manaCost;
	}
	
	public void setSpellEvent(PlayerSpellCast event) {
		this.spellCast = event;
	}
	
	public PlayerSpellCast getSpellEvent() {
		return this.spellCast;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public long getRemainingTime() {
		return (startTime + cooldownTime) - System.currentTimeMillis();
	}
	
	public void addCooldownTime(long amount) {
		this.cooldownTime += amount;
	}
	
	public int getManaCost() {
		return this.manaCost;
	}
	
	public void setManaCost(int manaCost) {
		this.manaCost = manaCost;
	}
	
	public void activateSpell(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (player.getItemInHand().getType().equals(Material.STICK)) {
				if (player.getItemInHand().getData().getData() == 0x4) {
					
				}
			}
		}
	}
}
