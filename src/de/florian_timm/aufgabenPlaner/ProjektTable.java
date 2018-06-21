package de.florian_timm.aufgabenPlaner;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import de.florian_timm.aufgabenPlaner.entity.Projekt;

public class ProjektTable extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private List<Projekt> projekte = new ArrayList<Projekt>();

	private String[] spalten = { "Auftraggeber", "Titel", "Zuständig", "Fälligkeit", "Status" };

	public ProjektTable() {
		this.projekte = Projekt.getProjekte();
	}

	public ProjektTable(List<Projekt> projekte) {
		this.projekte = projekte;
	}

	@Override
	public int getColumnCount() {
		return spalten.length;
	}

	@Override
	public int getRowCount() {
		return projekte.size();
	}

	@Override
	public Object getValueAt(int reihe, int spalte) {
		// TODO Auto-generated method stub

		Projekt projekt = projekte.get(reihe);

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
			return projekt.getFaelligkeit().toString();
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
