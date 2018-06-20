package de.florian_timm.aufgabenPlaner.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.florian_timm.aufgabenPlaner.PersonNotFoundException;

public class Person {
	private String username ;
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
			e.printStackTrace();
		}

		return getPerson(username);
	}
}
