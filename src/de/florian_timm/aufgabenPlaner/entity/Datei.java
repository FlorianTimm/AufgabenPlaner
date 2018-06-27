package de.florian_timm.aufgabenPlaner.entity;

import java.net.URL;
import java.util.Date;

public class Datei extends Entity  {
	private Date datum;
	private String beschreibung;
	private URL url;
	
	public Datei(Date datum, String beschreibung, URL url) {
		super();
		this.datum = datum;
		this.beschreibung = beschreibung;
		this.url = url;
	}
	
	public Date getDatum() {
		return datum;
	}
	public void setDatum(Date datum) {
		this.datum = datum;
	}
	public String getBeschreibung() {
		return beschreibung;
	}
	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}
	public URL getUrl() {
		return url;
	}
	public void setUrl(URL url) {
		this.url = url;
	}	
}
