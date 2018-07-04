package de.florian_timm.aufgabenPlaner.kontroll;

public class PersonenNotifier extends OrdnerNotifier {
	private static PersonenNotifier instanz;

	private PersonenNotifier() {
		super();
	}

	public static PersonenNotifier getInstanz() {
		if (instanz == null) {
			instanz = new PersonenNotifier();
		}
		return instanz;
	}
}
