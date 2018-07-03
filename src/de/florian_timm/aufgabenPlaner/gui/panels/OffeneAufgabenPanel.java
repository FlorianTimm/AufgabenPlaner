package de.florian_timm.aufgabenPlaner.gui.panels;

import java.awt.BorderLayout;
import java.awt.Window;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import de.florian_timm.aufgabenPlaner.entity.Person;
import de.florian_timm.aufgabenPlaner.gui.table.AufgabenTable;

@SuppressWarnings("serial")
public class OffeneAufgabenPanel extends JPanel {
	AufgabenTable aufgabenTable;

	public OffeneAufgabenPanel(Window window, Person person) {
		super();
		aufgabenTable = new AufgabenTable(window, person);
		this.setLayout(new BorderLayout());
		JScrollPane jsp = new JScrollPane(aufgabenTable);
		this.add(jsp, BorderLayout.CENTER);
	}

	public void close() {
		aufgabenTable.close();
	}
}
