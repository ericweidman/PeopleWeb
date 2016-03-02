package com.theironyard;


import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

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

}
