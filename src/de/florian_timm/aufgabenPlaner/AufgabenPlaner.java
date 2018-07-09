package de.florian_timm.aufgabenPlaner;

import java.awt.Frame;
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
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

import de.florian_timm.aufgabenPlaner.entity.ordner.AufgabenOrdner;
import de.florian_timm.aufgabenPlaner.entity.ordner.PersonenOrdner;
import de.florian_timm.aufgabenPlaner.entity.ordner.ProjektOrdner;
import de.florian_timm.aufgabenPlaner.entity.ordner.StatusOrdner;
import de.florian_timm.aufgabenPlaner.gui.AufgabenPlanerGUI;
import de.florian_timm.aufgabenPlaner.kontroll.ErrorNotifier;
import de.florian_timm.aufgabenPlaner.schnittstelle.DatenHaltung;

public class AufgabenPlaner implements WindowListener {
	private static AufgabenPlanerGUI gui;
	private ServerSocket serverSocket;
	private Thread thread;
	protected boolean running;
	private SystemLeistenIcon tray;
	private Timer t;
	private TimerTask task;

	public static void main(String[] args) {
		String dateiname = "aufgaben.db";
		if (args.length > 0) {
			dateiname = args[0];
		}
		new AufgabenPlaner(dateiname);

	}

	public AufgabenPlaner(String dateiname) {
		pruefeZweite();

		BufferedImage trayIconImage = null;
		try {
			URL url = AufgabenPlaner.class.getResource("icon.png");
			System.out.println(url.toString());
			trayIconImage = ImageIO.read(url);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		SystemLeistenIcon.makeInstanz(this, trayIconImage);
		tray = SystemLeistenIcon.getInstanz();

		DatenHaltung.setSourceFile(dateiname);

		gui = new AufgabenPlanerGUI(this);
		gui.addWindowListener(this);
		gui.setIconImage(trayIconImage);

		t = new Timer();
		task = new TimerTask() {
			public void run() {
				autoReload();
			}
		};
		t.schedule(task, 0, 15000);
	}

	private void autoReload() {
		System.out.println("AutoReload");
		StatusOrdner.getInstanz().loadData();
		PersonenOrdner.getInstanz().loadData();
		ProjektOrdner.getInstanz().loadData();
		Map<Integer, AufgabenOrdner> aufgabenListen = AufgabenOrdner.getAllAufgabenListen();
		for (AufgabenOrdner a : aufgabenListen.values()) {
			a.loadData();
		}
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// removeTrayIcon();
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		tray.closeGUI();
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		tray.closeGUI();

	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// makeTrayIcon();
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		tray.showGui();
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		tray.closeGUI();
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		tray.showGui();
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
								tray.showGui();
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
			close();
		}
	}

	public Frame getGui() {
		return gui;
	}

	public void close() {
		if (tray != null) {
			tray.removeIcon();
		}
		if (gui != null)
			gui.close();
		try {
			running = false;
			if (thread != null)
				thread.interrupt();
			if (serverSocket != null)
				serverSocket.close();
		} catch (IOException e) {
			ErrorNotifier.log(e);
		}
		System.exit(0);
	}
}
