package de.florian_timm.aufgabenPlaner.gui.panels;

import java.awt.Window;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import de.florian_timm.aufgabenPlaner.entity.ordner.PersonenOrdner;
import de.florian_timm.aufgabenPlaner.gui.table.AufgabenTable;
import de.florian_timm.aufgabenPlaner.gui.table.ProjektTable;

@SuppressWarnings("serial")
public class DashboardPanel extends JPanel {
	private ProjektTable faelligeProjekte;
	private AufgabenTable neusteAufgaben;

	public DashboardPanel(Window window) {
		super();

		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.add(Box.createHorizontalGlue());
		this.add(new JLabel("Bald fällige Projekte"));
		faelligeProjekte = new ProjektTable(window, PersonenOrdner.getInstanz().getNutzer(), 3);
		this.add(new JScrollPane(faelligeProjekte));
		this.add(new JLabel("Fällige Aufgaben"));
		neusteAufgaben = new AufgabenTable(window, PersonenOrdner.getInstanz().getNutzer(), 3);
		this.add(new JScrollPane(neusteAufgaben));
		this.add(new JLabel("Begonne Aufgaben"));
	}
	
	public void close() {
		faelligeProjekte.close();
		neusteAufgaben.close();
	}
	
}
