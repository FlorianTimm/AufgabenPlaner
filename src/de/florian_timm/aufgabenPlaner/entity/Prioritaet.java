package de.florian_timm.aufgabenPlaner.entity;

import de.florian_timm.aufgabenPlaner.schnittstelle.DatenHaltung;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Prioritaet extends EntitySortierung {
	private static Map<Integer, Prioritaet> alle = new HashMap<Integer, Prioritaet>();
	private String bezeichnung;
	private int sortierung;

	public Prioritaet(int dbId, String bezeichnung, int sortierung) {
		this.dbId = dbId;
		this.bezeichnung = bezeichnung;
		this.sortierung = sortierung;
	}

	public static Prioritaet getPrio(int id) {
		checkLoading();
		return alle.get(id);
	}

	private static void loadData() {
		alle.clear();
		DatenHaltung d = new DatenHaltung();
		d.query("SELECT * FROM prioritaet;");

		while (d.next()) {
			int dbId = d.getInt("id");
			String bezeichnung = d.getString("bezeichnung");
			int sortierung = d.getInt("sortierung");
			alle.put(dbId, new Prioritaet(dbId, bezeichnung, sortierung));
			System.out.println(alle.size());
		}

	}

	public static void createTable() {
		new DatenHaltung(true).update("CREATE TABLE IF NOT EXISTS prioritaet (id INTEGER PRIMARY KEY, "
				+ "bezeichnung TEXT UNIQUE NOT NULL, sortierung INTEGER);");

		new DatenHaltung(true).update(
				"INSERT INTO prioritaet (bezeichnung, sortierung) VALUES ('hoch', 100), ('niedrig', 0), ('mittel',50);");
	}

	public static Prioritaet[] getArray() {
		// TODO Auto-generated method stub
		checkLoading();
		Prioritaet[] p = alle.values().toArray(new Prioritaet[0]);
		Arrays.sort(p);
		return p;
	}

	protected static void checkLoading() {
		if (alle.size() == 0) {
			loadData();
		}
	}

	public String toString() {
		return bezeichnung;
	}

	public int getSortierung() {
		return sortierung;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((bezeichnung == null) ? 0 : bezeichnung.hashCode());
		result = prime * result + sortierung;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Prioritaet other = (Prioritaet) obj;
		if (bezeichnung == null) {
			if (other.bezeichnung != null)
				return false;
		} else if (!bezeichnung.equals(other.bezeichnung))
			return false;
		if (sortierung != other.sortierung)
			return false;
		return true;
	}

	@Override
	public void update(Entity neu) {
		// TODO Auto-generated method stub
		
	}

	
	
}
