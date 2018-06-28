package de.florian_timm.aufgabenPlaner.gui.table;

import javax.swing.table.AbstractTableModel;

import de.florian_timm.aufgabenPlaner.entity.Entity;

@SuppressWarnings("serial")
public abstract class TableModel extends AbstractTableModel {
	protected String[] spalten;
	protected Entity[] dataSource;
	protected Object[][] data;

	@Override
	public int getColumnCount() {
		return spalten.length;
	}

	@Override
	public int getRowCount() {
		return dataSource.length;

	}

	public String getColumnName(int column) {
		return spalten[column];
	}

	public boolean isCellEditable(int row, int spalte) {
		return false;
	}
	
	@Override
	public Object getValueAt(int reihe, int spalte) {
		return data[reihe][spalte];
	}

	public void setValueAt(Object value, int row, int column) {
		// keine Aenderung bisher moeglich
	}
	
	protected abstract void setDataSource(Entity[] daten);
	
	public Entity getData(int id) {
		return this.dataSource[id];
	}

}
