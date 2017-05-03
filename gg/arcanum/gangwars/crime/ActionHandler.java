package gg.arcanum.gangwars.crime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import gg.arcanum.core.Core;
import gg.arcanum.data.EconomyAPI;
import gg.arcanum.gangwars.karma.GWPlayer;

public final class ActionHandler {

	private ActionHandler() {
	}

	private static List<Action> actions = new ArrayList<>();
	private static Map<Player, BukkitTask> playerActions = new HashMap<>();
	private static Map<Player, Action> playerActionsObjs = new HashMap<>();

	private static String actionAttempt;
	private static String actionSuccess;
	private static String actionFailureReq;
	private static String actionFailure;
	private static String actionStopped;

	/**
	 * Get the Map<Integer, Action> of actions
	 * 
	 * @return
	 */
	public static List<Action> getActions() {
		return actions;
	}

	/**
	 * Get an action by its name
	 * 
	 * @param name
	 * @return
	 */
	public static Action getAction(String name) {
		for (Action action : actions) {
			if (action.getName().equalsIgnoreCase(name))
				return action;
		}
		return null;
	}

	/**
	 * Checks whether player can complete action or not
	 * 
	 * @param player
	 * @param action
	 * @return
	 */
	public static boolean canComplete(Player player, Action action) {
		ItemStack[] requirements = action.getRequirements();
		if (requirements != null)
			for (ItemStack itemStack : requirements)
				if (!player.getInventory().containsAtLeast(itemStack, itemStack.getAmount()))
					return false;
				else
					player.getInventory().removeItem(itemStack);
		if (action.getBalanceRequirement() != 0)
			if (EconomyAPI.get(player) < action.getBalanceRequirement())
				return false;
		return true;
	}

	/**
	 * Give player the rewards for completing an action
	 * 
	 * @param player
	 * @param action
	 */
	public static void giveRewards(Player player, Action action) {
		ItemStack[] rewards = action.getRewards();
		if (rewards != null)
			for (ItemStack itemStack : rewards)
				player.getInventory().addItem(itemStack);
		EconomyAPI.update(player, action.getBalanceReward());
	}

	/**
	 * if canComplete(player, action) then schedule the task
	 * 
	 * @param player
	 * @param action
	 * @return
	 */
	public static boolean performAction(Player player, Action action) {
		if (!canComplete(player, action)) {
			player.sendMessage(format(actionFailureReq, player, action));
			return false;
		}
		BukkitTask task = Bukkit.getScheduler().runTaskLater(Core.getPlugin(), new Runnable() {
			@Override
			public void run() {
				player.sendMessage(format(actionSuccess, player, action));
				giveRewards(player, action);
				playerActions.remove(player);
				playerActionsObjs.remove(player);
			}
		}, action.getTimeTaken());
		player.sendMessage(format(actionAttempt, player, action));
		playerActions.put(player, task);
		playerActionsObjs.put(player, action);
		return true;
	}

	/**
	 * Stop the action for a player
	 * 
	 * @param player
	 * @param stopper
	 * @return
	 */
	public static boolean stopAction(Player player, Player blocker) {
		if (!playerActions.containsKey(player))
			return false;
		playerActions.get(player).cancel();
		player.sendMessage(format(actionFailure, player, playerActionsObjs.get(player)).replaceAll("%blocker%",
				blocker.getName()));
		blocker.sendMessage(format(actionStopped, player, playerActionsObjs.get(player)));
		playerActions.remove(player);
		playerActionsObjs.remove(player);
		return true;
	}

	private static String format(String string, Player player, Action action) {
		return string.replaceAll("%player%", player.getName()).replaceAll("%past%", action.getVerbs()[0])
				.replaceAll("%present%", action.getVerbs()[1]).replaceAll("%future%", action.getVerbs()[2])
				.replaceAll("%cost%", String.valueOf(action.getBalanceRequirement()))
				.replaceAll("%reward%", String.valueOf(action.getBalanceReward()))
				.replaceAll("%actionName%", action.getName())
				.replaceAll("%playerKarma%", String.valueOf(GWPlayer.getPlayer(player).getKarma()))
				.replaceAll("%karmaChange%", String.valueOf(action.getKarmaChange()))
				.replaceAll("%seconds%", String.valueOf(action.getTimeTaken() / 20))
				.replaceAll("%minutes%", String.valueOf(action.getTimeTaken() / 1200));
	}

	public static void setActionSuccess(String actionSuccess) {
		ActionHandler.actionSuccess = actionSuccess;
	}

	public static void setActionFailureReq(String actionFailureReq) {
		ActionHandler.actionFailureReq = actionFailureReq;
	}

	public static void setActionFailure(String actionFailure) {
		ActionHandler.actionFailure = actionFailure;
	}

	public static void setActionStopped(String actionStopped) {
		ActionHandler.actionStopped = actionStopped;
	}

	public static void setActionAttempt(String actionAttempt) {
		ActionHandler.actionAttempt = actionAttempt;
	}
}
