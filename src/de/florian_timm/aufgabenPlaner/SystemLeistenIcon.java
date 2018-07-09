package de.florian_timm.aufgabenPlaner;

import java.awt.AWTException;
import java.awt.Frame;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class SystemLeistenIcon extends java.awt.TrayIcon implements ActionListener {
	private static SystemLeistenIcon instanz = null;
	private SystemTray tray;
	private boolean inTray = false;
	private AufgabenPlaner ap;

	public static SystemLeistenIcon getInstanz() {
		return instanz;
	}

	public static void makeInstanz(AufgabenPlaner ap, BufferedImage trayIconImage) {
		instanz = new SystemLeistenIcon(ap, trayIconImage);
	}

	public void closeGUI() {
		if (!SystemTray.isSupported()) {
			System.out.println("SystemTray is not supported");
			ap.close();
			return;
		}
		ap.getGui().setVisible(false);
		makeAlert("AufgabenPlaner", "Programm wurde minimiert, \nzum Schließen rechte Maustaste \nauf das Symbol");
	}
	
	

	private SystemLeistenIcon(AufgabenPlaner ap, BufferedImage trayIconImage) {

		super(trayIconImage, "AufgabenPlaner");
		this.ap = ap;

		final PopupMenu popup = new PopupMenu();
		tray = SystemTray.getSystemTray();

		// Create a pop-up menu components
		MenuItem aboutItem = new MenuItem("Öffnen");
		aboutItem.addActionListener(this);
		aboutItem.setActionCommand("oeffnen");
		MenuItem exitItem = new MenuItem("Beenden");
		exitItem.addActionListener(this);
		exitItem.setActionCommand("ende");

		// Add components to pop-up menu
		popup.add(aboutItem);
		popup.addSeparator();
		popup.add(exitItem);

		this.setPopupMenu(popup);
		this.setImageAutoSize(true);
		this.addActionListener(this);
		this.setActionCommand("oeffnen");

		try {
			tray.add(this);
		} catch (AWTException e) {
			System.out.println("TrayIcon could not be added.");
		}

		inTray = true;
	}

	public void makeAlert(String heading, String alert) {
		this.displayMessage(heading, alert, MessageType.INFO);
	}

	void showGui() {
		if (inTray) {
			ap.getGui().setState(Frame.NORMAL);
			ap.getGui().setVisible(true);
		}
		if (ap.getGui() != null)
			ap.getGui().requestFocus();
	}
	
	void removeIcon() {
		tray.remove(this);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		switch (ae.getActionCommand()) {
		case "oeffnen":
			showGui();
			break;
		case "ende":
			ap.close();
			break;
		}
		showGui();
	}
}
