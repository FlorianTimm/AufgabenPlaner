package de.florian_timm.aufgabenPlaner.entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.florian_timm.aufgabenPlaner.schnittstelle.DatenhaltungS;

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

		Connection c = DatenhaltungS.getConnection();
		try {
			PreparedStatement stmt = c.prepareStatement(sql);
			stmt.setString(1, titel);
			stmt.setString(2, beschreibung);
			stmt.setInt(3, prio.getId());
			stmt.setInt(4, zustaendig.getId());
			stmt.setInt(5, kostentraeger.getId());
			stmt.setString(6, new SimpleDateFormat("yyyy-MM-dd").format(faelligkeit));
			stmt.setInt(7, auftraggeber.getId());
			stmt.setString(8, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
			System.out.println(stmt.executeUpdate());
			stmt.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		loadData();
		informListener();
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

	private static void loadData() {
		alle.clear();
		try {
			ResultSet rs = DatenhaltungS.query(
					"SELECT p.*, AVG(s.sortierung) as status FROM projekt as p LEFT JOIN aufgabe as a ON p.id = a.projekt LEFT JOIN status as s ON a.status = s.id GROUP BY p.id;");

			while (rs.next()) {
				System.out.println(rs.getString("titel"));
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
				alle.put(dbId, new Projekt(dbId, titel, beschreibung, zustaendig, prioritaet, erstellt, faelligkeit,
						kostentraeger, archiviert, auftraggeber, status));
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void createTable() {
		DatenhaltungS.update("CREATE TABLE IF NOT EXISTS projekt (" + "id INTEGER PRIMARY KEY, "
				+ "titel	TEXT NOT NULL, " + "beschreibung TEXT NOT NULL, " + "zustaendig INTEGER,"
				+ "prioritaet INTEGER," + "erstellt DATE," + "faelligkeit DATE," + "kostentraeger INTEGER,"
				+ "archiviert BOOLEAN," + "auftraggeber INTEGER," + "FOREIGN KEY (zustaendig) REFERENCES person(id),"
				+ "FOREIGN KEY (auftraggeber) REFERENCES person(id),"
				+ "FOREIGN KEY (prioritaet) REFERENCES prioritaet(id),"
				+ "FOREIGN KEY (kostentraeger) REFERENCES kostentraeger(id)" + ");");
	}

	public static Projekt[] getArray() {
		checkLoading();
		Projekt[] p = (Projekt[]) alle.values().toArray(new Projekt[0]);
		// Arrays.sort(k);
		return p;
	}

	protected static void checkLoading() {
		if (alle.size() == 0) {
			loadData();
		}
	}

	void loadAufgaben() {
		this.aufgaben = Aufgabe.getAufgaben(this);
	}
	
	private void checkAufgabenLoading() {
		if (aufgaben.size() == 0) {
			loadAufgaben();
		}
	}
	
	public Aufgabe[] getAufgaben() {
		checkAufgabenLoading();
		Aufgabe[] a = (Aufgabe[]) aufgaben.values().toArray(new Aufgabe[0]);
		// Arrays.sort(k);
		return a;
	}

}
