package de.florian_timm.aufgabenPlaner.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import de.florian_timm.aufgabenPlaner.schnittstelle.DatenhaltungS;

public class Status extends Entity implements Comparable<Status> {
	private String bezeichnung;
	private int sortierung;

	private static Map<Integer, Status> alle = new HashMap<Integer, Status>();

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
		try {
			ResultSet rs = DatenhaltungS.query("SELECT * FROM status;");

			while (rs.next()) {
				int dbId = rs.getInt("id");
				String bezeichnung = rs.getString("bezeichnung");
				int sortierung = rs.getInt("sortierung");
				
				alle.put(dbId, new Status(dbId, bezeichnung, sortierung));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
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

	public static void createTable() {
		DatenhaltungS.update("CREATE TABLE IF NOT EXISTS status (id INTEGER PRIMARY KEY, "
				+ "bezeichnung TEXT UNIQUE NOT NULL, sortierung INTEGER);");

		DatenhaltungS.update(
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
			a[i++] = (Status) pair.getValue();
		}
	    Arrays.sort(a);
		return a;
	}

	@Override
	public int compareTo(Status other) {
		// TODO Auto-generated method stub
		int t = this.getSortierung();
		int o = other.getSortierung();
		if (t > o) {
			return 1;
		} else if (o > t) {
			return -1;
		} else {
			return 0;
		}
	}

}
