package gg.arcanum.gangwars.crime;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Action {

	private String name;
	private String[] verbs;
	private int karmaChange;
	private int position;
	private long timeTaken;
	private ItemStack[] requirements;
	private double balanceRequirement;
	private ItemStack[] rewards;
	private double balanceReward;
	private ItemStack logo;

	/**
	 * New action constructor
	 * 
	 * @param name
	 * @param karmaChange
	 * @param timeTaken
	 * @param verbs
	 */
	public Action(String name, int karmaChange, long timeTaken, int position, String[] verbs, ItemStack[] requirements,
			ItemStack[] rewards, double balanceRequirement, double balanceReward) {
		ActionHandler.getActions().add(this);
		this.name = name;
		this.karmaChange = karmaChange;
		this.timeTaken = timeTaken * 20;
		this.position = position;
		this.verbs = verbs;
		this.requirements = requirements;
		this.rewards = rewards;
		this.balanceRequirement = balanceRequirement;
		this.balanceReward = balanceReward;
	}

	/**
	 * Set the logo based on itemstack requirements
	 * 
	 * @param material
	 * @param quantity
	 * @param displayName
	 * @param lore
	 * @return Created itemstack
	 */
	public ItemStack setLogo(Material material, int quantity, String displayName, List<String> lore) {
		ItemStack logo = new ItemStack(material, quantity);
		ItemMeta meta = logo.getItemMeta();
		meta.setDisplayName(displayName);
		meta.setLore(lore);
		logo.setItemMeta(meta);
		this.logo = logo;
		return logo;
	}

	/**
	 * Set the logo as an itemstack
	 * 
	 * @param logo
	 */
	public void setLogo(ItemStack logo) {
		this.logo = logo;
	}

	/**
	 * Get the name of the action
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the karma change
	 * 
	 * @return
	 */
	public int getKarmaChange() {
		return karmaChange;
	}

	/**
	 * Get the logo
	 * 
	 * @return
	 */
	public ItemStack getLogo() {
		return logo;
	}

	/**
	 * Get the 4 verbs (past, present, conditional, future)
	 * 
	 * @return
	 */
	public String[] getVerbs() {
		return verbs;
	}

	/**
	 * Get the time taken to complete the action (20 = 1 second)
	 * 
	 * @return
	 */
	public long getTimeTaken() {
		return timeTaken;
	}

	/**
	 * Get position of action in GUI
	 * 
	 * @return
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * Get the requirements
	 * 
	 * @return
	 */
	public ItemStack[] getRequirements() {
		return requirements;
	}

	/**
	 * Get the rewards
	 * 
	 * @return
	 */
	public ItemStack[] getRewards() {
		return rewards;
	}

	/**
	 * Get the balance requirement
	 * 
	 * @return
	 */
	public double getBalanceRequirement() {
		return balanceRequirement;
	}

	/**
	 * Get the balance reward
	 * 
	 * @return
	 */
	public double getBalanceReward() {
		return balanceReward;
	}
}
