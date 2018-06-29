package de.florian_timm.aufgabenPlaner.entity;

import de.florian_timm.aufgabenPlaner.kontroll.EntityListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public abstract class Entity {
	private static Set<EntityListener> listener = new HashSet<EntityListener>();
	protected int dbId;

	public static void addListener(EntityListener newListener) {
		listener.add(newListener);
	}

	public static void removeListener(EntityListener el) {
		listener.remove(el);
	}

	public static void informListener() {
		System.out.println("Listener: " + listener.size());
		EntityListener[] ls = listener.toArray(new EntityListener[0]);
		for (EntityListener el : ls) {
			el.dataChanged();
		}
	}

	public int getId() {
		return dbId;
	}

}
