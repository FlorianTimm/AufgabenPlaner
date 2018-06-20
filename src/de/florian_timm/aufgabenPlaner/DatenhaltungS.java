package de.florian_timm.aufgabenPlaner;

import java.sql.*;

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

		try {
			Statement stmt = c.createStatement();

			String sql = "CREATE TABLE IF NOT EXISTS user (id INTEGER PRIMARY KEY, username	TEXT NOT NULL, name TEXT NOT NULL, email TEXT); "
					+ "CREATE TABLE IF NOT EXISTS aufgaben (id INTEGER PRIMARY KEY, titel	TEXT NOT NULL, beschreibung TEXT NOT NULL, auftraggeber INTEGER); "
					+ "CREATE TABLE IF NOT EXISTS aufgabenzuordnung (id INTEGER PRIMARY KEY, aufgabenid INTEGER, userid INTEGER); ";
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		instanz = this;
	}

	public static ResultSet query(String sql) {
		if (instanz == null) {
			instanz = new DatenhaltungS();
		}
		try {
			Statement stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			stmt.close();
			return rs;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}
	
	public static void update (String sql) {
		if (instanz == null) {
			instanz = new DatenhaltungS();
		}
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
