package de.florian_timm.aufgabenPlaner.gui.panels;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.jdatepicker.DateModel;
import org.jdatepicker.JDatePicker;

import de.florian_timm.aufgabenPlaner.entity.Aufgabe;
import de.florian_timm.aufgabenPlaner.entity.Person;
import de.florian_timm.aufgabenPlaner.entity.Projekt;
import de.florian_timm.aufgabenPlaner.entity.Status;
import de.florian_timm.aufgabenPlaner.entity.ordner.AufgabenOrdner;
import de.florian_timm.aufgabenPlaner.entity.ordner.StatusOrdner;
import de.florian_timm.aufgabenPlaner.gui.PersonGUI;
import de.florian_timm.aufgabenPlaner.gui.comp.PersonChooser;
import de.florian_timm.aufgabenPlaner.kontroll.PersonenNotifier;

@SuppressWarnings("serial")
public class AufgabenPanel extends JPanel implements ActionListener {
	private JTextField titelField;
	private JTextArea beschreibungField;
	private PersonChooser bearbeiterField;
	private JDatePicker faelligkeitField;
	private JComboBox<Status> statusField;
	private Projekt projekt;
	private Aufgabe aufgabe = null;
	private Window window;
	
	
	public AufgabenPanel(Window window, Projekt projekt) {
		super();
		makePanel(window);
		this.projekt = projekt;
	}

	public AufgabenPanel(Window window, Aufgabe aufgabe) {
		super();
		this.aufgabe = aufgabe;
		makePanel(window);
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
	}
	

	private void makePanel(Window window) {
		this.window= window;
		JLabel titelLabel = new JLabel("Titel");
		titelField = new JTextField();

		JLabel beschreibungLabel = new JLabel("Beschreibung");
		beschreibungField = new JTextArea(8, 35);
		beschreibungField.setLineWrap(true);
		beschreibungField.setWrapStyleWord(true);
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

		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
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
			window.setVisible(false);
			window.dispose();
			AufgabenOrdner.getInstanz(this.aufgabe.getProjekt()).removeFromDB(this.aufgabe.getId());
		}
	}

	private void newPerson() {
		PersonGUI pgui = new PersonGUI(window);
		pgui.setVisible(true);
	}

	private void speichern() {
		if (this.aufgabe == null)
			makeAufgabe();
		else
			updateAufgabe();
		window.setVisible(false);
		window.dispose();
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

	public void close() {
		bearbeiterField.close();
	}

}
