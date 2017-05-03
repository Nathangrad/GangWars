package gg.arcanum.gangwars.crime;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class ActionListener implements Listener {

	Player player;

	@EventHandler
	public void testActionCreate(BlockBreakEvent e) {
		if (e.getBlock() != null && e.getBlock().getType() != null
				&& e.getBlock().getType() == Material.DIAMOND_BLOCK) {
			player = e.getPlayer();
			Action action = ActionHandler.getAction("RAPE");
			ActionHandler.performAction(player, action);
		}
	}

	@EventHandler
	public void testActionStop(BlockBreakEvent e) {
		if (e.getBlock() != null && e.getBlock().getType() != null
				&& e.getBlock().getType() == Material.EMERALD_BLOCK) {
			Player blocker = e.getPlayer();
			ActionHandler.stopAction(player, blocker);
		}
	}
}
