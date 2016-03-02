package com.theironyard;


import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by ericweidman on 3/2/16.
 */
public class MainTest {

    public Connection startConnection() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:h2:./test");
        PeopleWeb.createTables(conn);
        return conn;
    }

    public void endConnection(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("DROP TABLE people");
        conn.close();
    }

    @Test
    public void testInsertPerson() throws SQLException {
        Connection conn = startConnection();
        PeopleWeb.insertPerson(conn, "Eric", "Weidman", "ericweidman@gmail.com", "United States", "THIS IS TOTALLY AN IPADRESS");
        Person person = PeopleWeb.selectPerson(conn, 1);
        endConnection(conn);
        Assert.assertTrue(person != null);


    }

    @Test
    public void testInsertPopulate() throws SQLException, FileNotFoundException {
        Connection conn = startConnection();
        PeopleWeb.populateDatabase(conn);
        Person person = PeopleWeb.selectPerson(conn, 1000);
        endConnection(conn);
        Assert.assertTrue(person != null);

    }

    @Test
    public void testSelectPeople() throws SQLException {
        Connection conn = startConnection();
        PeopleWeb.insertPerson(conn, "", "", "", "", "");
        PeopleWeb.insertPerson(conn, " ", " ", " ", " ", " ");
        ArrayList<Person> persons = PeopleWeb.selectPeople(conn, 1);
        endConnection(conn);
        Assert.assertTrue(persons.size() == 1);




    }
}
