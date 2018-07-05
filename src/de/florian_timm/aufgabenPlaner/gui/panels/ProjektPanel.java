package de.florian_timm.aufgabenPlaner.gui.panels;

import de.florian_timm.aufgabenPlaner.entity.Kostentraeger;
import de.florian_timm.aufgabenPlaner.entity.Person;
import de.florian_timm.aufgabenPlaner.entity.Prioritaet;
import de.florian_timm.aufgabenPlaner.entity.Projekt;
import de.florian_timm.aufgabenPlaner.entity.ordner.ProjektOrdner;
import de.florian_timm.aufgabenPlaner.gui.PersonGUI;
import de.florian_timm.aufgabenPlaner.gui.comp.PersonChooser;

import org.jdatepicker.DateModel;
import org.jdatepicker.JDatePicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;

@SuppressWarnings("serial")
public class ProjektPanel extends JPanel implements ActionListener {
	private JTextField titelField;
	private JTextArea beschreibungField;
	private JComboBox<Prioritaet> prioField;
	private PersonChooser zustaendigField, auftraggeberField;
	private JComboBox<Kostentraeger> kostentraegerField;
	private JDatePicker faelligkeitField;
	private Projekt projekt = null;
	private Window window2Close = null;
	private Window window;

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
	}

	private void makeWindow() {
		JLabel titelLabel = new JLabel("Titel");
		titelField = new JTextField();

		JLabel beschreibungLabel = new JLabel("Beschreibung");
		beschreibungField = new JTextArea(3, 20);
		JScrollPane jsp = new JScrollPane(beschreibungField);

		JLabel prioLabel = new JLabel("Prio");
		prioField = new JComboBox<Prioritaet>(Prioritaet.getArray());

		JLabel zustaendigLabel = new JLabel("Zuständig");
		zustaendigField = new PersonChooser();

		JButton neuPerson = new JButton("neu...");
		neuPerson.addActionListener(this);
		neuPerson.setActionCommand("neuePerson");

		JLabel auftraggeberLabel = new JLabel("Auftraggeber");
		auftraggeberField = new PersonChooser();

		JLabel kostentraegerLabel = new JLabel("Kostenträger");
		kostentraegerField = new JComboBox<Kostentraeger>(Kostentraeger.getArray());

		JLabel faelligkeitLabel = new JLabel("Fällig am");
		Date eineWoche = new Date(System.currentTimeMillis() + 3600 * 1000 * 24 * 7);
		faelligkeitField = new JDatePicker(eineWoche);

		JButton okButton = new JButton("Speichern");
		okButton.addActionListener(this);
		JButton delButton = new JButton("Löschen");
		delButton.addActionListener(this);

		GroupLayout layout = new GroupLayout(this);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		this.setLayout(layout);

		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(titelLabel)
						.addComponent(beschreibungLabel).addComponent(prioLabel).addComponent(zustaendigLabel)
						.addComponent(auftraggeberLabel).addComponent(kostentraegerLabel).addComponent(faelligkeitLabel)
						.addComponent(okButton))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(titelField)
						.addComponent(jsp).addComponent(prioField).addComponent(zustaendigField)
						.addComponent(auftraggeberField).addComponent(kostentraegerField).addComponent(faelligkeitField)
						.addComponent(delButton))
				.addComponent(neuPerson));

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
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(okButton)
						.addComponent(delButton)));
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
				auftraggeber);
	}

	public void setWindow2Close(Window window) {
		this.window2Close = window;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Speichern")) {
			if (projekt == null) {
				makeProjekt();
			} else {
				updateProjekt();
			}
			if (window2Close != null) {
				window2Close.setVisible(false);
				window2Close.dispose();
			}
		} else {
			PersonGUI pgui = new PersonGUI(window);
			pgui.setVisible(true);
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
		projekt.updateDB(titel, beschreibung, prio, zustaendig, kostentraeger, faelligkeit, auftraggeber);

	}

	public void close() {
		auftraggeberField.close();
		zustaendigField.close();

	}
}
