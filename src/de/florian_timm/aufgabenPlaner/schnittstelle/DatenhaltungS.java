package de.florian_timm.aufgabenPlaner.schnittstelle;

import de.florian_timm.aufgabenPlaner.entity.*;
import de.florian_timm.aufgabenPlaner.kontroll.ErrorHub;

import java.io.File;
import java.sql.*;

import org.sqlite.SQLiteConfig;

public class DatenhaltungS {
	private static String sourceFile;
	private static boolean geprueft = false;

	private Connection c = null;
	private Statement stmt = null;
	private PreparedStatement ppst = null;
	private ResultSet resultset = null;
	private boolean writable = false;

	public static void setSourceFile(String file) {
		sourceFile = file;
	}

	public DatenhaltungS() {
		this(false);
	}

	public DatenhaltungS(boolean writable) {
		this.writable = writable;
		try {
			Class.forName("org.sqlite.JDBC");

			SQLiteConfig config = new SQLiteConfig();
			config.setReadOnly(!writable);
			checkDB();
			c = DriverManager.getConnection("jdbc:sqlite:" + sourceFile, config.toProperties());
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			ErrorHub.log(e);
		}
	}

	public void update(String sql) {

		try {
			stmt = c.createStatement();
			stmt.execute(sql);
		} catch (SQLException e) {
			ErrorHub.log(e);
		} finally {
			close();
		}

	}
	
	public void update () {
		try {
			ppst.execute();
		} catch (SQLException e) {
			ErrorHub.log(e);
		} finally {
			close();
		}
	}

	public void prepareStatement(String sql) {
		try {
			ppst = c.prepareStatement(sql);
		} catch (SQLException e) {
			ErrorHub.log(e);
			close();
		}
	}

	public void setInt(int i, int zahl) {
		try {
			ppst.setInt(i, zahl);
		} catch (SQLException e) {
			ErrorHub.log(e);
			close();
		}
	}

	public void setString(int i, String text) {
		try {
			ppst.setString(i, text);
		} catch (SQLException e) {
			ErrorHub.log(e);
			close();
		}
	}
	
	public void query(String sql) {
		try {
			stmt = c.createStatement();
			resultset = stmt.executeQuery(sql);
		} catch (SQLException e) {
			ErrorHub.log(e);
		}
	}

	public boolean next() {
		try {
			if (resultset == null && !writable && ppst != null) {
				resultset = ppst.executeQuery();
			} 
			boolean r = resultset.next();
			if (!r) {
				close();
				return false;
			}
			return r;
		} catch (SQLException e) {
			ErrorHub.log(e);
			return false;
		}
	}
	
	public String getString(int i) {
		try {
			return resultset.getString(i);
		} catch (SQLException e) {
			ErrorHub.log(e);
			close();
			return null;
		}
	}
	
	public int getInt(int i) {
		try {
			return resultset.getInt(i);
		} catch (SQLException e) {
			ErrorHub.log(e);
			close();
			return -1;
		}
	}
	
	public double getDouble(int i) {
		try {
			return resultset.getDouble(i);
		} catch (SQLException e) {
			ErrorHub.log(e);
			close();
			return -1;
		}
	}

	public String getString(String s) {
		try {
			return resultset.getString(s);
		} catch (SQLException e) {
			ErrorHub.log(e);
			close();
			return null;
		}
	}
	
	public int getInt(String s) {
		try {
			return resultset.getInt(s);
		} catch (SQLException e) {
			ErrorHub.log(e);
			close();
			return -1;
		}
	}
	
	public double getDouble(String s) {
		try {
			return resultset.getDouble(s);
		} catch (SQLException e) {
			ErrorHub.log(e);
			close();
			return -1;
		}
	}
	
	public boolean getBoolean(String s) {
		try {
			return resultset.getBoolean(s);
		} catch (SQLException e) {
			ErrorHub.log(e);
			close();
			return false;
		}
	}
	
	public void close() {
		if (ppst != null) {
			try {
				ppst.close();
			} catch (SQLException e) {
				ErrorHub.log(e);
			}
		}

		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				ErrorHub.log(e);
			}
		}

		if (resultset != null) {
			try {
				resultset.close();
			} catch (SQLException e) {
				ErrorHub.log(e);
			}
		}

		if (c != null) {
			try {
				c.close();
			} catch (SQLException e) {

				ErrorHub.log(e);
			}
		}
	}

	public static void checkDB() {
		if (!geprueft) {
			geprueft = true;
			if (!(new File(sourceFile)).exists()) {
				try {
					Kostentraeger.createTable();
					Prioritaet.createTable();
					Person.createTable();
					Status.createTable();
					Projekt.createTable();
					Aufgabe.createTable();
				} catch (SQLException e) {
					ErrorHub.log(e);
				}
			}
		}
	}
}
