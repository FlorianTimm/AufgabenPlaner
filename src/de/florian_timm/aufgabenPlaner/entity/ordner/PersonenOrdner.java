package de.florian_timm.aufgabenPlaner.entity.ordner;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import de.florian_timm.aufgabenPlaner.entity.Person;
import de.florian_timm.aufgabenPlaner.kontroll.PersonenNotifier;
import de.florian_timm.aufgabenPlaner.schnittstelle.DatenHaltung;

public class PersonenOrdner {
	private Person nutzer;
	private boolean geladen = false;
	private Map<Integer, Person> alle = new HashMap<Integer, Person>();

	private long lastUpdate = 0;

	private static PersonenOrdner instanz = null;

	private PersonenOrdner() {
		alle = new HashMap<Integer, Person>();
	}

	public static PersonenOrdner getInstanz() {
		if (instanz == null) {
			instanz = new PersonenOrdner();
		}
		return instanz;
	}

	public Person getNutzer() {
		return nutzer;
	}

	public void setNutzer(Person nutzer) {
		this.nutzer = nutzer;
	}

	public Person getPerson(String username) {
		return loadPersonFromDB("upper(username) == upper('" + username + "')");
	}

	public Person getPerson(int id) {
		if (alle.containsKey(id)) {
			return alle.get(id);
		} else {
			Person p = loadPersonFromDB("id = " + id);
			PersonenNotifier.getInstanz().informListener();
			return p;
		}
	}

	public Person loadPersonFromDB(String filter) {
		String sql = "SELECT * FROM person where " + filter + ";";
		DatenHaltung d = new DatenHaltung();
		d.query(sql);

		if (d.next()) {
			return getPersonFromResult(d);
		}

		return null;
	}

	private Person getPersonFromResult(DatenHaltung rs) {
		int id = rs.getInt("id");
		String name = rs.getString("name");
		String vorname = rs.getString("vorname");
		String username = rs.getString("username");
		String email = rs.getString("email");
		Person p = new Person(id, username, name, vorname, email);
		alle.put(id, p);
		return p;
	}

	private void loadData() {
		DatenHaltung d = new DatenHaltung();
		d.query("SELECT * FROM person;");
		while (d.next()) {
			getPersonFromResult(d);
		}
		geladen = true;
		PersonenNotifier.getInstanz().informListener();
	}

	public Person makePerson(String username, String name, String vorname, String email) throws SQLException {

		new DatenHaltung(true).update("INSERT INTO person (name, vorname, username, email) VALUES ('" + name + "','"
				+ vorname + "','" + username + "','" + email + "');");

		Person p = getPerson(username);
		PersonenNotifier.getInstanz().informListener();
		return p;

	}

	public static void createTable() throws SQLException {
		new DatenHaltung(true).update("CREATE TABLE IF NOT EXISTS person (" + "id INTEGER PRIMARY KEY, "
				+ "username	TEXT UNIQUE, name TEXT NOT NULL, vorname TEXT, email TEXT, CONSTRAINT nameEinzigartig UNIQUE(name, vorname));");
	}

	public Person[] getArray() {
		checkLoading();
		Person[] p = alle.values().toArray(new Person[0]);
		Arrays.sort(p);
		return p;
	}

	protected void checkLoading() {
		if (!geladen)
			loadData();
	}

	public void update(Person person) {
		String sql = "UPDATE person SET name = ?, vorname = ?, email = ? WHERE id = ?;";

		DatenHaltung d = new DatenHaltung(true);
		d.prepareStatement(sql);
		d.setString(1, person.getNachName());
		d.setString(2, person.getVorname());
		d.setString(3, person.getEmail());
		d.setInt(4, person.getId());
		d.update();

		PersonenNotifier.getInstanz().informListener();
	}
}
