package de.florian_timm.aufgabenPlaner.entity;

public abstract class Entity {

	protected int dbId;

	public int getId() {
		return dbId;
	}
	
	public abstract String toString();
	
	public abstract void update(Entity neu);
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + dbId;
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
		Entity other = (Entity) obj;
		if (dbId != other.dbId)
			return false;
		return true;
	}

}
