package de.florian_timm.aufgabenPlaner.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import de.florian_timm.aufgabenPlaner.entity.Aufgabe;
import de.florian_timm.aufgabenPlaner.entity.Projekt;
import de.florian_timm.aufgabenPlaner.gui.table.AufgabenTableModel;
import de.florian_timm.aufgabenPlaner.gui.table.Table;
import de.florian_timm.aufgabenPlaner.kontroll.EntityListener;

public class ProjektViewGUI extends JDialog implements ActionListener, MouseListener, EntityListener {
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
		
		projektPanel = new ProjektPanel(projekt);
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
		this.setVisible(true);

		Aufgabe.addListener(this);
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
		System.out.println("dataChanged ProjektWindow");
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
}
