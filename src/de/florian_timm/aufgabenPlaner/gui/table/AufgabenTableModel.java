package de.florian_timm.aufgabenPlaner.gui.table;

import de.florian_timm.aufgabenPlaner.entity.Aufgabe;
import de.florian_timm.aufgabenPlaner.entity.Entity;
import de.florian_timm.aufgabenPlaner.entity.Person;
import de.florian_timm.aufgabenPlaner.entity.Projekt;

@SuppressWarnings("serial")
public class AufgabenTableModel extends TableModel {
	private Projekt projekt = null;
	private Person person = null;
	
	public AufgabenTableModel(Projekt projekt) {
		super();
		this.projekt = projekt;
		makeModel();
		this.setDataSource(projekt.getAufgaben().values().toArray(new Entity[0]));
	}

	public AufgabenTableModel(Person person, int limit) {
		super();
		this.person = person;
		makeModel();
		this.setDataSource(person.getAufgaben(limit));
	}

	private void makeModel() {
		int a = 0;
		if (projekt != null) {
			this.spalten = new String[4];
			this.spalten[a++] = "Bearbeiter";
		} else if (person != null) {
			this.spalten = new String[5];
			this.spalten[a++] = "Projekt";
			this.spalten[a++] = "Prio";
		}
		this.spalten[a++] = "Titel";
		this.spalten[a++] = "Fälligkeit";
		this.spalten[a++] = "Status";
	}

	protected void setDataSource(Entity[] map) {
		this.dataSource = map;
		this.data = new Object[map.length][spalten.length];

		for (int i = 0; i < dataSource.length; i++) {
			Aufgabe aufgabe = (Aufgabe) dataSource[i];
			int a = 0;
			if (projekt != null) {
				this.data[i][a++] = aufgabe.getBearbeiter();
			} else if (person != null) {
				this.data[i][a++] = aufgabe.getProjekt().getTitel();
				this.data[i][a++] = aufgabe.getProjekt().getPrioritaet();
			}
			this.data[i][a++] = aufgabe.getTitel();
			this.data[i][a++] = aufgabe;
			this.data[i][a++] = aufgabe.getStatus();
		}
	}
}
