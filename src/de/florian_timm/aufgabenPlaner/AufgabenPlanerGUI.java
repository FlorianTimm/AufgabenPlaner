package de.florian_timm.aufgabenPlaner;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

public class AufgabenPlanerGUI extends JFrame implements ActionListener {

	/**
	 *  GUI
	 */
	private static final long serialVersionUID = 1L;
	private DatenSpeicher data = null;
	
	public AufgabenPlanerGUI (String dateiname) {
		super("Aufgabenplaner");
		
		data = new DatenSpeicher(dateiname);
		
		Aufgabe[] aufgaben = data.getAufgaben();
		
		for (Aufgabe a:aufgaben) {
			a.getBeschreibung();
		}
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
		
		
		
		
	}

	public static void main(String[] args) {
		
		String dateiname = "test.db";
		if (args.length > 0) {
			dateiname = args[0];
		}
		
		new AufgabenPlanerGUI(dateiname);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {

	}
}
