package de.florian_timm.aufgabenPlaner.gui;

import de.florian_timm.aufgabenPlaner.entity.Projekt;
import de.florian_timm.aufgabenPlaner.gui.panels.ProjektPanel;
import de.florian_timm.aufgabenPlaner.gui.table.AufgabenTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ProjektViewGUI extends JDialog implements ActionListener, WindowListener {
	private static final long serialVersionUID = 1L;
	private Projekt projekt;
	private AufgabenTable aufgabenTable;
	private ProjektPanel projektPanel;

	public ProjektViewGUI(Window window, Projekt projekt) {
		super(window, projekt.toString(), Dialog.ModalityType.DOCUMENT_MODAL);
		this.setTitle(projekt.getTitel());
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

		this.setPreferredSize(new Dimension(500, 500));
		this.pack();
		this.setLocationRelativeTo(window);
		this.setVisible(true);


		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}
	
	public void close() {
		aufgabenTable.close();
		projektPanel.close();
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
		this.close();
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		this.close();
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
