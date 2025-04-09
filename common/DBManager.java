package common;
import java.sql.*;

public class DBManager {
    private static final String jdbcURL = "jdbc:mariadb://classdb2.csc.ncsu.edu:3306/dhshah3";
    private static final String user = "dhshah3";
    private static final String password = "200599697";

    private static Connection connection = null;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(jdbcURL, user, password);
            connection.setAutoCommit(false);
        }
        return connection;
    }

    public static void beginTransaction() throws SQLException {
        getConnection().setAutoCommit(false);
    }

    public static void commitTransaction() throws SQLException {
        getConnection().commit();
    }

    public static void rollbackTransaction() {
        try {
            getConnection().rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean execute(String sql) {
        try (Statement stmt = getConnection().createStatement()) {
            stmt.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            return false;
        }
    }

    public static ResultSet query(String sql) throws SQLException {
        Statement stmt = getConnection().createStatement();
        return stmt.executeQuery(sql);
    }

    public static void close() {
        try {
            if (connection != null && !connection.isClosed()) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}