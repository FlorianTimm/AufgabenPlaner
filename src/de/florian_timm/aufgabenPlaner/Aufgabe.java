package de.florian_timm.aufgabenPlaner;

import java.util.ArrayList;
import java.util.List;

public class Aufgabe {
	private String beschreibung = "";
	private List<Person> bearbeiter = new ArrayList<Person>();
	
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
