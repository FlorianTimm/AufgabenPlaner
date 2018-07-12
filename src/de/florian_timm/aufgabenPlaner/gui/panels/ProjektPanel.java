package de.florian_timm.aufgabenPlaner.gui.panels;

import de.florian_timm.aufgabenPlaner.entity.Kostentraeger;
import de.florian_timm.aufgabenPlaner.entity.Person;
import de.florian_timm.aufgabenPlaner.entity.Prioritaet;
import de.florian_timm.aufgabenPlaner.entity.Projekt;
import de.florian_timm.aufgabenPlaner.entity.ordner.PersonenOrdner;
import de.florian_timm.aufgabenPlaner.entity.ordner.ProjektOrdner;
import de.florian_timm.aufgabenPlaner.gui.PersonGUI;
import de.florian_timm.aufgabenPlaner.gui.comp.ClosableComponent;
import de.florian_timm.aufgabenPlaner.gui.comp.PersonChooser;
import de.florian_timm.aufgabenPlaner.kontroll.ErrorNotifier;

import org.jdatepicker.DateModel;
import org.jdatepicker.JDatePicker;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SuppressWarnings("serial")
public class ProjektPanel extends JPanel implements ActionListener, DocumentListener, ItemListener, ChangeListener {
	private JTextField titelField;
	private JTextArea beschreibungField;
	private JComboBox<Prioritaet> prioField;
	private PersonChooser zustaendigField, auftraggeberField;
	private JComboBox<Kostentraeger> kostentraegerField;
	private JDatePicker faelligkeitField;
	private Projekt projekt = null;
	private Window window;
	private JButton delButton;
	private JButton ordnerButton;
	private JButton neuPerson;
	private JButton okButton;
	private JButton selectOrdnerButton;
	private File ordner = null;

	public ProjektPanel(Window window) {
		this.window = window;
		makeWindow();
	}

	public ProjektPanel(Window window, Projekt projekt) {
		this(window);

		this.projekt = projekt;
		titelField.setText(projekt.getTitel());
		beschreibungField.setText(projekt.getBeschreibung());
		prioField.setSelectedItem(projekt.getPrioritaet());
		zustaendigField.setSelectedItem(projekt.getZustaendig());
		auftraggeberField.setSelectedItem(projekt.getAuftraggeber());
		kostentraegerField.setSelectedItem(projekt.getKostentraeger());
		DateModel<?> m = faelligkeitField.getModel();
		Calendar d = Calendar.getInstance();
		d.setTime(projekt.getFaelligkeit());
		m.setYear(d.get(Calendar.YEAR));
		m.setMonth(d.get(Calendar.MONTH));
		m.setDay(d.get(Calendar.DATE));

		ordner = projekt.getProjektordner();
		if (ordner != null) {
			makeOrdnerButtonText();
			ordnerButton.setEnabled(true);
		}

		delButton.setVisible(true);
	}

	private void makeOrdnerButtonText() {
		String path = ordner.getAbsolutePath();
		if (path.length() >= 50) {
			ordnerButton.setText("..." + path.substring(path.length() - 50));

		} else {
			ordnerButton.setText(path);
		}
		ordnerButton.setToolTipText(path);
	}

	private void makeWindow() {
		JLabel titelLabel = new JLabel("Titel");
		titelField = new JTextField();
		titelField.getDocument().addDocumentListener(this);

		JLabel beschreibungLabel = new JLabel("Beschreibung");
		beschreibungField = new JTextArea(8, 35);
		beschreibungField.setLineWrap(true);
		beschreibungField.setWrapStyleWord(true);
		beschreibungField.getDocument().addDocumentListener(this);
		JScrollPane jsp = new JScrollPane(beschreibungField);

		JLabel prioLabel = new JLabel("Prio");
		prioField = new JComboBox<Prioritaet>(Prioritaet.getArray());
		prioField.addItemListener(this);

		JLabel zustaendigLabel = new JLabel("Zuständig");
		zustaendigField = new PersonChooser();
		zustaendigField.addItemListener(this);

		neuPerson = new JButton("neu...");
		neuPerson.addActionListener(this);
		neuPerson.setActionCommand("neuePerson");

		JLabel auftraggeberLabel = new JLabel("Auftraggeber");
		auftraggeberField = new PersonChooser();
		auftraggeberField.addItemListener(this);

		JLabel kostentraegerLabel = new JLabel("Kostenträger");
		kostentraegerField = new JComboBox<Kostentraeger>(Kostentraeger.getArray());
		kostentraegerField.addItemListener(this);

		JLabel faelligkeitLabel = new JLabel("Fällig am");
		Date eineWoche = new Date(System.currentTimeMillis() + 3600 * 1000 * 24 * 7);
		faelligkeitField = new JDatePicker(eineWoche);
		faelligkeitField.getModel().addChangeListener(this);

		JLabel ordnerLabel = new JLabel("Projektordner");

		ordnerButton = new JButton("                                  ");
		ordnerButton.setEnabled(false);
		ordnerButton.addActionListener(this);
		ordnerButton.setActionCommand("ordnerButton");

		selectOrdnerButton = new JButton("...");
		selectOrdnerButton.addActionListener(this);
		selectOrdnerButton.setActionCommand("selectOrdnerButton");

		okButton = new JButton("Speichern");
		okButton.addActionListener(this);
		okButton.setEnabled(false);

		delButton = new JButton("Löschen");
		delButton.addActionListener(this);
		delButton.setVisible(false);

		GroupLayout layout = new GroupLayout(this);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		this.setLayout(layout);

		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(titelLabel)
						.addComponent(beschreibungLabel).addComponent(prioLabel).addComponent(zustaendigLabel)
						.addComponent(auftraggeberLabel).addComponent(kostentraegerLabel).addComponent(faelligkeitLabel)
						.addComponent(ordnerLabel).addComponent(okButton))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(titelField)
						.addComponent(jsp).addComponent(prioField).addComponent(zustaendigField)
						.addComponent(auftraggeberField).addComponent(kostentraegerField).addComponent(faelligkeitField)
						.addComponent(ordnerButton).addComponent(delButton))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(neuPerson)
						.addComponent(selectOrdnerButton)));

		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(titelLabel)
						.addComponent(titelField))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(beschreibungLabel)
						.addComponent(jsp))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(prioLabel)
						.addComponent(prioField))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(zustaendigLabel)
						.addComponent(zustaendigField).addComponent(neuPerson))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(auftraggeberLabel)
						.addComponent(auftraggeberField))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(kostentraegerLabel)
						.addComponent(kostentraegerField))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(faelligkeitLabel)
						.addComponent(faelligkeitField))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(ordnerLabel)
						.addComponent(selectOrdnerButton).addComponent(ordnerButton))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(okButton)
						.addComponent(delButton)));
	}

	public boolean isVeraendert() {
		if (projekt != null) {
			DateModel<?> m = faelligkeitField.getModel();
			Date d = (Date) m.getValue();
			SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
			if (!titelField.getText().equals(projekt.getTitel())) {
				return true;
			} else if (!beschreibungField.getText().equals(projekt.getBeschreibung())) {
				return true;
			} else if (!zustaendigField.getSelectedItem().equals(projekt.getZustaendig())) {
				return true;
			} else if (!auftraggeberField.getSelectedItem().equals(projekt.getAuftraggeber())) {
				return true;
			} else if (!prioField.getSelectedItem().equals(projekt.getPrioritaet())) {
				return true;
			} else if (!kostentraegerField.getSelectedItem().equals(projekt.getKostentraeger())) {
				return true;
			} else if (!fmt.format(d).equals(fmt.format(projekt.getFaelligkeit()))) {
				return true;
			} else if ((projekt.getProjektordner() == null && ordner != null)
					|| (ordner != null && !ordner.equals(projekt.getProjektordner()))) {
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

	public void makeProjekt() {
		String titel = this.titelField.getText();
		String beschreibung = this.beschreibungField.getText();
		Prioritaet prio = (Prioritaet) this.prioField.getSelectedItem();
		Person zustaendig = (Person) this.zustaendigField.getSelectedItem();
		Person auftraggeber = (Person) this.auftraggeberField.getSelectedItem();
		Kostentraeger kostentraeger = (Kostentraeger) this.kostentraegerField.getSelectedItem();
		Date faelligkeit = (Date) this.faelligkeitField.getModel().getValue();
		ProjektOrdner.getInstanz().makeProjekt(titel, beschreibung, prio, zustaendig, kostentraeger, faelligkeit,
				auftraggeber, ordner);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "Speichern":
			speichern();
			break;
		case "neuePerson":
			neuePerson();
			break;
		case "Löschen":
			loeschen();
			break;
		case "ordnerButton":
			ordnerOeffnen();
			break;
		case "selectOrdnerButton":
			selectOrdner();
			break;
		}
	}

	private void selectOrdner() {
		JFileChooser chooser = new JFileChooser();
		if (ordner != null)
			chooser.setCurrentDirectory(ordner);
		chooser.setDialogTitle("Projektordner wählen");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			ordner = chooser.getSelectedFile();
			makeOrdnerButtonText();
			ordnerButton.setEnabled(true);
			this.checkModified();
		}

	}

	private void ordnerOeffnen() {
		try {
			Runtime.getRuntime().exec("explorer.exe \"" + ordner.getAbsolutePath() + "\"");
		} catch (IOException e) {
			ErrorNotifier.log(e);
		}
	}

	private void loeschen() {
		int ans = JOptionPane.showConfirmDialog(window, "Möchten Sie das Projekt wirklich löschen?", "Löschen",
				JOptionPane.OK_CANCEL_OPTION);
		if (ans == JOptionPane.OK_OPTION) {
			window.setVisible(false);
			ProjektOrdner.getInstanz().removeFromDB(projekt.getId());
		}
	}

	private void neuePerson() {
		PersonGUI pgui = new PersonGUI(window);
		pgui.setVisible(true);
	}

	public void speichern() {
		if (projekt == null) {
			makeProjekt();
			((ClosableComponent) window).close();
		} else {
			updateProjekt();
			this.checkModified();
		}
	}

	private void updateProjekt() {
		String titel = this.titelField.getText();
		String beschreibung = this.beschreibungField.getText();
		Prioritaet prio = (Prioritaet) this.prioField.getSelectedItem();
		Person zustaendig = (Person) this.zustaendigField.getSelectedItem();
		Person auftraggeber = (Person) this.auftraggeberField.getSelectedItem();
		Kostentraeger kostentraeger = (Kostentraeger) this.kostentraegerField.getSelectedItem();
		Date faelligkeit = (Date) this.faelligkeitField.getModel().getValue();
		Person bearbeitetVon = PersonenOrdner.getInstanz().getNutzer();

		projekt.updateDB(titel, beschreibung, prio, zustaendig, kostentraeger, faelligkeit, auftraggeber, ordner,
				bearbeitetVon);
	}

	public void close() {
		auftraggeberField.close();
		zustaendigField.close();
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
