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
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class NeueAufgabeGUI extends JDialog implements ActionListener {
	DatenSpeicher data = null;
	
	public NeueAufgabeGUI(JFrame frame, DatenSpeicher data) {
		super(frame, "Neue Aufgabe");
		this.data = data;
		Container cp = this.getContentPane();
		cp.setLayout(new GridLayout(4,2));
		
		cp.add(new JLabel("Titel:"));
		JTextField titel = new JTextField("");
		cp.add(titel);
		cp.add(new JLabel("Beschreibung:"));
		JTextArea beschreibung = new JTextArea("");
		JScrollPane jsp = new JScrollPane(beschreibung);
		cp.add(jsp);
		cp.add(new JLabel("Auftraggeber:"));
		
		JComboBox<Person> auftraggeber = new JComboBox<Person>();
		for(Person p:data.getPersonen()) {
			auftraggeber.addItem(p);
		}
		cp.add(auftraggeber);
		
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
