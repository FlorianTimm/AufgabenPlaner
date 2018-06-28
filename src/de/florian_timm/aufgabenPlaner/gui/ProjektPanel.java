package de.florian_timm.aufgabenPlaner.gui;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.jdatepicker.DateModel;
import org.jdatepicker.JDatePicker;

import de.florian_timm.aufgabenPlaner.entity.Kostentraeger;
import de.florian_timm.aufgabenPlaner.entity.Person;
import de.florian_timm.aufgabenPlaner.entity.Prioritaet;
import de.florian_timm.aufgabenPlaner.entity.Projekt;

@SuppressWarnings("serial")
public class ProjektPanel extends JPanel implements ActionListener {
	private JTextField titelField;
	private JTextArea beschreibungField;
	private JComboBox<Prioritaet> prioField;
	private JComboBox<Person> zustaendigField, auftraggeberField;
	private JComboBox<Kostentraeger> kostentraegerField;
	private JDatePicker faelligkeitField;
	private Projekt projekt = null;
	private Window window2Close = null;

	public ProjektPanel() {
		makeWindow();
	}

	public ProjektPanel(Projekt projekt) {
		this();

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

		JLabel prioLabel = new JLabel("Prio");
		prioField = new JComboBox<Prioritaet>(Prioritaet.getArray());

		JLabel zustaendigLabel = new JLabel("Zuständig");
		zustaendigField = new JComboBox<Person>(Person.getArray());
		JButton neuPerson = new JButton("neu...");

		JLabel auftraggeberLabel = new JLabel("Auftraggeber");
		auftraggeberField = new JComboBox<Person>(Person.getArray());

		JLabel kostentraegerLabel = new JLabel("Kostenträger");
		kostentraegerField = new JComboBox<Kostentraeger>(Kostentraeger.getArray());

		JLabel faelligkeitLabel = new JLabel("Fällig am");
		Date eineWoche = new Date(System.currentTimeMillis() + 3600 * 1000 * 24 * 7);
		faelligkeitField = new JDatePicker(eineWoche);

		JButton okButton = new JButton("Speichern");
		okButton.addActionListener(this);

		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);

		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(titelLabel)
						.addComponent(beschreibungLabel).addComponent(prioLabel).addComponent(zustaendigLabel)
						.addComponent(auftraggeberLabel).addComponent(kostentraegerLabel)
						.addComponent(faelligkeitLabel))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(titelField)
						.addComponent(beschreibungField).addComponent(prioField).addComponent(zustaendigField)
						.addComponent(auftraggeberField).addComponent(kostentraegerField).addComponent(faelligkeitField)
						.addComponent(okButton))
				.addComponent(neuPerson));

		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(titelLabel)
						.addComponent(titelField))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(beschreibungLabel)
						.addComponent(beschreibungField))
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
				.addComponent(okButton));
	}

	public void makeProjekt() {
		String titel = this.titelField.getText();
		String beschreibung = this.beschreibungField.getText();
		Prioritaet prio = (Prioritaet) this.prioField.getSelectedItem();
		Person zustaendig = (Person) this.zustaendigField.getSelectedItem();
		Person auftraggeber = (Person) this.auftraggeberField.getSelectedItem();
		Kostentraeger kostentraeger = (Kostentraeger) this.kostentraegerField.getSelectedItem();
		Date faelligkeit = (Date) this.faelligkeitField.getModel().getValue();
		Projekt.makeProjekt(titel, beschreibung, prio, zustaendig, kostentraeger, faelligkeit, auftraggeber);
	}
	
	public void setWindow2Close(Window window) {
		this.window2Close = window;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (projekt == null) {
			makeProjekt();
		} else {
			updateProjekt();
		}
		if (window2Close != null) {
			window2Close.setVisible(false);
			window2Close.dispose();
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
		projekt.update(titel, beschreibung, prio, zustaendig, kostentraeger, faelligkeit, auftraggeber);
		
	}
}
