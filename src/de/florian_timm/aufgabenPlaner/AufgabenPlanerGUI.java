package de.florian_timm.aufgabenPlaner;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import de.florian_timm.aufgabenPlaner.entity.Aufgabe;
import de.florian_timm.aufgabenPlaner.entity.Person;

public class AufgabenPlanerGUI extends JFrame implements ActionListener {

	/**
	 * GUI
	 */
	private static final long serialVersionUID = 1L;
	private DatenhaltungS data = null;
	Person nutzer = null;
	int nutzerid = 0;

	public AufgabenPlanerGUI(String dateiname) {

		super("Aufgabenplaner");

		data = new DatenhaltungS(dateiname);

		String username = System.getProperty("user.name");
		try {
			nutzer = data.getPerson(username);

		} catch (PersonNotFoundException pnf) {
			JTextField name = new JTextField();
			JTextField email = new JTextField();
			Object[] message = { "Name", name, "Email", email };

			JOptionPane pane = new JOptionPane(message, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
			pane.createDialog(null, "Neuer Nutzer").setVisible(true);

			// If a string was returned, say so.
			if ((name.getText() != null) && (name.getText().length() > 0)) {
				try {
					nutzer = data.newPerson(username, name.getText(), email.getText());
				} catch (PersonNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.exit(0);
				}
			} else {
				System.exit(0);
			}
		}

		nutzerid = nutzer.getId();
		this.setTitle(this.getTitle() +  " f�r " + nutzer.getName());
		List<Aufgabe> aufgaben = data.getAufgaben(System.getProperty("user.name"));

		for (Aufgabe a : aufgaben) {
			a.getBeschreibung();
		}

		Container cp = this.getContentPane();
		
		JButton neueAufgabe = new JButton("Neue Aufgabe");
		neueAufgabe.setActionCommand("neueAufgabe");
		neueAufgabe.addActionListener(this);
		cp.add(neueAufgabe);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);

	}

	public static void main(String[] args) {

		String dateiname = "aufgaben.db";
		if (args.length > 0) {
			dateiname = args[0];
		}

		new AufgabenPlanerGUI(dateiname);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		switch (ae.getActionCommand()) {
		case "neueAufgabe":
			neueAufgabe();
		}
	}
	
	private void neueAufgabe () {
		NeueAufgabeGUI nag = new NeueAufgabeGUI(this, this.data);
	}
}
