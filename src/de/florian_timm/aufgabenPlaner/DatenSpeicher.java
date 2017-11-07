package de.florian_timm.aufgabenPlaner;

import java.sql.*;

public class DatenSpeicher {
	private Connection c = null;

	public DatenSpeicher(String file) {
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:"+file);
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Opened database successfully");
	}
	
	public Person[] getPersonen() {
		Person[] personen = null;
		
		return personen;
	}
	
	public Person getPerson(int id) {
		Person person = null;
		
		return person;
	}
	
	public Aufgabe[] getAufgaben() {
		Aufgabe[] aufgaben = null;
		
		return aufgaben;
	}
	
}

