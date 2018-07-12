package de.florian_timm.aufgabenPlaner.gui.table;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;

import de.florian_timm.aufgabenPlaner.entity.Status;

import java.awt.*;

@SuppressWarnings("serial")
public class ProgressCellRenderer extends JProgressBar implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        int progress = 0;
        if (value instanceof Float) {
            progress = Math.round(((Float) value) * 100f);
        } else if (value instanceof Integer) {
            progress = (int) value;
        } else if (value instanceof Status) {
        	progress = ((Status) value).getSortierung();
        }
        setValue(progress);
        this.setForeground(new Color(0x66ff66));
        return this;
    }

}
