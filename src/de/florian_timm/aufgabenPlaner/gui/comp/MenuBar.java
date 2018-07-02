package de.florian_timm.aufgabenPlaner.gui.comp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import de.florian_timm.aufgabenPlaner.AufgabenPlanerGUI;
import de.florian_timm.aufgabenPlaner.gui.ProjektGUI;

@SuppressWarnings("serial")
public class MenuBar extends JMenuBar implements ActionListener {
	private AufgabenPlanerGUI apgui;

	public MenuBar(AufgabenPlanerGUI apgui) {
		super();
		this.apgui = apgui;

		JMenu mDatei = new JMenu("Datei");
		JMenuItem miBeenden = new JMenuItem("Beenden");
		miBeenden.addActionListener(this);
		mDatei.add(miBeenden);
		this.add(mDatei);

		JMenu mProjekt = new JMenu("Projekt");
		JMenuItem miNeuesProjekt = new JMenuItem("Neues Projekt...");
		miNeuesProjekt.setActionCommand("neuesProjekt");
		miNeuesProjekt.addActionListener(this);
		mProjekt.add(miNeuesProjekt);
		this.add(mProjekt);

		JMenu mHilfe = new JMenu("?");
		JMenuItem miUeber = new JMenuItem("Über...");
		miUeber.setActionCommand("ueber");
		miUeber.addActionListener(this);
		mHilfe.add(miUeber);
		this.add(mHilfe);

	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		switch (ae.getActionCommand()) {
		case "Beenden":
			apgui.dispose();
			break;
		case "ueber":
			JOptionPane.showMessageDialog(this, "Florian Timm\nLandesbetrieb Geoinformation und Vermessung",
					"AufgabenPlaner", JOptionPane.INFORMATION_MESSAGE);
			break;
		case "neuesProjekt":
			new ProjektGUI(apgui);
			break;
		}
	}
}
