package info_processing;

import common.*;
import java.sql.*;
public class CustomerOperations {
   public static void main(String[] args) {
       try {
           DBManager.beginTransaction();
           System.out.println("1. Insert\n2. Update\n3. Delete");
           int choice = Input.getInt("Choose operation for ClubMember");
           boolean response = true;
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
                   response = DBManager.execute(insertSQL);
                   break;
               case 2:
                   int customerID = Input.getInt("Enter customer ID to update");
                   System.out.println("1. First Name\n2. Last Name\n3. Home Address\n4. MembershipLevel\n5. Email Address\n6. Phone\n7. Active Status");
                   int detail = Input.getInt("Choose what info you want to update");
                   String newInfo = "";
                   String column = "";
                   switch(detail){
                       case 1:
                           newInfo = Input.getString("Enter new first name");
                           column = "firstName";
                           break;

                       case 2:
                           newInfo = Input.getString("Enter new last name");
                           column= "lastName";
                           break;
                       case 3:
                           newInfo = Input.getString("Enter new home address");
                           column= "homeAddress";
                           break;
                       case 4:
                           newInfo = Input.getString("Enter new membership level (Basic/Gold/Platinum)");
                           column= "membershipLevel";
                           break;
                       case 5:
                           newInfo = Input.getString("Enter new email address");
                           column= "emailAddress";
                           break;
                       case 6:
                           newInfo = Input.getString("Enter new phone");
                           column= "phone";
                           break;
                       case 7:
                           newInfo = Input.getString("Enter new active status (Active/Inactive)");
                           column= "activeStatus";
                           break;
                   }
                   String updateSQL = String.format("UPDATE ClubMember SET %s = '%s' WHERE customerID = %d", column, newInfo, customerID);
                   response = DBManager.execute(updateSQL);
//                    String newLevel = Input.getString("Enter new membership level");
//                    String updateSQL = String.format("UPDATE ClubMember SET membershipLevel = '%s' WHERE customerID = %d", newLevel, customerID);
//                    response = DBManager.execute(updateSQL);
                   break;
               case 3:
                   int deleteID = Input.getInt("Enter customer ID to delete");
                   String deleteSQL = String.format("DELETE FROM ClubMember WHERE customerID = %d", deleteID);
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




