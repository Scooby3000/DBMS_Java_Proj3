package common;

import java.sql.*;

public class AdminInterface {
    public static void main(String[] args) {
        while (true) {
            try {
                DBManager.beginTransaction();
                boolean continueSession = true;

                while (continueSession) {
                    System.out.println("\n--- Admin Interface ---");
                    System.out.println("1. Store Operations");
                    System.out.println("2. Staff Operations");
                    System.out.println("3. Club Member Operations");
                    System.out.println("4. Manage Transactions");
                    System.out.println("5. Add Inventory");
                    System.out.println("6. Restock Inventory");
                    System.out.println("7. Return Products");
                    System.out.println("8. Transfer Inventory");
                    System.out.println("9. Manage Product Promotions");
                    System.out.println("10. Generate Bills");
                    System.out.println("11. Generate Rewards");
                    System.out.println("12. Stock Reports by Store");
                    System.out.println("13. Stock Reports by Product");
                    System.out.println("14. Customer Growth Report");
                    System.out.println("15. Sales Reports");
                    System.out.println("16. Customer Activity By Time Period");
                    System.out.println("17. Exit");

                    int choice = Input.getInt("Select an operation");
    
                    switch (choice) {
                        case 1:
                            info_processing.StoreOperations.main(null);
                            break;
                        case 2:
                            int storeID = Input.getInt("Which store would you like to perform operations on");
                            info_processing.StaffOperations.main(new String[]{String.valueOf(storeID)});
                            break;
                        case 3:
                            info_processing.CustomerOperations.main(null);
                            break;
                        case 4:
                            analytics.TransactionTotals.main(null);
                            break;
                        case 5:
                            inventory.AddInventory.main(null);
                            break;
                        case 6:
                            inventory.RestockInventory.main(null);
                            break;
                        case 7:
                            inventory.ReturnProduct.main(null);
                            break;
                        case 8:
                            inventory.TransferInventory.main(null);
                            break;
                        case 9:
                            promotions.ApplyDiscount.main(null);
                            break;
                        case 10:
                            billing.GenerateBills.main(null);
                            break;
                        case 11:
                            rewards.GenerateRewards.main(null);
                            break;
                        case 12:
                            analytics.StockReportAllStores.main(null);
                            break;
                        case 13:
                            analytics.StockReportByProduct.main(null);
                            break;
                        case 14:
                            analytics.CustomerGrowthReport.main(null);
                            break;
                        case 15:
                            analytics.SalesReport.main(null);
                            break;
                        case 16:
                            analytics.CustomerActivityByPeriod.main(null);
                            break;
                        case 17:
                            continueSession = false;
                            break;
                        default:
                            System.out.println("Invalid option. Please try again.");

                    }
                }

                DBManager.commitTransaction();
            } catch (Exception e) {
                e.printStackTrace();
                DBManager.rollbackTransaction();
            } finally {
                DBManager.close(); // Reconnect if needed inside loop
            }

            System.out.println("Session ended. Returning to staff ID input...\n");
        }
    }
}
