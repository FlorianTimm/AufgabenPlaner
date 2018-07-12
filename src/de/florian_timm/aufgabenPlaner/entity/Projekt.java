package de.florian_timm.aufgabenPlaner.entity;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import de.florian_timm.aufgabenPlaner.entity.ordner.AufgabenOrdner;
import de.florian_timm.aufgabenPlaner.entity.ordner.ProjektOrdner;

public class Projekt extends Entity {
	private String titel;
	private String beschreibung;
	private Person zustaendig;
	private Prioritaet prioritaet;
	private Date erstellt;
	private Date faelligkeit;
	private Kostentraeger kostentraeger;
	private boolean archiviert;
	private Person auftraggeber;
	private int status;
	private AufgabenOrdner aufgaben;
	private Person bearbeitetVon;
	private File ordner;

	public Projekt(int dbId, String titel, String beschreibung, Person zustaendig, Prioritaet prioritaet, Date erstellt,
			Date faelligkeit, Kostentraeger kostentraeger, boolean archiviert, File ordner, Person auftraggeber, int status, Person bearbeitetVon) {
		this.dbId = dbId;
		this.titel = titel;
		this.auftraggeber = auftraggeber;
		this.beschreibung = beschreibung;
		this.zustaendig = zustaendig;
		this.kostentraeger = kostentraeger;
		this.prioritaet = prioritaet;
		this.erstellt = erstellt;
		this.faelligkeit = faelligkeit;
		this.status = status;
		this.aufgaben = AufgabenOrdner.getInstanz(this);
		this.bearbeitetVon = bearbeitetVon;
		this.ordner = ordner;
	}

	public String getTitel() {
		return titel;
	}

	public String getBeschreibung() {
		return beschreibung;
	}

	public Person getZustaendig() {
		return zustaendig;
	}

	public Prioritaet getPrioritaet() {
		return prioritaet;
	}

	public Date getErstellt() {
		return erstellt;
	}

	public Date getFaelligkeit() {
		return faelligkeit;
	}

	public Kostentraeger getKostentraeger() {
		return kostentraeger;
	}

	public boolean isArchiviert() {
		return archiviert;
	}

	public Person getAuftraggeber() {
		return auftraggeber;
	}

	public int getStatus() {
		return status;
	}
	
	public Person getBearbeitetVon() {
		return bearbeitetVon;
	}
	
	public File getProjektordner() {
		return ordner;
	}



	public void update(Entity e) {
		Projekt p = (Projekt) e;
		this.titel = p.titel;
		this.auftraggeber = p.auftraggeber;
		this.beschreibung = p.beschreibung;
		this.zustaendig = p.zustaendig;
		this.kostentraeger = p.kostentraeger;
		this.prioritaet = p.prioritaet;
		this.erstellt = p.erstellt;
		this.faelligkeit = p.faelligkeit;
		this.status = p.status;
		this.bearbeitetVon = p.bearbeitetVon;
		this.ordner = p.ordner;
	}

	public Map<Integer, Entity> getAufgaben() {
		return aufgaben.getAufgaben();
	}

	public void updateDB(String titel, String beschreibung, Prioritaet prio, Person zustaendig,
			Kostentraeger kostentraeger, Date faelligkeit, Person auftraggeber, File ordner, Person bearbeitetVon) {
		this.titel = titel;
		this.beschreibung = beschreibung;
		this.zustaendig = zustaendig;
		this.kostentraeger = kostentraeger;
		this.prioritaet = prio;
		this.faelligkeit = faelligkeit;
		this.auftraggeber = auftraggeber;
		this.bearbeitetVon = bearbeitetVon;
		this.ordner = ordner;
		updateDB();
	}

	private void updateDB() {
		ProjektOrdner.getInstanz().updateDB(this);
	}
	
	public void plus(int tage) {
		Calendar cal = Calendar.getInstance();
        cal.setTime(this.getFaelligkeit());
        cal.add(Calendar.DATE, tage);
        this.faelligkeit = cal.getTime();
        updateDB();
	}

	@Override
	public String toString() {
		return getTitel();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (archiviert ? 1231 : 1237);
		result = prime * result + ((auftraggeber == null) ? 0 : auftraggeber.hashCode());
		result = prime * result + ((bearbeitetVon == null) ? 0 : bearbeitetVon.hashCode());
		result = prime * result + ((beschreibung == null) ? 0 : beschreibung.hashCode());
		result = prime * result + ((erstellt == null) ? 0 : erstellt.hashCode());
		result = prime * result + ((faelligkeit == null) ? 0 : faelligkeit.hashCode());
		result = prime * result + ((kostentraeger == null) ? 0 : kostentraeger.hashCode());
		result = prime * result + ((prioritaet == null) ? 0 : prioritaet.hashCode());
		result = prime * result + ((ordner == null) ? 0 : ordner.hashCode());
		result = prime * result + status;
		result = prime * result + ((titel == null) ? 0 : titel.hashCode());
		result = prime * result + ((zustaendig == null) ? 0 : zustaendig.hashCode());
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
		Projekt other = (Projekt) obj;
		if (archiviert != other.archiviert)
			return false;
		if (auftraggeber == null) {
			if (other.auftraggeber != null)
				return false;
		} else if (!auftraggeber.equals(other.auftraggeber))
			return false;
		if (bearbeitetVon == null) {
			if (other.bearbeitetVon != null)
				return false;
		} else if (!bearbeitetVon.equals(other.bearbeitetVon))
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
		if (kostentraeger == null) {
			if (other.kostentraeger != null)
				return false;
		} else if (!kostentraeger.equals(other.kostentraeger))
			return false;
		if (prioritaet == null) {
			if (other.prioritaet != null)
				return false;
		} else if (!prioritaet.equals(other.prioritaet))
			return false;
		if (ordner == null) {
			if (other.ordner != null)
				return false;
		} else if (!ordner.equals(other.ordner))
			return false;
		if (status != other.status)
			return false;
		if (titel == null) {
			if (other.titel != null)
				return false;
		} else if (!titel.equals(other.titel))
			return false;
		if (zustaendig == null) {
			if (other.zustaendig != null)
				return false;
		} else if (!zustaendig.equals(other.zustaendig))
			return false;
		return true;
	}

}
