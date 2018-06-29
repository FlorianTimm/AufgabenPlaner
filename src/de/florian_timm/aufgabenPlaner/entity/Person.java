package de.florian_timm.aufgabenPlaner.entity;

import de.florian_timm.aufgabenPlaner.kontroll.ErrorHub;
import de.florian_timm.aufgabenPlaner.schnittstelle.DatenhaltungS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Person extends Entity implements Comparable<Person> {
	private static Map<Integer, Person> alle = new HashMap<Integer, Person>();
	private String username;
	private String name;
	private String email;
	private String vorname;
	private static int nutzerId;

	public static Person getNutzer() {
		return getPerson(nutzerId);
	}

	public static void setNutzer(Person nutzer) {
		Person.nutzerId = nutzer.getId();
	}

	public Person(String username, String name, String vorname, String email, int id) {
		this.username = username;
		this.vorname = vorname;
		this.name = name;
		this.email = email;
		this.dbId = id;
	}

	public static Person getPerson(String username) {
		return loadPersonFromDB("username = '" + username + "'");
	}

	public static Person getPerson(int id) {
		if (alle.containsKey(id)) {
			return alle.get(id);
		} else {
			Person p = loadPersonFromDB("id = " + id);
			Person.informListener();
			return p;

		}
	}

	public static Person loadPersonFromDB(String filter) {
		try {
			String sql = "SELECT * FROM person where " + filter + ";";
			System.out.println(sql);
			ResultSet rs = DatenhaltungS.query(sql);

			if (rs != null && rs.next()) {
				return getPersonFromResult(rs);
			}
			rs.close();
		} catch (SQLException e) {
			ErrorHub.log(e);
		}

		return null;
	}

	private static Person getPersonFromResult(ResultSet rs) throws SQLException {
		int id = rs.getInt("id");
		String name = rs.getString("name");
		String vorname = rs.getString("vorname");
		String username = rs.getString("username");
		String email = rs.getString("email");
		Person p = new Person(username, name, vorname, email, id);
		alle.put(id, p);
		return p;
	}

	private static void loadData() {
		alle.clear();
		try {
			ResultSet rs = DatenhaltungS.query("SELECT * FROM person;");
			while (rs.next()) {
				getPersonFromResult(rs);
			}
			rs.close();
		} catch (SQLException e) {
			ErrorHub.log(e);
		}
		informListener();
	}

	public static Person makePerson(String username, String name, String vorname, String email) throws SQLException {

		DatenhaltungS.update("INSERT INTO person (name, vorname, username, email) VALUES ('" + name + "','" + vorname
				+ "','" + username + "','" + email + "');");

		Person p = getPerson(username);
		informListener();
		return p;

	}

	public static void createTable() throws SQLException {
		DatenhaltungS.update("CREATE TABLE IF NOT EXISTS person (" + "id INTEGER PRIMARY KEY, "
				+ "username	TEXT UNIQUE, name TEXT NOT NULL, vorname TEXT, email TEXT, CONSTRAINT nameEinzigartig UNIQUE(name, vorname));");
	}

	public static Person[] getArray() {
		checkLoading();
		Person[] p = alle.values().toArray(new Person[0]);
		Arrays.sort(p);
		return p;
	}

	protected static void checkLoading() {
		// da unsicher, ob alle Einträge immer vorhanden sind, wird immer neu geladen
		loadData();
	}

	public String getUserName() {
		return username;
	}

	public String getNachName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	@Override
	public String toString() {
		return getNachName() + ((getVorname() != null) ? ", " + getVorname() : "");
	}

	@Override
	public int compareTo(Person other) {
		return this.toString().compareTo(other.toString());
	}

	public String getVorname() {
		return this.vorname;
	}

	public void update(String username, String name, String vorname, String email) {
		String sql = "UPDATE person SET username = ?, name = ?, vorname = ?, email = ? WHERE id = ?;";

		Connection c = DatenhaltungS.getConnection();
		try {
			PreparedStatement stmt = c.prepareStatement(sql);
			stmt.setString(1, username);
			stmt.setString(2, name);
			stmt.setString(3, vorname);
			stmt.setString(4, email);
			stmt.setInt(5, this.getId());
			stmt.executeUpdate();
			stmt.close();

		} catch (SQLException e) {
			ErrorHub.log(e);
		}
		informListener();
	}
}
