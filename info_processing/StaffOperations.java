package info_processing;

import common.DBManager;
import common.Input;
import java.sql.*;

public class StaffOperations {
    public static void main(String[] args) {
        try {
            DBManager.beginTransaction();

            int storeID = -1;

            if (args != null && args.length > 0) {
                storeID = Integer.parseInt(args[0]);
                System.out.println("[Admin] Operating directly on storeID: " + storeID);
            } else {
                // Manager mode: ask for staff ID to get their store
                int managerID = Input.getInt("Enter your staff ID to verify for this session");
                String getStoreQuery = String.format("SELECT storeID FROM WorksAt WHERE staffID = %d", managerID);
                ResultSet storeRS = DBManager.query(getStoreQuery);
                if (storeRS.next()) {
                    storeID = storeRS.getInt("storeID");
                } else {
                    System.out.println("Could not determine your store. Exiting.");
                    return;
                }
            }

            System.out.println("1. Insert\n2. Update\n3. Delete");
            int choice = Input.getInt("Choose operation for StaffMember");

            switch (choice) {
                case 1: { // INSERT STAFF
                    String name = Input.getLine("Enter staff name");
                    int age = Input.getInt("Enter age");
                    String address = Input.getLine("Enter address");
                    String title = Input.getString("Enter title (Billing, Cashier, Registration Officer, Warehouse Worker, Manager, Assistant Manager, Other)");
                    String phone = Input.getString("Enter phone number");
                    String email = Input.getString("Enter email");
                    String insertSQL = String.format(
                        "INSERT INTO StaffMember (name, age, address, title, phone, emailAddress) VALUES ('%s', %d, '%s', '%s', '%s', '%s')",
                        name, age, address, title, phone, email
                    );
                    DBManager.execute(insertSQL);

                    // Get newly inserted staffID
                    ResultSet rs = DBManager.query("SELECT LAST_INSERT_ID() AS newID");
                    if (rs.next()) {
                        int newStaffID = rs.getInt("newID");
                        String worksAtSQL = String.format("INSERT INTO WorksAt (staffID, storeID) VALUES (%d, %d)", newStaffID, storeID);
                        DBManager.execute(worksAtSQL);
                        System.out.println("Staff inserted and assigned to storeID " + storeID + ".");
                    }
                    break;
                }

                case 2: { // UPDATE STAFF MEMBER DETAILS
                    int staffID = Input.getInt("Enter staff ID to update");
                
                    String checkSQL = String.format(
                        "SELECT sm.title FROM StaffMember sm JOIN WorksAt w ON sm.staffID = w.staffID WHERE sm.staffID = %d AND w.storeID = %d",
                        staffID, storeID
                    );
                    ResultSet rs = DBManager.query(checkSQL);
                
                    if (rs.next()) {
                        String title = rs.getString("title");
                        if (title.equalsIgnoreCase("Manager")) {
                            System.out.println("You cannot modify another Manager.");
                        } else {
                            System.out.println("1. Name\n2. Age\n3. Home Address\n4. Title\n5. Email Address\n6. Phone\n7. Employment Time");
                            int detail = Input.getInt("Choose what info you want to update");
                
                            String newInfo = "";
                            String column = "";
                
                            switch (detail) {
                                case 1:
                                    newInfo = Input.getString("Enter new name");
                                    column = "name";
                                    break;
                                case 2:
                                    newInfo = Input.getString("Enter new age");
                                    column = "age";
                                    break;
                                case 3:
                                    newInfo = Input.getString("Enter new home address");
                                    column = "address";
                                    break;
                                case 4:
                                    newInfo = Input.getString("Enter new title (Billing, Cashier, Registration Officer, Warehouse Worker, Manager, Assistant Manager, Other)");
                                    column = "title";
                                    break;
                                case 5:
                                    newInfo = Input.getString("Enter new email address");
                                    column = "emailAddress";
                                    break;
                                case 6:
                                    newInfo = Input.getString("Enter new phone");
                                    column = "phone";
                                    break;
                                case 7:
                                    newInfo = Input.getString("Enter new Employment Time (YYYY-MM-DD HH:MM:SS)");
                                    column = "employmentTime";
                                    break;
                                default:
                                    System.out.println("Invalid selection.");
                                    return;
                            }
                
                            String updateSQL = String.format("UPDATE StaffMember SET %s = '%s' WHERE staffID = %d", column, newInfo, staffID);
                            DBManager.execute(updateSQL);
                            System.out.println("Staff information updated.");
                        }
                    } else {
                        System.out.println("This staff member is not part of this store.");
                    }
                    break;
                }
                
                case 3: { // DELETE STAFF
                    int deleteID = Input.getInt("Enter staff ID to delete");
                    String checkSQL = String.format(
                        "SELECT sm.title FROM StaffMember sm JOIN WorksAt w ON sm.staffID = w.staffID WHERE sm.staffID = %d AND w.storeID = %d",
                        deleteID, storeID
                    );
                    ResultSet rs = DBManager.query(checkSQL);

                    if (rs.next()) {
                        String title = rs.getString("title");
                        if (title.equals("Manager")) {
                            System.out.println("You cannot delete another Manager.");
                        } else {
                            String deleteSQL = String.format("DELETE FROM StaffMember WHERE staffID = %d", deleteID);
                            DBManager.execute(deleteSQL);
                            System.out.println("Staff member deleted.");
                        }
                    } else {
                        System.out.println("This staff member is not part of this store.");
                    }
                    break;
                }

                default:
                    System.out.println("Invalid choice");
            }

            DBManager.commitTransaction();
            System.out.println("Operation completed.");

        } catch (Exception e) {
            e.printStackTrace();
            DBManager.rollbackTransaction();
        } finally {
            DBManager.close();
        }
    }
}
