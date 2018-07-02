package de.florian_timm.aufgabenPlaner.entity;

import de.florian_timm.aufgabenPlaner.schnittstelle.DatenhaltungS;

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
		DatenhaltungS d = new DatenhaltungS();
		d.query("SELECT * FROM kostentraeger;");

		while (d.next()) {
			int dbId = d.getInt("id");
			String bezeichnung = d.getString("bezeichnung");
			String sapName = d.getString("sapname");
			alle.put(dbId, new Kostentraeger(dbId, bezeichnung, sapName));
		}

	}

	public static void createTable() {
		new DatenhaltungS(true).update("CREATE TABLE IF NOT EXISTS kostentraeger (id INTEGER PRIMARY KEY, "
				+ "bezeichnung TEXT UNIQUE NOT NULL, sapname TEXT);");

		new DatenhaltungS(true).update("INSERT INTO kostentraeger (bezeichnung) VALUES ('- keiner -');");
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
}
