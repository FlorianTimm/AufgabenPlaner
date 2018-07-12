package de.florian_timm.aufgabenPlaner.gui.table;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

import de.florian_timm.aufgabenPlaner.entity.Prioritaet;

import java.awt.*;

@SuppressWarnings("serial")
public class PrioRenderer extends DefaultTableCellRenderer {

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		Prioritaet wert = (Prioritaet) value;

		Component c = super.getTableCellRendererComponent(table, wert, isSelected, hasFocus, row, column);

		int zahl = wert.getSortierung();
		int r = 255 - zahl * 2;
		c.setBackground(new Color(255, r, r));
		if (zahl > 80)
			c.setFont(c.getFont().deriveFont(Font.BOLD));

		return c;
	}

}
