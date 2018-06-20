package de.florian_timm.aufgabenPlaner.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.florian_timm.aufgabenPlaner.DatenhaltungS;

public class Aufgabe {
	private int dbId;
	private Person bearbeiter;
	private String titel;
	private String beschreibung;
	private Date erstellt;
	private Date faelligkeit;
	private Status status;
	private boolean storniert;

	public Aufgabe(Person bearbeiter, String titel, String beschreibung, Date erstellt, Date faelligkeit) {
		this.bearbeiter = bearbeiter;
		this.titel = titel;
		this.beschreibung = beschreibung;
		this.erstellt = erstellt;
		this.faelligkeit = faelligkeit;
	}

	public Aufgabe(int dbId, Person bearbeiter, String titel, String beschreibung, Date erstellt, Date faelligkeit,
			Status status, boolean storniert) {
		this.dbId = dbId;
		this.bearbeiter = bearbeiter;
		this.titel = titel;
		this.beschreibung = beschreibung;
		this.erstellt = erstellt;
		this.faelligkeit = faelligkeit;
		this.status = status;
		this.storniert = storniert;
	}

	public Person getBearbeiter() {
		return bearbeiter;
	}

	public void setBearbeiter(Person bearbeiter) {
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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
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

	public static List<Aufgabe> getAufgaben(Person person) {
		List<Aufgabe> aufgaben = new ArrayList<Aufgabe>();
		try {
			ResultSet rs = DatenhaltungS.query(
					"SELECT * FROM aufgabe where aufgabe.bearbeiter = "
							+ person.getId() + ";");

			while (rs.next()) {
				int dbId = rs.getInt("id");
				Person bearbeiter = person;
				String titel = rs.getString("titel");
				String beschreibung = rs.getString("beschreibung");
				Date erstellt = rs.getDate("erstellt");
				Date faelligkeit = rs.getDate("faelligkeit");
				Status status = Status.getStatus(rs.getInt("status"));
				boolean storniert = rs.getBoolean("storniert");
				rs.close();
				aufgaben.add(new Aufgabe(dbId, bearbeiter, titel, beschreibung, erstellt,
						faelligkeit, status, storniert) );
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return aufgaben;
	}

}
