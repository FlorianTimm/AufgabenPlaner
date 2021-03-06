package de.florian_timm.aufgabenPlaner.gui;

import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import de.florian_timm.aufgabenPlaner.AufgabenPlaner;
import de.florian_timm.aufgabenPlaner.entity.Person;
import de.florian_timm.aufgabenPlaner.entity.ordner.PersonenOrdner;
import de.florian_timm.aufgabenPlaner.gui.comp.MenuBar;
import de.florian_timm.aufgabenPlaner.gui.panels.DashboardPanel;
import de.florian_timm.aufgabenPlaner.gui.panels.OffeneAufgabenPanel;
import de.florian_timm.aufgabenPlaner.gui.panels.ProjektUebersichtPanel;
import de.florian_timm.aufgabenPlaner.gui.table.Table;
import de.florian_timm.aufgabenPlaner.kontroll.ErrorNotifier;
import de.florian_timm.aufgabenPlaner.kontroll.ErrorListener;

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
	private DashboardPanel dashboard;

	public AufgabenPlanerGUI(AufgabenPlaner ap) {

		super("AufgabenPlaner");
		this.ap = ap;
		ErrorNotifier.addListener(this);

		String username = System.getProperty("user.name");
		int counter = 0;
		Person nutzer = null;
		while ((nutzer = PersonenOrdner.getInstanz().getPerson(username)) == null) {
			PersonGUI pgui = new PersonGUI(this, username);
			pgui.setModal(true);
			pgui.setVisible(true);
			counter++;
			if (counter > 2) {
				ap.close();
			}
		}
		PersonenOrdner.getInstanz().setNutzer(nutzer);
		this.setTitle(this.getTitle() + " für " + nutzer);
		this.setJMenuBar(new MenuBar(this));

		Container cp = this.getContentPane();
		dashboard = new DashboardPanel(this);
		projektUerbersichtPanel = new ProjektUebersichtPanel(this);
		projektMeinePanel = new ProjektUebersichtPanel(this, PersonenOrdner.getInstanz().getNutzer(), true);
		offeneAufgabenPanel = new OffeneAufgabenPanel(this, PersonenOrdner.getInstanz().getNutzer());

		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Aktuelles", dashboard);
		tabbedPane.addTab("Offene Aufgaben", offeneAufgabenPanel);
		tabbedPane.addTab("Meine Projekte", projektMeinePanel);
		tabbedPane.addTab("Alle Projekte", projektUerbersichtPanel);
		cp.add(tabbedPane);

		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setPreferredSize(new Dimension(900, 800));
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);

	}

	public void exit() {
		ap.close();
	}

	public void close() {
		if (dashboard != null)
			dashboard.close();
		if (projektUerbersichtPanel != null)
			projektUerbersichtPanel.close();
		if (projektMeinePanel != null)
			projektMeinePanel.close();
		if (offeneAufgabenPanel != null)
			offeneAufgabenPanel.close();
	}

	@Override
	public void errorInformation(Exception e) {
		JOptionPane.showMessageDialog(this, e.getLocalizedMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
	}

}
