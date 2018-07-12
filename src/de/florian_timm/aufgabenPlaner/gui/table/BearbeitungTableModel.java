package de.florian_timm.aufgabenPlaner.gui.table;

import de.florian_timm.aufgabenPlaner.entity.Aufgabe;
import de.florian_timm.aufgabenPlaner.entity.Bearbeitung;
import de.florian_timm.aufgabenPlaner.entity.Entity;
import de.florian_timm.aufgabenPlaner.entity.Person;

import java.text.SimpleDateFormat;

@SuppressWarnings("serial")
public class BearbeitungTableModel extends TableModel {
	private Aufgabe aufgabe = null;
	private Person person = null;
	
	
	public BearbeitungTableModel(Aufgabe aufgabe) {
		super();
		this.aufgabe = aufgabe;
		makeModel();
		Entity[] source = aufgabe.getBearbeitungen().values().toArray(new Entity[0]);
		this.setDataSource(source);
	}

	public BearbeitungTableModel(Person person, int limit) {
		super();
		this.person = person;
		makeModel();
		this.setDataSource(person.getBearbeitungen(limit));
	}

	private void makeModel() {
		int a = 0;
		if (aufgabe != null) {
			this.spalten = new String[4];
			this.spalten[a++] = "Bearbeiter";
		} else if (person != null) {
			this.spalten = new String[5];
			this.spalten[a++] = "Projekt";
			this.spalten[a++] = "Aufgabe";
		}
		this.spalten[a++] = "Start";
		this.spalten[a++] = "Dauer";
		this.spalten[a++] = "Bemerkung";
	}

	protected void setDataSource(Entity[] map) {
		this.dataSource = map;
		this.data = new Object[map.length][spalten.length];

		for (int i = 0; i < dataSource.length; i++) {
			Bearbeitung bearbeitung = (Bearbeitung) dataSource[i];
			int a = 0;
			if (aufgabe != null) {
				this.data[i][a++] = bearbeitung.getBearbeiter();
			} else if (person != null) {
				this.data[i][a++] = bearbeitung.getAufgabe().getProjekt().getTitel();
				this.data[i][a++] = bearbeitung.getAufgabe().getTitel();
			}
			this.data[i][a++] = new SimpleDateFormat("yyyy-MM-dd H:m").format(bearbeitung.getStart());
			if (bearbeitung.getDauer() != null) {
				long s = bearbeitung.getDauer().getSeconds();
				this.data[i][a++] = String.format("%d:%02d", s / 3600, (s % 3600) / 60);;
			} else {
				this.data[i][a++] = null;
			}
				this.data[i][a++] = bearbeitung.getBemerkung();
		}
	}
}
