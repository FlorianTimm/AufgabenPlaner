package de.florian_timm.aufgabenPlaner.gui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import de.florian_timm.aufgabenPlaner.AufgabenPlaner;
import de.florian_timm.aufgabenPlaner.entity.Person;
import de.florian_timm.aufgabenPlaner.entity.ordner.PersonenOrdner;
import de.florian_timm.aufgabenPlaner.gui.comp.MenuBar;
import de.florian_timm.aufgabenPlaner.gui.panels.OffeneAufgabenPanel;
import de.florian_timm.aufgabenPlaner.gui.panels.ProjektUebersichtPanel;
import de.florian_timm.aufgabenPlaner.gui.table.Table;
import de.florian_timm.aufgabenPlaner.kontroll.ErrorNotifier;
import de.florian_timm.aufgabenPlaner.kontroll.ErrorListener;
import de.florian_timm.aufgabenPlaner.schnittstelle.DatenHaltung;

public class AufgabenPlanerGUI extends JFrame implements ErrorListener {

	/**
	 * GUI
	 */
	private static final long serialVersionUID = 1L;
	Table projektTable;
	private ProjektUebersichtPanel projektUerbersichtPanel;
	private ProjektUebersichtPanel projektMeinePanel;
	private OffeneAufgabenPanel offeneAufgabenPanel;
	private AufgabenPlaner ap;


	public AufgabenPlanerGUI(AufgabenPlaner ap, String dateiname) {

		super("AufgabenPlaner");
		this.ap = ap;
		ErrorNotifier.addListener(this);

		DatenHaltung.setSourceFile(dateiname);

		String username = System.getProperty("user.name");
		int counter = 0;
		Person nutzer = null;
		while ((nutzer = PersonenOrdner.getInstanz().getPerson(username)) == null) {
			PersonGUI pgui = new PersonGUI(this, username);
			pgui.setModal(true);
			pgui.setVisible(true);
			counter++;
			if (counter > 2) {
				close();
			}
		}
		PersonenOrdner.getInstanz().setNutzer(nutzer);
		this.setTitle(this.getTitle() + " für " + nutzer);
		this.setJMenuBar(new MenuBar(this));

		Container cp = this.getContentPane();
		projektUerbersichtPanel = new ProjektUebersichtPanel(this);
		projektMeinePanel = new ProjektUebersichtPanel(this, PersonenOrdner.getInstanz().getNutzer());
		offeneAufgabenPanel = new OffeneAufgabenPanel(this, PersonenOrdner.getInstanz().getNutzer());

		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Aktuelles", new JPanel());
		tabbedPane.addTab("Offene Aufgaben", offeneAufgabenPanel);
		tabbedPane.addTab("Meine Projekte", projektMeinePanel);
		tabbedPane.addTab("Alle Projekte", projektUerbersichtPanel);
		cp.add(tabbedPane);

		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setPreferredSize(new Dimension(700, 600));
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);

	}
	
	public void exit() {
		ap.close();
	}

	public void close() {
		projektUerbersichtPanel.close();
		projektMeinePanel.close();
		offeneAufgabenPanel.close();
	}

	@Override
	public void errorInformation(Exception e) {
		JOptionPane.showMessageDialog(this, e.getLocalizedMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
	}

}
