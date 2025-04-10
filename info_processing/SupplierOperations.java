package info_processing;

import common.*;
import java.sql.*;
public class SupplierOperations {
    public static void main(String[] args) {
        try {
            DBManager.beginTransaction();
            System.out.println("1. Insert\n2. Update\n3. Delete");
            int choice = Input.getInt("Choose operation for Supplier");
            boolean response = true;
            switch (choice) {
                case 1:
                    String name = Input.getLine("Enter supplier name");
                    String phone = Input.getString("Enter phone number");
                    String email = Input.getString("Enter email");
                    String location = Input.getLine("Enter location");
                    String insertSQL = String.format("INSERT INTO Supplier (supplierName, phoneNumber, emailAddress, location) VALUES ('%s', '%s', '%s', '%s')",
                            name, phone, email, location);
                    response = DBManager.execute(insertSQL);
                    break;
                case 2:
                    int supplierID = Input.getInt("Enter supplier ID to update");
                    System.out.println("1. Name\n2. Phone\n3. Email Address\n4. Location");
                    int detail = Input.getInt("Choose what info you want to update");
                    String newInfo = "";
                    String column = "";
                    switch(detail) {
                        case 1:
                            newInfo = Input.getString("Enter new name");
                            column = "supplierName";
                            break;
                        case 2:
                            newInfo = Input.getString("Enter new phone number");
                            column = "phoneNumber";
                            break;
                        case 3:
                            newInfo = Input.getString("Enter new email address");
                            column = "emailAddress";
                            break;
                        case 4:
                            newInfo = Input.getString("Enter new location");
                            column = "location";
                            break;
                    }
                    String updateSQL = String.format("UPDATE Supplier SET %s = '%s' WHERE supplierID = %d", column, newInfo, supplierID);
                    response = DBManager.execute(updateSQL);
                    break;
                case 3:
                    int deleteID = Input.getInt("Enter supplier ID to delete");
                    String deleteSQL = String.format("DELETE FROM Supplier WHERE supplierID = %d", deleteID);
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
