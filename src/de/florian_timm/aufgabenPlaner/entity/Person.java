package de.florian_timm.aufgabenPlaner.entity;

import de.florian_timm.aufgabenPlaner.entity.ordner.AufgabenOrdner;
import de.florian_timm.aufgabenPlaner.entity.ordner.PersonenOrdner;
import java.util.List;

public class Person extends Entity implements Comparable<Person> {
	private String username;
	private String name;
	private String email;
	private String vorname;

	public Person(int id, String username, String name, String vorname, String email) {
		this.username = username;
		this.dbId = id;
		this.update(name, vorname, email);
	}

	public String getUserName() {
		return username;
	}

	public String getNachName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	@Override
	public String toString() {
		return getNachName() + ((getVorname() != null) ? ", " + getVorname() : "");
	}

	@Override
	public int compareTo(Person other) {
		return this.toString().compareTo(other.toString());
	}

	public String getVorname() {
		return this.vorname;
	}

	public void update(String name, String vorname, String email) {
		this.vorname = vorname;
		this.name = name;
		this.email = email;
	}

	public void updateDB(String name, String vorname, String email) {
		this.update(name, vorname, email);
		PersonenOrdner.getInstanz().update(this);
	}

	public Aufgabe[] getAufgaben() {
		List<Aufgabe> list = AufgabenOrdner.getOffeneAufgaben(this);
		return list.toArray(new Aufgabe[0]);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		result = prime * result + ((vorname == null) ? 0 : vorname.hashCode());
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
		Person other = (Person) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		if (vorname == null) {
			if (other.vorname != null)
				return false;
		} else if (!vorname.equals(other.vorname))
			return false;
		return true;
	}

	@Override
	public void update(Entity neu) {
		// TODO Auto-generated method stub
	}

}
