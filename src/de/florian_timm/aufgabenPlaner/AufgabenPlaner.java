package de.florian_timm.aufgabenPlaner;

import java.awt.AWTException;
import java.awt.Frame;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;

import de.florian_timm.aufgabenPlaner.gui.AufgabenPlanerGUI;
import de.florian_timm.aufgabenPlaner.kontroll.ErrorNotifier;

public class AufgabenPlaner implements ActionListener, WindowListener {
	private static AufgabenPlanerGUI gui;
	private BufferedImage trayIconImage;
	private TrayIcon trayIcon;
	private SystemTray tray;
	private boolean inTray = false;
	private ServerSocket serverSocket;
	private Thread thread;
	protected boolean running;

	public static void main(String[] args) {
		String dateiname = "aufgaben.db";
		if (args.length > 0) {
			dateiname = args[0];
		}
		new AufgabenPlaner(dateiname);

	}

	public AufgabenPlaner(String dateiname) {
		pruefeZweite();
		gui = new AufgabenPlanerGUI(this, dateiname);
		gui.addWindowListener(this);

		trayIconImage = null;
		try {
			URL url = AufgabenPlaner.class.getResource("icon.png");
			System.out.println(url.toString());
			trayIconImage = ImageIO.read(url);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		gui.setIconImage(trayIconImage);
	}

	private void makeTrayIcon() {
		if (inTray)
			return;
		if (!SystemTray.isSupported()) {
			System.out.println("SystemTray is not supported");
			return;
		}
		final PopupMenu popup = new PopupMenu();

		trayIcon = new TrayIcon(trayIconImage, "AufgabenPlaner");

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

		trayIcon.setPopupMenu(popup);
		trayIcon.setImageAutoSize(true);
		trayIcon.addActionListener(this);
		trayIcon.setActionCommand("oeffnen");

		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			System.out.println("TrayIcon could not be added.");
		}
		gui.setVisible(false);

		trayIcon.displayMessage("AufgabenPlaner",
				"Programm wurde minimiert, \nzum Schließen rechte Maustaste \nauf das Symbol", MessageType.INFO);

		inTray = true;
	}

	private void removeTrayIcon() {
		if (!inTray)
			return;
		gui.setState(Frame.NORMAL);
		tray.remove(trayIcon);
		gui.setVisible(true);
		inTray = false;
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		switch (ae.getActionCommand()) {
		case "oeffnen":
			removeTrayIcon();
			break;
		case "ende":
			close();
			break;
		}
		removeTrayIcon();
	}

	public void close() {
		removeTrayIcon();
		gui.close();
		try {
			running = false;
			thread.interrupt();
			serverSocket.close();
		} catch (IOException e) {
			ErrorNotifier.log(e);
		}
		System.exit(0);
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		//removeTrayIcon();
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		makeTrayIcon();
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		makeTrayIcon();

	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		//makeTrayIcon();
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		removeTrayIcon();
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		makeTrayIcon();
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		removeTrayIcon();
	}

	private void pruefeZweite() {
		try {
			serverSocket = new ServerSocket(4232);
			thread = new Thread(new Runnable() {
				public void run() {
					running = true;
					while (running) {
						try {
							Socket s = serverSocket.accept();
							BufferedReader buffy = new BufferedReader(new InputStreamReader(s.getInputStream()));
							if (buffy.readLine().equals("AufgabenPlaner")) {
								removeTrayIcon();
							}
							buffy.close();
							s.close();
						} catch (IOException e) {
							running = false;
							System.out.println("Verbindung beendet");
						}
					}
				}
			});
			thread.start();
		} catch (IOException e) {
			try {
				Socket s = new Socket("localhost", 4232);
				s.getOutputStream().write("AufgabenPlaner".getBytes());
				s.close();
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			System.exit(0);
		}
	}
}
