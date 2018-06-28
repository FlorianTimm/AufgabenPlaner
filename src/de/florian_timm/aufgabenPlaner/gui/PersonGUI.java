package de.florian_timm.aufgabenPlaner.gui;

import de.florian_timm.aufgabenPlaner.entity.Person;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PersonGUI extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JTextField usernameField, nameField, vornameField, emailField;
	private Person person = null;

	public PersonGUI(Window window) {
		super(window, "Person");
		makeWindow();
	}
	
	public PersonGUI(Window window, String username) {
		super(window, "Person");
		makeWindow();
		usernameField.setText(username);
		usernameField.setEditable(false);
	}

	public PersonGUI(Window window, Person person) {
		this(window);
		this.person = person;

		usernameField.setText(person.toString());
		nameField.setText(person.getNachName());
		emailField.setText(person.getEmail());
		vornameField.setText(person.getVorname());
	}

	private void makeWindow() {

		Container cp = this.getContentPane();

		JLabel usernameLabel = new JLabel("Username");
		usernameField = new JTextField();

		JLabel nameLabel = new JLabel("Name");
		nameField = new JTextField();

		JLabel vornameLabel = new JLabel("Vorname");
		vornameField = new JTextField();

		JLabel emailLabel = new JLabel("Email");
		emailField = new JTextField();
		emailLabel.setVisible(false);
		emailField.setVisible(false);

		JButton okButton = new JButton("Speichern");
		okButton.addActionListener(this);

		GroupLayout layout = new GroupLayout(cp);
		cp.setLayout(layout);

		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(usernameLabel)
						.addComponent(nameLabel).addComponent(vornameLabel).addComponent(emailLabel))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(usernameField)
						.addComponent(nameField).addComponent(vornameField).addComponent(emailField)
						.addComponent(okButton))
				);

		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(usernameLabel)
						.addComponent(usernameField))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(nameLabel)
						.addComponent(nameField))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(vornameLabel)
						.addComponent(vornameField))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(emailLabel)
						.addComponent(emailField))
				.addComponent(okButton));

		this.pack();
	}

	public void makePerson() {
		String username = this.usernameField.getText();
		String name = this.nameField.getText();
		String vorname = this.vornameField.getText();
		String email = this.emailField.getText();
		Person.makePerson(username, name, vorname, email);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (this.person == null)
			makePerson();
		else
			updatePerson();
		this.setVisible(false);
		this.dispose();
	}

	private void updatePerson() {
		String username = this.usernameField.getText();
		String name = this.nameField.getText();
		String vorname = this.vornameField.getText();
		String email = this.nameField.getText();
		person.update(username, name, vorname, email);
	}
}
