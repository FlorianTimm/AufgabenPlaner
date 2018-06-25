package de.florian_timm.aufgabenPlaner.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ComboBoxModel;

import de.florian_timm.aufgabenPlaner.DatenhaltungS;

public class Prioritaet implements Comparable<Prioritaet> {
	private int dbId;
	private String bezeichnung;
	private int sortierung;
	private static Map<Integer, Prioritaet> alle = new HashMap<Integer, Prioritaet>();

	public Prioritaet(int dbId, String bezeichnung, int sortierung) {
		this.dbId = dbId;
		this.bezeichnung = bezeichnung;
		this.sortierung = sortierung;
	}

	public int getId() {
		return dbId;
	}

	public String toString() {
		return bezeichnung;
	}

	public int getSortierung() {
		return sortierung;
	}

	public static Prioritaet getPrio(int id) {
		if (alle.size() == 0) {
			loadPrio();
		}
		return alle.get(id);
	}

	private static void loadPrio() {
		alle.clear();
		try {
			ResultSet rs = DatenhaltungS.query("SELECT * FROM prioritaet;");

			while (rs.next()) {
				int dbId = rs.getInt("id");
				String bezeichnung = rs.getString("bezeichnung");
				int sortierung = rs.getInt("sortierung");
				alle.put(dbId, new Prioritaet(dbId, bezeichnung, sortierung));
				System.out.println(alle.size());
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void createTable() {
		DatenhaltungS.update("CREATE TABLE IF NOT EXISTS prioritaet (id INTEGER PRIMARY KEY, "
				+ "bezeichnung TEXT UNIQUE NOT NULL, sortierung INTEGER);");

		DatenhaltungS.update(
				"INSERT INTO prioritaet (bezeichnung, sortierung) VALUES ('hoch', 100), ('niedrig', 0), ('mittel',50);");
	}

	public static Prioritaet[] getArray() {
		// TODO Auto-generated method stub
		Prioritaet[] p = (Prioritaet[]) alle.values().toArray(new Prioritaet[0]);
		Arrays.sort(p);
		return p;
	}

	@Override
	public int compareTo(Prioritaet other) {
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
