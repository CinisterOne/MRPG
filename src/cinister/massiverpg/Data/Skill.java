package cinister.massiverpg.Data;

import java.util.ArrayList;

public class Skill {
	public int maxLevel;
	public String skillName;
	public int cost;
	private String shrine;
	private ArrayList<String> bonuses = new ArrayList<String>();
	
	public Skill(int maxLevel, String skillName, int cost, String shrine, ArrayList<String> bonuses) {
		this.maxLevel = maxLevel;
		this.cost = cost;
		this.shrine = shrine;
		this.bonuses = bonuses;
		this.skillName = skillName;
	}
	
	public int getMaxLevel() {
		return this.maxLevel;
	}
	
	public String getName() {
		return this.skillName;
	}
	
	public String getShrine() {
		return this.shrine;
	}
	
	public int getCost() {
		return this.cost;
	}
	
	public ArrayList<String> getBonuses() {
		return this.bonuses;
	}
}
