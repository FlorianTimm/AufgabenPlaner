package de.florian_timm.aufgabenPlaner.entity;

import de.florian_timm.aufgabenPlaner.entity.ordner.AufgabenOrdner;
import java.util.Date;

public class Aufgabe extends Entity {
	private Person bearbeiter;
	private String titel;
	private String beschreibung;
	private Date erstellt;
	private Date faelligkeit;
	private Status status;
	private Projekt projekt;

	public Aufgabe(int dbId, Person bearbeiter, String titel, String beschreibung, Date faelligkeit, Status status,
			Projekt projekt) {
		this.dbId = dbId;
		this.bearbeiter = bearbeiter;
		this.titel = titel;
		this.beschreibung = beschreibung;
		// this.erstellt = erstellt;
		this.faelligkeit = faelligkeit;
		this.status = status;
		this.projekt = projekt;
	}

	public String getTitel() {
		return titel;
	}

	public String toString() {
		return getTitel();
	}

	public Person getBearbeiter() {
		return bearbeiter;
	}

	public void setBearbeiter(Person bearbeiter) {
		this.bearbeiter = bearbeiter;
	}

	public void setTitel(String titel) {
		this.titel = titel;
	}

	public String getBeschreibung() {
		return beschreibung;
	}

	public Date getErstellt() {
		return erstellt;
	}

	public Date getFaelligkeit() {
		return faelligkeit;
	}

	public Status getStatus() {
		return status;
	}

	public Projekt getProjekt() {
		return projekt;
	}

	public void update(Aufgabe aufgabe) {
		this.bearbeiter = aufgabe.getBearbeiter();
		this.titel = aufgabe.getTitel();
		this.beschreibung = aufgabe.getBeschreibung();
		this.faelligkeit = aufgabe.getFaelligkeit();
		this.status = aufgabe.getStatus();
	}

	public void updateDB(String titel, String beschreibung, Person bearbeiter, Date faelligkeit, Status status) {
		this.bearbeiter = bearbeiter;
		this.titel = titel;
		this.beschreibung = beschreibung;
		this.faelligkeit = faelligkeit;
		this.status = status;
		AufgabenOrdner.getInstanz(projekt).update(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((bearbeiter == null) ? 0 : bearbeiter.hashCode());
		result = prime * result + ((beschreibung == null) ? 0 : beschreibung.hashCode());
		result = prime * result + ((erstellt == null) ? 0 : erstellt.hashCode());
		result = prime * result + ((faelligkeit == null) ? 0 : faelligkeit.hashCode());
		result = prime * result + ((projekt == null) ? 0 : projekt.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((titel == null) ? 0 : titel.hashCode());
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
		Aufgabe other = (Aufgabe) obj;
		if (bearbeiter == null) {
			if (other.bearbeiter != null)
				return false;
		} else if (!bearbeiter.equals(other.bearbeiter))
			return false;
		if (beschreibung == null) {
			if (other.beschreibung != null)
				return false;
		} else if (!beschreibung.equals(other.beschreibung))
			return false;
		if (erstellt == null) {
			if (other.erstellt != null)
				return false;
		} else if (!erstellt.equals(other.erstellt))
			return false;
		if (faelligkeit == null) {
			if (other.faelligkeit != null)
				return false;
		} else if (!faelligkeit.equals(other.faelligkeit))
			return false;
		if (projekt == null) {
			if (other.projekt != null)
				return false;
		} else if (!projekt.equals(other.projekt))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (titel == null) {
			if (other.titel != null)
				return false;
		} else if (!titel.equals(other.titel))
			return false;
		return true;
	}

	@Override
	public void update(Entity neu) {
		// TODO Auto-generated method stub
		
	}



}
