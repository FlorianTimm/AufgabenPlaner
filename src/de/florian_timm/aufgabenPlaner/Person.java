package de.florian_timm.aufgabenPlaner;

public class Person {
	private String name = "";
	private String email = "";
	private int id = -1;

	/**
	 * @param name
	 * @param email
	 * @param id
	 */
	public Person(String name, String email, int id) {
		this.name = name;
		this.email = email;
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
}
