package de.florian_timm.aufgabenPlaner.gui.table;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.text.DateFormat;
import java.util.Calendar;

@SuppressWarnings("serial")
public class DateRenderer extends DefaultTableCellRenderer {

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		DateRenderable wert = (DateRenderable) value;
		
		Component c = super.getTableCellRendererComponent(table, DateFormat.getDateInstance().format(wert), isSelected,
				hasFocus, row, column);

		Calendar datum = wert.getCalendar();

		Calendar woche = Calendar.getInstance();
		woche.add(Calendar.DAY_OF_YEAR, +7);
		Calendar bald = Calendar.getInstance();
		bald.add(Calendar.DAY_OF_YEAR, +3);

		Calendar heute = Calendar.getInstance();
		if (wert.isFertig()) {
			c.setBackground(null);
			return c;
		} else if (datum.get(Calendar.YEAR) <= heute.get(Calendar.YEAR)
				&& datum.get(Calendar.DAY_OF_YEAR) < heute.get(Calendar.DAY_OF_YEAR)) {
			c.setBackground(Color.red);
		} else if (datum.get(Calendar.YEAR) <= bald.get(Calendar.YEAR)
				&& datum.get(Calendar.DAY_OF_YEAR) < bald.get(Calendar.DAY_OF_YEAR)) {
			c.setBackground(Color.ORANGE);
		} else if (datum.get(Calendar.YEAR) <= woche.get(Calendar.YEAR)
				&& datum.get(Calendar.DAY_OF_YEAR) < woche.get(Calendar.DAY_OF_YEAR)) {
			c.setBackground(Color.YELLOW);
		} else {
			c.setBackground(null);
		}

		return c;
	}

}
