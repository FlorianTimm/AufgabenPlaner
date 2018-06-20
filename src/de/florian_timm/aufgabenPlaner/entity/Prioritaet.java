package de.florian_timm.aufgabenPlaner.entity;

public class Prioritaet {
	private String bezeichnung;
	private int sortierung;
	
	public Prioritaet(String bezeichnung, int sortierung) {
		this.bezeichnung = bezeichnung;
		this.sortierung = sortierung;
	}
	
	public String getBezeichnung() {
		return bezeichnung;
	}
	
	public int getSortierung() {
		return sortierung;
	}
}
