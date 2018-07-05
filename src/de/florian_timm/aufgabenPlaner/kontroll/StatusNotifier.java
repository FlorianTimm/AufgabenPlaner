package de.florian_timm.aufgabenPlaner.kontroll;

public class StatusNotifier extends OrdnerNotifier {
	private static StatusNotifier instanz;

	private StatusNotifier() {
		super();
	}

	public static StatusNotifier getInstanz() {
		if (instanz == null) {
			instanz = new StatusNotifier();
		}
		return instanz;
	}
}
