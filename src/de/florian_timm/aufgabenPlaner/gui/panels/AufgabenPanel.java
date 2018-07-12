package de.florian_timm.aufgabenPlaner.gui.panels;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.SimpleDateFormat;
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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

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

@SuppressWarnings("serial")
public class AufgabenPanel extends JPanel implements ActionListener, DocumentListener, ItemListener, ChangeListener {
	private JTextField titelField;
	private JTextArea beschreibungField;
	private PersonChooser bearbeiterField;
	private JDatePicker faelligkeitField;
	private JComboBox<Status> statusField;
	private Projekt projekt;
	private Aufgabe aufgabe = null;
	private Window window;
	private JButton okButton;
	private JButton delButton;

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

	public boolean isVeraendert() {
		if (aufgabe != null) {
			DateModel<?> m = faelligkeitField.getModel();
			Date d = (Date) m.getValue();
			SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
			if (!titelField.getText().equals(aufgabe.toString())) {
				return true;
			} else if (!beschreibungField.getText().equals(aufgabe.getBeschreibung())) {
				return true;
			} else if (!bearbeiterField.getSelectedItem().equals(aufgabe.getBearbeiter())) {
				return true;
			} else if (!statusField.getSelectedItem().equals(aufgabe.getStatus())) {
				return true;
			} else if (!fmt.format(d).equals(fmt.format(aufgabe.getFaelligkeit()))) {
				return true;
			}
		} else {
			if (!titelField.getText().equals("")) {
				return true;
			} else if (!beschreibungField.getText().equals("")) {
				return true;
			}

		}
		return false;
	}

	private void makePanel(Window window) {
		this.window = window;
		JLabel titelLabel = new JLabel("Titel");
		titelField = new JTextField();
		titelField.getDocument().addDocumentListener(this);

		JLabel beschreibungLabel = new JLabel("Beschreibung");
		beschreibungField = new JTextArea(8, 35);
		beschreibungField.setLineWrap(true);
		beschreibungField.setWrapStyleWord(true);
		beschreibungField.getDocument().addDocumentListener(this);
		JScrollPane jsp = new JScrollPane(beschreibungField);

		JLabel bearbeiterLabel = new JLabel("Bearbeiter");
		bearbeiterField = new PersonChooser();
		bearbeiterField.addItemListener(this);

		JButton neuPerson = new JButton("neu...");
		neuPerson.addActionListener(this);
		neuPerson.setActionCommand("neuePerson");

		JLabel faelligkeitLabel = new JLabel("Fällig am");
		Date eineWoche = new Date(System.currentTimeMillis() + 3600 * 1000 * 24 * 7);
		faelligkeitField = new JDatePicker(eineWoche);
		faelligkeitField.getModel().addChangeListener(this);

		JLabel statusLabel = new JLabel("Status");
		statusField = new JComboBox<Status>(StatusOrdner.getInstanz().getArray());
		statusField.addItemListener(this);

		okButton = new JButton("Speichern");
		okButton.addActionListener(this);
		okButton.setEnabled(false);

		delButton = new JButton("Löschen");
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

	public void speichern() {
		if (this.aufgabe == null) {
			makeAufgabe();
			close();
			window.dispose();
		} else {
			updateAufgabe();
			this.checkModified();
		}
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

	private void checkModified() {
		if (isVeraendert()) {
			okButton.setEnabled(true);
		} else {
			okButton.setEnabled(false);
		}
	}

	@Override
	public void changedUpdate(DocumentEvent arg0) {
		checkModified();
	}

	@Override
	public void insertUpdate(DocumentEvent arg0) {
		checkModified();
	}

	@Override
	public void removeUpdate(DocumentEvent arg0) {
		checkModified();
	}

	@Override
	public void itemStateChanged(ItemEvent arg0) {
		checkModified();
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		checkModified();
	}
}
