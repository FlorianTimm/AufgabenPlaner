package de.florian_timm.aufgabenPlaner.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import de.florian_timm.aufgabenPlaner.DatenhaltungS;

public class Status {
	private int dbId;
	private String bezeichnung;
	private int sortierung;

	private static Map<Integer, Status> alle = new HashMap<Integer, Status>();



	public Status(int dbId, String bezeichnung, int sortierung) {
		this.dbId = dbId;
		this.bezeichnung = bezeichnung;
		this.sortierung = sortierung;
	}
	
	

	public static Status getStatus(int id) {
		if (alle.size() == 0) {
			loadStatus();
		}
		return alle.get(id);
	}

	private static void loadStatus() {
		alle.clear();
		try {
			ResultSet rs = DatenhaltungS.query("SELECT * FROM status;");

			while (rs.next()) {
				int dbId = rs.getInt("id");
				String bezeichnung = rs.getString("bezeichnung");
				int sortierung = rs.getInt("sortierung");
				rs.close();
				alle.put(dbId, new Status(dbId, bezeichnung, sortierung));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	public int getDbId() {
		return dbId;
	}



	public void setDbId(int dbId) {
		this.dbId = dbId;
	}



	public String getBezeichnung() {
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

}
