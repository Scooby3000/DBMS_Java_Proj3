package info_processing;

import common.DBManager;
import common.Input;
import java.sql.*;
public class StaffOperations {
    public static void main(String[] args) {
        try {
            DBManager.beginTransaction();
            System.out.println("1. Insert\n2. Update\n3. Delete");
            int choice = Input.getInt("Choose operation for StaffMember");

            switch (choice) {
                case 1:
                    String name = Input.getLine("Enter staff name");
                    int age = Input.getInt("Enter age");
                    String address = Input.getLine("Enter address");
                    String title = Input.getString("Enter title (Billing, Cashier, Registration Officer, Warehouse Worker, Manager, Assistant Manager, Other)");
                    String phone = Input.getString("Enter phone number");
                    String email = Input.getString("Enter email");
                    String insertSQL = String.format(
                        "INSERT INTO StaffMember (name, age, address, title, phone, emailAddress) VALUES ('%s', %d, '%s', '%s', '%s', '%s')",
                        name, age, address, title, phone, email);
                    DBManager.execute(insertSQL);
                    break;
                case 2:
                    int staffID = Input.getInt("Enter staff ID to update");
                    String newTitle = Input.getString("Enter new title");
                    String updateSQL = String.format("UPDATE StaffMember SET title = '%s' WHERE staffID = %d", newTitle, staffID);
                    DBManager.execute(updateSQL);
                    break;
                case 3:
                    int deleteID = Input.getInt("Enter staff ID to delete");
                    String deleteSQL = String.format("DELETE FROM StaffMember WHERE staffID = %d", deleteID);
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