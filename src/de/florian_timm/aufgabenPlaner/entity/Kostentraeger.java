package de.florian_timm.aufgabenPlaner.entity;

public class Kostentraeger {
	private int dbId;
	private String bezeichnung;
	private String sapName;
	
	
	public Kostentraeger(String bezeichnung, String sapName) {
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
}
