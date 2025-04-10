package analytics;

import common.DBManager;
import common.Input;
import java.sql.*;

public class CustomerGrowthReport {
    public static void main(String[] args) {
        try {
            DBManager.beginTransaction();

            System.out.println("\nCustomer Growth Report Menu:");
            System.out.println("1. View Monthly Growth");
            System.out.println("2. View Yearly Growth");

            int choice = Input.getInt("Choose an option:");

            switch (choice) {
                case 1:
                    viewMonthlyGrowth();
                    break;
                case 2:
                    viewYearlyGrowth();
                    break;
                default:
                    System.out.println("Invalid option.");
            }

            DBManager.commitTransaction();

        } catch (Exception e) {
            e.printStackTrace();
            DBManager.rollbackTransaction();
        } finally {
            DBManager.close();
        }
    }

    private static void viewMonthlyGrowth() throws SQLException {
        String query = "SELECT YEAR(purchaseDate) AS year, MONTH(purchaseDate) AS month, " +
                       "COUNT(DISTINCT customerID) AS new_customers FROM Transaction " +
                       "GROUP BY YEAR(purchaseDate), MONTH(purchaseDate) ORDER BY year, month";

        ResultSet rs = DBManager.query(query);
        System.out.println("\nCustomer Growth Report By Month:");
        System.out.println("-----------------------------------");
        while (rs.next()) {
            int year = rs.getInt("year");
            int month = rs.getInt("month");
            int count = rs.getInt("new_customers");
            System.out.printf("%d-%02d → %d new customers\n", year, month, count);
        }
    }

    private static void viewYearlyGrowth() throws SQLException {
        String query = "SELECT YEAR(purchaseDate) AS year, COUNT(DISTINCT customerID) AS new_customers " +
                       "FROM Transaction GROUP BY YEAR(purchaseDate) ORDER BY year";

        ResultSet rs = DBManager.query(query);
        System.out.println("\nCustomer Growth Report By Year:");
        System.out.println("--------------------------------");
        while (rs.next()) {
            int year = rs.getInt("year");
            int count = rs.getInt("new_customers");
            System.out.printf("%d → %d new customers\n", year, count);
        }
    }
}
