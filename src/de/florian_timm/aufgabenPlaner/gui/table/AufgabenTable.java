package de.florian_timm.aufgabenPlaner.gui.table;

import java.awt.Point;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JTable;
import javax.swing.table.TableRowSorter;

import de.florian_timm.aufgabenPlaner.entity.Aufgabe;
import de.florian_timm.aufgabenPlaner.entity.Person;
import de.florian_timm.aufgabenPlaner.entity.Projekt;
import de.florian_timm.aufgabenPlaner.gui.AufgabenGUI;
import de.florian_timm.aufgabenPlaner.gui.ProjektViewGUI;
import de.florian_timm.aufgabenPlaner.kontroll.EntityListener;

@SuppressWarnings("serial")
public class AufgabenTable extends Table implements MouseListener, EntityListener {

	private Window window;
	private Projekt projekt = null;
	private Person person = null;
	private TableRowSorter<AufgabenTableModel> sorter;



	public AufgabenTable(Window window, Projekt projekt) {
		super();
		this.projekt = projekt;
		makeTable(window);
	}

	public AufgabenTable(Window window, Person person) {
		super();
		this.person = person;
		makeTable(window);
	}
	
	public void makeTable(Window window) {
		this.window = window;

		this.addMouseListener(this);
		Aufgabe.addListener(this);
		Projekt.addListener(this);

		sorter = new TableRowSorter<AufgabenTableModel>();
		this.setRowSorter(sorter);
		dataChanged();
	}

	protected void openAufgabe(Aufgabe aufgabe) {
		new AufgabenGUI(this.window, aufgabe);
	}

	public void close() {
		Aufgabe.removeListener(this);
		Projekt.removeListener(this);
	}

	@Override
	public void dataChanged() {
		System.out.println("AufgabenTable dataChanged");
		AufgabenTableModel model = null;
		if (projekt != null) {
			model = new AufgabenTableModel(this.projekt);
		} else if (person != null) {
			model = new AufgabenTableModel(this.person);
		}
		this.setModel(model);
		sorter.setModel(model);
		this.getColumn("Status").setCellRenderer(new ProgressCellRender());
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
			if (table.getSelectedColumn() <= 1 && person != null) {
				Projekt projekt = ((Aufgabe) this.getData(row)).getProjekt();
				new ProjektViewGUI(window, projekt);
			} else {
				Aufgabe a = (Aufgabe) this.getData(row);
				System.out.println(a.toString());
				openAufgabe(a);
			}

		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

}
