package cinister.massiverpg.Listeners;


import org.getspout.spoutapi.event.input.InputListener;
import org.getspout.spoutapi.event.input.KeyPressedEvent;
import org.getspout.spoutapi.keyboard.Keyboard;

import cinister.massiverpg.StatsPage;


public class SpoutInputListener extends InputListener {
	
	/**
	 * TODO
	 *  -Add everything
	 */
	@Override
	public void onKeyPressedEvent(KeyPressedEvent event) {
		if (event.getKey() == Keyboard.KEY_K) {
			StatsPage page = new StatsPage();
		}
	}
}
