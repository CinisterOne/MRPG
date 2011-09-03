package cinister.massiverpg.Data.Spells;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import cinister.massiverpg.Utils.Utils;
import cinister.massiverpg.Utils.Utils.CommandLevel;


 /*
  * Data type for the listener, implement this into a spell with which you would like to act as a spell.
  * The methods that return a boolean return true if the event is to be canceled, and false if nothing should change.
  */
public abstract class PlayerSpellCast {
	// When the player casts the spell
	public abstract void onCast(CastEvent event);
	// When the player finishes the cast, when MassiveTimer.java returns that it has run out
	public void onCastFinish(Player player) {Utils.sendPrivateMessage(CommandLevel.NOTIFICATION, "Your spell has worn off", player);}
	// Called on every second during the spells active period.
	public void onCastTick(Player player) {}
	// Called when player clicks a block
	public boolean onBlockClick(PlayerInteractEvent event, Block actualBlock) {return false;}
	// Called when the player breaks a block
	public boolean onBlockBreak(BlockBreakEvent event) {return false;}
	// Called when the player is placing a block
	public boolean onBlockPlace(BlockPlaceEvent event) {return false;}
	// Called when a player right clicks an entity
	public boolean onEntityClick(PlayerInteractEntityEvent event) {return false;}
	// Called when a player gets hit
	public boolean onCasterHit(EntityDamageEvent event) {return false;}
	// Called when the caster hits another entity
	public boolean onCasterDamage(EntityDamageByEntityEvent event) {return false;}
	// Called when the caster dies
	public void onCasterDie(EntityDeathEvent event) {return;}
	// Called when the caster moves
	public boolean onCasterMove(PlayerMoveEvent event) {return false;}
}
