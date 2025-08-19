package sample;

import java.sql.*;

public class Connectiondb {

    // Connect to the database
    public static Connection connect() {
        String url = "jdbc:mysql://localhost:3307/database_freelancers";
        String user = "root";
        String password = "";

        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.out.println("Error connecting to DB: " + e.getMessage());
            return null;
        }
    }

    // Insert query (INSERT INTO ...)
    public static void insert(String query) {
        try (Connection conn = connect();
             Statement statement = conn.createStatement()) {
            statement.execute(query);
        } catch (SQLException ex) {
            System.out.println("Error in insert() method: " + ex.getMessage());
        }
    }

    // Display / SELECT query
    public static ResultSet display(String query) {
        try {
            Connection conn = connect();
            Statement statement = conn.createStatement();
            return statement.executeQuery(query);
        } catch (SQLException ex) {
            System.out.println("Error in display() method: " + ex.getMessage());
            return null;
        }
    }
    public static double getDoubleValue(String query) {
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getDouble(1);  // get first column as double
            }
        } catch (SQLException ex) {
            System.out.println("Error in getDoubleValue(): " + ex.getMessage());
        }
        return 0.0;
    }
    public static int getCount(String query) {
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            System.out.println("Error in getCount(): " + ex.getMessage());
        }
        return 0;
    }
    // Update query (UPDATE ...)
    public static int update(String query) {
        try (Connection conn = connect();
             Statement statement = conn.createStatement()) {
            return statement.executeUpdate(query);
        } catch (SQLException ex) {
            System.out.println("Error in update() method: " + ex.getMessage());
            return 0;
        }
    }

    // Delete query (DELETE FROM ...)
    public static void delete(String query) {
        try (Connection conn = connect();
             Statement statement = conn.createStatement()) {
            statement.execute(query);
        } catch (SQLException ex) {
            System.out.println("Error in delete() method: " + ex.getMessage());
        }
    }
}
