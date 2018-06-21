package de.florian_timm.aufgabenPlaner.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import de.florian_timm.aufgabenPlaner.DatenhaltungS;

public class Kostentraeger {
	private int dbId;
	private String bezeichnung;
	private String sapName;
	private static Map<Integer, Kostentraeger> alle = new HashMap<Integer, Kostentraeger>();
	
	
	public Kostentraeger(int dbId, String bezeichnung, String sapName) {
		this.dbId = dbId;
		this.bezeichnung = bezeichnung;
		this.sapName = sapName;
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

	public static Kostentraeger get(int id) {
		if (alle.size() == 0) {
			loadStatus();
		}
		return alle.get(id);
	}

	private static void loadStatus() {
		alle.clear();
		try {
			ResultSet rs = DatenhaltungS.query("SELECT * FROM kostentraeger;");

			while (rs.next()) {
				int dbId = rs.getInt("id");
				String bezeichnung = rs.getString("bezeichnung");
				String sapName = rs.getString("sapname");
				rs.close();
				alle.put(dbId, new Kostentraeger(dbId, bezeichnung, sapName));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void createTable() {
		DatenhaltungS.update("CREATE TABLE IF NOT EXISTS kostentraeger (id INTEGER PRIMARY KEY, "
				+ "bezeichnung TEXT UNIQUE NOT NULL, sapname TEXT);");

		DatenhaltungS.update(
				"INSERT INTO kostentraeger (bezeichnung) VALUES ('- keiner -');");
	}

}
