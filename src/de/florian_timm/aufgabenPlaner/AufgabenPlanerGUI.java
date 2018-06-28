package de.florian_timm.aufgabenPlaner;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import de.florian_timm.aufgabenPlaner.entity.Aufgabe;
import de.florian_timm.aufgabenPlaner.entity.Person;
import de.florian_timm.aufgabenPlaner.entity.Projekt;
import de.florian_timm.aufgabenPlaner.gui.ProjektGUI;
import de.florian_timm.aufgabenPlaner.gui.ProjektViewGUI;
import de.florian_timm.aufgabenPlaner.gui.table.ProgressCellRender;
import de.florian_timm.aufgabenPlaner.gui.table.ProjektTableModel;
import de.florian_timm.aufgabenPlaner.gui.table.Table;
import de.florian_timm.aufgabenPlaner.kontroll.EntityListener;
import de.florian_timm.aufgabenPlaner.schnittstelle.DatenhaltungS;

public class AufgabenPlanerGUI extends JFrame implements ActionListener, MouseListener, EntityListener, WindowListener {

	/**
	 * GUI
	 */
	private static final long serialVersionUID = 1L;
	Person nutzer = null;
	Table projektTable;

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
				nutzer = Person.makePerson(username, name.getText(), email.getText());
			} else {
				System.exit(0);
			}
		}

		Container cp = this.getContentPane();
		cp.setLayout(new BorderLayout());

		this.setTitle(this.getTitle() + " für " + nutzer.getName());
		projektTable = new Table();
		dataChanged();
		JScrollPane jsp = new JScrollPane(projektTable);
		cp.add(jsp, BorderLayout.CENTER);
		Projekt.addListener(this);
		Aufgabe.addListener(this);

		projektTable.addMouseListener(this);

		JButton neueAufgabe = new JButton("Neues Projekt");
		neueAufgabe.setActionCommand("neuesProjekt");
		neueAufgabe.addActionListener(this);
		cp.add(neueAufgabe, BorderLayout.NORTH);

		this.addWindowListener(this);

		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setPreferredSize(new Dimension(600, 400));
		this.setLocationRelativeTo(null);
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
		ProjektGUI npgui = new ProjektGUI(this);
		npgui.setVisible(true);

	}

	private void openProjekt(Projekt projekt) {
		new ProjektViewGUI(this, projekt);
	}

	public void dataChanged() {
		System.out.println("dataChanged AufgabenPlanerGUI");
		projektTable.setModel(new ProjektTableModel());
		projektTable.getColumn("Status").setCellRenderer(new ProgressCellRender());
	}

	@Override
	public void mousePressed(MouseEvent mouseEvent) {

		JTable table = (JTable) mouseEvent.getSource();
		Point point = mouseEvent.getPoint();
		int row = table.rowAtPoint(point);
		if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
			Projekt p = (Projekt) projektTable.getData(row);
			System.out.println("Zeile:" + row + "(" + p.toString() + ")");
			openProjekt(p);

		}
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		this.setVisible(false);
		this.dispose();
		DatenhaltungS.close();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////// Nicht benötigte Listener ////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public void windowActivated(WindowEvent arg0) {
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

}
