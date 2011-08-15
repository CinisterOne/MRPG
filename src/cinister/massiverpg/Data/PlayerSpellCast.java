package cinister.massiverpg.Data;

 // Data type for the listener, implement this into a spell with which you would like to act as a spell.
public interface PlayerSpellCast {
	public void onCast(CastEvent event);
}
