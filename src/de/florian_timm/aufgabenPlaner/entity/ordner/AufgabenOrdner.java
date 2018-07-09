package de.florian_timm.aufgabenPlaner.entity.ordner;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.florian_timm.aufgabenPlaner.SystemLeistenIcon;
import de.florian_timm.aufgabenPlaner.entity.Aufgabe;
import de.florian_timm.aufgabenPlaner.entity.Entity;
import de.florian_timm.aufgabenPlaner.entity.Person;
import de.florian_timm.aufgabenPlaner.entity.Projekt;
import de.florian_timm.aufgabenPlaner.entity.Status;
import de.florian_timm.aufgabenPlaner.kontroll.AufgabenNotifier;
import de.florian_timm.aufgabenPlaner.kontroll.ErrorNotifier;
import de.florian_timm.aufgabenPlaner.kontroll.ProjektNotifier;
import de.florian_timm.aufgabenPlaner.schnittstelle.DatenHaltung;

public class AufgabenOrdner extends Ordner {
	private Projekt projekt;
	private static Map<Integer, AufgabenOrdner> aufgabenListen = new HashMap<Integer, AufgabenOrdner>();

	public static Map<Integer, AufgabenOrdner> getAllAufgabenListen() {
		return aufgabenListen;
	}

	private AufgabenOrdner(Projekt projekt) {
		alle = new HashMap<Integer, Entity>();
		notifier = AufgabenNotifier.getInstanz();
		this.projekt = projekt;
	}

	public static AufgabenOrdner getInstanz(Projekt projekt) {
		projekt.getId();
		if (aufgabenListen.containsKey(projekt.getId())) {
			return aufgabenListen.get(projekt.getId());
		} else {
			AufgabenOrdner al = new AufgabenOrdner(projekt);
			aufgabenListen.put(projekt.getId(), al);
			return al;
		}
	}

	public static void removeListe(int id) {
		aufgabenListen.get(id).removeAll();
		aufgabenListen.remove(id);
	}

	public Map<Integer, Entity> getAufgaben() {
		loadData();
		return alle;
	}

	public boolean loadData() {
		String sql = "SELECT * FROM aufgabe where projekt = ? AND ";
		if (firstLoaded) {
			sql += "(bearbeitet > ? OR geloescht > ? OR storniert > ?);";
		} else {
			sql += "geloescht IS NULL AND storniert IS NULL;";
		}
		DatenHaltung d = new DatenHaltung();
		d.prepareStatement(sql);
		d.setInt(1, projekt.getId());
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
				Aufgabe p = (Aufgabe) getEntityFromResult(d);
				if (this.add(p)) {
					dataChanged = true;
				}
			}
			lastUpdate = Math.max(Math.max(lastUpdate, geloescht), Math.max(bearbeitet, storniert));
		}

		//System.out.println("Anzahl Aufgaben: " + alle.size());

		if (dataChanged && firstLoaded) {
			notifier.informListener();
		}
		firstLoaded = true;
		return dataChanged;
	}

	public static List<Aufgabe> getOffeneAufgaben(Person person) {
		List<Aufgabe> aufgaben = new ArrayList<Aufgabe>();
		String sql = "SELECT a.* FROM aufgabe a LEFT JOIN status ON a.status = status.id where a.geloescht IS NULL AND a.storniert IS NULL AND status.sortierung < 100 AND a.person = ?;";
		DatenHaltung c = new DatenHaltung();
		c.prepareStatement(sql);
		c.setInt(1, person.getId());

		while (c.next()) {
			int projektId = c.getInt("projekt");
			Projekt projekt = ProjektOrdner.getInstanz().get(projektId);
			projekt.getId();
			Aufgabe a = (Aufgabe) getEntityFromResult(c);
			AufgabenOrdner.getInstanz(projekt).add(a);
			aufgaben.add(a);
		}
		return aufgaben;
	}

	private static Entity getEntityFromResult(DatenHaltung rs) {
		int dbId = rs.getInt("id");
		Projekt projekt = ProjektOrdner.getInstanz().get(rs.getInt("projekt"));
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
		Status status = StatusOrdner.getInstanz().getStatus(rs.getInt("status"));
		Person bearbeitetVon = PersonenOrdner.getInstanz().getPerson(rs.getInt("bearbeitetVon"));

		Aufgabe a = new Aufgabe(dbId, bearbeiter, titel, beschreibung, faelligkeit, status, projekt, bearbeitetVon);
		return a;

	}

	public static void createTable() {
		new DatenHaltung(true).update("CREATE TABLE IF NOT EXISTS aufgabe (" + "id INTEGER PRIMARY KEY, "
				+ "projekt INTEGER NOT NULL, person INTEGER NOT NULL, titel	TEXT NOT NULL, "
				+ "beschreibung TEXT NOT NULL, faelligkeit DATE, erstellt INTEGER, bearbeitet INTEGER, "
				+ "geloescht INTEGER, storniert INTEGER, bearbeitetVon INTEGER, status INTEGER NOT NULL, "
				+ "FOREIGN KEY (person) REFERENCES person(id),"
				+ "FOREIGN KEY (projekt) REFERENCES projekt(id), FOREIGN KEY (status) REFERENCES status(id), FOREIGN KEY (bearbeitetVon) REFERENCES person(id));");
	}

	public void makeAufgabe(Projekt projekt, String titel, String beschreibung, Person bearbeiter, Date faelligkeit,
			Status status) {
		String sql = "INSERT INTO aufgabe (titel, beschreibung, person, faelligkeit, status, projekt, erstellt, bearbeitet, bearbeitetVon) VALUES (?,?,?,?,?, ?, ?, ?, ?);";

		DatenHaltung d = new DatenHaltung(true);
		d.prepareStatement(sql);
		d.setString(1, titel);
		d.setString(2, beschreibung);
		d.setInt(3, bearbeiter.getId());
		d.setString(4, new SimpleDateFormat("yyyy-MM-dd").format(faelligkeit));
		d.setInt(5, status.getId());
		d.setInt(6, projekt.getId());
		long time = Instant.now().getEpochSecond();
		d.setLong(7, time);
		d.setLong(8, time);
		d.setInt(9, PersonenOrdner.getInstanz().getNutzer().getId());
		d.update();

		AufgabenNotifier.getInstanz().informListener();
	}

	public void update(Aufgabe aufgabe) {
		String sql = "UPDATE aufgabe SET titel = ?, beschreibung = ?, person = ?, faelligkeit = ?, status = ?, bearbeitet = ?, bearbeitetVon = ? WHERE id = ?;";
		System.out.println("Aufgabenupdate");
		DatenHaltung d = new DatenHaltung(true);
		d.prepareStatement(sql);
		d.setString(1, aufgabe.getTitel());
		d.setString(2, aufgabe.getBeschreibung());
		d.setInt(3, aufgabe.getBearbeiter().getId());
		d.setString(4, new SimpleDateFormat("yyyy-MM-dd").format(aufgabe.getFaelligkeit()));
		d.setInt(5, aufgabe.getStatus().getId());
		long time = Instant.now().getEpochSecond();
		d.setLong(6, time);
		d.setInt(7, PersonenOrdner.getInstanz().getNutzer().getId());
		d.setInt(8, aufgabe.getId());
		d.update();
		AufgabenNotifier.getInstanz().informListener();
	}

	@Override
	public void removeFromDB(int id) {
		long time = Instant.now().getEpochSecond();
		DatenHaltung d1 = new DatenHaltung(true);
		d1.prepareStatement("UPDATE aufgabe SET geloescht = ?, bearbeitetVon = ? WHERE id = ?;");
		d1.setLong(1, time);
		d1.setInt(2, PersonenOrdner.getInstanz().getNutzer().getId());
		d1.setInt(3, id);
		d1.update();

		AufgabenNotifier.getInstanz().informListener();
		ProjektNotifier.getInstanz().informListener();
	}

	protected void alertNew(Entity p) {
		Aufgabe a = (Aufgabe) p;
		int nid = PersonenOrdner.getInstanz().getNutzer().getId();
		if (a.getBearbeiter().getId() == nid && a.getBearbeitetVon().getId() != nid) {
			SystemLeistenIcon.getInstanz().makeAlert("Neue Aufgabe in " + a.getProjekt().getTitel(),
					"Es wurde eine neue Aufgabe für Sie angelegt:\n" + a.getTitel() + "\n" + a.getBeschreibung());
		}
	}

	@Override
	protected void alertChanged(Entity p) {
		Aufgabe a = (Aufgabe) p;
		int nid = PersonenOrdner.getInstanz().getNutzer().getId();
		if (a.getBearbeiter().getId() == nid && a.getBearbeitetVon().getId() != nid) {
			SystemLeistenIcon.getInstanz().makeAlert("Veränderte Aufgabe in " + a.getProjekt().getTitel(),
					"Es wurde eine ihrer Aufgabe verändert oder Ihnen neu zugewiesen:\n" + a.getTitel() + "\n"
							+ a.getBeschreibung());
		}
	}

	@Override
	protected void alertRemoved(Entity p) {
		Aufgabe a = (Aufgabe) p;
		int nid = PersonenOrdner.getInstanz().getNutzer().getId();
		if (a.getBearbeiter().getId() == nid && a.getBearbeitetVon().getId() != nid) {
			SystemLeistenIcon.getInstanz().makeAlert("Gelöschte Aufgabe in " + a.getProjekt().getTitel(),
					"Es wurde eine ihrer Aufgabe gelöscht:\n" + a.getTitel() + "\n"
							+ a.getBeschreibung());
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((projekt == null) ? 0 : projekt.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AufgabenOrdner other = (AufgabenOrdner) obj;
		if (projekt == null) {
			if (other.projekt != null)
				return false;
		} else if (!projekt.equals(other.projekt))
			return false;
		return true;
	}



}
