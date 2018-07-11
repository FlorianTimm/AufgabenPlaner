package de.florian_timm.aufgabenPlaner.gui;

import de.florian_timm.aufgabenPlaner.entity.Aufgabe;
import de.florian_timm.aufgabenPlaner.entity.Bearbeitung;
import de.florian_timm.aufgabenPlaner.entity.Person;
import de.florian_timm.aufgabenPlaner.entity.ordner.BearbeitungOrdner;
import de.florian_timm.aufgabenPlaner.gui.comp.PersonChooser;
import de.florian_timm.aufgabenPlaner.kontroll.ErrorNotifier;
import de.florian_timm.aufgabenPlaner.kontroll.PersonenNotifier;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

public class BearbeitungGUI extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	private Aufgabe aufgabe;
	private Bearbeitung bearbeitung = null;
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd H:m");
	private JTextArea bemerkungField;
	private JTextField startField;
	private PersonChooser bearbeiterField;
	private JTextField dauerField;

	public BearbeitungGUI(Window window, Aufgabe aufgabe) {
		super(window, "Neue Bearbeitung");
		makeWindow(window);
		this.aufgabe = aufgabe;
		this.setModal(true);
	}

	public BearbeitungGUI(Window window, Bearbeitung bearbeitung) {
		super(window, "Bearbeitung bearbeiten");
		this.bearbeitung = bearbeitung;
		makeWindow(window);
		// System.out.println(aufgabe);

		startField.setText(dateFormat.format(bearbeitung.getStart()));
		dauerField.setText("" + bearbeitung.getDauer().getSeconds()/60);
		bemerkungField.setText(bearbeitung.getBemerkung());
		bearbeiterField.setSelectedItem(bearbeitung.getBearbeiter());
		bearbeiterField.setEnabled(false);
		this.setModal(true);
	}

	private void makeWindow(Window window) {

		Container cp = this.getContentPane();

		JLabel startLabel = new JLabel("Start");
		startField = new JTextField();
		startField.setText(dateFormat.format(new Date()));

		JLabel dauerLabel = new JLabel("Dauer [min]");
		dauerField = new JTextField();

		JLabel bemerkungLabel = new JLabel("Bemerkung");
		bemerkungField = new JTextArea(6, 30);
		JScrollPane jsp = new JScrollPane(bemerkungField);

		JLabel bearbeiterLabel = new JLabel("Bearbeiter");
		bearbeiterField = new PersonChooser();
		JButton neuPerson = new JButton("neu...");
		neuPerson.addActionListener(this);
		neuPerson.setActionCommand("neuePerson");

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
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(startLabel)
						.addComponent(dauerLabel).addComponent(bemerkungLabel).addComponent(bearbeiterLabel)
						.addComponent(okButton))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(startField)
						.addComponent(dauerField).addComponent(jsp).addComponent(bearbeiterField)
						.addComponent(delButton))
				.addComponent(neuPerson));

		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(startLabel)
						.addComponent(startField))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(dauerLabel)
						.addComponent(dauerField))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(bemerkungLabel)
						.addComponent(jsp))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(bearbeiterLabel)
						.addComponent(bearbeiterField).addComponent(neuPerson))
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
		int ans = JOptionPane.showConfirmDialog(this, "Möchten Sie die Bearbeitung wirklich löschen?", "Löschen",
				JOptionPane.OK_CANCEL_OPTION);
		if (ans == JOptionPane.OK_OPTION) {
			this.setVisible(false);
			this.dispose();
			BearbeitungOrdner.getInstanz(this.bearbeitung.getAufgabe()).removeFromDB(this.bearbeitung.getId());
		}
	}

	private void newPerson() {
		PersonGUI pgui = new PersonGUI(this);
		pgui.setVisible(true);
	}

	private void speichern() {
		if (this.bearbeitung == null)
			makeBearbeitung();
		else
			updateBearbeitung();
		this.setVisible(false);
		this.dispose();
		PersonenNotifier.getInstanz().removeListener(bearbeiterField);
	}

	public void makeBearbeitung() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd H:m");
		Date start = null;
		try {
			if (this.startField.getText() != null)
				start = df.parse(this.startField.getText());
		} catch (NullPointerException | ParseException e) {
			ErrorNotifier.log(e);
		}
		String bemerkung = this.bemerkungField.getText();
		Person bearbeiter = (Person) this.bearbeiterField.getSelectedItem();
		Duration dauer = null;
		try {
			dauer = Duration.ofMinutes(Long.parseLong(dauerField.getText()));
		} catch (NumberFormatException e) {
			
		}
		BearbeitungOrdner.getInstanz(aufgabe).makeAufgabe(aufgabe, bearbeiter, start, dauer, bemerkung);
	}

	private void updateBearbeitung() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd H:m");
		Date start = null;
		try {
			if (this.startField.getText() != null)
				start = df.parse(this.startField.getText());
		} catch (NullPointerException | ParseException e) {
			ErrorNotifier.log(e);
		}
		String bemerkung = this.bemerkungField.getText();
		Duration dauer = null;
		try {
			dauer = Duration.ofMinutes(Long.parseLong(dauerField.getText()));
		} catch (NumberFormatException e) {
			
		}
		bearbeitung.updateDB(start, dauer, bemerkung);
	}
}
