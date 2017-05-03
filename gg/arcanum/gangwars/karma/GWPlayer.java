package gg.arcanum.gangwars.karma;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

import gg.arcanum.data.MySqlManager;

public class GWPlayer {

	private static Map<UUID, GWPlayer> players = new HashMap<>();

	private UUID uuid;
	private int karma;

	public GWPlayer(UUID uuid, int karma) {
		players.put(uuid, this);
		this.uuid = uuid;
		this.karma = karma;
	}

	/**
	 * Remove player
	 * 
	 * @param p
	 *            Player
	 */
	public static void removePlayer(Player p) {
		if (players.containsKey(p.getUniqueId()))
			players.remove(p.getUniqueId());
	}

	/**
	 * Get players
	 * 
	 * @return Player map
	 */
	public static Map<UUID, GWPlayer> getPlayers() {
		return players;
	}

	/**
	 * Get specific player
	 * 
	 * @param player
	 *            Player to get
	 * @return GWPlayer object
	 */
	public static GWPlayer getPlayer(Player player) {
		if (players.containsKey(player.getUniqueId()))
			return players.get(player.getUniqueId());
		else
			return null;
	}

	/**
	 * Get UUID of the player
	 * 
	 * @return UUID of the player
	 */
	public UUID getUUID() {
		return uuid;
	}

	/**
	 * Get the the karma value of the player
	 * 
	 * @return Karma as int
	 */
	public int getKarma() {
		return karma;
	}

	/**
	 * Update the player's karma by a positive / negative integer
	 * 
	 * @param change
	 *            The number to change (positive / negative)
	 */
	public void updateKarma(int change) {
		karma += change;
		MySqlManager.doRunnable(new Runnable() {
			@Override
			public void run() {
				try {
					MySqlManager sql = new MySqlManager();
					sql.open();
					sql.executeNonQuery("UPDATE players SET karma=" + karma + " WHERE uuid='" + uuid.toString() + "'");
					sql.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Get the group the player belongs to
	 * 
	 * @return GWGroup of the player
	 */
	public GWGroup getGroup() {
		List<GWGroup> groups = GWGroup.getGroups();
		int groupCount = groups.size();
		for (int i = 0; i < groupCount; i++) {
			GWGroup group = groups.get(i);
			Bounds groupBounds = group.getBounds();
			if (karma >= groupBounds.getMin() && karma <= groupBounds.getMax())
				return group;
		}
		return null;
	}
}
