package de.florian_timm.aufgabenPlaner.gui;

import java.awt.Container;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.jdatepicker.JDatePicker;

import de.florian_timm.aufgabenPlaner.entity.Aufgabe;
import de.florian_timm.aufgabenPlaner.entity.Kostentraeger;
import de.florian_timm.aufgabenPlaner.entity.Person;
import de.florian_timm.aufgabenPlaner.entity.Prioritaet;
import de.florian_timm.aufgabenPlaner.entity.Projekt;
import de.florian_timm.aufgabenPlaner.entity.Status;

public class NeueAufgabeGUI extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JTextField titelField;
	private JTextArea beschreibungField;
	private JComboBox<Person> bearbeiterField;
	private JDatePicker faelligkeitField;
	private JComboBox<Status> statusField;
	private Projekt projekt;
	
	
	public NeueAufgabeGUI(Frame frame, Projekt projekt) {
		super(frame);
		this.projekt = projekt;

		Container cp = this.getContentPane();

		JLabel titelLabel = new JLabel("Titel");
		titelField = new JTextField();

		JLabel beschreibungLabel = new JLabel("Beschreibung");
		beschreibungField = new JTextArea(3,20);

		JLabel bearbeiterLabel = new JLabel("Bearbeiter");
		bearbeiterField = new JComboBox<Person>(Person.getArray());
		JButton neuPerson = new JButton("neu...");

		JLabel faelligkeitLabel = new JLabel("Fällig am");
		Date eineWoche = new Date(System.currentTimeMillis() + 3600 * 1000 * 24 * 7);
		faelligkeitField = new JDatePicker(eineWoche);
		
		JLabel statusLabel = new JLabel("Status");
		statusField = new JComboBox<Status>(Status.getArray());

		JButton okButton = new JButton("Speichern");
		okButton.addActionListener(this);

		GroupLayout layout = new GroupLayout(cp);
		cp.setLayout(layout);

		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(titelLabel)
						.addComponent(beschreibungLabel).addComponent(bearbeiterLabel)
						.addComponent(faelligkeitLabel).addComponent(statusLabel))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(titelField)
						.addComponent(beschreibungField).addComponent(bearbeiterField)
						.addComponent(faelligkeitField).addComponent(statusField)
						.addComponent(okButton))
				.addComponent(neuPerson));

		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(titelLabel)
						.addComponent(titelField))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(beschreibungLabel)
						.addComponent(beschreibungField))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(bearbeiterLabel)
						.addComponent(bearbeiterField)
						.addComponent(neuPerson))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(faelligkeitLabel)
						.addComponent(faelligkeitField))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(statusLabel)
						.addComponent(statusField))
				.addComponent(okButton));

		this.pack();
		this.setVisible(true);

	}

	public void makeProjekt() {
		String titel = this.titelField.getText();
		String beschreibung = this.beschreibungField.getText();
		Person bearbeiter = (Person) this.bearbeiterField.getSelectedItem();
		Date faelligkeit = (Date) this.faelligkeitField.getModel().getValue();
		Status status = (Status) this.statusField.getSelectedItem();
		Aufgabe.makeAufgabe(projekt, titel, beschreibung, bearbeiter, faelligkeit, status);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		makeProjekt();
		this.setVisible(false);
	}
}
