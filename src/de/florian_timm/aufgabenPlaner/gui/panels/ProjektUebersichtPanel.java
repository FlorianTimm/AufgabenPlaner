package de.florian_timm.aufgabenPlaner.gui.panels;

import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import de.florian_timm.aufgabenPlaner.entity.Person;
import de.florian_timm.aufgabenPlaner.gui.ProjektGUI;
import de.florian_timm.aufgabenPlaner.gui.table.ProjektTable;

@SuppressWarnings("serial")
public class ProjektUebersichtPanel extends JPanel implements ActionListener {
	ProjektTable projektTable;
	private Window window;

	public ProjektUebersichtPanel(Window window) {
		super();
		projektTable = new ProjektTable();
		makePanel(window);
	}

	public ProjektUebersichtPanel(Window window, Person person) {
		super();
		projektTable = new ProjektTable(person);
		makePanel(window);
	}

	private void makePanel(Window window) {
		this.window = window;
		this.setLayout(new BorderLayout());

		JScrollPane jsp = new JScrollPane(projektTable);
		this.add(jsp, BorderLayout.CENTER);

		JButton neueAufgabe = new JButton("Neues Projekt");
		neueAufgabe.setActionCommand("neuesProjekt");
		neueAufgabe.addActionListener(this);
		this.add(neueAufgabe, BorderLayout.NORTH);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		switch (ae.getActionCommand()) {
		case "neuesProjekt":
			new ProjektGUI(this.window);
			break;
		}
	}

	public void close() {
		projektTable.close();
	}
}
