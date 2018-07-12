package de.florian_timm.aufgabenPlaner.gui;

import de.florian_timm.aufgabenPlaner.entity.Projekt;
import de.florian_timm.aufgabenPlaner.gui.comp.ClosableComponent;
import de.florian_timm.aufgabenPlaner.gui.panels.ProjektPanel;
import de.florian_timm.aufgabenPlaner.gui.table.AufgabenTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ProjektViewGUI extends JDialog implements ActionListener, WindowListener, ClosableComponent {
	private static final long serialVersionUID = 1L;
	private Projekt projekt;
	private AufgabenTable aufgabenTable;
	private ProjektPanel projektPanel;

	public ProjektViewGUI(Window window, Projekt projekt) {
		super(window, projekt.getTitel(), ModalityType.APPLICATION_MODAL);
		this.setModal(true);
		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(this);
		
		this.projekt = projekt;

		Container cp = this.getContentPane();
		cp.setLayout(new BorderLayout());

		projektPanel = new ProjektPanel(this, projekt);
		cp.add(projektPanel, BorderLayout.NORTH);

		aufgabenTable = new AufgabenTable(this, projekt);
		JScrollPane jsp = new JScrollPane(aufgabenTable);
		cp.add(jsp, BorderLayout.CENTER);
		
		JButton neu = new JButton("neue Aufgabe");
		neu.addActionListener(this);
		cp.add(neu, BorderLayout.SOUTH);

		this.setPreferredSize(new Dimension(700, 800));
		this.pack();
		this.setLocationRelativeTo(window);
		this.setVisible(true);
	}
	
	public void close() {
		aufgabenTable.close();
		projektPanel.close();
		this.setModal(false);
		this.setVisible(false);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		AufgabenGUI agui = new AufgabenGUI(this, projekt);
		agui.setVisible(true);
	}
	
	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent arg0) {

	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		System.out.println("Closing...");
		if (projektPanel.isVeraendert()) {
			System.out.println("verändert");
			int ans = JOptionPane.showConfirmDialog(this,
					"Sie haben die Daten verändert. Möchten Sie die Veränderung speichern?", "Daten wurden verändert",
					JOptionPane.YES_NO_CANCEL_OPTION);

			if (ans == JOptionPane.YES_OPTION) {
				System.out.println("YES");
				projektPanel.speichern();
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
		//this.close();
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
