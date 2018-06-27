package de.florian_timm.aufgabenPlaner.gui;

import java.awt.Container;
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

import de.florian_timm.aufgabenPlaner.entity.Kostentraeger;
import de.florian_timm.aufgabenPlaner.entity.Person;
import de.florian_timm.aufgabenPlaner.entity.Prioritaet;
import de.florian_timm.aufgabenPlaner.entity.Projekt;

public class NeuesProjektGUI extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JTextField titelField;
	private JTextArea beschreibungField;
	private JComboBox<Prioritaet> prioField;
	private JComboBox<Person> zustaendigField, auftraggeberField;
	private JComboBox<Kostentraeger> kostentraegerField;
	private JDatePicker faelligkeitField;

	public NeuesProjektGUI(JFrame frame) {
		super(frame);

		Container cp = this.getContentPane();

		JLabel titelLabel = new JLabel("Titel");
		titelField = new JTextField();

		JLabel beschreibungLabel = new JLabel("Beschreibung");
		beschreibungField = new JTextArea(3,20);

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

		GroupLayout layout = new GroupLayout(cp);
		cp.setLayout(layout);

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
						.addComponent(zustaendigField)
						.addComponent(neuPerson))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(auftraggeberLabel)
						.addComponent(auftraggeberField))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(kostentraegerLabel)
						.addComponent(kostentraegerField))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(faelligkeitLabel)
						.addComponent(faelligkeitField))
				.addComponent(okButton));

		this.pack();
		this.setVisible(true);

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

	@Override
	public void actionPerformed(ActionEvent e) {
		makeProjekt();
		this.setVisible(false);
	}
}
