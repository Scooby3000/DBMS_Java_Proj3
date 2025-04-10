package info_processing;

import common.DBManager;
import common.Input;
import java.sql.*;
public class SupplierOperations {
    public static void main(String[] args) {
        try {
            DBManager.beginTransaction();
            System.out.println("1. Insert\n2. Update\n3. Delete");
            int choice = Input.getInt("Choose operation for Supplier");

            switch (choice) {
                case 1:
                    String name = Input.getLine("Enter supplier name");
                    String phone = Input.getString("Enter phone number");
                    String email = Input.getString("Enter email");
                    String location = Input.getLine("Enter location");
                    String insertSQL = String.format("INSERT INTO Supplier (supplierName, phoneNumber, emailAddress, location) VALUES ('%s', '%s', '%s', '%s')",
                            name, phone, email, location);
                    DBManager.execute(insertSQL);
                    break;
                case 2:
                    int supplierID = Input.getInt("Enter supplier ID to update");
                    String newPhone = Input.getString("Enter new phone number");
                    String updateSQL = String.format("UPDATE Supplier SET phoneNumber = '%s' WHERE supplierID = %d", newPhone, supplierID);
                    DBManager.execute(updateSQL);
                    break;
                case 3:
                    int deleteID = Input.getInt("Enter supplier ID to delete");
                    String deleteSQL = String.format("DELETE FROM Supplier WHERE supplierID = %d", deleteID);
                    DBManager.execute(deleteSQL);
                    break;
                default:
                    System.out.println("Invalid choice");
            }

            DBManager.commitTransaction();
            System.out.println("Operation completed");

        } catch (Exception e) {
            e.printStackTrace();
            DBManager.rollbackTransaction();
        } finally {
            DBManager.close();
        }
    }
}
