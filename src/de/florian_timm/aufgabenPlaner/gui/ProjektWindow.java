package de.florian_timm.aufgabenPlaner.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTable;

import de.florian_timm.aufgabenPlaner.entity.Aufgabe;
import de.florian_timm.aufgabenPlaner.entity.Projekt;
import de.florian_timm.aufgabenPlaner.kontroll.Listener;

public class ProjektWindow extends JDialog implements ActionListener, Listener {
	private static final long serialVersionUID = 1L;
	private Projekt projekt;
	private JTable aufgabenTable;
	private Frame frame;

	public ProjektWindow(Frame frame, Projekt projekt) {
		super(frame);
		this.setTitle(projekt.getTitel());
		this.projekt = projekt;
		
		Container cp = this.getContentPane();
		cp.setLayout(new BorderLayout());
		
		dataChanged();
		cp.add(aufgabenTable, BorderLayout.CENTER);
		
		JButton neu = new JButton("neue Aufgabe");
		neu.addActionListener(this);
		cp.add(neu, BorderLayout.SOUTH);
		
		this.pack();
		this.setVisible(true);
		
		Aufgabe.addListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		new NeueAufgabeGUI(frame, projekt);
	}

	@Override
	public void dataChanged() {
		aufgabenTable = new JTable(new AufgabenTable(projekt));
	}
}
