package masp.spells.test;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import cinister.massiverpg.Data.Spells.CastEvent;
import cinister.massiverpg.Data.Spells.PlayerSpellCast;
import cinister.massiverpg.Utils.Utils;
import cinister.massiverpg.Utils.Utils.CommandLevel;

/*
 * A simple test for the spell usage created by Denkfaehigkeit
 * Creates cobblestone surrounding the player and effectively trapping him.
 */
public class TestSpell extends PlayerSpellCast {
	Block blockOrigin;
	HashMap<Location, Integer> blocks = new HashMap<Location, Integer>();
	
	/*
	 * Called when the spell is cast, this is where it creates the trap, and stores all the values for later removal and restoration.
	 */
	@Override
	public void onCast(CastEvent event) {
		Player player = event.getCaster();
		Entity entity = event.getLiveTarget();
		// Checks to see that the player clicked an entity and not just clicked a block.
		if (entity != null && entity instanceof LivingEntity) {
			if (player.isOnline()) {
				// Creates a hollow rectangle that traps the entity and/or person, and stores the positions and types.
				Location locationBring = player.getLocation();
				blockOrigin = player.getWorld().getBlockAt(player.getLocation().getBlockX() - 1, player.getLocation().getBlockY() - 1, player.getLocation().getBlockZ() - 1);
				for (int x = 0; x < 3; x++) {
					for (int z = 0; z < 3; z++) {
						Block lowerBlock = blockOrigin.getRelative(x, 0, z);
						Block higherBlock = blockOrigin.getRelative(x, 3, z);
						blocks.put(lowerBlock.getLocation(), lowerBlock.getTypeId());
						blocks.put(higherBlock.getLocation(), higherBlock.getTypeId());
						lowerBlock.setType(Material.COBBLESTONE);
						higherBlock.setType(Material.COBBLESTONE);
					}
				}
				for (int x = 0; x < 3; x++) {
					for (int y = 1; y < 3; y++) {
						Block lowerBlock = blockOrigin.getRelative(x, y, 0);
						Block higherBlock = blockOrigin.getRelative(x, y, 2);
						blocks.put(lowerBlock.getLocation(), lowerBlock.getTypeId());
						blocks.put(higherBlock.getLocation(), higherBlock.getTypeId());
						lowerBlock.setType(Material.COBBLESTONE);
						higherBlock.setType(Material.COBBLESTONE);
					}
				}
				for (int z = 0; z < 3; z++) {
					for (int y = 1; y < 3; y++) {
						Block lowerBlock = blockOrigin.getRelative(0, y, z);
						Block higherBlock = blockOrigin.getRelative(2, y, z);
						blocks.put(lowerBlock.getLocation(), lowerBlock.getTypeId());
						blocks.put(higherBlock.getLocation(), higherBlock.getTypeId());
						lowerBlock.setType(Material.COBBLESTONE);
						higherBlock.setType(Material.COBBLESTONE);
					}
				}
				// Teleports the player to the block to prevent suffocation from glitching.
				entity.teleport(player.getWorld().getBlockAt(locationBring.getBlockX(), locationBring.getBlockY(), locationBring.getBlockZ()).getLocation());
				if (entity instanceof Player) {
					Utils.sendPrivateMessage(CommandLevel.WARNING, "You have been trapped by a magical force!", (Player) entity);
				}
				Utils.sendPrivateMessage(CommandLevel.NOTIFICATION, "You have successfully cast your spell, trapping your victim.", player);
			}
		} else {
			Utils.sendPrivateMessage(CommandLevel.WARNING, "You must use this on an creature and/or player.", player);
		}
	}
	
	/*
	 * Method is called when the spell casting is over.
	 * In this method, all previous setting of blocks is reverted to its previous state, preventing griefing.
	 */
	@Override
	public void onCastFinish(Player player) {
		for (int x = 0; x < 3; x++) {
			for (int z = 0; z < 3; z++) {
				for (int y = 0; y < 4; y++) {
					// Revert the block to its previous state before creating the trap.
					blockOrigin.getRelative(x, y, z).setTypeId(blocks.get(blockOrigin.getWorld().getBlockAt(x, y, z).getLocation()));
				}
			}
		}
	}
}
