package analytics;

import common.DBManager;
import common.Input;
import java.sql.*;

public class CustomerGrowthYearly {
    public static void main(String[] args) {
        try {
            DBManager.beginTransaction();

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

            DBManager.commitTransaction();

        } catch (Exception e) {
            e.printStackTrace();
            DBManager.rollbackTransaction();
        } finally {
            DBManager.close();
        }
    }
}