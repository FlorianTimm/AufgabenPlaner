package de.florian_timm.aufgabenPlaner.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.florian_timm.aufgabenPlaner.DatenhaltungS;

public class Person {
	private String username;
	private String name;
	private String email;
	private int id;

	/**
	 * @param name
	 * @param email
	 * @param id
	 */
	public Person(String username, String name, String email, int id) {
		this.username = username;
		this.name = name;
		this.email = email;
		this.id = id;
	}

	public String getUserName() {
		return username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String toString() {
		return getName() + " <" + getEmail() + ">";
	}

	public static Person getPerson(String username) {
		return getPersonSQL("username = '" + username + "'");
	}

	public static Person getPerson(int id) {
		return getPersonSQL("id = " + id);
	}

	public static Person getPersonSQL(String sql) {
		try {
			sql = "SELECT * FROM person where " + sql + ";";
			System.out.println(sql);
			ResultSet rs = DatenhaltungS.query(sql);

			while (rs.next()) {
				int id = rs.getInt("id");
				String username = rs.getString("username");
				String name = rs.getString("name");
				String email = rs.getString("email");
				rs.close();
				return new Person(username, name, email, id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static List<Person> getPersonen() {
		List<Person> personen = null;
		try {
			ResultSet rs = DatenhaltungS.query("SELECT * FROM person;");

			personen = new ArrayList<Person>();
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String username = rs.getString("username");
				String email = rs.getString("email");
				personen.add(new Person(username, name, email, id));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return personen;
	}

	public static Person newPerson(String username, String name, String email) {
		DatenhaltungS.update("INSERT INTO person (name, username, email) VALUES ('" + name + "','" + username + "','"
				+ email + "');");
		return getPerson(username);
	}
	
	public static void createTable() {
		DatenhaltungS.update(
				"CREATE TABLE IF NOT EXISTS person ("
				+ "id INTEGER PRIMARY KEY, "
				+ "username	TEXT NOT NULL, "
				+ "name TEXT NOT NULL, "
				+ "email TEXT);");
	}
}
