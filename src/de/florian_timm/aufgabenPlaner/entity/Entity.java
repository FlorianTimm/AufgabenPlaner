package de.florian_timm.aufgabenPlaner.entity;

import de.florian_timm.aufgabenPlaner.kontroll.EntityListener;

import java.util.ArrayList;
import java.util.List;

public abstract class Entity {
    private static List<EntityListener> listener = new ArrayList<EntityListener>();
    protected int dbId;

    public static void addListener(EntityListener newListener) {
        listener.add(newListener);
    }

    public static void informListener() {
        for (EntityListener l : listener) {
            l.dataChanged();
        }
    }

    public int getId() {
        return dbId;
    }

}
