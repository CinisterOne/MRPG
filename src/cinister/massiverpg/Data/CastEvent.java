package cinister.massiverpg.Data;

import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class CastEvent {
	public boolean cancelled;
	public Player caster;
	public LivingEntity entityTarget;
	public Block blockTarget;
	
	public CastEvent(Player caster, LivingEntity entityTarget, Block blockTarget) {
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
	
	public LivingEntity getLiveTarget() {
		return this.entityTarget;
	}
	
	public void setLivingTarget(LivingEntity target) {
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
