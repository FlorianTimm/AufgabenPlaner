package de.florian_timm.aufgabenPlaner.kontroll;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ErrorHub {
	private static List<ErrorListener> listener = new ArrayList<ErrorListener>();

	public static void log(Exception e) {
		for (ErrorListener el : listener) {
			el.errorInformation(e);
		}

		try {
			PrintWriter pw = new PrintWriter(new File("log.txt"));
			pw.print((new Date()).toString() + "\t" + System.getProperty("user.name") + ":\n");
		    e.printStackTrace(pw);
		    pw.close();

		} catch (IOException ioe) {
			e.printStackTrace();
			ioe.printStackTrace();
		}
	}

	public static void addListener(ErrorListener el) {
		listener.add(el);
	}

}
