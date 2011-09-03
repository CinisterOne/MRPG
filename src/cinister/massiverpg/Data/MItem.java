package cinister.massiverpg.Data;

public class MItem {
	public double attackSpeed;
	public int spellDamage;
	public double attackDamage;
	public int charm;
	public double spellResistance;
	public double attackDefense;
	
	public MItem(double attackSpeed, int spellDamage, double attackDamage, int charm, int spellResistance, int attackDefense) {
		this.attackSpeed = attackSpeed;
		this.spellDamage = spellDamage;
		this.attackDamage = attackDamage;
		this.charm = charm;
		this.spellResistance = spellResistance;
		this.attackDefense = attackDefense;
	}
	
	public double getAttackSpeed() {
		return this.attackSpeed;
	}
	
	public void setAttackSpeed(double attackSpeed) {
		this.attackSpeed = attackSpeed;
	}
	
	public int getSpellDamage() {
		return this.spellDamage;
	}
	
	public void setSpellDamage(int spellDamage) {
		this.spellDamage = spellDamage;
	}
	
	public double getAttackDamage() {
		return this.attackDamage;
	}
	
	public void setAttackDamage(double attackDamage) {
		this.attackDamage = attackDamage;
	}
	
	public int getCharm() {
		return this.charm;
	}
	
	public void setCharm(int charm) {
		this.charm = charm;
	}
	
	public double getSpellResistance() {
		return this.spellResistance;
	}
	
	public void setSpellResistance(double spellResistance) {
		this.spellResistance = spellResistance;
	}
	
	public double getAttackDefense() {
		return this.attackDefense;
	}
	
	public void setAttackDefense(double attackDefense) {
		this.attackDefense = attackDefense;
	}
}
