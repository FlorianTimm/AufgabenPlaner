package de.florian_timm.aufgabenPlaner;

import java.io.File;
import java.sql.*;

import de.florian_timm.aufgabenPlaner.entity.Aufgabe;
import de.florian_timm.aufgabenPlaner.entity.Kostentraeger;
import de.florian_timm.aufgabenPlaner.entity.Person;
import de.florian_timm.aufgabenPlaner.entity.Prioritaet;
import de.florian_timm.aufgabenPlaner.entity.Projekt;
import de.florian_timm.aufgabenPlaner.entity.Status;

public class DatenhaltungS {
	private static Connection c = null;
	private static String sourceFile;
	private static DatenhaltungS instanz = null;

	public static void setSourceFile(String file) {
		sourceFile = file;
	}

	// public static DatenhaltungS getInstanz() {
	// if (instanz == null) {
	// instanz = new DatenhaltungS();
	// }
	// return instanz;
	// }

	private DatenhaltungS() {
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + sourceFile);
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Opened database successfully");

		instanz = this;
	}

	private static void checkDB() {
		boolean exists = (new File(sourceFile)).exists();
		if (instanz == null) {
			instanz = new DatenhaltungS();

			if (!exists) {
				Kostentraeger.createTable();
				Prioritaet.createTable();
				Person.createTable();
				Status.createTable();
				Projekt.createTable();
				Aufgabe.createTable();
			}
		}
	}

	public static ResultSet query(String sql) {
		checkDB();
		try {
			Statement stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			// stmt.close();
			return rs;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public static void update(String sql) {
		checkDB();
		try {
			Statement stmt = c.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
