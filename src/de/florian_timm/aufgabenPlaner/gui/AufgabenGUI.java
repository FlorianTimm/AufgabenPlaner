package de.florian_timm.aufgabenPlaner.gui;

import de.florian_timm.aufgabenPlaner.entity.Aufgabe;
import de.florian_timm.aufgabenPlaner.entity.Projekt;
import de.florian_timm.aufgabenPlaner.gui.comp.ClosableComponent;
import de.florian_timm.aufgabenPlaner.gui.panels.AufgabenPanel;
import de.florian_timm.aufgabenPlaner.gui.table.BearbeitungTable;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class AufgabenGUI extends JDialog implements ActionListener, WindowListener, ClosableComponent {
	private static final long serialVersionUID = 1L;
	private Aufgabe aufgabe = null;
	private AufgabenPanel aufgabenPanel;
	private BearbeitungTable bearbeitungTable;

	public AufgabenGUI(Window window, Projekt projekt) {
		super(window, "Neue Aufgabe erfassen", ModalityType.APPLICATION_MODAL);
		aufgabenPanel = new AufgabenPanel(this, projekt);
		makeWindow(window);
	}

	public AufgabenGUI(Window window, Aufgabe aufgabe) {
		super(window, "Aufgabe bearbeiten", ModalityType.APPLICATION_MODAL);
		aufgabenPanel = new AufgabenPanel(this, aufgabe);
		this.aufgabe = aufgabe;
		makeWindow(window);
		this.setModal(true);
	}

	private void makeWindow(Window window) {
		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(this);

		Container cp = this.getContentPane();

		cp.setLayout(new BorderLayout());
		cp.add(aufgabenPanel, BorderLayout.NORTH);

		if (aufgabe != null) {
			bearbeitungTable = new BearbeitungTable(this, aufgabe);
			JScrollPane jsp = new JScrollPane(bearbeitungTable);
			jsp.setPreferredSize(new Dimension(200, 200));
			cp.add(jsp, BorderLayout.CENTER);

			JButton neu = new JButton("Neue Bearbeitung");
			neu.addActionListener(this);
			cp.add(neu, BorderLayout.SOUTH);
		}

		this.pack();
		this.setLocationRelativeTo(window);
		this.setVisible(true);
	}

	public void close() {
		System.out.println("AufgabenGUI close()");
		aufgabenPanel.close();
		if (aufgabe != null) {
			bearbeitungTable.close();
		}
		this.setModal(false);
		this.setVisible(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		new BearbeitungGUI(this, aufgabe);
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		if (aufgabenPanel.isVeraendert()) {
			System.out.println("verändert");
			int ans = JOptionPane.showConfirmDialog(this,
					"Sie haben die Daten verändert. Möchten Sie die Veränderung speichern?", "Daten wurden verändert",
					JOptionPane.YES_NO_CANCEL_OPTION);

			if (ans == JOptionPane.YES_OPTION) {
				System.out.println("YES");
				aufgabenPanel.speichern();
				close();
			} else if (ans == JOptionPane.NO_OPTION) {
				System.out.println("NO");
				close();
			} else if (ans == JOptionPane.CANCEL_OPTION) {
				System.out.println("CANCEL");
				return;
			}

		} else {
			System.out.println("unverändert");
			close();
		}
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
