package cinister.massiverpg.Data;

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
		
	}
}
