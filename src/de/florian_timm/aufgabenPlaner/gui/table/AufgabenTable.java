package de.florian_timm.aufgabenPlaner.gui.table;

import java.awt.Point;
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

import de.florian_timm.aufgabenPlaner.entity.Aufgabe;
import de.florian_timm.aufgabenPlaner.entity.Person;
import de.florian_timm.aufgabenPlaner.entity.Projekt;
import de.florian_timm.aufgabenPlaner.entity.Status;
import de.florian_timm.aufgabenPlaner.entity.ordner.AufgabenOrdner;
import de.florian_timm.aufgabenPlaner.gui.AufgabenGUI;
import de.florian_timm.aufgabenPlaner.gui.BearbeitungGUI;
import de.florian_timm.aufgabenPlaner.gui.ProjektViewGUI;
import de.florian_timm.aufgabenPlaner.kontroll.AufgabenNotifier;
import de.florian_timm.aufgabenPlaner.kontroll.EntityListener;
import de.florian_timm.aufgabenPlaner.kontroll.ProjektNotifier;

@SuppressWarnings("serial")
public class AufgabenTable extends Table implements MouseListener, EntityListener, ActionListener {

	private Window window;
	private Projekt projekt = null;
	private Person person = null;
	private TableRowSorter<AufgabenTableModel> sorter;
	private int limit = -1;
	private JMenuItem miLoeschen;
	private JMenuItem miBearbeiten;
	private JPopupMenu popup;
	private JMenuItem miDetails;
	private JMenuItem miPlus7;
	private JMenuItem miPlus1;
	private JMenuItem miProjektDetails;
	private JMenuItem miFertig;

	public AufgabenTable(Window window, Projekt projekt) {
		super();
		this.projekt = projekt;
		makeTable(window);
		this.limit = -1;
	}

	public AufgabenTable(Window window, Person person) {
		this(window, person, -1);
	}

	public AufgabenTable(Window window, Person person, int limit) {
		super();
		this.limit = limit;
		this.person = person;
		makeTable(window);
	}

	public void makeTable(Window window) {
		this.window = window;

		this.addMouseListener(this);
		AufgabenNotifier.getInstanz().addListener(this);
		ProjektNotifier.getInstanz().addListener(this);

		sorter = new TableRowSorter<AufgabenTableModel>();
		this.setRowSorter(sorter);
		dataChanged();
		sorter.setComparator(this.getColumn("Fälligkeit").getModelIndex(), new Comparator<DateRenderable>() {
			public int compare(DateRenderable d1, DateRenderable d2) {
				return d1.compareTo(d2);
			}
		});
		sorter.setComparator(this.getColumn("Status").getModelIndex(), new Comparator<Status>() {
			@Override
			public int compare(Status s0, Status s1) {
				Integer i0 = s0.getSortierung();
				Integer i1 = s1.getSortierung();
				return i0.compareTo(i1);
			}
		});

		popup = new JPopupMenu();

		miDetails = new JMenuItem("Aufgaben-Details...");
		miDetails.addActionListener(this);
		popup.add(miDetails);

		miProjektDetails = new JMenuItem("Projekt-Details...");
		miProjektDetails.addActionListener(this);
		popup.add(miProjektDetails);

		popup.addSeparator();

		miBearbeiten = new JMenuItem("Bearbeitung beginnen...");
		miBearbeiten.addActionListener(this);
		popup.add(miBearbeiten);

		miFertig = new JMenuItem("Fertig!");
		miFertig.addActionListener(this);
		popup.add(miFertig);

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

	protected void openAufgabe(Aufgabe aufgabe) {
		new AufgabenGUI(this.window, aufgabe);
	}

	public void close() {
		AufgabenNotifier.getInstanz().removeListener(this);
		ProjektNotifier.getInstanz().removeListener(this);
	}

	@Override
	public void dataChanged() {
		System.out.println("AufgabenTable dataChanged");
		AufgabenTableModel model = null;
		if (projekt != null) {
			model = new AufgabenTableModel(this.projekt);
		} else if (person != null) {
			model = new AufgabenTableModel(this.person, this.limit);
		}
		this.setModel(model);
		sorter.setModel(model);

		try {
			this.getColumn("Status").setCellRenderer(new ProgressCellRenderer());
			this.getColumn("Fälligkeit").setCellRenderer(new DateRenderer());
			this.getColumn("Prio").setCellRenderer(new PrioRenderer());
		} catch (java.lang.IllegalArgumentException e) {
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {

		if (e.getButton() == MouseEvent.BUTTON2) {
			Point point = e.getPoint();
			int currentRow = this.rowAtPoint(point);
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
			int col = this.columnAtPoint(event.getPoint());
			Aufgabe aufgabe = (Aufgabe) this.getData(row);
			if (person != null && col <= 2)
				openProjekt(aufgabe.getProjekt());
			else
				openAufgabe(aufgabe);

		}
	}

	private void openProjekt(Projekt projekt) {
		new ProjektViewGUI(window, projekt);
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
			Aufgabe aufgabe = (Aufgabe) this.getData(row);
			if (menu == miLoeschen) {
				loeschen(aufgabe);
			} else if (menu == miDetails) {
				openAufgabe(aufgabe);
			} else if (menu == miProjektDetails) {
				new ProjektViewGUI(window, aufgabe.getProjekt());
			} else if (menu == miBearbeiten) {
				bearbeiten(aufgabe);
			} else if (menu == miPlus7) {
				aufgabe.plus(7);
			} else if (menu == miPlus1) {
				aufgabe.plus(1);
			} else if (menu == miFertig) {
				aufgabe.setFertig();
			}
		}
	}

	private void bearbeiten(Aufgabe aufgabe) {
		// TODO Auto-generated method stub
		// JOptionPane.showMessageDialog(window, "noch nicht implementiert", "Warnung",
		// JOptionPane.INFORMATION_MESSAGE);
		new BearbeitungGUI(window, aufgabe);
	}

	private void loeschen(Aufgabe aufgabe) {
		int ans = JOptionPane.showConfirmDialog(this,
				"Möchten Sie die Aufgabe \"" + aufgabe.getTitel() + "\" wirklich löschen?", "Löschen",
				JOptionPane.OK_CANCEL_OPTION);
		if (ans == JOptionPane.OK_OPTION) {
			AufgabenOrdner.getInstanz(aufgabe.getProjekt()).removeFromDB(aufgabe.getId());
		}
	}
}
