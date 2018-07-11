package de.florian_timm.aufgabenPlaner.entity;

import java.time.Duration;
import java.util.Date;

import de.florian_timm.aufgabenPlaner.entity.ordner.BearbeitungOrdner;

public class Bearbeitung extends Entity {
	private Date start;
	private Duration dauer;
	private String bemerkung;
	private Aufgabe aufgabe;
	private Person bearbeiter;

	public Bearbeitung(int dbId, Aufgabe aufgabe, Person bearbeiter, Date start, Duration dauer, String bemerkung) {
		this.dbId = dbId;
		this.aufgabe = aufgabe;
		this.start = start;
		this.dauer = dauer;
		this.bemerkung = bemerkung;
		this.bearbeiter = bearbeiter;
	}

	public Date getStart() {
		return start;
	}

	public Duration getDauer() {
		return dauer;
	}

	public String getBemerkung() {
		return bemerkung;
	}

	public Person getBearbeiter() {
		return bearbeiter;
	}

	public Aufgabe getAufgabe() {
		return aufgabe;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Entity neu) {
		Bearbeitung b = (Bearbeitung) neu;
		this.aufgabe = b.getAufgabe();
		this.bemerkung = b.getBemerkung();
		this.start = b.getStart();
		this.dauer = b.getDauer();
		this.bearbeiter = b.getBearbeiter();
	}

	public void updateDB(Date start, Duration dauer, String bemerkung) {
		this.start = start;
		this.dauer = dauer;
		this.bemerkung = bemerkung;
		BearbeitungOrdner.getInstanz(aufgabe).updateDB(this);
	}

	public void plus(long stunden) {
		Duration d = Duration.ofHours(stunden);
		if (this.dauer == null)
			this.dauer = d;
		else
			this.dauer = Duration.ofSeconds(this.dauer.getSeconds() + stunden * 60 * 60);
		BearbeitungOrdner.getInstanz(aufgabe).updateDB(this);
	}

}
