package de.florian_timm.aufgabenPlaner.entity.ordner;

import java.util.HashMap;
import java.util.Map;
import de.florian_timm.aufgabenPlaner.entity.Entity;
import de.florian_timm.aufgabenPlaner.entity.Person;
import de.florian_timm.aufgabenPlaner.entity.Projekt;
import de.florian_timm.aufgabenPlaner.kontroll.OrdnerNotifier;

public abstract class Ordner {
	protected Map<Integer, Entity> alle = new HashMap<Integer, Entity>();
	protected OrdnerNotifier notifier;
	protected boolean firstLoaded = false;
	protected long lastUpdate = 0;

	public boolean remove(int id) {
		if (alle.containsKey(id)) {
			alle.remove(id);
			return true;
		}
		return false;
	}
	
	protected abstract boolean loadData();

	abstract public void removeFromDB(int id);

	// protected abstract Entity getEntityFromResult(DatenHaltung d);

	public boolean remove(Entity p) {
		return remove(p.getId());
	}

	public void removeAll() {
		alle.clear();
	}

	public boolean add(Entity p) {
		int id = p.getId();
		if (alle.containsKey(id)) {
			return update(p);
		} else {
			alle.put(id, p);
			if (firstLoaded)
				alertNew(p);
			return true;
		}
	}

	protected abstract void alertNew(Entity p);
	protected abstract void alertChanged(Entity p);
	

	public boolean update(Entity neu) {
		Entity alt = alle.get(neu.getId());
		if (alt.equals(neu)) {
			return false;
		}
		alt.update(neu);
		alertChanged(neu);
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((alle == null) ? 0 : alle.hashCode());
		result = prime * result + (firstLoaded ? 1231 : 1237);
		result = prime * result + (int) (lastUpdate ^ (lastUpdate >>> 32));
		result = prime * result + ((notifier == null) ? 0 : notifier.hashCode());
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
		Ordner other = (Ordner) obj;
		if (alle == null) {
			if (other.alle != null)
				return false;
		} else if (!alle.equals(other.alle))
			return false;
		if (firstLoaded != other.firstLoaded)
			return false;
		if (lastUpdate != other.lastUpdate)
			return false;
		if (notifier == null) {
			if (other.notifier != null)
				return false;
		} else if (!notifier.equals(other.notifier))
			return false;
		return true;
	}
	
	
}