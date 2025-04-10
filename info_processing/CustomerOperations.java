package info_processing;

import common.DBManager;
import common.Input;
import java.sql.*;
public class CustomerOperations {
    public static void main(String[] args) {
        try {
            DBManager.beginTransaction();
            System.out.println("1. Insert\n2. Update\n3. Delete");
            int choice = Input.getInt("Choose operation for ClubMember");

            switch (choice) {
                case 1:
                    String firstName = Input.getString("Enter first name");
                    String lastName = Input.getString("Enter last name");
                    String status = Input.getString("Enter active status (Active/Inactive)");
                    String level = Input.getString("Enter membership level (Basic/Gold/Platinum)");
                    String email = Input.getString("Enter email");
                    String phone = Input.getString("Enter phone");
                    String address = Input.getLine("Enter home address");
                    String startDate = Input.getString("Enter start date (YYYY-MM-DD)");
                    String endDate = Input.getString("Enter end date (YYYY-MM-DD)");
                    String insertSQL = String.format("INSERT INTO ClubMember (activeStatus, firstName, lastName, membershipLevel, emailAddress, phone, homeAddress, startDate, endDate) VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')",
                            status, firstName, lastName, level, email, phone, address, startDate, endDate);
                    DBManager.execute(insertSQL);
                    break;
                case 2:
                    int customerID = Input.getInt("Enter customer ID to update");
                    String newLevel = Input.getString("Enter new membership level");
                    String updateSQL = String.format("UPDATE ClubMember SET membershipLevel = '%s' WHERE customerID = %d", newLevel, customerID);
                    DBManager.execute(updateSQL);
                    break;
                case 3:
                    int deleteID = Input.getInt("Enter customer ID to delete");
                    String deleteSQL = String.format("DELETE FROM ClubMember WHERE customerID = %d", deleteID);
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