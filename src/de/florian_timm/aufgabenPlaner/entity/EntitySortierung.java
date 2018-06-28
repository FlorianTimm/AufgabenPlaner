package de.florian_timm.aufgabenPlaner.entity;

public abstract class EntitySortierung extends Entity implements Comparable<EntitySortierung> {

    public abstract int getSortierung();

    public int compareTo(EntitySortierung other) {
        // TODO Auto-generated method stub
        int t = this.getSortierung();
        int o = other.getSortierung();
        if (t > o) {
            return 1;
        } else if (o > t) {
            return -1;
        } else {
            return 0;
        }
    }
}
