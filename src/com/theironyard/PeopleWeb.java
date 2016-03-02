package com.theironyard;

import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.*;

public class PeopleWeb {

    public static void createTables(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("DROP TABLE IF EXISTS people");
        stmt.execute("CREATE TABLE IF NOT EXISTS people (id IDENTITY, first_name VARCHAR, last_name VARCHAR, email VARCHAR," +
                "country VARCHAR, ip VARCHAR )");

    }

    public static void insertPerson(Connection conn, String firstName, String lastName, String email, String country, String ipAddress) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO people VALUES (NULL, ?, ?, ?, ?, ?)");
        stmt.setString(1, firstName);
        stmt.setString(2, lastName);
        stmt.setString(3, email);
        stmt.setString(4, country);
        stmt.setString(5, ipAddress);
        stmt.execute();
    }

    public static Person selectPerson(Connection conn, int id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM people WHERE id = ?");
        stmt.setInt(1, id);
        ResultSet results = stmt.executeQuery();
        if (results.next()) {
            String firstName = (results.getString("first_name"));
            String lastName = (results.getString("last_name"));
            String email = (results.getString("email"));
            String country = (results.getString("country"));
            String ipAddress = (results.getString("ip"));
            return new Person(id, firstName, lastName, email, country, ipAddress);
        }
        return null;
    }

    public static Person populateDatabase(Connection conn) throws FileNotFoundException, SQLException {
        File f = new File("people.csv");
        Scanner fileScanner = new Scanner(f);
        fileScanner.nextLine();
        while (fileScanner.hasNext()) {
            String line = fileScanner.nextLine();
            String column[] = line.split(",");
            insertPerson(conn, column[1], column[2], column[3], column[4], column[5]);
        }
        return null;
    }

    public static ArrayList<Person> selectPeople(Connection conn, int offset) throws SQLException {
        ArrayList<Person> persons = new ArrayList<>();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM people LIMIT 20 OFFSET ?");
        stmt.setInt(1, offset);
        ResultSet results = stmt.executeQuery();
        while (results.next()) {
            int id = results.getInt("id");
            String name = results.getString("first_name");
            String lastName = results.getString("last_name");
            String email = results.getString("email");
            String country = results.getString("country");
            String ip = results.getString("ip");
            Person person = new Person(id, name, lastName, email, country, ip);
            persons.add(person);

        }
        return persons;

    }

    public static void main(String[] args) throws FileNotFoundException, SQLException {

        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        createTables(conn);
        populateDatabase(conn);

        Spark.init();

        Spark.get(
                "/",
                ((request, response) -> {
                    HashMap m = new HashMap();
                    String userClick = request.queryParams("userClick");
                    int userClickValue = 0;
                    boolean last = false;
                    boolean next = false;

                    if (userClick != null) {
                        userClickValue = Integer.valueOf(userClick);
                    }

                    ArrayList<Person> twentyPeople = selectPeople(conn, userClickValue);
                    if (userClickValue >= 20) {
                        last = true;
                    }
                    if (selectPeople(conn, userClickValue + 20).size() > 0) {
                        next = true;
                    }

                    m.put("person", twentyPeople);
                    m.put("forward", userClickValue + 20);
                    m.put("previous", userClickValue - 20);
                    m.put("next", next);
                    m.put("last", last);
                    return new ModelAndView(m, "home.html");
                }),
                new MustacheTemplateEngine()
        );
        Spark.get(
                "/person",
                ((request, response) -> {
                    int idNumber = Integer.valueOf(request.queryParams("id"));
                    HashMap m = new HashMap();
                    Person person = selectPerson(conn, idNumber);
                    m.put("person", person);
                    return new ModelAndView(m, "person.html");
                }),
                new MustacheTemplateEngine()
        );
    }
}