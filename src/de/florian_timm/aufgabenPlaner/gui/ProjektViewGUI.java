package de.florian_timm.aufgabenPlaner.gui;

import de.florian_timm.aufgabenPlaner.entity.Aufgabe;
import de.florian_timm.aufgabenPlaner.entity.Projekt;
import de.florian_timm.aufgabenPlaner.gui.panels.ProjektPanel;
import de.florian_timm.aufgabenPlaner.gui.table.AufgabenTableModel;
import de.florian_timm.aufgabenPlaner.gui.table.Table;
import de.florian_timm.aufgabenPlaner.kontroll.EntityListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ProjektViewGUI extends JDialog implements ActionListener, MouseListener, EntityListener, WindowListener {
	private static final long serialVersionUID = 1L;
	private Projekt projekt;
	private Table aufgabenTable;
	private ProjektPanel projektPanel;

	public ProjektViewGUI(Window window, Projekt projekt) {
		super(window, projekt.toString(), Dialog.ModalityType.DOCUMENT_MODAL);
		this.setTitle(projekt.getTitel());
		this.projekt = projekt;

		Container cp = this.getContentPane();
		cp.setLayout(new BorderLayout());

		projektPanel = new ProjektPanel(this, projekt);
		cp.add(projektPanel, BorderLayout.NORTH);

		aufgabenTable = new Table();
		JScrollPane jsp = new JScrollPane(aufgabenTable);
		dataChanged();
		cp.add(jsp, BorderLayout.CENTER);

		aufgabenTable.addMouseListener(this);

		JButton neu = new JButton("neue Aufgabe");
		neu.addActionListener(this);
		cp.add(neu, BorderLayout.SOUTH);

		this.setPreferredSize(new Dimension(500, 500));
		this.pack();
		this.setLocationRelativeTo(window);
		this.setVisible(true);

		Aufgabe.addListener(this);
		this.addWindowListener(this);

		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

	protected void openAufgabe(Aufgabe aufgabe) {
		AufgabenGUI agui = new AufgabenGUI(this, aufgabe);

		agui.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				projekt.reload();
				dataChanged();
			}
		});
		agui.setVisible(true);

	}

	public void close() {
		projektPanel.close();
		Aufgabe.removeListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		AufgabenGUI agui = new AufgabenGUI(this, projekt);

		agui.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				projekt.reload();
				dataChanged();
			}
		});
		agui.setVisible(true);
	}

	@Override
	public void dataChanged() {
		System.out.println("ProjektGUI dataChanged");
		aufgabenTable.setModel(new AufgabenTableModel(this.projekt));
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent mouseEvent) {

		JTable table = (JTable) mouseEvent.getSource();
		Point point = mouseEvent.getPoint();
		int row = table.rowAtPoint(point);
		if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
			Aufgabe a = (Aufgabe) aufgabenTable.getData(row);
			System.out.println("Zeile:" + row + "(" + a.toString() + ")");
			openAufgabe(a);

		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

		this.close();
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
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
