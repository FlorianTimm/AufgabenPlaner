package de.florian_timm.aufgabenPlaner.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.florian_timm.aufgabenPlaner.DatenhaltungS;

public class Aufgabe {
	private int dbId;
	private List<Person> bearbeiter = new ArrayList<Person>();
	private String titel = "";
	private String beschreibung = "";
	private Date erstellt;
	private Date faelligkeit;
	private int status;
	private boolean storniert;

	public Aufgabe(List<Person> bearbeiter, String titel, String beschreibung, Date erstellt, Date faelligkeit) {
		super();
		this.bearbeiter = bearbeiter;
		this.titel = titel;
		this.beschreibung = beschreibung;
		this.erstellt = erstellt;
		this.faelligkeit = faelligkeit;
	}

	public Aufgabe(int dbId, List<Person> bearbeiter, String titel, String beschreibung, Date erstellt,
			Date faelligkeit, int status, boolean storniert) {
		super();
		this.dbId = dbId;
		this.bearbeiter = bearbeiter;
		this.titel = titel;
		this.beschreibung = beschreibung;
		this.erstellt = erstellt;
		this.faelligkeit = faelligkeit;
		this.status = status;
		this.storniert = storniert;
	}

	public List<Person> getBearbeiter() {
		return bearbeiter;
	}

	public void setBearbeiter(List<Person> bearbeiter) {
		this.bearbeiter = bearbeiter;
	}

	public String getTitel() {
		return titel;
	}

	public void setTitel(String titel) {
		this.titel = titel;
	}

	public String getBeschreibung() {
		return beschreibung;
	}

	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}

	public Date getErstellt() {
		return erstellt;
	}

	public void setErstellt(Date erstellt) {
		this.erstellt = erstellt;
	}

	public Date getFaelligkeit() {
		return faelligkeit;
	}

	public void setFaelligkeit(Date faelligkeit) {
		this.faelligkeit = faelligkeit;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public boolean isStorniert() {
		return storniert;
	}

	public void setStorniert(boolean storniert) {
		this.storniert = storniert;
	}

	public int getDbId() {
		return dbId;
	}

	public List<Aufgabe> getAufgaben(String userid) {
		List<Aufgabe> aufgaben = new ArrayList<Aufgabe>();
		try {
			ResultSet rs = DatenhaltungS.query(
					"SELECT * FROM aufgaben, aufgabenzuordnung where aufgaben.id = aufgabenzuordnung.aufgabenid and aufgabenzuordnung.userid = '"
							+ userid + "';");

			while (rs.next()) {
				int id = rs.getInt("id");
				String titel = rs.getString("titel");
				String beschreibung = rs.getString("beschreibung");
				rs.close();
				aufgaben.add(new Aufgabe(titel, beschreibung, id));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return aufgaben;
	}

}
