package de.florian_timm.aufgabenPlaner.entity;

import de.florian_timm.aufgabenPlaner.schnittstelle.DatenHaltung;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Kostentraeger extends Entity {
	private static Map<Integer, Kostentraeger> alle = new HashMap<Integer, Kostentraeger>();
	private String bezeichnung;
	private String sapName;

	public Kostentraeger(int dbId, String bezeichnung, String sapName) {
		this.dbId = dbId;
		this.bezeichnung = bezeichnung;
		this.sapName = sapName;
	}

	public static Kostentraeger get(int id) {
		checkLoading();
		return alle.get(id);
	}

	private static void loadData() {
		alle.clear();
		DatenHaltung d = new DatenHaltung();
		d.query("SELECT * FROM kostentraeger;");

		while (d.next()) {
			int dbId = d.getInt("id");
			String bezeichnung = d.getString("bezeichnung");
			String sapName = d.getString("sapname");
			alle.put(dbId, new Kostentraeger(dbId, bezeichnung, sapName));
		}

	}

	public static void createTable() {
		new DatenHaltung(true).update("CREATE TABLE IF NOT EXISTS kostentraeger (id INTEGER PRIMARY KEY, "
				+ "bezeichnung TEXT UNIQUE NOT NULL, sapname TEXT);");

		new DatenHaltung(true).update("INSERT INTO kostentraeger (bezeichnung) VALUES ('- keiner -');");
	}

	public static Kostentraeger[] getArray() {
		checkLoading();
		Kostentraeger[] k = alle.values().toArray(new Kostentraeger[0]);
		Arrays.sort(k);
		return k;
	}

	protected static void checkLoading() {
		if (alle.size() == 0) {
			loadData();
		}
	}

	public String getSapName() {
		return sapName;
	}

	public void setSapName(String sapName) {
		this.sapName = sapName;
	}

	public String getBezeichnung() {
		return bezeichnung;
	}

	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	public String toString() {
		return bezeichnung;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((bezeichnung == null) ? 0 : bezeichnung.hashCode());
		result = prime * result + ((sapName == null) ? 0 : sapName.hashCode());
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
		Kostentraeger other = (Kostentraeger) obj;
		if (bezeichnung == null) {
			if (other.bezeichnung != null)
				return false;
		} else if (!bezeichnung.equals(other.bezeichnung))
			return false;
		if (sapName == null) {
			if (other.sapName != null)
				return false;
		} else if (!sapName.equals(other.sapName))
			return false;
		return true;
	}

	@Override
	public void update(Entity neu) {
		// TODO Auto-generated method stub
		
	}
	
	
}
