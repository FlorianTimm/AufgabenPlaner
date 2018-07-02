package de.florian_timm.aufgabenPlaner.gui;

import javax.swing.*;

import de.florian_timm.aufgabenPlaner.gui.panels.ProjektPanel;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class ProjektGUI extends JDialog implements WindowListener {
    private static final long serialVersionUID = 1L;
	private ProjektPanel projektPanel;

    public ProjektGUI(Window window) {
        super(window);
        this.setTitle("Neues Projekt");
        makeWindow();
        this.addWindowListener(this);
        this.pack();
        this.setLocationRelativeTo(window);
        this.setVisible(true);
    }

    private void makeWindow() {
        this.setModal(true);
        Container cp = this.getContentPane();
        cp.setLayout(new BorderLayout());

        projektPanel = new ProjektPanel(this);
        projektPanel.setWindow2Close(this);
        cp.add(projektPanel, BorderLayout.CENTER);

     
    }

	@Override
	public void windowDeactivated(WindowEvent arg0) {
	}

	@Override
	public void windowActivated(WindowEvent arg0) {	
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
	
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
	}

}
