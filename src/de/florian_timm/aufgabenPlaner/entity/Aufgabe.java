package de.florian_timm.aufgabenPlaner.entity;

import de.florian_timm.aufgabenPlaner.kontroll.ErrorHub;
import de.florian_timm.aufgabenPlaner.schnittstelle.DatenhaltungS;

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

import javax.swing.JOptionPane;

public class Aufgabe extends Entity {
    private Person bearbeiter;
    private String titel;
    private String beschreibung;
    private Date erstellt;
    private Date faelligkeit;
    private Status status;
    private boolean storniert;
    private Projekt projekt;

    public Aufgabe(int dbId, Person bearbeiter, String titel, String beschreibung, Date faelligkeit, Status status,
                   boolean storniert, Projekt projekt) {
        this.dbId = dbId;
        this.bearbeiter = bearbeiter;
        this.titel = titel;
        this.beschreibung = beschreibung;
        // this.erstellt = erstellt;
        this.faelligkeit = faelligkeit;
        this.status = status;
        this.storniert = storniert;
    }

    public static Map<Integer, Aufgabe> getAufgaben(Projekt projekt) {
        Map<Integer, Aufgabe> aufgaben = new HashMap<Integer, Aufgabe>();
        try {
            ResultSet rs = DatenhaltungS.query("SELECT * FROM aufgabe where projekt = " + projekt.getId() + ";");

            while (rs.next()) {
                Aufgabe a = getAufgabeFromResult(projekt, rs);

                aufgaben.put(a.getId(), a);
            }
            rs.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            JOptionPane.showMessageDialog(null, e.getLocalizedMessage(), "Fehler", JOptionPane.ERROR_MESSAGE); 
        }
        System.out.println("Anzahl Aufgaben: " + aufgaben.size());
        return aufgaben;
    }

    private static Aufgabe getAufgabeFromResult(Projekt projekt, ResultSet rs) throws SQLException {
        int dbId = rs.getInt("id");

        Person bearbeiter = Person.getPerson(rs.getInt("person"));
        String titel = rs.getString("titel");
        String beschreibung = rs.getString("beschreibung");
        Date faelligkeit = null;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if (rs.getString("faelligkeit") != null)
                faelligkeit = df.parse(rs.getString("faelligkeit"));
        } catch (NullPointerException | ParseException e) {
            ErrorHub.log(e);
        }
        Status status = Status.getStatus(rs.getInt("status"));
        boolean storniert = rs.getBoolean("storniert");

        Aufgabe a = new Aufgabe(dbId, bearbeiter, titel, beschreibung, faelligkeit, status, storniert, projekt);
        return a;

    }

    public static void createTable() throws SQLException {
        DatenhaltungS.update("CREATE TABLE IF NOT EXISTS aufgabe (" + "id INTEGER PRIMARY KEY, "
                + "projekt INTEGER NOT NULL," + "person INTEGER NOT NULL," + "titel	TEXT NOT NULL, "
                + "beschreibung TEXT NOT NULL, " + "faelligkeit DATE," + "erstellt DATE NOT NULL,"
                + "status INTEGER NOT NULL," + "storniert BOOLEAN," + "FOREIGN KEY (person) REFERENCES person(id),"
                + "FOREIGN KEY (projekt) REFERENCES projekt(id)," + "FOREIGN KEY (status) REFERENCES status(id)"
                + ");");
    }

    public static void makeAufgabe(Projekt projekt, String titel, String beschreibung, Person bearbeiter,
                                   Date faelligkeit, Status status) {
        String sql = "INSERT INTO aufgabe (titel, beschreibung, person, faelligkeit, status, projekt, erstellt) VALUES (?,?,?,?,?, ?, ?);";

        Connection c = DatenhaltungS.getConnection();
        try {
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setString(1, titel);
            stmt.setString(2, beschreibung);
            stmt.setInt(3, bearbeiter.getId());
            stmt.setString(4, new SimpleDateFormat("yyyy-MM-dd").format(faelligkeit));
            stmt.setInt(5, status.getId());
            stmt.setInt(6, projekt.getId());
            stmt.setString(7, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            System.out.println(stmt.executeUpdate());
            stmt.close();

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            ErrorHub.log(e);
        }
        informListener();
    }

    public String toString() {
        return titel;
    }

    public Person getBearbeiter() {
        return bearbeiter;
    }

    public void setBearbeiter(Person bearbeiter) {
        this.bearbeiter = bearbeiter;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public Date getErstellt() {
        return erstellt;
    }

    public Date getFaelligkeit() {
        return faelligkeit;
    }

    public Status getStatus() {
        return status;
    }

    public boolean isStorniert() {
        return storniert;
    }

    public Projekt getProjekt() {
        return projekt;
    }

    public void update(String titel, String beschreibung, Person bearbeiter, Date faelligkeit, Status status) {
        String sql = "UPDATE aufgabe SET titel = ?, beschreibung = ?, person = ?, faelligkeit = ?, status = ? WHERE id = ?;";

        Connection c = DatenhaltungS.getConnection();
        try {
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setString(1, titel);
            stmt.setString(2, beschreibung);
            stmt.setInt(3, bearbeiter.getId());
            stmt.setString(4, new SimpleDateFormat("yyyy-MM-dd").format(faelligkeit));
            stmt.setInt(5, status.getId());
            stmt.setInt(6, this.getId());
            stmt.executeUpdate();
            stmt.close();

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            ErrorHub.log(e);
        }
        informListener();
    }
}
