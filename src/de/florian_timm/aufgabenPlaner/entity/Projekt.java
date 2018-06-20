package de.florian_timm.aufgabenPlaner.entity;

import java.util.Date;

public class Projekt {
	private int dbId;
	private String titel;
	private String beschreibung;
	private Person zustaendig;
	private Prioritaet prioritaet;
	private Date erstellt;
	private Date faelligkeit;
	private Kostentraeger kostentraeger;
	boolean archiviert;
	
	public Projekt() {
	}
	
	public Projekt(String titel, String beschreibung, Person zustaendig, Kostentraeger kostentraeger,
			Prioritaet prioritaet, Date erstellt, Date faelligkeit) {
		super();
		this.titel = titel;
		this.beschreibung = beschreibung;
		this.zustaendig = zustaendig;
		this.kostentraeger = kostentraeger;
		this.prioritaet = prioritaet;
		this.erstellt = erstellt;
		this.faelligkeit = faelligkeit;
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
	public Person getZustaendig() {
		return zustaendig;
	}
	public void setZustaendig(Person zustaendig) {
		this.zustaendig = zustaendig;
	}
	public Prioritaet getPrioritaet() {
		return prioritaet;
	}
	public void setPrioritaet(Prioritaet prioritaet) {
		this.prioritaet = prioritaet;
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
	public Kostentraeger getKostentraeger() {
		return kostentraeger;
	}
	public void setKostentraeger(Kostentraeger kostentraeger) {
		this.kostentraeger = kostentraeger;
	}
	public boolean isArchiviert() {
		return archiviert;
	}
	public void setArchiviert(boolean archiviert) {
		this.archiviert = archiviert;
	}

}
