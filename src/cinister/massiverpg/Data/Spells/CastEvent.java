package cinister.massiverpg.Data.Spells;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class CastEvent {
	public boolean cancelled;
	public Player caster;
	public Entity entityTarget;
	public Block blockTarget;
	
	public CastEvent(Player caster, Entity entityTarget, Block blockTarget) {
		this.caster = caster;
		this.entityTarget = entityTarget;
		this.blockTarget = blockTarget;
	}
	
	public Player getCaster() {
		return this.caster;
	}
	
	public void setCaster(Player player) {
		this.caster = player;
	}
	
	public Entity getLiveTarget() {
		return this.entityTarget;
	}
	
	public void setLivingTarget(Entity target) {
		this.entityTarget = target;
	}
	
	public Block getBlock() {
		return this.blockTarget;
	}
	
	public void setBlock(Block block) {
		this.blockTarget = block;
	}
	
	public boolean isCancelled() {
		return this.cancelled;
	}
	
	public void setCancelled(boolean isCancelled) {
		this.cancelled = isCancelled;
	}
}
