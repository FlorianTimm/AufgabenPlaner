package de.florian_timm.aufgabenPlaner.entity;

import de.florian_timm.aufgabenPlaner.schnittstelle.DatenhaltungS;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Person extends Entity implements Comparable<Person> {
    private static Map<Integer, Person> alle = new HashMap<Integer, Person>();
    private String username;
    private String name;
    private String email;

    public Person(String username, String name, String email, int id) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.dbId = id;
    }

    public static Person getPerson(String username) {
        return loadPersonFromDB("username = '" + username + "'");
    }

    public static Person getPerson(int id) {
        if (alle.containsKey(id)) {
            return alle.get(id);
        } else {
            return loadPersonFromDB("id = " + id);
        }
    }

    public static Person loadPersonFromDB(String filter) {
        try {
            String sql = "SELECT * FROM person where " + filter + ";";
            System.out.println(sql);
            ResultSet rs = DatenhaltungS.query(sql);

            if (rs.next()) {
                return getPersonFromResult(rs);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static Person getPersonFromResult(ResultSet rs) {
        try {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String username = rs.getString("username");
            String email = rs.getString("email");
            Person p = new Person(username, name, email, id);
            alle.put(id, p);
            return p;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private static void loadData() {
        alle.clear();
        try {
            ResultSet rs = DatenhaltungS.query("SELECT * FROM person;");
            while (rs.next()) {
                getPersonFromResult(rs);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Person makePerson(String username, String name, String email) {
        DatenhaltungS.update("INSERT INTO person (name, username, email) VALUES ('" + name + "','" + username + "','"
                + email + "');");
        return getPerson(username);
    }

    public static void createTable() {
        DatenhaltungS.update("CREATE TABLE IF NOT EXISTS person (" + "id INTEGER PRIMARY KEY, "
                + "username	TEXT NOT NULL, " + "name TEXT NOT NULL, " + "email TEXT);");
    }

    public static Person[] getArray() {
        checkLoading();
        Person[] p = alle.values().toArray(new Person[0]);
        // Arrays.sort(k);
        return p;
    }

    protected static void checkLoading() {
        // da unsicher, ob alle Einträge immer vorhanden sind, wird immer neu geladen
        loadData();
    }

    public String getUserName() {
        return username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return getName() + " <" + getEmail() + ">";
    }

    @Override
    public int compareTo(Person other) {
        return this.getName().compareTo(other.getName());
    }
}
