package cinister.massiverpg.Data.Spells;

import org.bukkit.entity.Player;


/*
 * Simple class for recording a spell while it is being cast in MassiveTimer.java, and making referencing it easier, this class is a lazily put together class.
 */
public class SpellDuration {
	private Player owner;
	private Spell spell;
	private long duration;
	
	public SpellDuration(Player owner, Spell spell, long duration) {
		this.owner = owner;
		this.spell = spell;
		this.duration = duration;
	}
	
	public long getTime() {
		return this.duration;
	}
	
	public void setTime(long duration) {
		this.duration = duration;
	}
	
	public void tick() {
		this.duration -= 1000;
	}
	
	public Player getOwner() {
		return this.owner;
	}
	
	public Spell getSpell() {
		return this.spell;
	}
}
