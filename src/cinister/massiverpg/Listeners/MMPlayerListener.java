package cinister.massiverpg.Listeners;


import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;

import cinister.massiverpg.MassiveRPG;
import cinister.massiverpg.MassiveTimer;
import cinister.massiverpg.Users;
import cinister.massiverpg.Data.GeneralData;
import cinister.massiverpg.Data.PlayerProfile;
import cinister.massiverpg.Data.Spells.Spell;
import cinister.massiverpg.Data.Spells.SpellDuration;
import cinister.massiverpg.Utils.TargetBlock;
import cinister.massiverpg.Utils.Utils;
import cinister.massiverpg.Utils.Utils.CommandLevel;
import cinister.massiverpg.Utils.Utils.SpellActions;

public class MMPlayerListener extends PlayerListener {
	public MassiveRPG plugin;
	
	public MMPlayerListener(MassiveRPG plugin) {
		this.plugin = plugin;
	}
	
	@Override 
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.isCancelled()) return;
		Player player = event.getPlayer();
		PlayerProfile profile = Users.getUser(player);
		Block block = event.getClickedBlock();
		if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_AIR) {
			TargetBlock aiming = new TargetBlock(event.getPlayer(), 3000, 0.2);
			block = aiming.getTargetBlock();
		}
		if (player.getItemInHand().getType() == Material.BOW) {
			if (profile.getArrowDUS() < System.currentTimeMillis()) {
				event.setCancelled(true);
			} else {
				profile.setArrowDUS(System.currentTimeMillis() + GeneralData.ARROW_SHOOT_DELAY * 1000);
			}
		}
		if (block != null) {
			if (MassiveTimer.hasPlayerSpell(player.getName())) {
				SpellDuration spellDuration = MassiveTimer.getSpellBeingCasted(player.getName());
				if (spellDuration != null) {
					event.setCancelled(spellDuration.getSpell().getSpellCast().onBlockClick(event, block));
				}
			}
			if (player.getItemInHand().getType().equals(Material.STICK)) {
				if (player.getItemInHand().getData().getData() == 0x4) {
					Spell spell = Spell.getSpell(profile.getQuickSpell(this.getSpellAction(event.getAction(), player)));
					if (spell != null) {
						spell.activateSpell(player, block, null);
					} else {
						Utils.sendPrivateMessage(CommandLevel.NOTIFICATION, "Please set a quick spell to cast.", player);
					}
					event.setCancelled(true);
				}
			}
		}
	}
	
	@Override
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		if (event.isCancelled()) return;
		Player player = event.getPlayer();
		PlayerProfile profile = Users.getUser(player);
		Entity entity = event.getRightClicked();
		if (player.getItemInHand().getType() == Material.BOW) {
			if (profile.getArrowDUS() < System.currentTimeMillis()) {
				event.setCancelled(true);
			} else {
				profile.setArrowDUS(System.currentTimeMillis() + GeneralData.ARROW_SHOOT_DELAY * 1000);
			}
		}
		SpellActions action = SpellActions.RMB;
		if (player.isSneaking()) action = SpellActions.sRMB;
		if (entity != null) {
			if (MassiveTimer.hasPlayerSpell(player.getName())) {
				SpellDuration spellDuration = MassiveTimer.getSpellBeingCasted(player.getName());
				if (spellDuration != null) {
					event.setCancelled(spellDuration.getSpell().getSpellCast().onEntityClick(event));
				}
			}
			if (player.getItemInHand().getType().equals(Material.STICK)) {
				if (player.getItemInHand().getData().getData() == 0x4) {
					Spell.getSpell(profile.getQuickSpell(action)).activateSpell(player, null, entity);
					event.setCancelled(true);
				}
			}
		}
	}
	
	@Override
	public void onPlayerMove(PlayerMoveEvent event) {
		if (event.isCancelled()) return;
		Player player = event.getPlayer();
		if (MassiveTimer.hasPlayerSpell(player.getName())) {
			SpellDuration duration = MassiveTimer.getSpellBeingCasted(player.getName());
			event.setCancelled(duration.getSpell().getSpellCast().onCasterMove(event));
		}
	}
	
	public SpellActions getSpellAction(Action action, Player player) {
		SpellActions spellAction = null;
		if (action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR) {
			if (player.isSneaking()) {
				spellAction = SpellActions.sRMB;
			} else {
				spellAction = SpellActions.RMB;
			}
		} else if (action == Action.LEFT_CLICK_BLOCK || action == Action.LEFT_CLICK_AIR) {
			if (player.isSneaking()) {
				spellAction = SpellActions.sLMB;
			} else {
				spellAction = SpellActions.LMB;
			}
		}
		return spellAction;
	}
}
