package de.florian_timm.aufgabenPlaner.gui;

import de.florian_timm.aufgabenPlaner.entity.Aufgabe;
import de.florian_timm.aufgabenPlaner.entity.Person;
import de.florian_timm.aufgabenPlaner.entity.Projekt;
import de.florian_timm.aufgabenPlaner.entity.Status;
import de.florian_timm.aufgabenPlaner.gui.comp.PersonChooser;

import org.jdatepicker.DateModel;
import org.jdatepicker.JDatePicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;

public class AufgabenGUI extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JTextField titelField;
	private JTextArea beschreibungField;
	private PersonChooser bearbeiterField;
	private JDatePicker faelligkeitField;
	private JComboBox<Status> statusField;
	private Projekt projekt;
	private Aufgabe aufgabe = null;

	public AufgabenGUI(Window window, Projekt projekt) {
		super(window, "Aufgabe");
		this.projekt = projekt;
		makeWindow();
	}

	public AufgabenGUI(Window window, Aufgabe aufgabe) {
		this(window, aufgabe.getProjekt());
		this.aufgabe = aufgabe;

		titelField.setText(aufgabe.toString());
		beschreibungField.setText(aufgabe.getBeschreibung());
		bearbeiterField.setSelectedItem(aufgabe.getBearbeiter());
		statusField.setSelectedItem(aufgabe.getStatus());
		DateModel<?> m = faelligkeitField.getModel();
		Calendar d = Calendar.getInstance();
		d.setTime(aufgabe.getFaelligkeit());
		m.setYear(d.get(Calendar.YEAR));
		m.setMonth(d.get(Calendar.MONTH));
		m.setDay(d.get(Calendar.DATE));
	}

	private void makeWindow() {

		Container cp = this.getContentPane();

		JLabel titelLabel = new JLabel("Titel");
		titelField = new JTextField();

		JLabel beschreibungLabel = new JLabel("Beschreibung");
		beschreibungField = new JTextArea(6, 30);
		JScrollPane jsp = new JScrollPane(beschreibungField);

		JLabel bearbeiterLabel = new JLabel("Bearbeiter");
		bearbeiterField = new PersonChooser();
		JButton neuPerson = new JButton("neu...");
		neuPerson.addActionListener(this);
		neuPerson.setActionCommand("neuePerson");

		JLabel faelligkeitLabel = new JLabel("Fällig am");
		Date eineWoche = new Date(System.currentTimeMillis() + 3600 * 1000 * 24 * 7);
		faelligkeitField = new JDatePicker(eineWoche);

		JLabel statusLabel = new JLabel("Status");
		statusField = new JComboBox<Status>(Status.getArray());

		JButton okButton = new JButton("Speichern");
		okButton.addActionListener(this);

		GroupLayout layout = new GroupLayout(cp);
		cp.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(titelLabel)
						.addComponent(beschreibungLabel).addComponent(bearbeiterLabel).addComponent(faelligkeitLabel)
						.addComponent(statusLabel))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(titelField)
						.addComponent(jsp).addComponent(bearbeiterField).addComponent(faelligkeitField)
						.addComponent(statusField).addComponent(okButton))
				.addComponent(neuPerson));

		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(titelLabel)
						.addComponent(titelField))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(beschreibungLabel)
						.addComponent(jsp))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(bearbeiterLabel)
						.addComponent(bearbeiterField).addComponent(neuPerson))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(faelligkeitLabel)
						.addComponent(faelligkeitField))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(statusLabel)
						.addComponent(statusField))
				.addComponent(okButton));

		this.pack();
	}

	public void makeAufgabe() {
		String titel = this.titelField.getText();
		String beschreibung = this.beschreibungField.getText();
		Person bearbeiter = (Person) this.bearbeiterField.getSelectedItem();
		Date faelligkeit = (Date) this.faelligkeitField.getModel().getValue();
		Status status = (Status) this.statusField.getSelectedItem();
		Aufgabe.makeAufgabe(projekt, titel, beschreibung, bearbeiter, faelligkeit, status);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Speichern")) {
			if (this.aufgabe == null)
				makeAufgabe();
			else
				updateAufgabe();
			this.setVisible(false);
			this.dispose();
			Person.removeListener(bearbeiterField);

		} else {
			PersonGUI pgui = new PersonGUI(this);
			pgui.setVisible(true);
		}
	}

	private void updateAufgabe() {
		String titel = this.titelField.getText();
		String beschreibung = this.beschreibungField.getText();
		Person bearbeiter = (Person) this.bearbeiterField.getSelectedItem();
		Date faelligkeit = (Date) this.faelligkeitField.getModel().getValue();
		Status status = (Status) this.statusField.getSelectedItem();
		aufgabe.update(titel, beschreibung, bearbeiter, faelligkeit, status);
	}
}
