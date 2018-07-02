package de.florian_timm.aufgabenPlaner.entity;

import de.florian_timm.aufgabenPlaner.schnittstelle.DatenhaltungS;

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
		DatenhaltungS d = new DatenhaltungS();
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
		new DatenhaltungS(true).update("CREATE TABLE IF NOT EXISTS prioritaet (id INTEGER PRIMARY KEY, "
				+ "bezeichnung TEXT UNIQUE NOT NULL, sortierung INTEGER);");

		new DatenhaltungS(true).update(
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

}
