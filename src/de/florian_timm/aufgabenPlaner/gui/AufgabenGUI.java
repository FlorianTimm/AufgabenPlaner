package de.florian_timm.aufgabenPlaner.gui;

import de.florian_timm.aufgabenPlaner.entity.Aufgabe;
import de.florian_timm.aufgabenPlaner.entity.Person;
import de.florian_timm.aufgabenPlaner.entity.Projekt;
import de.florian_timm.aufgabenPlaner.entity.Status;
import de.florian_timm.aufgabenPlaner.entity.ordner.AufgabenOrdner;
import de.florian_timm.aufgabenPlaner.entity.ordner.ProjektOrdner;
import de.florian_timm.aufgabenPlaner.entity.ordner.StatusOrdner;
import de.florian_timm.aufgabenPlaner.gui.comp.PersonChooser;
import de.florian_timm.aufgabenPlaner.kontroll.PersonenNotifier;

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
		super(window, "Neue Aufgabe");
		makeWindow(window);
		this.projekt = projekt;
		this.setModal(true);
	}

	public AufgabenGUI(Window window, Aufgabe aufgabe) {
		super(window, "Aufgabe bearbeiten");
		this.aufgabe = aufgabe;
		makeWindow(window);
		// System.out.println(aufgabe);

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
		this.setModal(true);
	}

	private void makeWindow(Window window) {

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
		statusField = new JComboBox<Status>(StatusOrdner.getInstanz().getArray());

		JButton okButton = new JButton("Speichern");
		okButton.addActionListener(this);

		JButton delButton = new JButton("Löschen");
		delButton.addActionListener(this);
		delButton.setVisible(aufgabe != null);

		GroupLayout layout = new GroupLayout(cp);
		cp.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(titelLabel)
						.addComponent(beschreibungLabel).addComponent(bearbeiterLabel).addComponent(faelligkeitLabel)
						.addComponent(statusLabel).addComponent(okButton))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(titelField)
						.addComponent(jsp).addComponent(bearbeiterField).addComponent(faelligkeitField)
						.addComponent(statusField).addComponent(delButton))
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
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(okButton)
						.addComponent(delButton)));

		this.pack();
		this.setLocationRelativeTo(window);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "Speichern":
			speichern();
			break;
		case "Löschen":
			loeschen();
			break;
		case "neuePerson":
			newPerson();
			break;
		}
	}

	private void loeschen() {
		int ans = JOptionPane.showConfirmDialog(this, "Möchten Sie die Aufgabe wirklich löschen?", "Löschen",
				JOptionPane.OK_CANCEL_OPTION);
		if (ans == JOptionPane.OK_OPTION) {
			this.setVisible(false);
			this.dispose();
			AufgabenOrdner.getInstanz(this.aufgabe.getProjekt()).removeFromDB(this.aufgabe.getId());
		}
	}

	private void newPerson() {
		PersonGUI pgui = new PersonGUI(this);
		pgui.setVisible(true);
	}

	private void speichern() {
		if (this.aufgabe == null)
			makeAufgabe();
		else
			updateAufgabe();
		this.setVisible(false);
		this.dispose();
		PersonenNotifier.getInstanz().removeListener(bearbeiterField);
	}

	public void makeAufgabe() {
		String titel = this.titelField.getText();
		String beschreibung = this.beschreibungField.getText();
		Person bearbeiter = (Person) this.bearbeiterField.getSelectedItem();
		Date faelligkeit = (Date) this.faelligkeitField.getModel().getValue();
		Status status = (Status) this.statusField.getSelectedItem();
		AufgabenOrdner.getInstanz(projekt).makeAufgabe(projekt, titel, beschreibung, bearbeiter, faelligkeit, status);
	}

	private void updateAufgabe() {
		String titel = this.titelField.getText();
		String beschreibung = this.beschreibungField.getText();
		Person bearbeiter = (Person) this.bearbeiterField.getSelectedItem();
		Date faelligkeit = (Date) this.faelligkeitField.getModel().getValue();
		Status status = (Status) this.statusField.getSelectedItem();
		aufgabe.updateDB(titel, beschreibung, bearbeiter, faelligkeit, status);
	}
}
