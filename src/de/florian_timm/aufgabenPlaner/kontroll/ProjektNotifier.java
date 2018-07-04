package de.florian_timm.aufgabenPlaner.kontroll;

public class ProjektNotifier extends OrdnerNotifier {
	private static ProjektNotifier instanz;

	private ProjektNotifier() {
		super();
	}

	public static ProjektNotifier getInstanz() {
		if (instanz == null) {
			instanz = new ProjektNotifier();
		}
		return instanz;
	}
}
