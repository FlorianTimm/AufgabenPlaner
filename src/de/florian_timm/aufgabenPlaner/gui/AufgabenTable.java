package de.florian_timm.aufgabenPlaner.gui;

import java.text.DateFormat;
import javax.swing.table.AbstractTableModel;

import de.florian_timm.aufgabenPlaner.entity.Aufgabe;
import de.florian_timm.aufgabenPlaner.entity.Projekt;

public class AufgabenTable extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private Aufgabe[] aufgaben;

	private String[] spalten = { "Bearbeiter", "Titel", "Fälligkeit", "Status" };

	public AufgabenTable(Projekt projekt) {
		setTableSource(projekt.getAufgaben());
	}

	public AufgabenTable(Aufgabe[] aufgaben) {
		setTableSource(aufgaben);
	}

	private void setTableSource(Aufgabe[] aufgaben) {
		this.aufgaben = aufgaben;
	}

	@Override
	public int getColumnCount() {
		return spalten.length;
	}

	@Override
	public int getRowCount() {
		return aufgaben.length;

	}

	@Override
	public Object getValueAt(int reihe, int spalte) {

		Aufgabe aufgabe = aufgaben[reihe];

		switch (spalte) {
		case 0:
			return aufgabe.getBearbeiter().getName();
		case 1:
			return aufgabe.getTitel();
		case 2:
			if (aufgabe.getFaelligkeit() == null) {
				return null;
			}
			return DateFormat.getDateInstance().format(aufgabe.getFaelligkeit());
		case 3:
			return aufgabe.getStatus().toString();
		}

		return null;
	}

	public String getColumnName(int column) {
		return spalten[column];
	}

	public boolean isCellEditable(int row, int spalte) {
		return false;
	}

	public void setValueAt(Object value, int row, int column) {
		// keine Aenderung bisher moeglich
	}
}
