package de.florian_timm.aufgabenPlaner.entity;

import de.florian_timm.aufgabenPlaner.schnittstelle.DatenHaltung;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Status extends EntitySortierung {
	private static Map<Integer, Status> alle = new HashMap<Integer, Status>();
	private String bezeichnung;
	private int sortierung;

	public Status(int dbId, String bezeichnung, int sortierung) {
		this.dbId = dbId;
		this.bezeichnung = bezeichnung;
		this.sortierung = sortierung;
	}

	public static Status getStatus(int id) {
		checkStatus();
		return alle.get(id);
	}

	private static void checkStatus() {
		if (alle.size() == 0) {
			loadStatus();
		}
	}

	private static void loadStatus() {
		alle.clear();

		DatenHaltung d = new DatenHaltung();
		d.query("SELECT * FROM status;");

		while (d.next()) {
			int dbId = d.getInt("id");
			String bezeichnung = d.getString("bezeichnung");
			int sortierung = d.getInt("sortierung");

			alle.put(dbId, new Status(dbId, bezeichnung, sortierung));
		}
	}

	public static void createTable() {
		new DatenHaltung(true).update("CREATE TABLE IF NOT EXISTS status (id INTEGER PRIMARY KEY, "
				+ "bezeichnung TEXT UNIQUE NOT NULL, sortierung INTEGER);");

		new DatenHaltung(true).update(
				"INSERT INTO status (bezeichnung, sortierung) VALUES ('fertig', 100), ('nicht angefangen', 0), ('halbfertig',50);");
	}

	public static Status[] getArray() {
		checkStatus();
		Status[] a = new Status[alle.size()];
		int i = 0;
		Iterator<?> it = alle.entrySet().iterator();
		while (it.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<Integer, Status> pair = (Map.Entry<Integer, Status>) it.next();
			a[i++] = pair.getValue();
		}
		Arrays.sort(a);
		return a;
	}

	public String toString() {
		return bezeichnung;
	}

	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	public int getSortierung() {
		return sortierung;
	}

	public void setSortierung(int sortierung) {
		this.sortierung = sortierung;
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
		Status other = (Status) obj;
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
