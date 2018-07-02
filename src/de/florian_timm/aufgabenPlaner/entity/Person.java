package de.florian_timm.aufgabenPlaner.entity;

import de.florian_timm.aufgabenPlaner.kontroll.EntityListener;
import de.florian_timm.aufgabenPlaner.schnittstelle.DatenhaltungS;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Person extends Entity implements Comparable<Person> {
	private static Map<Integer, Person> alle = new HashMap<Integer, Person>();
	private String username;
	private String name;
	private String email;
	private String vorname;
	private static int nutzerId;
	private static boolean geladen = false;

	private static Set<EntityListener> listener = new HashSet<EntityListener>();

	public static void addListener(EntityListener newListener) {
		listener.add(newListener);
		System.out.println(
				"PersonListener: " + listener.size() + " (neu: " + newListener.getClass().getSimpleName() + ")");
	}

	public static void removeListener(EntityListener el) {
		listener.remove(el);
		System.out.println("PersonListener: " + listener.size() + " (entf: " + el.getClass().getSimpleName() + ")");
	}

	public static void informListener() {
		EntityListener[] ls = listener.toArray(new EntityListener[0]);
		for (EntityListener el : ls) {
			el.dataChanged();
		}
	}

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
		return loadPersonFromDB("upper(username) == upper('" + username + "')");
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
		String sql = "SELECT * FROM person where " + filter + ";";
		DatenhaltungS d = new DatenhaltungS();
		d.query(sql);

		if (d.next()) {
			return getPersonFromResult(d);
		}

		return null;
	}

	private static Person getPersonFromResult(DatenhaltungS rs) {
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
		DatenhaltungS d = new DatenhaltungS();
		d.query("SELECT * FROM person;");
		while (d.next()) {
			getPersonFromResult(d);
		}
		geladen = true;
		informListener();
	}

	public static Person makePerson(String username, String name, String vorname, String email) throws SQLException {

		new DatenhaltungS(true).update("INSERT INTO person (name, vorname, username, email) VALUES ('" + name + "','"
				+ vorname + "','" + username + "','" + email + "');");

		Person p = getPerson(username);
		informListener();
		return p;

	}

	public static void createTable() throws SQLException {
		new DatenhaltungS(true).update("CREATE TABLE IF NOT EXISTS person (" + "id INTEGER PRIMARY KEY, "
				+ "username	TEXT UNIQUE, name TEXT NOT NULL, vorname TEXT, email TEXT, CONSTRAINT nameEinzigartig UNIQUE(name, vorname));");
	}

	public static Person[] getArray() {
		checkLoading();
		Person[] p = alle.values().toArray(new Person[0]);
		Arrays.sort(p);
		return p;
	}

	protected static void checkLoading() {
		if (!geladen)
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

		DatenhaltungS d = new DatenhaltungS(true);
		d.prepareStatement(sql);
		d.setString(1, username);
		d.setString(2, name);
		d.setString(3, vorname);
		d.setString(4, email);
		d.setInt(5, this.getId());
		d.update();

		informListener();
	}
}
