package de.florian_timm.aufgabenPlaner.gui.table;

import de.florian_timm.aufgabenPlaner.entity.Aufgabe;
import de.florian_timm.aufgabenPlaner.entity.Entity;
import de.florian_timm.aufgabenPlaner.entity.Projekt;
import de.florian_timm.aufgabenPlaner.entity.ordner.StatusOrdner;

@SuppressWarnings("serial")
public class AufgabenProjektTableModel extends TableModel {
	public AufgabenProjektTableModel(Projekt projekt) {
		super();
		makeModel();
		this.setDataSource(projekt.getAufgaben().values().toArray(new Entity[0]));
	}

	private void makeModel() {
		this.spalten = new String[4];
		this.spalten[0] = "Bearbeiter";
		this.spalten[1] = "Titel";
		this.spalten[2] = "Fälligkeit";
		this.spalten[3] = "Status";
	}

	protected void setDataSource(Entity[] map) {
		this.dataSource = map;

		this.data = new Object[map.length][4];
		for (int i = 0; i < dataSource.length; i++) {

			Aufgabe aufgabe = (Aufgabe) dataSource[i];

			this.data[i][0] = aufgabe.getBearbeiter();
			this.data[i][1] = aufgabe.getTitel();
			this.data[i][2] = new DateRenderable(aufgabe.getFaelligkeit(),
					aufgabe.getStatus().equals(StatusOrdner.getFertig()));
			this.data[i][3] = aufgabe.getStatus();
		}

	}
}
