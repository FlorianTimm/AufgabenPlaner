package de.florian_timm.aufgabenPlaner.schnittstelle;

import de.florian_timm.aufgabenPlaner.entity.*;

import java.io.File;
import java.sql.*;

public class DatenhaltungS {
	private static Connection c = null;
	private static String sourceFile;
	private static DatenhaltungS instanz = null;

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

	// public static DatenhaltungS getInstanz() {
	// if (instanz == null) {
	// instanz = new DatenhaltungS();
	// }
	// return instanz;
	// }

	public static void setSourceFile(String file) {
		sourceFile = file;
	}

	private static void checkDB() {
		boolean exists = (new File(sourceFile)).exists();
		if (instanz == null) {
			instanz = new DatenhaltungS();

			if (!exists) {
				try {
					Kostentraeger.createTable();
					Prioritaet.createTable();
					Person.createTable();
					Status.createTable();
					Projekt.createTable();
					Aufgabe.createTable();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			}
		}
	}

	public static ResultSet query(String sql) throws SQLException {
		checkDB();
		Statement stmt = c.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		// stmt.close();
		return rs;

	}

	public static void update(String sql) throws SQLException {
		checkDB();
		Statement stmt = c.createStatement();
		stmt.executeUpdate(sql);
		stmt.close();

	}

	public static Connection getConnection() {
		return c;
	}

	public static void close() {
		try {
			c.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
