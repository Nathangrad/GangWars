package gg.arcanum.gangwars.karma;

import java.util.ArrayList;
import java.util.List;

public class GWGroup {

	private static List<GWGroup> groups = new ArrayList<GWGroup>();

	private String name;
	private Bounds bounds;

	public static List<GWGroup> getGroups() {
		return groups;
	}

	public GWGroup(String name, int min, int max) {
		groups.add(this);
		this.name = name;
		this.bounds = new Bounds(min, max);
	}

	public String getName() {
		return name;
	}

	public Bounds getBounds() {
		return bounds;
	}
}
