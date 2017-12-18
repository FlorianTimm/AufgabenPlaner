package de.florian_timm.aufgabenPlaner;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class AufgabenPlanerGUI extends JFrame implements ActionListener {

	/**
	 * GUI
	 */
	private static final long serialVersionUID = 1L;
	private DatenSpeicher data = null;
	Person nutzer = null;

	public AufgabenPlanerGUI(String dateiname) {

		super("Aufgabenplaner");

		data = new DatenSpeicher(dateiname);

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
			}
		}
		Aufgabe[] aufgaben = data.getAufgaben(System.getProperty("user.name"));

		for (Aufgabe a : aufgaben) {
			a.getBeschreibung();
		}

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);

	}

	public static void main(String[] args) {

		String dateiname = "test.db";
		if (args.length > 0) {
			dateiname = args[0];
		}

		new AufgabenPlanerGUI(dateiname);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {

	}
}
