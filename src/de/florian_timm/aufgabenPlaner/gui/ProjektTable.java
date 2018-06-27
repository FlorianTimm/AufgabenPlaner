package de.florian_timm.aufgabenPlaner.gui;

import java.text.DateFormat;
import javax.swing.table.AbstractTableModel;

import de.florian_timm.aufgabenPlaner.entity.Projekt;

public class ProjektTable extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private Projekt[] projekte;

	private String[] spalten = { "Auftraggeber", "Titel", "Zuständig", "Fälligkeit", "Status" };

	public ProjektTable() {
		setTableSource(Projekt.getArray());
	}

	public ProjektTable(Projekt[] projekte) {
		setTableSource(projekte);
	}

	private void setTableSource(Projekt[] projekte) {
		this.projekte = projekte;
	}

	@Override
	public int getColumnCount() {
		return spalten.length;
	}

	@Override
	public int getRowCount() {
		return projekte.length;

	}

	@Override
	public Object getValueAt(int reihe, int spalte) {

		Projekt projekt = projekte[reihe];

		switch (spalte) {
		case 0:
			if (projekt.getAuftraggeber() != null) {
				return projekt.getAuftraggeber().getName();
			} else {
				return null;
			}
		case 1:
			return projekt.getTitel();
		case 2:
			return projekt.getZustaendig().getName();
		case 3:
			if (projekt.getFaelligkeit() == null) {
				return null;
			}
			return DateFormat.getDateInstance().format(projekt.getFaelligkeit());
		case 4:
			return projekt.getStatus();
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
