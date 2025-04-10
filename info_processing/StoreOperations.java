package info_processing;

import common.*;
import java.sql.*;
public class StoreOperations {
    public static void main(String[] args) {
        try {
            DBManager.beginTransaction();
            System.out.println("1. Insert\n2. Update\n3. Delete");
            int choice = Input.getInt("Choose operation for Store");
            boolean response = true;
            switch (choice) {
                case 1: // Insert
                    String address = Input.getLine("Enter store address");
                    String phone = Input.getString("Enter phone number");
                    String insertSQL = String.format("INSERT INTO Store (address, phone) VALUES ('%s', '%s')", address, phone);
                    response = DBManager.execute(insertSQL);
                    break;
                case 2: // Update
                    int storeIDUpdate = Input.getInt("Enter store ID to update");
                    System.out.println("1. Address\n2. Phone");
                    int detail = Input.getInt("Choose what info you want to update");
                    String newInfo = "";
                    String column = "";
                    switch(detail) {
                        case 1:
                            newInfo = Input.getString("Enter new address");
                            column = "address";
                            break;
                        case 2:
                            newInfo = Input.getString("Enter new phone number");
                            column = "phone";
                            break;
                    }
                    String updateSQL = String.format("UPDATE Store SET %s = '%s' WHERE storeID = %d", column, newInfo, storeIDUpdate);
                    response = DBManager.execute(updateSQL);
                    break;
                case 3: // Delete
                    int storeIDDelete = Input.getInt("Enter store ID to delete");
                    String deleteSQL = String.format("DELETE FROM Store WHERE storeID = %d", storeIDDelete);
                    response = DBManager.execute(deleteSQL);
                    break;
                default:
                    System.out.println("Invalid choice");
            }

            if(response){
                DBManager.commitTransaction();
                System.out.println("Operation completed successfully.");
            }
            else{
                System.out.println("Operation could not be completed. Please try again");
            }

        } catch (Exception e) {
            e.printStackTrace();
            DBManager.rollbackTransaction();
        } finally {
            DBManager.close();
        }
    }
}
