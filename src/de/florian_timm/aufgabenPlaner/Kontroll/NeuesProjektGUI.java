package de.florian_timm.aufgabenPlaner.Kontroll;

import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.GroupLayout;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import de.florian_timm.aufgabenPlaner.entity.Status;

public class NeuesProjektGUI extends JDialog {
	private static final long serialVersionUID = 1L;
	JTextField titel;
	JTextArea beschreibung;
	private JComboBox<Status> prio;
	
	
	public NeuesProjektGUI (JFrame frame) {
		super(frame);
		
		Container cp = this.getContentPane();
		
		
		titel = new JTextField();
		beschreibung = new JTextArea();
		prio = new JComboBox<Status>();
		
		cp.setLayout(new GridLayout(4,2));

		cp.add(new JLabel("Titel"));
		cp.add(titel);
		
		
		this.pack();
		this.setVisible(true);
		
	}
}
