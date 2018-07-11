package de.florian_timm.aufgabenPlaner.gui;

import de.florian_timm.aufgabenPlaner.entity.Aufgabe;
import de.florian_timm.aufgabenPlaner.entity.Projekt;
import de.florian_timm.aufgabenPlaner.gui.panels.AufgabenPanel;
import de.florian_timm.aufgabenPlaner.gui.table.BearbeitungTable;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AufgabenGUI extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	private Aufgabe aufgabe = null;
	private AufgabenPanel aufgabenPanel;
	private BearbeitungTable bearbeitungTable;

	public AufgabenGUI(Window window, Projekt projekt) {
		super(window, "Neue Bearbeitung erfassen");
		aufgabenPanel = new AufgabenPanel(this, projekt);
		makeWindow(window);

		this.setModal(true);
	}

	public AufgabenGUI(Window window, Aufgabe aufgabe) {
		super(window, "Aufgabe bearbeiten");
		aufgabenPanel = new AufgabenPanel(this, aufgabe);
		this.aufgabe = aufgabe;
		makeWindow(window);
		this.setModal(true);
	}

	private void makeWindow(Window window) {

		Container cp = this.getContentPane();

		cp.setLayout(new BorderLayout());
		cp.add(aufgabenPanel, BorderLayout.NORTH);

		if (aufgabe != null) {
			bearbeitungTable = new BearbeitungTable(this, aufgabe);
			JScrollPane jsp = new JScrollPane(bearbeitungTable);
			jsp.setPreferredSize(new Dimension(200,200));
			cp.add(jsp, BorderLayout.CENTER);
			
			JButton neu = new JButton("neue Aufgabe");
			neu.addActionListener(this);
			cp.add(neu, BorderLayout.SOUTH);
		}

		this.pack();
		this.setLocationRelativeTo(window);
		this.setVisible(true);
	}

	public void close() {
		aufgabenPanel.close();
		if (aufgabe != null) {
			bearbeitungTable.close();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		new BearbeitungGUI(this, aufgabe);
	}
}
