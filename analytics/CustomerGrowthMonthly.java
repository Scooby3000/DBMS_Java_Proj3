package analytics;

import common.DBManager;
import common.Input;
import java.sql.*;

public class CustomerGrowthMonthly {
    public static void main(String[] args) {
        try {
            DBManager.beginTransaction();

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

            DBManager.commitTransaction();

        } catch (Exception e) {
            e.printStackTrace();
            DBManager.rollbackTransaction();
        } finally {
            DBManager.close();
        }
    }
}