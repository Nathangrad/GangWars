package gg.arcanum.gangwars.karma;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import gg.arcanum.gangwars.crime.Action;
import gg.arcanum.gangwars.crime.ActionHandler;
import gg.arcanum.gangwars.crime.ActionListener;

public class GangWars extends JavaPlugin {

	@Override
	public void onEnable() {
		handleGroups();
		handleActionConfig();
		handleActions();
		Bukkit.getServer().getPluginManager().registerEvents(new KarmaListener(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new ActionListener(), this);
		getLogger().info("Successfully enabled");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	void handleGroups() {
		List<LinkedHashMap> list = (List<LinkedHashMap>) getConfig().getList("karma.groups");
		for (int i = 0; i < list.size(); i++) {
			LinkedHashMap group = list.get(i);
			int min = (int) group.get("min");
			int max = (int) group.get("max");
			new GWGroup(group.keySet().toArray()[0].toString(), min, max);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	void handleActions() {
		List<LinkedHashMap> list = (List<LinkedHashMap>) getConfig().getList("crime.actions");
		for (int i = 0; i < list.size(); i++) {
			LinkedHashMap group = list.get(i);
			String name = (String) group.get("name");
			int karmaChange = (int) group.get("karmaChange");
			long timeTaken = (int) group.get("timeTaken");
			int position = (int) group.get("position");
			double balanceRequirement = (double) group.get("balanceRequirement");
			double balanceReward = (double) group.get("balanceReward");
			String[] verbs = ((String) group.get("verbs")).split(",");
			ItemStack[] requirements = getItemStacks((List<LinkedHashMap>) group.get("requirements"));
			ItemStack[] rewards = getItemStacks((List<LinkedHashMap>) group.get("rewards"));
			new Action(name, karmaChange, timeTaken, position, verbs, requirements, rewards, balanceRequirement,
					balanceReward);
		}
	}

	void handleActionConfig() {
		ActionHandler.setActionAttempt(getConfig().getString("crime.messages.action_attempt").replaceAll("&", "§"));
		ActionHandler.setActionSuccess(getConfig().getString("crime.messages.action_success").replaceAll("&", "§"));
		ActionHandler.setActionFailureReq(
				getConfig().getString("crime.messages.action_failure_requirement").replaceAll("&", "§"));
		ActionHandler.setActionFailure(getConfig().getString("crime.messages.action_failure").replaceAll("&", "§"));
		ActionHandler.setActionStopped(getConfig().getString("crime.messages.action_stopped").replaceAll("&", "§"));
	}

	@SuppressWarnings("rawtypes")
	ItemStack[] getItemStacks(List<LinkedHashMap> itemStacks) {
		ItemStack[] array = new ItemStack[itemStacks.size()];
		for (int i = 0; i < itemStacks.size(); i++) {
			LinkedHashMap items = itemStacks.get(i);
			ItemStack itemStack = new ItemStack(Material.getMaterial((String) items.get("material")),
					(int) items.get("quantity"));
			try {
				ItemMeta itemStackMeta = itemStack.getItemMeta();
				itemStackMeta.setDisplayName(((String) items.get("name")).replaceAll("&", "§"));
				String[] lore = ((String) items.get("lore")).split("\n");
				List<String> loreList = new ArrayList<>();
				for (int j = 0; j < lore.length; j++)
					loreList.add(lore[j].replaceAll("&", "§"));
				itemStackMeta.setLore(loreList);
				itemStack.setItemMeta(itemStackMeta);
			} catch (Exception s) {
			}
			array[i] = itemStack;
		}
		return array;
	}
}
