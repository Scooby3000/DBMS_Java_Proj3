package analytics;

import common.DBManager;
import common.Input;
import java.sql.*;
public class CustomerActivityByPeriod {
    public static void main(String[] args) {
        try {
            DBManager.beginTransaction();

            String start = Input.getString("Enter start date (YYYY-MM-DD)");
            String end = Input.getString("Enter end date (YYYY-MM-DD)");

            String query = String.format(
                "SELECT t.customerID, cm.firstName, cm.lastName, SUM(t.totalPrice) AS total_purchase_amount " +
                "FROM Transaction t JOIN ClubMember cm ON t.customerID = cm.customerID " +
                "WHERE t.purchaseDate BETWEEN '%s' AND '%s' " +
                "GROUP BY t.customerID, cm.firstName, cm.lastName ORDER BY total_purchase_amount DESC",
                start, end);

            ResultSet rs = DBManager.query(query);

            System.out.println("\nCustomer Activity Report:");
            System.out.println("---------------------------");
            while (rs.next()) {
                int id = rs.getInt("customerID");
                String name = rs.getString("firstName") + " " + rs.getString("lastName");
                float total = rs.getFloat("total_purchase_amount");
                System.out.printf("%d | %s | Total Spent: $%.2f\n", id, name, total);
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