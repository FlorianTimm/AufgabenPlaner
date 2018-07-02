package de.florian_timm.aufgabenPlaner;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import de.florian_timm.aufgabenPlaner.entity.Person;
import de.florian_timm.aufgabenPlaner.gui.PersonGUI;
import de.florian_timm.aufgabenPlaner.gui.comp.MenuBar;
import de.florian_timm.aufgabenPlaner.gui.panels.ProjektUebersichtPanel;
import de.florian_timm.aufgabenPlaner.gui.table.Table;
import de.florian_timm.aufgabenPlaner.kontroll.ErrorHub;
import de.florian_timm.aufgabenPlaner.kontroll.ErrorListener;
import de.florian_timm.aufgabenPlaner.schnittstelle.DatenhaltungS;

public class AufgabenPlanerGUI extends JFrame
		implements WindowListener, ErrorListener {

	/**
	 * GUI
	 */
	private static final long serialVersionUID = 1L;
	Table projektTable;
	private ProjektUebersichtPanel projektUerbersichtPanel;
	private ProjektUebersichtPanel projektMeinePanel;

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
		this.setTitle(this.getTitle() + " für " + nutzer);
		this.setJMenuBar(new MenuBar(this));
		
		Container cp = this.getContentPane();
		projektUerbersichtPanel = new ProjektUebersichtPanel(this);
		projektMeinePanel = new ProjektUebersichtPanel(this, Person.getNutzer());
		
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Aktuelles", new JPanel());
		tabbedPane.addTab("Offene Aufgaben", new JPanel());
		tabbedPane.addTab("Meine Projekte", projektMeinePanel);
		tabbedPane.addTab("Alle Projekte", projektUerbersichtPanel);
		cp.add(tabbedPane);
		
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setPreferredSize(new Dimension(600, 400));
		this.pack();
		this.setLocationRelativeTo(null);
		this.addWindowListener(this);
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
	public void windowClosing(WindowEvent arg0) {
		close();
	}

	private void close() {
		projektUerbersichtPanel.close();
		this.setVisible(false);
		this.dispose();
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}

