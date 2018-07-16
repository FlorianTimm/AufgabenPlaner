package de.florian_timm.aufgabenPlaner.entity.ordner;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import de.florian_timm.aufgabenPlaner.entity.Kostentraeger;
import de.florian_timm.aufgabenPlaner.entity.Person;
import de.florian_timm.aufgabenPlaner.entity.Prioritaet;
import de.florian_timm.aufgabenPlaner.entity.Projekt;
import de.florian_timm.aufgabenPlaner.SystemLeistenIcon;
import de.florian_timm.aufgabenPlaner.entity.Entity;
import de.florian_timm.aufgabenPlaner.kontroll.AufgabenNotifier;
import de.florian_timm.aufgabenPlaner.kontroll.ProjektNotifier;
import de.florian_timm.aufgabenPlaner.schnittstelle.DatenHaltung;

public class ProjektOrdner extends Ordner {
	private static ProjektOrdner instanz = null;

	private ProjektOrdner() {
		alle = new HashMap<Integer, Entity>();
		notifier = ProjektNotifier.getInstanz();
	}

	public static ProjektOrdner getInstanz() {
		if (instanz == null) {
			instanz = new ProjektOrdner();
		}
		return instanz;
	}

	public static Projekt[] getByUser(Person person, int limit, boolean showReady) {
		List<Projekt> projekte = new ArrayList<Projekt>();
		String sql = "SELECT p.*, AVG(s.sortierung) as status FROM projekt as p LEFT JOIN aufgabe as a ON a.geloescht IS NULL AND a.storniert IS NULL AND p.id = a.projekt LEFT JOIN status as s ON a.status = s.id WHERE p.geloescht IS NULL AND p.archiviert IS NULL AND (p.auftraggeber = ? OR p.zustaendig = ?) GROUP BY p.id";
		if (!showReady) {
			sql += " HAVING AVG(s.sortierung) < 100 OR COUNT(s.sortierung) = 0";
		}
		sql += " ORDER BY faelligkeit ASC";

		if (limit != -1) {
			sql += " LIMIT " + limit;
		}
		sql += ";";
		DatenHaltung d = new DatenHaltung();
		d.prepareStatement(sql);
		d.setInt(1, person.getId());
		d.setInt(2, person.getId());

		while (d.next()) {
			Projekt p = (Projekt) getEntityFromResult(d);
			ProjektOrdner.getInstanz().add(p);
			projekte.add(p);
		}

		return projekte.toArray(new Projekt[0]);
	}

	public Projekt get(int projektId) {
		loadData();
		return (Projekt) alle.get(projektId);
	}

	public static void createTable() {
		new DatenHaltung(true).update("CREATE TABLE projekt (id INTEGER NOT NULL , titel TEXT NOT NULL,"
				+ "	beschreibung TEXT, zustaendig INTEGER NOT NULL, prioritaet INTEGER NOT NULL,"
				+ "	faelligkeit DATE, kostentraeger INTEGER, auftraggeber INTEGER, ordner TEXT,"
				+ "	erstellt INTEGER NOT NULL, bearbeitet INTEGER NOT NULL, geloescht INTEGER,"
				+ "	archiviert INTEGER, bearbeitetVon INTEGER NOT NULL, PRIMARY KEY(id),"
				+ "	FOREIGN KEY(auftraggeber) REFERENCES person(id),"
				+ "	FOREIGN KEY(kostentraeger) REFERENCES kostentraeger(id),"
				+ "	FOREIGN KEY(zustaendig) REFERENCES person(id),"
				+ "	FOREIGN KEY (prioritaet) REFERENCES prioritaet(id),"
				+ " FOREIGN KEY(bearbeitetVon) REFERENCES person(id));");
	}

	public Projekt[] getArray() {
		loadData();
		Projekt[] p = alle.values().toArray(new Projekt[0]);
		return p;
	}

	public boolean loadData() {
		boolean dataChanged = false;
		DatenHaltung d = new DatenHaltung();
		String sql = "SELECT p.*, AVG(s.sortierung) as status FROM projekt as p LEFT JOIN aufgabe as a ON a.geloescht IS NULL AND a.storniert IS NULL AND p.id = a.projekt LEFT JOIN status as s ON a.status = s.id WHERE ";
		if (firstLoaded) {
			sql += "p.bearbeitet > ? OR p.geloescht > ? OR p.archiviert > ? ";

		} else {
			sql += "p.geloescht IS NULL AND p.archiviert IS NULL ";
		}
		sql += "GROUP BY p.id;";
		d.prepareStatement(sql);

		if (firstLoaded) {
			d.setLong(1, lastUpdate);
			d.setLong(2, lastUpdate);
			d.setLong(3, lastUpdate);
		}

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
		if (dataChanged && firstLoaded) {
			notifier.informListener();
		}
		firstLoaded = true;
		return dataChanged;
	}

	public void makeProjekt(String titel, String beschreibung, Prioritaet prio, Person zustaendig,
			Kostentraeger kostentraeger, Date faelligkeit, Person auftraggeber, File ordner) {
		String sql = "INSERT INTO projekt (titel, beschreibung, prioritaet, zustaendig, kostentraeger, faelligkeit, auftraggeber, ordner, erstellt, bearbeitet, bearbeitetVon) VALUES (?,?,?,?,?,?,?,?,?,?,?);";

		DatenHaltung c = new DatenHaltung(true);
		c.prepareStatement(sql);
		c.setString(1, titel);
		c.setString(2, beschreibung);
		c.setInt(3, prio.getId());
		c.setInt(4, zustaendig.getId());
		c.setInt(5, kostentraeger.getId());
		c.setString(6, new SimpleDateFormat("yyyy-MM-dd").format(faelligkeit));
		c.setInt(7, auftraggeber.getId());
		if (ordner != null) {
			c.setString(8, ordner.getAbsolutePath());
		}
		long time = Instant.now().getEpochSecond();
		c.setLong(9, time);
		c.setLong(10, time);
		c.setInt(11, PersonenOrdner.getInstanz().getNutzer().getId());
		c.update();

		loadData();
		ProjektNotifier.getInstanz().informListener();
	}

	private static Entity getEntityFromResult(DatenHaltung rs) {
		int dbId = rs.getInt("id");
		String titel = rs.getString("titel");
		String beschreibung = rs.getString("beschreibung");
		Person zustaendig = PersonenOrdner.getInstanz().getPerson(rs.getInt("zustaendig"));
		Prioritaet prioritaet = Prioritaet.getPrio(rs.getInt("prioritaet"));
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date erstellt = null;
		Date faelligkeit = null;
		try {
			if (rs.getString("erstellt") != null)
				erstellt = new Date(Integer.parseInt(rs.getString("erstellt")));
			if (rs.getString("faelligkeit") != null)
				faelligkeit = df.parse(rs.getString("faelligkeit"));
		} catch (NullPointerException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Kostentraeger kostentraeger = Kostentraeger.get(rs.getInt("kostentraeger"));
		boolean archiviert = rs.getBoolean("archiviert");
		Person auftraggeber = PersonenOrdner.getInstanz().getPerson(rs.getInt("auftraggeber"));
		int status = rs.getInt("status");
		Person bearbeitetVon = PersonenOrdner.getInstanz().getPerson(rs.getInt("bearbeitetVon"));
		String ordnerpfad = rs.getString("ordner");
		File ordner = null;
		if (ordnerpfad != null) {
			ordner = new File(ordnerpfad);
		}
		Projekt p = new Projekt(dbId, titel, beschreibung, zustaendig, prioritaet, erstellt, faelligkeit, kostentraeger,
				archiviert, ordner, auftraggeber, status, bearbeitetVon);
		return p;
	}

	public void updateDB(Projekt projekt) {

		String sql = "UPDATE projekt SET titel = ?, beschreibung = ?, zustaendig = ?, prioritaet = ?, faelligkeit = ?, kostentraeger = ?, auftraggeber = ?, bearbeitet = ?, bearbeitetVon = ?, ordner = ? WHERE id = ?;";

		DatenHaltung d = new DatenHaltung(true);
		d.prepareStatement(sql);
		d.setString(1, projekt.getTitel());
		d.setString(2, projekt.getBeschreibung());
		d.setInt(3, projekt.getZustaendig().getId());
		d.setInt(4, projekt.getPrioritaet().getId());
		d.setString(5, new SimpleDateFormat("yyyy-MM-dd").format(projekt.getFaelligkeit()));
		d.setInt(6, projekt.getKostentraeger().getId());
		d.setInt(7, projekt.getAuftraggeber().getId());
		d.setLong(8, Instant.now().getEpochSecond());
		d.setInt(9, PersonenOrdner.getInstanz().getNutzer().getId());
		if (projekt.getProjektordner() != null)
			d.setString(10, projekt.getProjektordner().getAbsolutePath());
		d.setInt(11, projekt.getId());
		d.update();
		ProjektNotifier.getInstanz().informListener();
	}

	@Override
	public void removeFromDB(int id) {
		long time = Instant.now().getEpochSecond();
		DatenHaltung d1 = new DatenHaltung(true);
		d1.prepareStatement("UPDATE aufgabe SET geloescht = ?, bearbeitetVon = ? WHERE projekt = ?;");
		d1.setLong(1, time);
		d1.setInt(2, PersonenOrdner.getInstanz().getNutzer().getId());
		d1.setInt(3, id);
		d1.update();

		DatenHaltung d2 = new DatenHaltung(true);
		d2.prepareStatement("UPDATE projekt SET geloescht = ?, bearbeitetVon = ? WHERE id = ?;");
		d2.setLong(1, time);
		d2.setInt(2, PersonenOrdner.getInstanz().getNutzer().getId());
		d2.setInt(3, id);
		d2.update();

		AufgabenOrdner.removeListe(id);
		this.remove(id);

		ProjektNotifier.getInstanz().informListener();
		AufgabenNotifier.getInstanz().informListener();
	}

	protected void alertNew(Entity p) {
		Projekt a = (Projekt) p;
		if ((a.getZustaendig().getId() == PersonenOrdner.getInstanz().getNutzer().getId()
				|| a.getAuftraggeber().getId() == PersonenOrdner.getInstanz().getNutzer().getId())
				&& a.getBearbeitetVon().getId() != PersonenOrdner.getInstanz().getNutzer().getId()) {
			SystemLeistenIcon.getInstanz().makeAlert("Neues Projekt: " + a.getTitel(),
					"Es wurde ein neues Projekt angelegt:\n" + a.getTitel() + "\n" + a.getBeschreibung());
		}
	}

	@Override
	protected void alertChanged(Entity p) {
		Projekt a = (Projekt) p;
		if ((a.getZustaendig().getId() == PersonenOrdner.getInstanz().getNutzer().getId()
				|| a.getAuftraggeber().getId() == PersonenOrdner.getInstanz().getNutzer().getId())
				&& a.getBearbeitetVon().getId() != PersonenOrdner.getInstanz().getNutzer().getId()) {
			SystemLeistenIcon.getInstanz().makeAlert("Verändertes Projekt: " + a.getTitel(),
					"Es wurde ein Projekt verändert:\n" + a.getTitel() + "\n" + a.getBeschreibung());
		}
	}

	@Override
	protected void alertRemoved(Entity p) {
		Projekt a = (Projekt) p;
		if ((a.getZustaendig().getId() == PersonenOrdner.getInstanz().getNutzer().getId()
				|| a.getAuftraggeber().getId() == PersonenOrdner.getInstanz().getNutzer().getId())
				&& a.getBearbeitetVon().getId() != PersonenOrdner.getInstanz().getNutzer().getId()) {
			SystemLeistenIcon.getInstanz().makeAlert("Gelöschtes Projekt: " + a.getTitel(),
					"Es wurde ein Projekt gelöscht:\n" + a.getTitel() + "\n" + a.getBeschreibung());
		}
	}
}
