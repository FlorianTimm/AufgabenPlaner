package de.florian_timm.aufgabenPlaner.gui.table;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Comparator;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.table.TableRowSorter;

import de.florian_timm.aufgabenPlaner.entity.Person;
import de.florian_timm.aufgabenPlaner.entity.Projekt;
import de.florian_timm.aufgabenPlaner.entity.ordner.ProjektOrdner;
import de.florian_timm.aufgabenPlaner.gui.ProjektViewGUI;
import de.florian_timm.aufgabenPlaner.kontroll.AufgabenNotifier;
import de.florian_timm.aufgabenPlaner.kontroll.EntityListener;
import de.florian_timm.aufgabenPlaner.kontroll.ProjektNotifier;

@SuppressWarnings("serial")
public class ProjektTable extends Table implements MouseListener, EntityListener, ActionListener {
	private Person person = null;
	private TableRowSorter<ProjektTableModel> sorter;
	private int limit = -1;
	private JMenuItem miLoeschen;
	private JMenuItem miDetails;
	private JPopupMenu popup;
	private JMenuItem miPlus1;
	private JMenuItem miPlus7;
	private Window window;

	public ProjektTable(Window window) {
		super();
		makeTable();
	}

	public ProjektTable(Window window, Person person) {
		super();
		this.window = window;
		this.person = person;
		makeTable();
	}

	public ProjektTable(Window window, Person person, int limit) {
		super();
		this.person = person;
		this.limit = limit;
		this.window = window;
		makeTable();
	}

	private void makeTable() {
		this.addMouseListener(this);

		ProjektNotifier.getInstanz().addListener(this);
		AufgabenNotifier.getInstanz().addListener(this);
		
		sorter = new TableRowSorter<ProjektTableModel>();
		this.setRowSorter(sorter);
		dataChanged();
		sorter.setComparator(this.getColumn("Fälligkeit").getModelIndex(), new Comparator<DateRenderable>() {
			public int compare(DateRenderable d1, DateRenderable d2) {
				return d1.compareTo(d2);
			}
		});
		sorter.setComparator(this.getColumn("Status").getModelIndex(), new Comparator<Integer>() {
			@Override
			public int compare(Integer i0, Integer i1) {
				return i0.compareTo(i1);
			}
		});

		popup = new JPopupMenu();

		miDetails = new JMenuItem("Details...");
		miDetails.addActionListener(this);
		popup.add(miDetails);

		popup.addSeparator();

		miPlus1 = new JMenuItem("+ 1 Tag");
		miPlus1.addActionListener(this);
		popup.add(miPlus1);

		miPlus7 = new JMenuItem("+ 7 Tage");
		miPlus7.addActionListener(this);
		popup.add(miPlus7);

		popup.addSeparator();

		miLoeschen = new JMenuItem("Löschen...");
		miLoeschen.addActionListener(this);
		popup.add(miLoeschen);
	}

	@Override
	public void mousePressed(MouseEvent mouseEvent) {
		if (mouseEvent.getClickCount() == 2 && this.getSelectedRow() != -1) {
			int row = this.rowAtPoint(mouseEvent.getPoint());
			Projekt projekt = (Projekt) this.getData(row);
			openProjekt(projekt);
		}
	}

	public void close() {
		ProjektNotifier.getInstanz().removeListener(this);
		AufgabenNotifier.getInstanz().removeListener(this);
	}

	private void openProjekt(Projekt projekt) {
		new ProjektViewGUI(window, projekt);
	}

	public void dataChanged() {
		ProjektTableModel model = null;
		if (person == null)
			model = new ProjektTableModel();
		else
			model = new ProjektTableModel(person, limit);
		
		
		this.setModel(model);
		sorter.setModel(model);
		try {		
			this.getColumn("Status").setCellRenderer(new ProgressCellRenderer());
			this.getColumn("Fälligkeit").setCellRenderer(new DateRenderer());
			this.getColumn("Prio").setCellRenderer(new PrioRenderer());
		} catch (java.lang.IllegalArgumentException e) {}
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
			int row = this.getSelectedRow();
			Projekt projekt = (Projekt) this.getData(row);
			JMenuItem menu = (JMenuItem) event.getSource();
			if (menu == miLoeschen) {
				loeschen(projekt);
			} else if (menu == miDetails) {
				openProjekt(projekt);
			} else if (menu == miPlus7) {
				projekt.plus(7);
			} else if (menu == miPlus1) {
				projekt.plus(1);
			}
		}
	}

	private void loeschen(Projekt projekt) {
		int ans = JOptionPane.showConfirmDialog(window,
				"Möchten Sie das Projekt \"" + projekt.getTitel() + "\" wirklich löschen?", "Löschen",
				JOptionPane.OK_CANCEL_OPTION);
		if (ans == JOptionPane.OK_OPTION) {
			window.setVisible(false);
			ProjektOrdner.getInstanz().removeFromDB(projekt.getId());
		}
	}

}
