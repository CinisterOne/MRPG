package cinister.massiverpg;


import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

import cinister.massiverpg.Data.GeneralData;
import cinister.massiverpg.Data.PlayerProfile;
import cinister.massiverpg.Data.Shrine;
import cinister.massiverpg.Data.Skill;
import cinister.massiverpg.Data.Spells.Spell;
import cinister.massiverpg.Utils.Utils;
import cinister.massiverpg.Utils.Utils.CommandLevel;
import cinister.massiverpg.Utils.Utils.SpellActions;

public class CommandParser {
	
	/*
	 * Method is called whenever a command is sent to the server, called in MassiveRPG.java in method onCommand.
	 */
	public static boolean parseCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		// Player commands
		if (sender instanceof Player) {
			Player player = (Player) sender;
			PlayerProfile profile = Users.getUser(player);
			if (commandLabel.equalsIgnoreCase("mrpg")) {
				// When the person wants to learn a skill at a shrine.
				if (args[0].equalsIgnoreCase("learn")) {
					// Get the shrine, and see if there is one where the player is
					Shrine shrine = Shrine.getShrine(player.getLocation());
					if (shrine != null) {
						// To check to see if the shrine has a skill attatched to it.
						boolean shrineTeaches = false;
						// Iterate through all the skills looking for one that has this shrine as a place.
						for (Skill skill : GeneralData.skills) {
							if (skill.getShrine().equalsIgnoreCase(shrine.getName())) {
								shrineTeaches = true;
								// If the person specifies how many of the skill he wants, args length is 1, if not, it's 2.
								if (args.length == 1) {
									if (profile.getLearningPoints() >= skill.getCost()) {
										profile.subtractLearningPoints(skill.getCost(), true);
										profile.addSkillPoints(skill.getName(), 1);
										Utils.sendPrivateMessage(CommandLevel.REWARD, "You have successfully gained 1 skill point in the " + skill.getName() + " skill.", player);
										return true;
									} else {
										Utils.sendPrivateMessage(CommandLevel.ERROR, "You do not have enough LP for this skill, the cost is " + skill.getCost() + " you have, '" + profile.getLearningPoints() + "'", player);
										return true;
									}
								} else if (args.length == 2) {
									int amount = Integer.parseInt(args[1]);
									// If the person has enough money for that many, does all the stuff.
									if (profile.getLearningPoints() >= skill.getCost() * amount) {
										profile.setLearningPoints(amount * skill.getCost());
										profile.addSkillPoints(skill.getName(), amount);
										Utils.sendPrivateMessage(CommandLevel.REWARD, "You have successfully gained " + amount + " skill points in the " + skill.getName() + " skill.", player);
										return true;
									} else {
										Utils.sendPrivateMessage(CommandLevel.ERROR, "You do not have enough LP for this skill, the cost is " + skill.getCost() * amount + " you have, '" + profile.getLearningPoints() + "'", player);
										return true;
									}
								} else {
									Utils.sendPrivateMessage(CommandLevel.ERROR, "Invalid number of arguments for the learn command in /mrp", player);
									return false;
								}
							}
						}
						if (!shrineTeaches) {
							Utils.sendPrivateMessage(CommandLevel.ERROR, "This shrine does not teach you anything, nothing happened.", player);
							return true;
						}
					} else {
						Utils.sendPrivateMessage(CommandLevel.ERROR, "You are not in a shrine!", player);
						return true;
					}
				} else if (args[0].equalsIgnoreCase("wand")) {
					if (args.length == 1) {
						if (player.getItemInHand().getType() == Material.STICK) {
							if (player.getItemInHand().getData().getData() != 0x4) {
								player.getItemInHand().setData(new MaterialData(0x4));
								return true;
							} else {
								Utils.sendPrivateMessage(CommandLevel.ERROR, "This is already a wand.", player);
								return true;
							}
						} else {
							Utils.sendPrivateMessage(CommandLevel.ERROR, "A wand may only be a stick, please hold in your hand a stick that is currently not a wand.", player);
							return true;
						}
					} else {
						Utils.sendPrivateMessage(CommandLevel.ERROR, "Incorrect number of parameters, amount required is 1", player);
						return false;
					}
				} else if (args[0].equalsIgnoreCase("bind")) {
					if (args.length == 3) {
						String spellName = args[1];
						SpellActions button = getSpellAction(args[2]);
						// Check to see if the person put in an invalid button
						if (button != null) {
							Spell spell = Spell.getSpell(spellName);
							if (spell != null) {
								profile.setQuickSpell(spellName, button);
								Utils.sendPrivateMessage(CommandLevel.REWARD, "Spell successfully binded.", player);
								return true;
							} else {
								Utils.sendPrivateMessage(CommandLevel.ERROR, "Spell does not exist.", player);
								return true;
							}
						} else {
							Utils.sendPrivateMessage(CommandLevel.ERROR, "Invalid button, available are (l, lmb), (r, rmb), (sl, slmb), (sr, srmb)", player);
							return false;
						}
					} else {
						Utils.sendPrivateMessage(CommandLevel.ERROR, "Invalid number of arguments", player);
						return false;
					}
				}
			}
		}
		return false;
	}
	
	public static void displayHelp(Player player) {
		Utils.sendPrivateMessage(CommandLevel.NOTIFICATION,
				"/mrpg - Shows a short list of help commands\n" +
				"/mrpg learn (SkillPointsAmount) - Purchases skill points using Learn Points at the shrine you are in\n" + 
				"/mrpg wand - Makes the stick in your hand into a wand", 
				player);
	}
	
	public static SpellActions getSpellAction(String actionName) {
		if (actionName.equalsIgnoreCase("l") || actionName.equalsIgnoreCase("lmb")) {
			return SpellActions.LMB;
		} else if (actionName.equalsIgnoreCase("r") || actionName.equalsIgnoreCase("rmb")) {
			return SpellActions.RMB;
		} else if (actionName.equalsIgnoreCase("sl") || actionName.equalsIgnoreCase("slmb")) {
			return SpellActions.sLMB;
		} else if (actionName.equalsIgnoreCase("rl") || actionName.equalsIgnoreCase("rlmb")) {
			return SpellActions.sRMB;
		}
		return null;
	}
}
