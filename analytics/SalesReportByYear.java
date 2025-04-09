package analytics;

import common.DBManager;
import common.Input;
import java.sql.*;
public class SalesReportByYear {
    public static void main(String[] args) {
        try {
            DBManager.beginTransaction();

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

            DBManager.commitTransaction();

        } catch (Exception e) {
            e.printStackTrace();
            DBManager.rollbackTransaction();
        } finally {
            DBManager.close();
        }
    }
}