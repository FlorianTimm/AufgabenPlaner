package de.florian_timm.aufgabenPlaner.entity.ordner;

import java.util.HashMap;
import java.util.Map;

import de.florian_timm.aufgabenPlaner.entity.Entity;
import de.florian_timm.aufgabenPlaner.kontroll.OrdnerNotifier;

public abstract class Ordner {
	protected Map<Integer, Entity> alle = new HashMap<Integer, Entity>();
	protected OrdnerNotifier notifier;
	protected long lastUpdate = 0;

	public boolean remove(int id) {
		if (alle.containsKey(id)) {
			alle.remove(id);
			return true;
		}
		return false;
	}
	
	protected abstract boolean loadData();
	
	//protected abstract Entity getEntityFromResult(DatenHaltung d);

	public boolean remove(Entity p) {
		return remove(p.getId());
	}

	public boolean add(Entity p) {
		int id = p.getId();
		if (alle.containsKey(id)) {
			return update(p);
		} else {
			alle.put(id, p);
			return true;
		}
	}

	public boolean update(Entity neu) {
		Entity alt = alle.get(neu.getId());
		if (alt.equals(neu)) {
			return false;
		}
		alt.update(neu);
		return true;
	}
	
	protected boolean checkLoading() {
		if (lastUpdate == 0) {
			loadData();
			return false;
		}
		return true;
	}

}