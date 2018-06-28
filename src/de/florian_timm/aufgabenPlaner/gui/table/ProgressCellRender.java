package de.florian_timm.aufgabenPlaner.gui.table;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

@SuppressWarnings("serial")
public class ProgressCellRender extends JProgressBar implements TableCellRenderer {

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		int progress = 0;
		if (value instanceof Float) {
			progress = Math.round(((Float) value) * 100f);
		} else if (value instanceof Integer) {
			progress = (int) value;
		}
		setValue(progress);
		this.setForeground(new Color(0x66ff66));
		return this;
	}

}
