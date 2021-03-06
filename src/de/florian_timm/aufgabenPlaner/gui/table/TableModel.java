package de.florian_timm.aufgabenPlaner.gui.table;

import de.florian_timm.aufgabenPlaner.entity.Entity;

import javax.swing.table.AbstractTableModel;

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
    	if (data.length > reihe && data[reihe].length > spalte)
    		//System.out.println(data[reihe][spalte]);
    		return data[reihe][spalte];
    	return null;
    }

    public void setValueAt(Object value, int row, int column) {
        // keine Aenderung bisher moeglich
    }

    protected abstract void setDataSource(Entity[] daten);

    public Entity getData(int id) {
        return this.dataSource[id];
    }

}
