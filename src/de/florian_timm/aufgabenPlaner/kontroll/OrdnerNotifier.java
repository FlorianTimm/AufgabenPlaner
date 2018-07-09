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
		//System.out.println("AufgabenListener: " + listener.size() + " (neu: " + newListener.getClass().getSimpleName() + ")");
	}

	public void removeListener(EntityListener el) {
		listener.remove(el);
		//System.out.println("AufgabenListener: " + listener.size() + " (entf: " + el.getClass().getSimpleName() + ")");
	}

	public void informListener() {
		EntityListener[] ls = listener.toArray(new EntityListener[0]);
		for (EntityListener el : ls) {
			el.dataChanged();
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((instanz == null) ? 0 : instanz.hashCode());
		result = prime * result + ((listener == null) ? 0 : listener.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrdnerNotifier other = (OrdnerNotifier) obj;
		if (instanz == null) {
			if (other.instanz != null)
				return false;
		} else if (!instanz.equals(other.instanz))
			return false;
		if (listener == null) {
			if (other.listener != null)
				return false;
		} else if (!listener.equals(other.listener))
			return false;
		return true;
	}

}