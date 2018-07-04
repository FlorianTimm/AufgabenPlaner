package de.florian_timm.aufgabenPlaner.kontroll;

import java.util.HashSet;
import java.util.Set;

public abstract class OrdnerNotifier {
	private Set<EntityListener> listener;
	private OrdnerNotifier instanz;

	protected OrdnerNotifier() {
		listener = new HashSet<EntityListener>();
	}

	public void addListener(EntityListener newListener) {
		listener.add(newListener);
		System.out.println(
				"AufgabenListener: " + listener.size() + " (neu: " + newListener.getClass().getSimpleName() + ")");
	}

	public void removeListener(EntityListener el) {
		listener.remove(el);
		System.out.println("AufgabenListener: " + listener.size() + " (entf: " + el.getClass().getSimpleName() + ")");
	}

	public void informListener() {
		EntityListener[] ls = listener.toArray(new EntityListener[0]);
		for (EntityListener el : ls) {
			el.dataChanged();
		}
	}

}