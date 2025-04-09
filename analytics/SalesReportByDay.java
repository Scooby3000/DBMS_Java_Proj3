package analytics;

import common.DBManager;
import common.Input;
import java.sql.*;
public class SalesReportByDay {
    public static void main(String[] args) {
        try {
            DBManager.beginTransaction();

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

            DBManager.commitTransaction();

        } catch (Exception e) {
            e.printStackTrace();
            DBManager.rollbackTransaction();
        } finally {
            DBManager.close();
        }
    }
}