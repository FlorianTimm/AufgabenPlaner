package de.florian_timm.aufgabenPlaner.kontroll;

public class AufgabenNotifier extends OrdnerNotifier {
	private static AufgabenNotifier instanz;

	private AufgabenNotifier() {
		super();
	}

	public static AufgabenNotifier getInstanz() {
		if (instanz == null) {
			instanz = new AufgabenNotifier();
		}
		return instanz;
	}
}
