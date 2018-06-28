package de.florian_timm.aufgabenPlaner.gui.table;

import de.florian_timm.aufgabenPlaner.entity.Aufgabe;
import de.florian_timm.aufgabenPlaner.entity.Entity;
import de.florian_timm.aufgabenPlaner.entity.Projekt;

import java.text.DateFormat;

public class AufgabenTableModel extends TableModel {
    private static final long serialVersionUID = 1L;

    public AufgabenTableModel(Projekt projekt) {
        this.spalten = new String[4];
        this.spalten[0] = "Bearbeiter";
        this.spalten[1] = "Titel";
        this.spalten[2] = "Fälligkeit";
        this.spalten[3] = "Status";

        this.setDataSource(projekt.getAufgaben());
    }

    protected void setDataSource(Entity[] aufgaben) {
        this.dataSource = aufgaben;
        this.data = new Object[aufgaben.length][4];

        for (int i = 0; i < aufgaben.length; i++) {
            Aufgabe aufgabe = (Aufgabe) aufgaben[i];

            this.data[i][0] = aufgabe.getBearbeiter();
            this.data[i][1] = aufgabe;
            if (aufgabe.getFaelligkeit() != null)
                this.data[i][2] = DateFormat.getDateInstance().format(aufgabe.getFaelligkeit());
            this.data[i][3] = aufgabe.getStatus();
        }
    }
}
