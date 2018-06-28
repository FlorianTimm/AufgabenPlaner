package de.florian_timm.aufgabenPlaner.entity;

import java.util.ArrayList;
import java.util.List;

import de.florian_timm.aufgabenPlaner.kontroll.EntityListener;

public abstract class Entity {
	protected int dbId;
	
	private static List<EntityListener> listener = new ArrayList<EntityListener>();
	
	public int getId() {
		return dbId;
	}
	
	public static void addListener (EntityListener newListener) {
		listener.add(newListener);
	}
	
	public static void informListener() {
		for(EntityListener l : listener) {
			l.dataChanged();
		}
	}

}
