package de.florian_timm.aufgabenPlaner.gui.table;

import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.table.TableRowSorter;

import de.florian_timm.aufgabenPlaner.entity.Aufgabe;
import de.florian_timm.aufgabenPlaner.entity.Bearbeitung;
import de.florian_timm.aufgabenPlaner.entity.Person;
import de.florian_timm.aufgabenPlaner.entity.ordner.BearbeitungOrdner;
import de.florian_timm.aufgabenPlaner.gui.AufgabenGUI;
import de.florian_timm.aufgabenPlaner.gui.BearbeitungGUI;
import de.florian_timm.aufgabenPlaner.gui.ProjektViewGUI;
import de.florian_timm.aufgabenPlaner.kontroll.BearbeitungNotifier;
import de.florian_timm.aufgabenPlaner.kontroll.EntityListener;

@SuppressWarnings("serial")
public class BearbeitungTable extends Table implements MouseListener, EntityListener, ActionListener {

	private Window window;
	private Aufgabe aufgabe = null;
	private Person person = null;
	private TableRowSorter<BearbeitungTableModel> sorter;
	private int limit = -1;
	private JMenuItem miLoeschen;
	private JMenuItem miBearbeiten;
	private JPopupMenu popup;
	private JMenuItem miAufgabenDetails;
	private JMenuItem miPlus8;
	private JMenuItem miPlus1;
	private JMenuItem miProjektDetails;

	public BearbeitungTable(Window window, Aufgabe aufgabe) {
		super();
		this.aufgabe = aufgabe;
		makeTable(window);
		this.limit = -1;
	}

	public BearbeitungTable(Window window, Person person) {
		this(window, person, -1);
	}

	public BearbeitungTable(Window window, Person person, int limit) {
		super();
		this.limit = limit;
		this.person = person;
		makeTable(window);
	}

	public void makeTable(Window window) {
		this.window = window;

		this.addMouseListener(this);
		BearbeitungNotifier.getInstanz().addListener(this);

		sorter = new TableRowSorter<BearbeitungTableModel>();
		this.setRowSorter(sorter);
		dataChanged();

		popup = new JPopupMenu();

		if (aufgabe == null) {
			miAufgabenDetails = new JMenuItem("Aufgabendetails...");
			miAufgabenDetails.addActionListener(this);
			popup.add(miAufgabenDetails);

			miProjektDetails = new JMenuItem("Projektdetails...");
			miProjektDetails.addActionListener(this);
			popup.add(miProjektDetails);
		}
		miBearbeiten = new JMenuItem("Bearbeiten...");
		miBearbeiten.addActionListener(this);
		popup.add(miBearbeiten);

		popup.addSeparator();

		miPlus1 = new JMenuItem("+ 1 Stunde");
		miPlus1.addActionListener(this);
		popup.add(miPlus1);

		miPlus8 = new JMenuItem("+ 8 Stunden");
		miPlus8.addActionListener(this);
		popup.add(miPlus8);

		popup.addSeparator();

		miLoeschen = new JMenuItem("Löschen...");
		miLoeschen.addActionListener(this);
		popup.add(miLoeschen);
	}

	public void close() {
		BearbeitungNotifier.getInstanz().removeListener(this);
	}

	@Override
	public void dataChanged() {
		System.out.println("AufgabenTable dataChanged");
		BearbeitungTableModel model = null;
		if (aufgabe != null) {
			model = new BearbeitungTableModel(this.aufgabe);
		} else if (person != null) {
			model = new BearbeitungTableModel(this.person, this.limit);
		}
		this.setModel(model);
		sorter.setModel(model);
	}

	@Override
	public void mouseClicked(MouseEvent e) {

		if (e.getButton() == MouseEvent.BUTTON2) {
			Point point = e.getPoint();
			int currentRow = this.rowAtPoint(point);
			System.out.println("Test");
			this.setRowSelectionInterval(currentRow, currentRow);

		}

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
	public void mousePressed(MouseEvent event) {

		if (event.getClickCount() == 2 && this.getSelectedRow() != -1) {
			int row = this.rowAtPoint(event.getPoint());
			Bearbeitung bearbeitung = (Bearbeitung) this.getData(row);
			bearbeiten(bearbeitung);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.isPopupTrigger()) {
			int row = this.rowAtPoint(e.getPoint());
			int column = this.columnAtPoint(e.getPoint());

			if (!this.isRowSelected(row))
				this.changeSelection(row, column, false, false);

			popup.show(e.getComponent(), e.getX(), e.getY());
		}

	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (this.getSelectedRow() != -1) {
			JMenuItem menu = (JMenuItem) event.getSource();
			int row = this.getSelectedRow();
			Bearbeitung bearbeitung = (Bearbeitung) this.getData(row);
			if (menu == miLoeschen) {
				loeschen(bearbeitung);
			} else if (menu == miAufgabenDetails) {
				new AufgabenGUI(this.window, bearbeitung.getAufgabe());
			} else if (menu == miProjektDetails) {
				new ProjektViewGUI(window, bearbeitung.getAufgabe().getProjekt());
			} else if (menu == miBearbeiten) {
				bearbeiten(bearbeitung);
			} else if (menu == miPlus8) {
				bearbeitung.plus(7);
			} else if (menu == miPlus1) {
				bearbeitung.plus(1);
			}
		}
	}

	private void bearbeiten(Bearbeitung bearbeitung) {
		// TODO Auto-generated method stub
		// JOptionPane.showMessageDialog(window, "noch nicht implementiert", "Warnung",
		// JOptionPane.INFORMATION_MESSAGE);
		new BearbeitungGUI(window, bearbeitung);
	}

	private void loeschen(Bearbeitung bearbeitung) {
		int ans = JOptionPane.showConfirmDialog(this, "Möchten Sie die Bearbeitung wirklich löschen?", "Löschen",
				JOptionPane.OK_CANCEL_OPTION);
		if (ans == JOptionPane.OK_OPTION) {
			BearbeitungOrdner.getInstanz(bearbeitung.getAufgabe()).removeFromDB(bearbeitung.getId());
		}
	}
}
