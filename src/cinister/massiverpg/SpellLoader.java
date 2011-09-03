package cinister.massiverpg;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.logging.Logger;


import cinister.massiverpg.Data.GeneralData;
import cinister.massiverpg.Data.Spells.PlayerSpellCast;
import cinister.massiverpg.Data.Spells.Spell;

public class SpellLoader {
	private static Logger log = Logger.getLogger("Minecraft");
	
	public static void load(String directory) throws ClassNotFoundException, Exception {
		File dir = new File(directory);
		log.info("Loading spells...");
		
		ClassLoader loader = new URLClassLoader(new URL[] {dir.toURI().toURL()}, PlayerSpellCast.class.getClassLoader());
		for (File file : dir.listFiles()) {
			String name = file.getName().substring(0, file.getName().lastIndexOf("."));
			Class<?> clazz = loader.loadClass(name);
			Object object = clazz.newInstance();
			if (!(object instanceof PlayerSpellCast)) {
				log.info("Loaded spell class file " + name + " is not in the proper format, or is not a spell class file.");
				continue;
			}
			for (Spell spell : GeneralData.spells) {
				if (spell.getName().equalsIgnoreCase(name)) {
					PlayerSpellCast spellCast = (PlayerSpellCast) object;
					spell.setSpellEvent(spellCast);
					GeneralData.spells.set(GeneralData.spells.indexOf(spell), spell);
					log.info("Loaded spell " + spellCast.getClass().getSimpleName() + " successfully");
				}
			}
			
		}
	}
}
