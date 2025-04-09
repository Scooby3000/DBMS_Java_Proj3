package info_processing;

import common.DBManager;
import common.Input;
import java.sql.*;
public class StoreOperations {
    public static void main(String[] args) {
        try {
            DBManager.beginTransaction();
            System.out.println("1. Insert\n2. Update\n3. Delete");
            int choice = Input.getInt("Choose operation for Store");

            switch (choice) {
                case 1: // Insert
                    String address = Input.getLine("Enter store address");
                    String phone = Input.getString("Enter phone number");
                    String insertSQL = String.format("INSERT INTO Store (address, phone) VALUES ('%s', '%s')", address, phone);
                    DBManager.execute(insertSQL);
                    break;
                case 2: // Update
                    int storeIDUpdate = Input.getInt("Enter store ID to update");
                    String newPhone = Input.getString("Enter new phone number");
                    String updateSQL = String.format("UPDATE Store SET phone = '%s' WHERE storeID = %d", newPhone, storeIDUpdate);
                    DBManager.execute(updateSQL);
                    break;
                case 3: // Delete
                    int storeIDDelete = Input.getInt("Enter store ID to delete");
                    String deleteSQL = String.format("DELETE FROM Store WHERE storeID = %d", storeIDDelete);
                    DBManager.execute(deleteSQL);
                    break;
                default:
                    System.out.println("Invalid choice");
            }

            DBManager.commitTransaction();
            System.out.println("Operation completed successfully.");

        } catch (Exception e) {
            e.printStackTrace();
            DBManager.rollbackTransaction();
        } finally {
            DBManager.close();
        }
    }
}
