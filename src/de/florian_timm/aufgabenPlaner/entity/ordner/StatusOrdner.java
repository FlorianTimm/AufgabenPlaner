package de.florian_timm.aufgabenPlaner.entity.ordner;

import java.util.Arrays;
import java.util.HashMap;
import de.florian_timm.aufgabenPlaner.entity.Entity;
import de.florian_timm.aufgabenPlaner.entity.Status;
import de.florian_timm.aufgabenPlaner.kontroll.StatusNotifier;
import de.florian_timm.aufgabenPlaner.schnittstelle.DatenHaltung;

public class StatusOrdner extends Ordner {
	private long lastUpdate = 0;
	private static StatusOrdner instanz = null;

	private StatusOrdner() {
		alle = new HashMap<Integer, Entity>();
		notifier = StatusNotifier.getInstanz();
	}

	public static StatusOrdner getInstanz() {
		if (instanz == null) {
			instanz = new StatusOrdner();
		}
		return instanz;
	}

	public Status getStatus(int id) {
		if (!alle.containsKey(id)) {
			loadData(true);
		}
		return (Status) alle.get(id);
	}

	public boolean loadData(boolean reload) {
		boolean dataChanged = false;
		DatenHaltung d = new DatenHaltung();
		d.prepareStatement("SELECT * FROM status WHERE bearbeitet > ? OR geloescht > ?;");
		d.setLong(1, lastUpdate);
		d.setLong(2, lastUpdate);
		while (d.next()) {
			long geloescht = d.getLong("geloescht");
			long bearbeitet = d.getLong("bearbeitet");
			int id = d.getInt("id");
			if (geloescht > 0) {
				if (alle.containsKey(id)) {
					if (this.remove(id))
						dataChanged = true;
				}
			} else {
				Status p = getStatusFromResult(d);
				if (this.add(p)) {
					dataChanged = true;
				}
			}
			lastUpdate = Math.max(Math.max(lastUpdate, geloescht), bearbeitet);

		}
		if (dataChanged && reload) {
			notifier.informListener();
		} else if (dataChanged) {
			return true;
		}
		return dataChanged;
	}

	private Status getStatusFromResult(DatenHaltung d) {
		int dbId = d.getInt("id");
		String bezeichnung = d.getString("bezeichnung");
		int sortierung = d.getInt("sortierung");
		return new Status(dbId, bezeichnung, sortierung);
	}

	public Status[] getArray() {
		Status[] p = alle.values().toArray(new Status[0]);
		Arrays.sort(p);
		return p;
	}

	public static void createTable() {
		new DatenHaltung(true).update("CREATE TABLE IF NOT EXISTS status (id INTEGER PRIMARY KEY, "
				+ "bezeichnung TEXT UNIQUE NOT NULL, sortierung INTEGER, bearbeitet INTEGER NOT NULL, geloescht INTEGER);");
		new DatenHaltung(true).update(
				"INSERT INTO status (bezeichnung, sortierung) VALUES ('fertig', 100), ('nicht angefangen', 0), ('halbfertig',50);");
	}

	@Override
	public void removeFromDB(int id) {
		// TODO Auto-generated method stub
		
	}


}
