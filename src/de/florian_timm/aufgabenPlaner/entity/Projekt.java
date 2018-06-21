package de.florian_timm.aufgabenPlaner.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.florian_timm.aufgabenPlaner.DatenhaltungS;

public class Projekt {
	private int dbId;
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

	public int getId() {
		return dbId;
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

	public static List<Projekt> getProjekte() {
		List<Projekt> projekte = new ArrayList<Projekt>();
		try {
			ResultSet rs = DatenhaltungS.query(
					"SELECT p.*, AVG(s.sortierung) as status FROM projekt as p LEFT JOIN aufgabe as a ON p.id = a.projekt LEFT JOIN status as s ON a.status = s.id GROUP BY p.id;");

			while (rs.next()) {
				int dbId = rs.getInt("id");
				String titel = rs.getString("titel");
				String beschreibung = rs.getString("beschreibung");
				Person zustaendig = Person.getPerson(rs.getInt("zustaendig"));
				Prioritaet prioritaet = Prioritaet.getPrio(rs.getInt("prioritaet"));
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				Date erstellt = null;
				Date faelligkeit = null;
				try {
					erstellt = df.parse(rs.getString("erstellt"));
					faelligkeit = df.parse(rs.getString("faelligkeit"));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Kostentraeger kostentraeger = Kostentraeger.get(rs.getInt("kostentraeger"));
				boolean archiviert = rs.getBoolean("archiviert");
				Person auftraggeber = Person.getPerson(rs.getInt("auftraggeber"));
				int status = rs.getInt("status");
				rs.close();
				projekte.add(new Projekt(dbId, titel, beschreibung, zustaendig, prioritaet, erstellt, faelligkeit,
						kostentraeger, archiviert, auftraggeber, status));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return projekte;
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

}
