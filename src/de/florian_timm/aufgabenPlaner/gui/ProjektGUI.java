package de.florian_timm.aufgabenPlaner.gui;

import javax.swing.*;
import java.awt.*;

public class ProjektGUI extends JDialog {
    private static final long serialVersionUID = 1L;

    public ProjektGUI(Window window) {
        super(window);
        this.setTitle("Neues Projekt");
        makeWindow();
    }

    private void makeWindow() {
        this.setModal(true);
        Container cp = this.getContentPane();
        cp.setLayout(new BorderLayout());

        ProjektPanel projektPanel = new ProjektPanel();
        projektPanel.setWindow2Close(this);
        cp.add(projektPanel, BorderLayout.CENTER);

        this.pack();
    }

}
