package analytics;
import common.DBManager;
import common.Input;

import java.sql.*;
public class TransactionTotals {
    public static void main(String[] args) {
        try {
            DBManager.beginTransaction();

            String query = "SELECT transactionID, totalPrice FROM Transaction";
            ResultSet rs = DBManager.query(query);

            System.out.println("\nTransaction Totals:");
            System.out.println("----------------------");
            while (rs.next()) {
                int id = rs.getInt("transactionID");
                float total = rs.getFloat("totalPrice");
                System.out.printf("Transaction ID %d → $%.2f\n", id, total);
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