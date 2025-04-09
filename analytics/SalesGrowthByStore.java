package analytics;

import common.DBManager;
import common.Input;
import java.sql.*;

public class SalesGrowthByStore {
    public static void main(String[] args) {
        try {
            DBManager.beginTransaction();

            int storeID = Input.getInt("Enter store ID");
            String start = Input.getString("Enter start date (YYYY-MM-DD)");
            String end = Input.getString("Enter end date (YYYY-MM-DD)");

            String query = String.format(
                "SELECT YEAR(purchaseDate) AS year, MONTH(purchaseDate) AS month, SUM(totalPrice) AS total_sales " +
                "FROM Transaction WHERE storeID = %d AND purchaseDate BETWEEN '%s' AND '%s' " +
                "GROUP BY YEAR(purchaseDate), MONTH(purchaseDate) ORDER BY year, month",
                storeID, start, end);

            ResultSet rs = DBManager.query(query);

            System.out.println("\nSales Growth Report:");
            System.out.println("----------------------");
            while (rs.next()) {
                int year = rs.getInt("year");
                int month = rs.getInt("month");
                float total = rs.getFloat("total_sales");
                System.out.printf("%d-%02d → $%.2f\n", year, month, total);
            }

            DBManager.commitTransaction();

        } catch (Exception e) {
            e.printStackTrace();
            DBManager.rollbackTransaction();
        } finally {
            DBManager.close();
        }
    }
}