package de.florian_timm.aufgabenPlaner.gui.table;

import de.florian_timm.aufgabenPlaner.entity.Aufgabe;
import de.florian_timm.aufgabenPlaner.entity.Entity;
import de.florian_timm.aufgabenPlaner.entity.Person;
import de.florian_timm.aufgabenPlaner.entity.ordner.StatusOrdner;

@SuppressWarnings("serial")
public class AufgabenPersonTableModel extends TableModel {
	public AufgabenPersonTableModel(Person person, int limit) {
		super();
		makeModel();
		this.setDataSource(person.getAufgaben(limit));
	}

	private void makeModel() {
		this.spalten = new String[5];
		this.spalten[0] = "Prio";
		this.spalten[1] = "Projekt";
		this.spalten[2] = "Titel";
		this.spalten[3] = "Fälligkeit";
		this.spalten[4] = "Status";
	}

	protected void setDataSource(Entity[] map) {
		this.dataSource = map;

		this.data = new Object[map.length][5];
		for (int i = 0; i < dataSource.length; i++) {
			Aufgabe aufgabe = (Aufgabe) dataSource[i];
			this.data[i][0] = aufgabe.getProjekt().getPrioritaet();
			this.data[i][1] = aufgabe.getProjekt().getTitel();
			this.data[i][2] = aufgabe.getTitel();
			this.data[i][3] = new DateRenderable(aufgabe.getFaelligkeit(),
					aufgabe.getStatus().equals(StatusOrdner.getFertig()));
			this.data[i][4] = aufgabe.getStatus();
		}

	}
}
