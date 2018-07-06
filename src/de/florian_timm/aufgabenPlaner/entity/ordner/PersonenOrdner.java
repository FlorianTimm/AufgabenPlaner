package de.florian_timm.aufgabenPlaner.entity.ordner;

import java.sql.SQLException;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import de.florian_timm.aufgabenPlaner.entity.Entity;
import de.florian_timm.aufgabenPlaner.entity.Person;
import de.florian_timm.aufgabenPlaner.kontroll.PersonenNotifier;
import de.florian_timm.aufgabenPlaner.schnittstelle.DatenHaltung;

public class PersonenOrdner extends Ordner {
	private Person nutzer;

	private long lastUpdate = 0;

	private static PersonenOrdner instanz = null;

	private PersonenOrdner() {
		alle = new HashMap<Integer, Entity>();
		notifier = PersonenNotifier.getInstanz();
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
		loadData();
		String sql = "SELECT id FROM person where upper(username) == upper(?) and geloescht IS null;";
		DatenHaltung d = new DatenHaltung();
		d.prepareStatement(sql);
		d.setString(1, username);

		if (d.next()) {
			int id = d.getInt("id");
			return getPerson(id);
		}
		d.close();
		return null;
	}

	public Person getPerson(int id) {
		if (!alle.containsKey(id)) {
			loadData();
		}
		return (Person) alle.get(id);
	}

	private Person getEntityFromResult(DatenHaltung rs) {
		int id = rs.getInt("id");
		String name = rs.getString("name");
		String vorname = rs.getString("vorname");
		String username = rs.getString("username");
		String email = rs.getString("email");
		Person p = new Person(id, username, name, vorname, email);
		return p;
	}

	public boolean loadData() {
		boolean dataChanged = false;
		DatenHaltung d = new DatenHaltung();
		d.prepareStatement("SELECT * FROM person WHERE bearbeitet > ? OR geloescht > ?;");
		d.setLong(1, lastUpdate);
		d.setLong(2, lastUpdate);
		while (d.next()) {
			long geloescht = d.getLong("geloescht");
			long bearbeitet = d.getLong("bearbeitet");
			int id = d.getInt("id");
			if (geloescht > 0) {
				if (alle.containsKey(id)) {
					if (this.remove(id))
						dataChanged = true;
				}
			} else {
				Person p = (Person) getEntityFromResult(d);
				if (this.add(p)) {
					dataChanged = true;
				}
			}
			lastUpdate = Math.max(Math.max(lastUpdate, geloescht), bearbeitet);

		}
		if (dataChanged && firstLoaded) {
			notifier.informListener();
		}
		d.close();
		firstLoaded = true;
		return dataChanged;
	}

	public Person makePerson(String username, String name, String vorname, String email) throws SQLException {
		long time = Instant.now().getEpochSecond();
		new DatenHaltung(true).update("INSERT INTO person (name, vorname, username, email, bearbeitet) VALUES ('" + name
				+ "','" + vorname + "','" + username + "','" + email + "', " + time + ");");

		Person p = getPerson(username);
		PersonenNotifier.getInstanz().informListener();
		return p;

	}

	public static void createTable() throws SQLException {
		new DatenHaltung(true).update("CREATE TABLE IF NOT EXISTS person (" + "id INTEGER PRIMARY KEY, "
				+ "username	TEXT UNIQUE, name TEXT NOT NULL, vorname TEXT, email TEXT, bearbeitet INTEGER NOT NULL, "
				+ "geloescht INTEGER, CONSTRAINT nameEinzigartig UNIQUE(name, vorname));");
	}

	public Person[] getArray() {
		System.out.println("Anzahl Personen: " + alle.size());
		Person[] p = alle.values().toArray(new Person[0]);
		Arrays.sort(p);
		return p;
	}

	public void update(Person person) {
		String sql = "UPDATE person SET name = ?, vorname = ?, email = ?, bearbeitet = ? WHERE id = ?;";

		DatenHaltung d = new DatenHaltung(true);
		d.prepareStatement(sql);
		d.setString(1, person.getNachName());
		d.setString(2, person.getVorname());
		d.setString(3, person.getEmail());
		long time = Instant.now().getEpochSecond();
		d.setLong(4, time);
		d.setInt(5, person.getId());
		d.update();

		PersonenNotifier.getInstanz().informListener();
	}

	@Override
	public void removeFromDB(int id) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void alertNew(Entity p) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void alertChanged(Entity p) {
		// TODO Auto-generated method stub

	}
}
