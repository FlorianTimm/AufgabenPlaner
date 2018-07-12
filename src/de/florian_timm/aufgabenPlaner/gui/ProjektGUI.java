package de.florian_timm.aufgabenPlaner.gui;

import javax.swing.*;

import de.florian_timm.aufgabenPlaner.gui.comp.ClosableComponent;
import de.florian_timm.aufgabenPlaner.gui.panels.ProjektPanel;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class ProjektGUI extends JDialog implements WindowListener, ClosableComponent {
    private static final long serialVersionUID = 1L;
	private ProjektPanel projektPanel;

    public ProjektGUI(Window window) {
        super(window,"Neues Projekt", ModalityType.APPLICATION_MODAL);
        makeWindow();
        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
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
        cp.add(projektPanel, BorderLayout.CENTER);
    }
    
    public void close() {
    	projektPanel.close();
		this.setModal(false);
		this.setVisible(false);
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
		if (projektPanel.isVeraendert()) {
			int ans = JOptionPane.showConfirmDialog(this,
					"Sie haben die Daten verändert. Möchten Sie die Veränderung speichern?", "Daten wurden verändert",
					JOptionPane.YES_NO_CANCEL_OPTION);

			if (ans == JOptionPane.YES_OPTION) {
				projektPanel.speichern();
				close();
			} else if (ans == JOptionPane.NO_OPTION) {
				close();
			} else if (ans == JOptionPane.CANCEL_OPTION) {
				return;
			}

		} else {
			close();
		}
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
