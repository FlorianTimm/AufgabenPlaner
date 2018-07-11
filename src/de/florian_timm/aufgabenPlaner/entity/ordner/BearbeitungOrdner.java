package de.florian_timm.aufgabenPlaner.entity.ordner;

import de.florian_timm.aufgabenPlaner.entity.Entity;
import de.florian_timm.aufgabenPlaner.entity.Person;
import de.florian_timm.aufgabenPlaner.entity.Projekt;
import de.florian_timm.aufgabenPlaner.kontroll.AufgabenNotifier;
import de.florian_timm.aufgabenPlaner.kontroll.BearbeitungNotifier;
import de.florian_timm.aufgabenPlaner.kontroll.ErrorNotifier;
import de.florian_timm.aufgabenPlaner.kontroll.ProjektNotifier;
import de.florian_timm.aufgabenPlaner.schnittstelle.DatenHaltung;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.florian_timm.aufgabenPlaner.entity.Aufgabe;
import de.florian_timm.aufgabenPlaner.entity.Bearbeitung;

public class BearbeitungOrdner extends Ordner {
	private Aufgabe aufgabe;
	private static Map<Integer, BearbeitungOrdner> bearbeitungsListen = new HashMap<Integer, BearbeitungOrdner>();

	public static Map<Integer, BearbeitungOrdner> getAllBearbeitungOrdner() {
		return bearbeitungsListen;
	}

	private BearbeitungOrdner(Aufgabe aufgabe) {
		alle = new HashMap<Integer, Entity>();
		notifier = BearbeitungNotifier.getInstanz();
		this.aufgabe = aufgabe;
	}

	public static BearbeitungOrdner getInstanz(Aufgabe aufgabe) {
		if (bearbeitungsListen.containsKey(aufgabe.getId())) {
			return bearbeitungsListen.get(aufgabe.getId());
		} else {
			BearbeitungOrdner al = new BearbeitungOrdner(aufgabe);
			bearbeitungsListen.put(aufgabe.getId(), al);
			return al;
		}
	}

	@Override
	protected boolean loadData() {
		String sql = "SELECT a.projekt, b.* FROM bearbeitung b LEFT JOIN aufgabe a ON b.aufgabe = a.id where a.id = ? AND ";
		if (firstLoaded) {
			sql += "(b.bearbeitet > ? OR b.geloescht > ? OR b.storniert > ?);";
		} else {
			sql += "b.geloescht IS NULL AND b.storniert IS NULL;";
		}
		DatenHaltung d = new DatenHaltung();
		d.prepareStatement(sql);
		d.setInt(1, aufgabe.getId());
		if (firstLoaded) {
			d.setLong(2, lastUpdate);
			d.setLong(3, lastUpdate);
			d.setLong(4, lastUpdate);
		}
		boolean dataChanged = false;

		while (d.next()) {
			long geloescht = d.getLong("geloescht");
			long storniert = d.getLong("storniert");
			long bearbeitet = d.getLong("bearbeitet");
			int id = d.getInt("id");

			if (geloescht > 0 || storniert > 0) {
				if (alle.containsKey(id)) {
					if (this.remove(id))
						dataChanged = true;
				}
			} else {
				Bearbeitung p = (Bearbeitung) getEntityFromResult(d);
				if (this.add(p)) {
					dataChanged = true;
				}
			}
			lastUpdate = Math.max(Math.max(lastUpdate, geloescht), Math.max(bearbeitet, storniert));
		}

		if (dataChanged && firstLoaded) {
			notifier.informListener();
		}
		firstLoaded = true;
		return dataChanged;
	}

	private static Bearbeitung getEntityFromResult(DatenHaltung rs) {
		int dbId = rs.getInt("id");
		Person bearbeiter = PersonenOrdner.getInstanz().getPerson(rs.getInt("bearbeiter"));
		String bemerkung = rs.getString("bemerkung");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd H:m");
		Date start = null;
		try {
			if (rs.getString("start") != null)
				start = df.parse(rs.getString("start"));
		} catch (NullPointerException | ParseException e) {
			ErrorNotifier.log(e);
		}
		Duration dauer = null;
		if (rs.getString("dauer") != null)
			dauer = Duration.ofMinutes(rs.getLong("dauer"));

		Aufgabe aufgabe = (Aufgabe) ProjektOrdner.getInstanz().get(rs.getInt("projekt")).getAufgaben()
				.get(rs.getInt("aufgabe"));

		Bearbeitung bearbeitung = new Bearbeitung(dbId, aufgabe, bearbeiter, start, dauer, bemerkung);
		return bearbeitung;
	}

	@Override
	public void removeFromDB(int id) {
		long time = Instant.now().getEpochSecond();
		DatenHaltung d1 = new DatenHaltung(true);
		d1.prepareStatement("UPDATE bearbeitung SET geloescht = ?, bearbeitetVon = ? WHERE id = ?;");
		d1.setLong(1, time);
		d1.setInt(2, PersonenOrdner.getInstanz().getNutzer().getId());
		d1.setInt(3, id);
		d1.update();

		AufgabenNotifier.getInstanz().informListener();
		ProjektNotifier.getInstanz().informListener();
	}

	public static void createTable() {
		new DatenHaltung(true).update("CREATE TABLE IF NOT EXISTS bearbeitung (id INTEGER PRIMARY KEY, "
				+ "aufgabe INTEGER NOT NULL, bearbeiter INTEGER NOT NULL, start	TEXT NOT NULL, dauer INTEGER NULL,"
				+ "bemerkung TEXT, erstellt INTEGER, bearbeitet INTEGER, "
				+ "geloescht INTEGER, storniert INTEGER, bearbeitetVon INTEGER, "
				+ "FOREIGN KEY (bearbeiter) REFERENCES person(id)," + "FOREIGN KEY (aufgabe) REFERENCES aufgabe(id),"
				+ "FOREIGN KEY (bearbeitetVon) REFERENCES person(id));");
	}

	@Override
	protected void alertNew(Entity p) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void alertChanged(Entity p) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void alertRemoved(Entity p) {
		// TODO Auto-generated method stub

	}

	public void updateDB(Bearbeitung bearbeitung) {
		String sql = "UPDATE bearbeitung SET start = ?, dauer = ?, bearbeiter = ?, bemerkung = ?, bearbeitet = ?, bearbeitetVon = ? "
				+ "WHERE id = ?;";
		System.out.println("Aufgabenupdate");
		DatenHaltung d = new DatenHaltung(true);
		d.prepareStatement(sql);
		d.setString(1, new SimpleDateFormat("yyyy-MM-dd H:m").format(bearbeitung.getStart()));
		if (bearbeitung.getDauer() != null)
			d.setLong(2, bearbeitung.getDauer().getSeconds() / 60);
		d.setInt(3, bearbeitung.getBearbeiter().getId());
		d.setString(4, bearbeitung.getBemerkung());
		long time = Instant.now().getEpochSecond();
		d.setLong(5, time);
		d.setInt(6, PersonenOrdner.getInstanz().getNutzer().getId());
		d.setInt(7, bearbeitung.getId());
		d.update();
		notifier.informListener();
	}

	public void makeAufgabe(Aufgabe aufgabe, Person bearbeiter, Date start, Duration dauer, String bemerkung) {
		String sql = "INSERT INTO bearbeitung (aufgabe, start, dauer, bearbeiter, bemerkung, erstellt, bearbeitet, bearbeitetVon) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
		System.out.println("Aufgabenupdate");
		DatenHaltung d = new DatenHaltung(true);
		d.prepareStatement(sql);
		d.setInt(1, aufgabe.getId());
		d.setString(2, new SimpleDateFormat("yyyy-MM-dd H:m").format(start));
		if (dauer != null)
			d.setLong(3, dauer.getSeconds() / 60);
		d.setInt(4, bearbeiter.getId());
		d.setString(5, bemerkung);
		long time = Instant.now().getEpochSecond();
		d.setLong(6, time);
		d.setLong(7, time);
		d.setInt(8, PersonenOrdner.getInstanz().getNutzer().getId());
		d.update();
		notifier.informListener();

	}

	public Map<Integer, Entity> getBearbeitungen() {
		loadData();
		return alle;
	}

	public static List<Bearbeitung> getBearbeitungen(Person person, int limit) {
		List<Bearbeitung> bearbeitung = new ArrayList<Bearbeitung>();
		String sql = "SELECT a.projekt, b.* FROM bearbeitung b LEFT JOIN aufgabe a ON b.aufgabe = a.id LEFT JOIN status ON a.status = status.id where a.geloescht IS NULL AND a.storniert IS NULL AND status.sortierung < 100 AND b.bearbeiter = ? ORDER BY b.start DESC";
		if (limit != -1) {
			sql += " LIMIT " + limit;
		}
		sql += ";";

		DatenHaltung c = new DatenHaltung();
		c.prepareStatement(sql);
		c.setInt(1, person.getId());

		while (c.next()) {
			int projektId = c.getInt("projekt");
			Projekt projekt = ProjektOrdner.getInstanz().get(projektId);
			projekt.getId();
			Bearbeitung b = (Bearbeitung) getEntityFromResult(c);
			BearbeitungOrdner.getInstanz(b.getAufgabe()).add(b);
			bearbeitung.add(b);
		}
		return bearbeitung;
	}

}
