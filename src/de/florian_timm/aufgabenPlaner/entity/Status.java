package de.florian_timm.aufgabenPlaner.entity;

public class Status extends EntitySortierung {
	private String bezeichnung;
	private int sortierung;

	public Status(int dbId, String bezeichnung, int sortierung) {
		this.dbId = dbId;
		this.bezeichnung = bezeichnung;
		this.sortierung = sortierung;
	}


	public String toString() {
		return bezeichnung;
	}

	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	public int getSortierung() {
		return sortierung;
	}

	public void setSortierung(int sortierung) {
		this.sortierung = sortierung;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((bezeichnung == null) ? 0 : bezeichnung.hashCode());
		result = prime * result + sortierung;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Status other = (Status) obj;
		if (bezeichnung == null) {
			if (other.bezeichnung != null)
				return false;
		} else if (!bezeichnung.equals(other.bezeichnung))
			return false;
		if (sortierung != other.sortierung)
			return false;
		return true;
	}

	@Override
	public void update(Entity neu) {
		// TODO Auto-generated method stub
		
	}
	
	

}
