package billing;

import common.DBManager;
import common.Input;
import java.sql.*;

public class GenerateBills {
    public static void main(String[] args) {
        try {
            DBManager.beginTransaction();

            System.out.println("\nBilling Operations:");
            System.out.println("1. View total due per supplier");
            System.out.println("2. Generate new bill");

            int choice = Input.getInt("Choose an option:");

            switch (choice) {
                case 1:
                    viewTotalDuePerSupplier();
                    break;
                case 2:
                    createNewBill();
                    break;
                default:
                    System.out.println("Invalid option.");
                    DBManager.rollbackTransaction();
                    return;
            }

            DBManager.commitTransaction();

        } catch (Exception e) {
            e.printStackTrace();
            DBManager.rollbackTransaction();
        } finally {
            DBManager.close();
        }
    }

    private static void viewTotalDuePerSupplier() throws SQLException {
        String query = "SELECT SUM(billAmount) AS TOTAL_DUE, supplierName " +
                       "FROM Bills NATURAL JOIN Supplier GROUP BY supplierName";

        ResultSet rs = DBManager.query(query);
        System.out.println("\nTotal Due Per Supplier:");
        System.out.println("------------------------");
        while (rs.next()) {
            String supplierName = rs.getString("supplierName");
            float totalDue = rs.getFloat("TOTAL_DUE");
            System.out.printf("%s: $%.2f\n", supplierName, totalDue);
        }
    }

    private static void createNewBill() throws SQLException {
        int supplierID = Input.getInt("Enter supplier ID");
        if (!isValidSupplier(supplierID)) {
            System.out.println(" Invalid supplier ID. Aborting operation.");
            DBManager.rollbackTransaction();
            return;
        }

        int staffID = Input.getInt("Enter your staff ID (Billing Staff)");
        if (!isValidBillingStaff(staffID)) {
            System.out.println(" Invalid billing staff ID. Aborting operation.");
            DBManager.rollbackTransaction();
            return;
        }

        float billAmount = Input.getFloat("Enter bill amount");
        String billDate = Input.getString("Enter bill date (YYYY-MM-DD)");

        String insertSQL = String.format(
            "INSERT INTO Bills (bstaffID, supplierId, billDate, billAmount) " +
            "VALUES (%d, %d, '%s', %.2f)",
            staffID, supplierID, billDate, billAmount
        );

        DBManager.execute(insertSQL);
        System.out.println(" New bill added successfully.");
    }

    private static boolean isValidSupplier(int supplierID) throws SQLException {
        String query = "SELECT 1 FROM Supplier WHERE supplierID = " + supplierID;
        ResultSet rs = DBManager.query(query);
        return rs.next();
    }

    private static boolean isValidBillingStaff(int staffID) throws SQLException {
        String query = "SELECT 1 FROM BillingStaff WHERE staffID = " + staffID;
        ResultSet rs = DBManager.query(query);
        return rs.next();
    }
}
