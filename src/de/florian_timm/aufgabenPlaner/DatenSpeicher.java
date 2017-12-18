package de.florian_timm.aufgabenPlaner;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatenSpeicher {
	private Connection c = null;
	private Statement stmt = null;

	public DatenSpeicher(String file) {
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + file);
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Opened database successfully");

		try {
			stmt = c.createStatement();

			String sql = "CREATE TABLE IF NOT EXISTS user (id INTEGER PRIMARY KEY, username	TEXT NOT NULL, name TEXT NOT NULL, email TEXT); "+
					"CREATE TABLE IF NOT EXISTS aufgaben (id INTEGER PRIMARY KEY, titel	TEXT NOT NULL, beschreibung TEXT NOT NULL, auftraggeber INTEGER); "+
					"CREATE TABLE IF NOT EXISTS aufgabenzuordnung (id INTEGER PRIMARY KEY, aufgabenid INTEGER, userid INTEGER); ";
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

	}

	public List<Person> getPersonen() {
		List<Person> personen = null;
		try {
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM user;");

			personen = new ArrayList<Person>();
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String username = rs.getString("username");
				String email = rs.getString("email");
				personen.add(new Person(username, name, email, id));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return personen;
	}

	public Person newPerson(String username, String name, String email) throws PersonNotFoundException {
		try {
			stmt = c.createStatement();
			String sql = "INSERT INTO user (name, username, email) " + "VALUES ('" + name + "','" + username + "','"
					+ email + "');";

			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return getPerson(username);
	}

	public Person getPerson(String username) throws PersonNotFoundException {
		try {
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM user where username = '" + username + "';");

			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String email = rs.getString("email");
				rs.close();
				stmt.close();
				return new Person(username, name, email, id);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		throw new PersonNotFoundException("Person not found!");
	}

	public Person getPerson(int id) {
		Person person = null;

		return person;
	}

	public List<Aufgabe> getAufgaben(String userid) {
		List<Aufgabe> aufgaben = new ArrayList<Aufgabe>(); 
		try {
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM aufgaben, aufgabenzuordnung where aufgaben.id = aufgabenzuordnung.aufgabenid and aufgabenzuordnung.userid = '" + userid + "';");

			while (rs.next()) {
				int id = rs.getInt("id");
				String titel = rs.getString("titel");
				String beschreibung = rs.getString("beschreibung");
				rs.close();
				stmt.close();
				aufgaben.add(new Aufgabe(titel, beschreibung, id));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return aufgaben;
	}

}
