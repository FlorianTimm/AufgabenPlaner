package de.florian_timm.aufgabenPlaner;

import java.util.ArrayList;
import java.util.List;

public class Aufgabe {
	private List<Person> bearbeiter = new ArrayList<Person>();
	private String titel = "";
	private String beschreibung = "";
	private int id = 0;
	
	
	public Aufgabe (String titel, String beschreibung, int id) {
		this.titel = titel;
		this.beschreibung = beschreibung;
		this.id = id;
	}
	
	
	public String getTitel() {
		return titel;
	}

	public void setTitel(String titel) {
		this.titel = titel;
	}
	
	/**
	 * @return the beschreibung
	 */
	public String getBeschreibung() {
		return beschreibung;
	}
	/**
	 * @param Description
	 */
	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}
	/**
	 * Gets the list of workers
	 * @return the workers list
	 */
	public List<Person> getBearbeiter() {
		return bearbeiter;
	}
	/**
	 * Adds a worker to an exercise
	 * @param worker to add
	 */
	public void addBearbeiter(Person bearbeiter) {
		this.bearbeiter.add(bearbeiter);
	}
	
	
}
