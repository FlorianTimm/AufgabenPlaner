package de.florian_timm.aufgabenPlaner.entity;

import java.util.ArrayList;
import java.util.List;

import de.florian_timm.aufgabenPlaner.kontroll.Listener;

public abstract class Entity {
	protected int dbId;
	
	private static List<Listener> listener = new ArrayList<Listener>();
	
	public int getId() {
		return dbId;
	}
	
	public static void addListener (Listener newListener) {
		listener.add(newListener);
	}
	
	public static void informListener() {
		for(Listener l : listener) {
			l.dataChanged();
		}
	}
	

}
