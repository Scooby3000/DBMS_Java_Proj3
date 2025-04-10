package analytics;

import common.DBManager;
import common.Input;
import java.sql.*;

public class SalesReport {
    public static void main(String[] args) {
        try {
            DBManager.beginTransaction();

            System.out.println("\nSales Report Menu:");
            System.out.println("1. View by Day");
            System.out.println("2. View by Month");
            System.out.println("3. View by Year");
            System.out.println("4. View Sales Growth by Store");

            int choice = Input.getInt("Choose an option:");

            switch (choice) {
                case 1:
                    viewByDay();
                    break;
                case 2:
                    viewByMonth();
                    break;
                case 3:
                    viewByYear();
                    break;
                case 4:
                    viewSalesGrowthByStore();
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

    private static void viewByDay() throws SQLException {
        String query = "SELECT purchaseDate, SUM(totalPrice) AS total_sales " +
                       "FROM Transaction GROUP BY purchaseDate ORDER BY purchaseDate";
        ResultSet rs = DBManager.query(query);

        System.out.println("\nSales Report By Day:");
        System.out.println("------------------------");
        while (rs.next()) {
            String date = rs.getString("purchaseDate");
            float total = rs.getFloat("total_sales");
            System.out.printf("%s → $%.2f\n", date, total);
        }
    }

    private static void viewByMonth() throws SQLException {
        String query = "SELECT YEAR(purchaseDate) AS year, MONTH(purchaseDate) AS month, " +
                       "SUM(totalPrice) AS total_sales FROM Transaction " +
                       "GROUP BY YEAR(purchaseDate), MONTH(purchaseDate) ORDER BY year, month";
        ResultSet rs = DBManager.query(query);

        System.out.println("\nSales Report By Month:");
        System.out.println("------------------------");
        while (rs.next()) {
            int year = rs.getInt("year");
            int month = rs.getInt("month");
            float total = rs.getFloat("total_sales");
            System.out.printf("%d-%02d → $%.2f\n", year, month, total);
        }
    }

    private static void viewByYear() throws SQLException {
        String query = "SELECT YEAR(purchaseDate) AS year, SUM(totalPrice) AS total_sales " +
                       "FROM Transaction GROUP BY YEAR(purchaseDate) ORDER BY year";
        ResultSet rs = DBManager.query(query);

        System.out.println("\nSales Report By Year:");
        System.out.println("------------------------");
        while (rs.next()) {
            int year = rs.getInt("year");
            float total = rs.getFloat("total_sales");
            System.out.printf("%d → $%.2f\n", year, total);
        }
    }

    private static void viewSalesGrowthByStore() throws SQLException {
        int storeID = Input.getInt("Enter store ID");
        String start = Input.getString("Enter start date (YYYY-MM-DD)");
        String end = Input.getString("Enter end date (YYYY-MM-DD)");

        String query = String.format(
            "SELECT YEAR(purchaseDate) AS year, MONTH(purchaseDate) AS month, SUM(totalPrice) AS total_sales " +
            "FROM Transaction WHERE storeID = %d AND purchaseDate BETWEEN '%s' AND '%s' " +
            "GROUP BY YEAR(purchaseDate), MONTH(purchaseDate) ORDER BY year, month",
            storeID, start, end
        );

        ResultSet rs = DBManager.query(query);

        System.out.println("\nSales Growth Report for Store ID " + storeID + ":");
        System.out.println("--------------------------------------------------");
        while (rs.next()) {
            int year = rs.getInt("year");
            int month = rs.getInt("month");
            float total = rs.getFloat("total_sales");
            System.out.printf("%d-%02d → $%.2f\n", year, month, total);
        }
    }
}
