package de.florian_timm.aufgabenPlaner.gui.table;

import de.florian_timm.aufgabenPlaner.entity.Entity;
import de.florian_timm.aufgabenPlaner.entity.Person;
import de.florian_timm.aufgabenPlaner.entity.Projekt;
import de.florian_timm.aufgabenPlaner.entity.ordner.ProjektOrdner;

import java.text.DateFormat;

public class ProjektTableModel extends TableModel {
	private static final long serialVersionUID = 1L;

	public ProjektTableModel() {
		makeColumns();
		this.setDataSource(ProjektOrdner.getInstanz().getArray());
	}

	public ProjektTableModel(Person person, int limit) {
		makeColumns();
		this.setDataSource(ProjektOrdner.getByUser(person, limit));
	}

	private void makeColumns() {
		this.spalten = new String[6];
		this.spalten[0] = "Prio";
		this.spalten[1] = "Auftraggeber";
		this.spalten[2] = "Titel";
		this.spalten[3] = "Zuständig";
		this.spalten[4] = "Fälligkeit";
		this.spalten[5] = "Status";
	}

	protected void setDataSource(Entity[] projekte) {
		this.dataSource = projekte;
		this.data = new Object[projekte.length][spalten.length];

		for (int i = 0; i < projekte.length; i++) {
			Projekt projekt = (Projekt) projekte[i];
			this.data[i][0] = projekt.getPrioritaet();
			if (projekt.getAuftraggeber() != null)
				this.data[i][1] = projekt.getAuftraggeber();
			this.data[i][2] = projekt.getTitel();
			this.data[i][3] = projekt.getZustaendig();
			if (projekt.getFaelligkeit() != null)
				this.data[i][4] = DateFormat.getDateInstance().format(projekt.getFaelligkeit());
			this.data[i][5] = projekt.getStatus();
		}
	}

}
