package de.florian_timm.aufgabenPlaner.gui.table;

import java.awt.Point;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTable;
import javax.swing.SwingUtilities;

import de.florian_timm.aufgabenPlaner.entity.Aufgabe;
import de.florian_timm.aufgabenPlaner.entity.Person;
import de.florian_timm.aufgabenPlaner.entity.Projekt;
import de.florian_timm.aufgabenPlaner.gui.ProjektViewGUI;
import de.florian_timm.aufgabenPlaner.kontroll.EntityListener;

@SuppressWarnings("serial")
public class ProjektTable extends Table implements MouseListener, EntityListener {

	private Person person = null;

	public ProjektTable() {
		super();
		makeTable();
	}

	public ProjektTable(Person person) {
		super();
		this.person = person;
		makeTable();
	}

	private void makeTable() {
		this.addMouseListener(this);
		dataChanged();

		Projekt.addListener(this);
		Aufgabe.addListener(this);
	}

	@Override
	public void mousePressed(MouseEvent mouseEvent) {

		JTable table = (JTable) mouseEvent.getSource();
		Point point = mouseEvent.getPoint();
		int row = table.rowAtPoint(point);
		if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
			Projekt p = (Projekt) this.getData(row);
			System.out.println("Zeile:" + row + "(" + p.toString() + ")");
			openProjekt(p);

		}
	}

	public void close() {
		Projekt.removeListener(this);
		Aufgabe.removeListener(this);
	}

	private void openProjekt(Projekt projekt) {
		Window topFrame = SwingUtilities.windowForComponent(this);
		new ProjektViewGUI(topFrame, projekt);
	}

	public void dataChanged() {
		// System.out.println("dataChanged AufgabenPlanerGUI");
		if (person == null)
			this.setModel(new ProjektTableModel());
		else 
			this.setModel(new ProjektTableModel(person));
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
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

}
