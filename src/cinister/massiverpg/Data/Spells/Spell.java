package cinister.massiverpg.Data.Spells;

import java.util.ArrayList;


import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import cinister.massiverpg.MassiveTimer;
import cinister.massiverpg.Users;
import cinister.massiverpg.Data.GeneralData;
import cinister.massiverpg.Data.PlayerProfile;
import cinister.massiverpg.Utils.Utils;
import cinister.massiverpg.Utils.Utils.CommandLevel;
import cinister.massiverpg.Utils.Utils.SpellActions.SpellType;


public class Spell {
	String name;
	long lengthOfSpell;
	long cooldownTime;
	int manaCost;
	SpellType spellType;
	PlayerSpellCast spellCast;
	ArrayList<String> skillRequirements;
	
	public Spell(String name, long cooldownTime, int manaCost, long lengthofSpell, SpellType spellType, ArrayList<String> skillRequirements) {
		this.name = name;
		this.cooldownTime = cooldownTime;
		this.manaCost = manaCost;
		this.lengthOfSpell = lengthofSpell;
	}
	
	// Returns the spell that has the same name as it
	public static Spell getSpell(String spellName) {
		for (Spell spell : GeneralData.spells) {
			if (spell.getName().equalsIgnoreCase(spellName)) {
				return spell;
			}
		}
		return null;
	}
	
	public void setSpellEvent(PlayerSpellCast event) {
		this.spellCast = event;
	}
	
	public SpellType getSpellType() {
		return this.spellType;
	}
	
	public ArrayList<String> getSkillRequirements() {
		return this.skillRequirements;
	}
	
	public void addSkillRequirement(String skill, int requiredLevel) {
		this.skillRequirements.add(skill + ":" + Integer.toString(requiredLevel));
	}
	
	public void setSpellType(SpellType type) {
		this.spellType = type;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public long getLength() {
		return this.lengthOfSpell;
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
	
	public PlayerSpellCast getSpellCast() {
		return this.spellCast;
	}
	
	public void activateSpell(Player caster, Block block, Entity clickedEntity) {
		PlayerProfile profile = Users.getUser(caster);
		if (profile.getMana() >= this.getManaCost()) {
			if (!MassiveTimer.hasPlayerSpell(caster.getName())) {
				if (MassiveTimer.isCooldownFinished(caster.getName(), this.getName())) {
					this.getSpellCast().onCast(new CastEvent(caster, clickedEntity, block));
					MassiveTimer.addPlayerSpellCast(new SpellDuration(caster, this, this.getLength()));
					Utils.sendPrivateMessage(CommandLevel.NOTIFICATION, "You have cast the spell " + this.getName(), caster);
				} else {
					Utils.sendPrivateMessage(CommandLevel.ERROR, "You may not use that spell for another " + Utils.getTime(profile.getSpellTime(this.getName())), caster);
				}
			} else {
				Utils.sendPrivateMessage(CommandLevel.ERROR, "You have already casted a spell, please wait until it ends before casting another.", caster);
			}
		} else {
			Utils.sendPrivateMessage(CommandLevel.ERROR, "You do not have enough mana for that.", caster);
		}
	}
}
