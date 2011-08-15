package cinister.massiverpg.Listeners;

import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;

import cinister.massiverpg.MassiveRPG;
import cinister.massiverpg.Users;
import cinister.massiverpg.Data.GeneralData;
import cinister.massiverpg.Data.PlayerProfile;

public class MMBlockListener extends BlockListener {
	public MassiveRPG plugin;
	
	public MMBlockListener(MassiveRPG plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void onBlockBreak(BlockBreakEvent event) {
		Block block = event.getBlock();
		PlayerProfile playerProfile = Users.getUser(event.getPlayer());
		int xp = 0;
		if (GeneralData.blockEXP.get(block.getTypeId()) != 0) { //If the EXP given for getting the block isn't 0, it goes on, this prevents the unnecessary.
			xp = GeneralData.blockEXP.get(block.getTypeId());
			playerProfile.addXP(xp);
		}
	}
}