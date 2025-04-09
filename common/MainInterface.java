package common;
import java.sql.*;

public class MainInterface {
    public static void main(String[] args) {
        try {
            DBManager.beginTransaction();

            int staffID = -1;
            String staffTitle = null;
            boolean valid = false;

            while (!valid) {
                staffID = Input.getInt("Enter your staff ID");
                String query = String.format("SELECT title FROM StaffMember WHERE staffID = %d", staffID);

                ResultSet rs = DBManager.query(query);
                if (rs.next()) {
                    staffTitle = rs.getString("title");
                    valid = true;
                    System.out.println("Welcome, your role is: " + staffTitle);
                } else {
                    System.out.println("Invalid staff ID. Please try again.");
                }
            }

            int choice;
            switch (staffTitle) {
                case "Manager":
                    System.out.println("1. Store Operations\n2. Staff Operations\n3. Inventory Reports\n4. Customer Statistics");
                    choice = Input.getInt("Select an operation");
                    switch (choice) {
                        case 1: info_processing.StoreOperations.main(null); break;
                        case 2: info_processing.StaffOperations.main(null); break;
                        case 3: analytics.StockReportAllStores.main(null); break;
                        case 4: analytics.CustomerGrowthMonthly.main(null); break;
                        default: System.out.println("Invalid option");
                    }
                    break;

                case "Registration Officer":
                    System.out.println("1. Customer Operations");
                    choice = Input.getInt("Select an operation");
                    if (choice == 1) info_processing.CustomerOperations.main(null);
                    else System.out.println("Invalid option");
                    break;

                case "Warehouse Worker":
                    System.out.println("1. Add Inventory\n2. Restock Inventory\n3. Return Products\n4. Transfer Inventory");
                    choice = Input.getInt("Select an operation");
                    switch (choice) {
                        case 1: inventory.AddInventory.main(null); break;
                        case 2: inventory.RestockInventory.main(null); break;
                        case 3: inventory.ReturnProduct.main(null); break;
                        case 4: inventory.TransferInventory.main(null); break;
                        default: System.out.println("Invalid option");
                    }
                    break;

                case "Cashier":
                    System.out.println("1. View Transactions");
                    choice = Input.getInt("Select an operation");
                    if (choice == 1) analytics.TransactionTotals.main(null);
                    else System.out.println("Invalid option");
                    break;

                case "Billing":
                    System.out.println("1. Generate Bills\n2. Generate Rewards\n3. View Sales Reports");
                    choice = Input.getInt("Select an operation");
                    switch (choice) {
                        case 1: billing.GenerateBills.main(null); break;
                        case 2: rewards.GenerateRewards.main(null); break;
                        case 3: analytics.SalesReportByDay.main(null); break;
                        default: System.out.println("Invalid option");
                    }
                    break;

                default:
                    System.out.println("Your role does not have any defined operations at this time.");
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
