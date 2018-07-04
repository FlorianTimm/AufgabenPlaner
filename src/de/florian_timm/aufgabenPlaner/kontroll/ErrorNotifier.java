package de.florian_timm.aufgabenPlaner.kontroll;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class ErrorNotifier {
	private static Set<ErrorListener> listener = new HashSet<ErrorListener>();

	public static void log(Exception e) {
		for (ErrorListener el : listener) {
			el.errorInformation(e);
		}
		
		try {
			e.printStackTrace();
			
			PrintWriter pw = new PrintWriter(new FileOutputStream(
				    new File("log.txt"), 
				    true /* append = true */)); 
			pw.print((new Date()).toString() + "\t" + System.getProperty("user.name") + ":\n");
		    e.printStackTrace(pw);
		    pw.print("\n\n");
		    pw.close();
		} catch (IOException ioe) {
			e.printStackTrace();
			ioe.printStackTrace();
		}
	}

	public static void addListener(ErrorListener el) {
		listener.add(el);
	}
	
	public static void removeListener (ErrorListener el) {
		listener.remove(el);
	}

}
