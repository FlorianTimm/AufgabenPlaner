package de.florian_timm.aufgabenPlaner;

import java.awt.BorderLayout;

import java.awt.Container;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import de.florian_timm.aufgabenPlaner.Kontroll.NeuesProjektGUI;
import de.florian_timm.aufgabenPlaner.entity.Person;

public class AufgabenPlanerGUI extends JFrame implements ActionListener {

	/**
	 * GUI
	 */
	private static final long serialVersionUID = 1L;
	Person nutzer = null;

	public AufgabenPlanerGUI(String dateiname) {

		super("Aufgabenplaner");

		DatenhaltungS.setSourceFile(dateiname);

		String username = System.getProperty("user.name");
		if ((nutzer = Person.getPerson(username)) == null) {
			JTextField name = new JTextField();
			JTextField email = new JTextField();
			Object[] message = { "Name", name, "Email", email };

			JOptionPane pane = new JOptionPane(message, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
			pane.createDialog(null, "Neuer Nutzer").setVisible(true);

			// If a string was returned, say so.
			if ((name.getText() != null) && (name.getText().length() > 0)) {
				nutzer = Person.newPerson(username, name.getText(), email.getText());
			} else {
				System.exit(0);
			}
		}
		
		Container cp = this.getContentPane();
		cp.setLayout(new BorderLayout());

		this.setTitle(this.getTitle() + " für " + nutzer.getName());
		JTable projektTable = new JTable(new ProjektTable());
		JScrollPane jsp2 = new JScrollPane(projektTable);
		cp.add(jsp2, BorderLayout.CENTER);

		projektTable.addMouseListener(new MouseAdapter() {
		    public void mousePressed(MouseEvent mouseEvent) {
		        JTable table =(JTable) mouseEvent.getSource();
		        Point point = mouseEvent.getPoint();
		        int row = table.rowAtPoint(point);
		        if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
		            new JOptionPane("Zeile:" + row, JOptionPane.OK_OPTION);
		        }
		    }
		});

		JButton neueAufgabe = new JButton("Neues Projekt");
		neueAufgabe.setActionCommand("neuesProjekt");
		neueAufgabe.addActionListener(this);
		cp.add(neueAufgabe, BorderLayout.NORTH);

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
		case "neuesProjekt":
			neuesProjekt();
		}
	}

	private void neuesProjekt() {
		new NeuesProjektGUI(this);
	}
}
