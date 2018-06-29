package de.florian_timm.aufgabenPlaner;

import de.florian_timm.aufgabenPlaner.entity.Aufgabe;
import de.florian_timm.aufgabenPlaner.entity.Person;
import de.florian_timm.aufgabenPlaner.entity.Projekt;
import de.florian_timm.aufgabenPlaner.gui.PersonGUI;
import de.florian_timm.aufgabenPlaner.gui.ProjektGUI;
import de.florian_timm.aufgabenPlaner.gui.ProjektViewGUI;
import de.florian_timm.aufgabenPlaner.gui.table.ProgressCellRender;
import de.florian_timm.aufgabenPlaner.gui.table.ProjektTableModel;
import de.florian_timm.aufgabenPlaner.gui.table.Table;
import de.florian_timm.aufgabenPlaner.kontroll.EntityListener;
import de.florian_timm.aufgabenPlaner.kontroll.ErrorHub;
import de.florian_timm.aufgabenPlaner.kontroll.ErrorListener;
import de.florian_timm.aufgabenPlaner.schnittstelle.DatenhaltungS;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AufgabenPlanerGUI extends JFrame
		implements ActionListener, MouseListener, EntityListener, WindowListener, ErrorListener {

	/**
	 * GUI
	 */
	private static final long serialVersionUID = 1L;
	Table projektTable;

	public AufgabenPlanerGUI(String dateiname) {

		super("Aufgabenplaner");
		ErrorHub.addListener(this);

		DatenhaltungS.setSourceFile(dateiname);

		String username = System.getProperty("user.name");
		int counter = 0;
		Person nutzer = null;
		while ((nutzer = Person.getPerson(username)) == null) {
			PersonGUI pgui = new PersonGUI(this, username);
			pgui.setModal(true);
			pgui.setVisible(true);
			counter++;
			if (counter > 2) {
				close();
			}
		}
		Person.setNutzer(nutzer);

		Container cp = this.getContentPane();
		cp.setLayout(new BorderLayout());

		this.setTitle(this.getTitle() + " für " + nutzer);
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

		JMenuBar menuBar = new JMenuBar();
		JMenu mDatei = new JMenu("Datei");
		menuBar.add(mDatei);
		JMenu mHilfe = new JMenu("?");
		menuBar.add(mHilfe);
		JMenuItem miBeenden = new JMenuItem("Beenden");
		miBeenden.addActionListener(this);
		JMenuItem miUeber = new JMenuItem("Über...");
		miUeber.setActionCommand("ueber");
		miUeber.addActionListener(this);
		mDatei.add(miBeenden);
		mHilfe.add(miUeber);
		this.setJMenuBar(menuBar);

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
			break;
		case "Beenden":
			close();
			break;
		case "ueber":
			JOptionPane.showMessageDialog(this, "Florian Timm\nLandesbetrieb Geoinformation und Vermessung", "AufgabenPlaner", JOptionPane.INFORMATION_MESSAGE);
			break;
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
		// System.out.println("dataChanged AufgabenPlanerGUI");
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
		close();
	}

	private void close() {
		this.setVisible(false);
		this.dispose();
		DatenhaltungS.close();
		System.exit(0);
	}

	@Override
	public void errorInformation(Exception e) {
		JOptionPane.showMessageDialog(this, e.getLocalizedMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
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
