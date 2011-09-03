package cinister.massiverpg.Data;


import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import cinister.massiverpg.Users;


public class Spell {
	private String name;
	private long cooldownTime;
	private long startTime = -1;
	private int manaCost;
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
	
	public void activateSpell(Player caster, LivingEntity target, Block targetBlock) {
		PlayerProfile profile = Users.getUser(caster);
		if (profile.getMana() >= this.manaCost) {
			this.spellCast.onCast(new CastEvent(caster, target, targetBlock));
		}
	}
}
				