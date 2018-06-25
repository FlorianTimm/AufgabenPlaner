package de.florian_timm.aufgabenPlaner.Kontroll;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.GroupLayout;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import de.florian_timm.aufgabenPlaner.entity.Person;
import de.florian_timm.aufgabenPlaner.entity.Prioritaet;
import de.florian_timm.aufgabenPlaner.entity.Status;

public class NeuesProjektGUI extends JDialog {
	private static final long serialVersionUID = 1L;
	JTextField titel;
	JTextArea beschreibung;
	private JComboBox<Prioritaet> prio;
	private Component zustaendig;
	
	
	public NeuesProjektGUI (JFrame frame) {
		super(frame);
		
		Container cp = this.getContentPane();
		
		
		titel = new JTextField();
		beschreibung = new JTextArea();
		prio = new JComboBox<Prioritaet>(Prioritaet.getArray());
		zustaendig = new JComboBox<Person>(Person.getArray());
		
		cp.setLayout(new GridLayout(4,2));

		cp.add(new JLabel("Titel"));
		cp.add(titel);
		cp.add(new JLabel("Prio"));
		cp.add(prio);
		cp.add(new JLabel("Zuständig"));
		cp.add(zustaendig);
		
		
		this.pack();
		this.setVisible(true);
		
	}
}
