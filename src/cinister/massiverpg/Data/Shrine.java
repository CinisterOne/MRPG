package cinister.massiverpg.Data;

import org.bukkit.Location;

public class Shrine {
	private String name;
	private Location locationOrigin;
	private Location locationEnd;
	private String world;
	
	public Shrine(String name, Location locOri, Location locEnd, String world) {
		this.name = name;
		this.locationOrigin = locOri;
		this.locationEnd = locEnd;
		this.world = world;
	}
	
	public static Shrine getShrine(Location loc) {
		for (Shrine shrine : GeneralData.shrines) {
			if (shrine.isWithin(loc)) {
				return shrine;
			}
		}
		return null;
	}
	
	public boolean isWithin(Location loc) {
		return (((loc.getX() >= locationOrigin.getX() && loc.getY() >= locationOrigin.getY() && loc.getZ() >= locationOrigin.getZ()) && (loc.getX() <= locationEnd.getX() && loc.getY() <= locationEnd.getY() && loc.getZ() <= locationEnd.getZ())) && loc.getWorld().getName().equals(world));
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setLocation(Location locOri, Location locEnd) {
		this.locationOrigin = locOri;
		this.locationEnd = locEnd;
	}
	
	public Location getLocationOrigin() {
		return this.locationOrigin;
	}
	
	public Location getLocationEnd() {
		return this.locationEnd;
	}
}