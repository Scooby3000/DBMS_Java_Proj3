package analytics;

import common.DBManager;
import common.Input;
import java.sql.*;

public class SalesReportByMonth {
    public static void main(String[] args) {
        try {
            DBManager.beginTransaction();

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

            DBManager.commitTransaction();

        } catch (Exception e) {
            e.printStackTrace();
            DBManager.rollbackTransaction();
        } finally {
            DBManager.close();
        }
    }
}
