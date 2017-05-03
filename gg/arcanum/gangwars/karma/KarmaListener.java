package gg.arcanum.gangwars.karma;

import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import gg.arcanum.data.DataRow;
import gg.arcanum.data.DataTable;
import gg.arcanum.data.MySqlManager;

public class KarmaListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void karmaJoin(PlayerJoinEvent e) {
		MySqlManager.doRunnable(new Runnable() {
			@Override
			public void run() {
				MySqlManager mysql = new MySqlManager();
				try {
					mysql.open();
					DataTable dt = mysql.executeQuery(
							"SELECT * FROM players WHERE uuid='" + e.getPlayer().getUniqueId().toString() + "'");
					DataRow r = dt.getRows()[0];
					int karma = Integer.parseInt(r.getCell("karma").toString());
					new GWPlayer(e.getPlayer().getUniqueId(), karma);
					mysql.close();
					System.out.println(e.getPlayer().getName() + " is a "
							+ GWPlayer.getPlayer(e.getPlayer()).getGroup().getName());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@EventHandler
	public void karmaQuit(PlayerQuitEvent e) {
		GWPlayer player = GWPlayer.getPlayer(e.getPlayer());
		if (player == null)
			return;
		final int karma = player.getKarma();
		final UUID uuid = player.getUUID();
		GWPlayer.removePlayer(e.getPlayer());
		MySqlManager.doRunnable(new Runnable() {
			@Override
			public void run() {
				MySqlManager mysql = new MySqlManager();
				try {
					mysql.open();
					mysql.executeNonQuery(
							"UPDATE players SET karma=" + karma + " WHERE uuid='" + uuid.toString() + "'");
					mysql.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
