package de.florian_timm.aufgabenPlaner.entity.ordner;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.florian_timm.aufgabenPlaner.entity.Aufgabe;
import de.florian_timm.aufgabenPlaner.entity.Entity;
import de.florian_timm.aufgabenPlaner.entity.Person;
import de.florian_timm.aufgabenPlaner.entity.Projekt;
import de.florian_timm.aufgabenPlaner.entity.Status;
import de.florian_timm.aufgabenPlaner.kontroll.AufgabenNotifier;
import de.florian_timm.aufgabenPlaner.kontroll.ErrorNotifier;
import de.florian_timm.aufgabenPlaner.schnittstelle.DatenHaltung;

public class AufgabenOrdner extends Ordner {
	private Projekt projekt;
	private static Map<Integer, AufgabenOrdner> aufgabenListen = new HashMap<Integer, AufgabenOrdner>();

	private AufgabenOrdner(Projekt projekt) {
		alle = new HashMap<Integer, Entity>();
		this.projekt = projekt;
	}

	public static AufgabenOrdner getInstanz(Projekt projekt) {
		if (aufgabenListen.containsKey(projekt.getId())) {
			return aufgabenListen.get(projekt.getId());
		} else {
			AufgabenOrdner al = new AufgabenOrdner(projekt);
			aufgabenListen.put(projekt.getId(), al);
			return al;
		}
	}

	public Map<Integer, Entity> getAufgaben() {
		loadData();
		return alle;
	}

	public boolean loadData() {
		String sql = "SELECT * FROM aufgabe where projekt = ?;";
		DatenHaltung d = new DatenHaltung();
		d.prepareStatement(sql);
		d.setInt(1, projekt.getId());
		boolean dataChanged = false;
		
		while (d.next()) {
			long geloescht = d.getLong("geloescht");
			long archiviert = d.getLong("archiviert");
			long bearbeitet = d.getLong("bearbeitet");
			int id = d.getInt("id");
			System.out.println(d.getString("titel"));

			if (geloescht > 0 || archiviert > 0) {
				if (alle.containsKey(id)) {
					if (this.remove(id))
						dataChanged = true;
				}
			} else {
				Projekt p = (Projekt) getEntityFromResult(d);
				if (this.add(p)) {
					dataChanged = true;
				}
			}
			lastUpdate = Math.max(Math.max(lastUpdate, geloescht), Math.max(bearbeitet, archiviert));
		}
		System.out.println("Anzahl Aufgaben: " + alle.size());
		
		if (dataChanged) {
			notifier.informListener();
			return true;
		}
		return false;
	}

	public static List<Aufgabe> getOffeneAufgaben(Person person) {
		List<Aufgabe> aufgaben = new ArrayList<Aufgabe>();
		String sql = "SELECT aufgabe.* FROM aufgabe LEFT JOIN status ON aufgabe.status = status.id where status.sortierung < 100 AND person = ?;";
		DatenHaltung c = new DatenHaltung();
		c.prepareStatement(sql);
		c.setInt(1, person.getId());

		while (c.next()) {
			int projektId = c.getInt("projekt");
			Projekt projekt = ProjektOrdner.getInstanz().get(projektId);
			Aufgabe a = (Aufgabe) getEntityFromResult(c);
			AufgabenOrdner.getInstanz(projekt).add(a);
			aufgaben.add(a);
		}
		System.out.println("Anzahl Aufgaben: " + aufgaben.size());
		return aufgaben;
	}

	private static Entity getEntityFromResult(DatenHaltung rs) {
		int dbId = rs.getInt("id");
		int projektId = rs.getInt("projekt");
		Person bearbeiter = PersonenOrdner.getInstanz().getPerson(rs.getInt("person"));
		String titel = rs.getString("titel");
		String beschreibung = rs.getString("beschreibung");
		Date faelligkeit = null;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if (rs.getString("faelligkeit") != null)
				faelligkeit = df.parse(rs.getString("faelligkeit"));
		} catch (NullPointerException | ParseException e) {
			ErrorNotifier.log(e);
		}
		Status status = Status.getStatus(rs.getInt("status"));

		Aufgabe a = new Aufgabe(dbId, bearbeiter, titel, beschreibung, faelligkeit, status,
				ProjektOrdner.getInstanz().get(projektId));
		return a;

	}

	public static void createTable() {
		new DatenHaltung(true).update("CREATE TABLE IF NOT EXISTS aufgabe (" + "id INTEGER PRIMARY KEY, "
				+ "projekt INTEGER NOT NULL," + "person INTEGER NOT NULL," + "titel	TEXT NOT NULL, "
				+ "beschreibung TEXT NOT NULL, " + "faelligkeit DATE," + "erstellt DATE NOT NULL,"
				+ "status INTEGER NOT NULL," + "storniert BOOLEAN," + "FOREIGN KEY (person) REFERENCES person(id),"
				+ "FOREIGN KEY (projekt) REFERENCES projekt(id)," + "FOREIGN KEY (status) REFERENCES status(id)"
				+ ");");
	}

	public void makeAufgabe(Projekt projekt, String titel, String beschreibung, Person bearbeiter, Date faelligkeit,
			Status status) {
		String sql = "INSERT INTO aufgabe (titel, beschreibung, person, faelligkeit, status, projekt, erstellt) VALUES (?,?,?,?,?, ?, ?);";

		DatenHaltung d = new DatenHaltung(true);
		d.prepareStatement(sql);
		d.setString(1, titel);
		d.setString(2, beschreibung);
		d.setInt(3, bearbeiter.getId());
		d.setString(4, new SimpleDateFormat("yyyy-MM-dd").format(faelligkeit));
		d.setInt(5, status.getId());
		d.setInt(6, projekt.getId());
		d.setString(7, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		d.update();

		AufgabenNotifier.getInstanz().informListener();
	}

	public void update(Aufgabe aufgabe) {
		String sql = "UPDATE aufgabe SET titel = ?, beschreibung = ?, person = ?, faelligkeit = ?, status = ? WHERE id = ?;";

		DatenHaltung d = new DatenHaltung(true);
		d.prepareStatement(sql);
		d.setString(1, aufgabe.getTitel());
		d.setString(2, aufgabe.getBeschreibung());
		d.setInt(3, aufgabe.getBearbeiter().getId());
		d.setString(4, new SimpleDateFormat("yyyy-MM-dd").format(aufgabe.getFaelligkeit()));
		d.setInt(5, aufgabe.getStatus().getId());
		d.setInt(6, aufgabe.getId());
		d.update();
		AufgabenNotifier.getInstanz().informListener();
	}

}
