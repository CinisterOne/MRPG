package cinister.massiverpg.Listeners;


import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

import cinister.massiverpg.MassiveRPG;
import cinister.massiverpg.MassiveTimer;
import cinister.massiverpg.Users;
import cinister.massiverpg.Data.GeneralData;
import cinister.massiverpg.Data.PlayerProfile;
import cinister.massiverpg.Data.Spells.SpellDuration;

public class MMBlockListener extends BlockListener {
	public MassiveRPG plugin;
	
	public MMBlockListener(MassiveRPG plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void onBlockBreak(BlockBreakEvent event) {
		if (event.isCancelled()) return;
		Block block = event.getBlock();
		PlayerProfile playerProfile = Users.getUser(event.getPlayer());
		int xp = 0;
		if (GeneralData.blockEXP.get(block.getTypeId()) != 0) { //If the EXP given for getting the block isn't 0, it goes on, this prevents the unnecessary.
			xp = GeneralData.blockEXP.get(block.getTypeId());
			playerProfile.addXP(xp);
		}
		// Call the spell events
		if (MassiveTimer.hasPlayerSpell(event.getPlayer().getName())) {
			SpellDuration duration = MassiveTimer.getSpellBeingCasted(event.getPlayer().getName());
			event.setCancelled(duration.getSpell().getSpellCast().onBlockBreak(event));
		}
	}
	
	@Override
	public void onBlockPlace(BlockPlaceEvent event) {
		if (event.isCancelled()) return;
		Block block = event.getBlock();
		Player player = event.getPlayer();
		// Call the spell event
		if (block != null) {
			if (MassiveTimer.hasPlayerSpell(player.getName())) {
				SpellDuration duration = MassiveTimer.getSpellBeingCasted(event.getPlayer().getName());
				event.setCancelled(duration.getSpell().getSpellCast().onBlockPlace(event));
			}
		}
	}
}