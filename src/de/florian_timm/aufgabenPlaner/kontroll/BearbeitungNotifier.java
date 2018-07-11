package de.florian_timm.aufgabenPlaner.kontroll;

public class BearbeitungNotifier extends OrdnerNotifier {
	private static BearbeitungNotifier instanz;

	private BearbeitungNotifier() {
		super();
	}

	public static BearbeitungNotifier getInstanz() {
		if (instanz == null) {
			instanz = new BearbeitungNotifier();
		}
		return instanz;
	}
}
