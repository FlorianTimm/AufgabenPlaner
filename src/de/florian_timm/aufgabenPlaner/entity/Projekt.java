package de.florian_timm.aufgabenPlaner.entity;

import de.florian_timm.aufgabenPlaner.kontroll.EntityListener;
import de.florian_timm.aufgabenPlaner.schnittstelle.DatenhaltungS;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Projekt extends Entity {
	private static Map<Integer, Projekt> alle = new HashMap<Integer, Projekt>();
	private String titel;
	private String beschreibung;
	private Person zustaendig;
	private Prioritaet prioritaet;
	private Date erstellt;
	private Date faelligkeit;
	private Kostentraeger kostentraeger;
	private boolean archiviert;
	private Person auftraggeber;
	private int status;
	private Map<Integer, Aufgabe> aufgaben = new HashMap<Integer, Aufgabe>();
	private static Set<EntityListener> listener = new HashSet<EntityListener>();

	public static void addListener(EntityListener newListener) {
		listener.add(newListener);
		System.out.println(
				"ProjektListener: " + listener.size() + " (neu: " + newListener.getClass().getSimpleName() + ")");
	}

	public static void removeListener(EntityListener el) {
		listener.remove(el);
		System.out.println("ProjektListener: " + listener.size() + " (entf: " + el.getClass().getSimpleName() + ")");
	}

	public static void informListener() {
		EntityListener[] ls = listener.toArray(new EntityListener[0]);
		for (EntityListener el : ls) {
			el.dataChanged();
		}
	}

	public Projekt(int dbId, String titel, String beschreibung, Person zustaendig, Prioritaet prioritaet, Date erstellt,
			Date faelligkeit, Kostentraeger kostentraeger, boolean archiviert, Person auftraggeber, int status) {
		this.dbId = dbId;
		this.titel = titel;
		this.auftraggeber = auftraggeber;
		this.beschreibung = beschreibung;
		this.zustaendig = zustaendig;
		this.kostentraeger = kostentraeger;
		this.prioritaet = prioritaet;
		this.erstellt = erstellt;
		this.faelligkeit = faelligkeit;
		this.status = status;
	}

	public static void makeProjekt(String titel, String beschreibung, Prioritaet prio, Person zustaendig,
			Kostentraeger kostentraeger, Date faelligkeit, Person auftraggeber) {
		String sql = "INSERT INTO projekt (titel, beschreibung, prioritaet, zustaendig, kostentraeger, faelligkeit, auftraggeber, erstellt) VALUES (?,?,?,?,?,?,?,?);";

		DatenhaltungS c = new DatenhaltungS(true);
		c.prepareStatement(sql);
		c.setString(1, titel);
		c.setString(2, beschreibung);
		c.setInt(3, prio.getId());
		c.setInt(4, zustaendig.getId());
		c.setInt(5, kostentraeger.getId());
		c.setString(6, new SimpleDateFormat("yyyy-MM-dd").format(faelligkeit));
		c.setInt(7, auftraggeber.getId());
		c.setString(8, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		c.update();

		loadData();
		informListener();
	}

	private static void loadData() {
		alle.clear();
		String sql = "SELECT p.*, AVG(s.sortierung) as status FROM projekt as p LEFT JOIN aufgabe as a ON p.id = a.projekt LEFT JOIN status as s ON a.status = s.id GROUP BY p.id";
		DatenhaltungS c = new DatenhaltungS();
		c.query(sql);

		while (c.next()) {
			Projekt p = getProjektFromResult(c);
			alle.put(p.getId(), p);
		}

	}

	private static Projekt getProjektFromResult(DatenhaltungS rs) {
		int dbId = rs.getInt("id");
		String titel = rs.getString("titel");
		String beschreibung = rs.getString("beschreibung");
		Person zustaendig = Person.getPerson(rs.getInt("zustaendig"));
		Prioritaet prioritaet = Prioritaet.getPrio(rs.getInt("prioritaet"));
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date erstellt = null;
		Date faelligkeit = null;
		try {
			if (rs.getString("erstellt") != null)
				erstellt = df.parse(rs.getString("erstellt"));
			if (rs.getString("faelligkeit") != null)
				faelligkeit = df.parse(rs.getString("faelligkeit"));
		} catch (NullPointerException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Kostentraeger kostentraeger = Kostentraeger.get(rs.getInt("kostentraeger"));
		boolean archiviert = rs.getBoolean("archiviert");
		Person auftraggeber = Person.getPerson(rs.getInt("auftraggeber"));
		int status = rs.getInt("status");
		Projekt p = new Projekt(dbId, titel, beschreibung, zustaendig, prioritaet, erstellt, faelligkeit, kostentraeger,
				archiviert, auftraggeber, status);
		return p;
	}

	public static void createTable() {
		new DatenhaltungS(true).update("CREATE TABLE IF NOT EXISTS projekt (" + "id INTEGER PRIMARY KEY, " + "titel	TEXT NOT NULL, "
				+ "beschreibung TEXT NOT NULL, " + "zustaendig INTEGER," + "prioritaet INTEGER," + "erstellt DATE,"
				+ "faelligkeit DATE," + "kostentraeger INTEGER," + "archiviert BOOLEAN," + "auftraggeber INTEGER,"
				+ "FOREIGN KEY (zustaendig) REFERENCES person(id),"
				+ "FOREIGN KEY (auftraggeber) REFERENCES person(id),"
				+ "FOREIGN KEY (prioritaet) REFERENCES prioritaet(id),"
				+ "FOREIGN KEY (kostentraeger) REFERENCES kostentraeger(id)" + ");");
	}

	public static Projekt[] getArray() {
		checkLoading();
		Projekt[] p = alle.values().toArray(new Projekt[0]);
		// Arrays.sort(k);
		return p;
	}

	protected static void checkLoading() {
		if (alle.size() == 0) {
			loadData();
		}
	}

	public String getTitel() {
		return titel;
	}

	public String getBeschreibung() {
		return beschreibung;
	}

	public Person getZustaendig() {
		return zustaendig;
	}

	public Prioritaet getPrioritaet() {
		return prioritaet;
	}

	public Date getErstellt() {
		return erstellt;
	}

	public Date getFaelligkeit() {
		return faelligkeit;
	}

	public Kostentraeger getKostentraeger() {
		return kostentraeger;
	}

	public boolean isArchiviert() {
		return archiviert;
	}

	public Person getAuftraggeber() {
		return auftraggeber;
	}

	public int getStatus() {
		return status;
	}

	public void reload() {
		String sql = "SELECT p.*, AVG(s.sortierung) as status FROM projekt as p LEFT JOIN aufgabe as a ON p.id = a.projekt LEFT JOIN status as s ON a.status = s.id WHERE p.id = ? GROUP BY p.id ;";
		DatenhaltungS d = new DatenhaltungS();
		d.prepareStatement(sql);
		d.setInt(1, this.dbId);

		if (d.next()) {
			Projekt p = getProjektFromResult(d);
			alle.put(p.getId(), p);
			this.setAll(p);
		}

		informListener();
	}

	private void setAll(Projekt p) {
		this.dbId = p.dbId;
		this.titel = p.titel;
		this.auftraggeber = p.auftraggeber;
		this.beschreibung = p.beschreibung;
		this.zustaendig = p.zustaendig;
		this.kostentraeger = p.kostentraeger;
		this.prioritaet = p.prioritaet;
		this.erstellt = p.erstellt;
		this.faelligkeit = p.faelligkeit;
		this.status = p.status;
		this.aufgaben = p.aufgaben;
	}

	public void loadAufgaben() {
		this.aufgaben = Aufgabe.getAufgaben(this);
	}

	private void checkAufgabenLoading() {
		if (aufgaben.size() == 0) {
			loadAufgaben();
		}
	}

	public Aufgabe[] getAufgaben() {
		checkAufgabenLoading();
		Aufgabe[] a = aufgaben.values().toArray(new Aufgabe[0]);
		// Arrays.sort(k);
		return a;
	}

	public void update(String titel, String beschreibung, Prioritaet prio, Person zustaendig,
			Kostentraeger kostentraeger, Date faelligkeit, Person auftraggeber) {

		String sql = "UPDATE projekt SET titel = ?, beschreibung = ?, zustaendig = ?, prioritaet = ?, faelligkeit = ?, kostentraeger = ?, auftraggeber = ? WHERE id = ?;";

		DatenhaltungS d = new DatenhaltungS(true);
		d.prepareStatement(sql);
		d.setString(1, titel);
		d.setString(2, beschreibung);
		d.setInt(3, zustaendig.getId());
		d.setInt(4, prio.getId());
		d.setString(5, new SimpleDateFormat("yyyy-MM-dd").format(faelligkeit));
		d.setInt(6, kostentraeger.getId());
		d.setInt(7, auftraggeber.getId());
		d.setInt(8, this.getId());
		d.update();

		this.reload();
		Projekt.informListener();

	}

	public static Projekt[] getByUser(Person person) {
		List<Projekt> projekte = new ArrayList<Projekt>();
		String sql = "SELECT p.*, AVG(s.sortierung) as status FROM projekt as p LEFT JOIN aufgabe as a ON p.id = a.projekt LEFT JOIN status as s ON a.status = s.id WHERE p.auftraggeber = ? OR p.zustaendig = ? GROUP BY p.id;";

		DatenhaltungS d = new DatenhaltungS();
		d.prepareStatement(sql);
		d.setInt(1, person.getId());
		d.setInt(2, person.getId());

		while (d.next()) {
			Projekt p = getProjektFromResult(d);
			alle.put(p.getId(), p);
			projekte.add(p);
		}

		return projekte.toArray(new Projekt[0]);
	}

	public static Projekt get(int projektId) {
		checkLoading();
		return alle.get(projektId);
	}

}
