package de.florian_timm.aufgabenPlaner.gui.table;

import java.text.DateFormat;

import de.florian_timm.aufgabenPlaner.entity.Entity;
import de.florian_timm.aufgabenPlaner.entity.Projekt;

public class ProjektTableModel extends TableModel {
	private static final long serialVersionUID = 1L;

	public ProjektTableModel() {
		this.spalten = new String[5];
		this.spalten[0] = "Auftraggeber";
		this.spalten[1] = "Titel";
		this.spalten[2] = "Zuständig";
		this.spalten[3] = "Fälligkeit";
		this.spalten[4] = "Status";
		this.setDataSource(Projekt.getArray());
	}

	protected void setDataSource(Entity[] projekte) {
		this.dataSource = projekte;
		this.data = new Object[projekte.length][5];

		for (int i = 0; i < projekte.length; i++) {
			Projekt projekt = (Projekt) projekte[i];

			if (projekt.getAuftraggeber() != null)
				this.data[i][0] = projekt.getAuftraggeber().getName();
			this.data[i][1] = projekt.getTitel();
			this.data[i][2] = projekt.getZustaendig().getName();
			if (projekt.getFaelligkeit() != null)
				this.data[i][3] = DateFormat.getDateInstance().format(projekt.getFaelligkeit());
			this.data[i][4] = projekt.getStatus();
		}
	}

}
