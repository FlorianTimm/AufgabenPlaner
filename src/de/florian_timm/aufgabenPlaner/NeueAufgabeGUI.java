package de.florian_timm.aufgabenPlaner;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import de.florian_timm.aufgabenPlaner.entity.Person;

public class NeueAufgabeGUI extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	public NeueAufgabeGUI(JFrame frame) {
		super(frame, "Neue Aufgabe");
		Container cp = this.getContentPane();
		cp.setLayout(new GridLayout(4,2));
		JLabel lTitel = new JLabel("Titel:");
		cp.add(lTitel);
		JTextField titel = new JTextField("");
		cp.add(titel);
		cp.add(new JLabel("Beschreibung:"));
		JTextArea beschreibung = new JTextArea("");
		JScrollPane jsp = new JScrollPane(beschreibung);
		cp.add(jsp);
		cp.add(new JLabel("Auftraggeber:"));
		
		JComboBox<Person> auftraggeber = new JComboBox<Person>();
		for(Person p:Person.getPersonen()) {
			auftraggeber.addItem(p);
		}
		cp.add(auftraggeber);
		cp.add(new JLabel());
		JButton speichern = new JButton("Speichern");
		speichern.addActionListener(this);
		cp.add(speichern);
		this.pack();
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		switch (ae.getActionCommand()) {
		case "speichern":
			speichern();
			break;
		}
		
	}
	
	private void speichern( ) {
		this.setVisible(false);
	}
}
