package de.florian_timm.aufgabenPlaner;

public class Person {
	private String username = "";
	private String name = "";
	private String email = "";
	private int id = -1;

	/**
	 * @param name
	 * @param email
	 * @param id
	 */
	public Person(String username, String name, String email, int id) {
		this.username = username;
		this.name = name;
		this.email = email;
		this.id = id;
	}

	/**
	 * @return the username
	 */
	public String getUserName() {
		return username;
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
	
	public String toString() {
		return getName();
	}
}
