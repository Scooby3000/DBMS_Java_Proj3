package promotions;

import common.DBManager;
import common.Input;
import java.sql.*;

public class ApplyDiscount {
    public static void main(String[] args) {
        try {
            DBManager.beginTransaction();

            System.out.println("\nDiscount Operations:");
            System.out.println("1. Apply Discount");
            System.out.println("2. Remove Discount");

            int choice = Input.getInt("Choose an option:");

            switch (choice) {
                case 1:
                    applyDiscount();
                    break;
                case 2:
                    removeDiscount();
                    break;
                default:
                    System.out.println("Invalid option.");
                    DBManager.rollbackTransaction();
                    return;
            }

            DBManager.commitTransaction();

        } catch (Exception e) {
            e.printStackTrace();
            DBManager.rollbackTransaction();
        } finally {
            DBManager.close();
        }
    }

    private static void applyDiscount() throws SQLException {
        int storeID = Input.getInt("Enter store ID");
        if (!isValidStore(storeID)) {
            System.out.println(" Invalid store ID. Aborting operation.");
            DBManager.rollbackTransaction();
            return;
        }

        int productID = Input.getInt("Enter product ID to apply discount");
        if (!isValidProductForStore(storeID, productID)) {
            System.out.println(" Invalid product ID for the given store. Aborting operation.");
            DBManager.rollbackTransaction();
            return;
        }

        float discountPercent = Input.getFloat("Enter discount percentage (e.g., 20 for 20%)");
        if (discountPercent < 0 || discountPercent > 100) {
            System.out.println(" Discount percentage must be between 0 and 100.");
            DBManager.rollbackTransaction();
            return;
        }

        String saleEndDate = Input.getString("Enter sale end date (YYYY-MM-DD)");
        float discountMultiplier = (100 - discountPercent) / 100;

        String updateSQL = String.format(
            "UPDATE Inventory SET discountedPrice = sellPrice * %.2f, saleEndDate = '%s' " +
            "WHERE storeID = %d AND productID = %d",
            discountMultiplier, saleEndDate, storeID, productID
        );

        DBManager.execute(updateSQL);
        System.out.println("Discount applied successfully.");
    }

    private static void removeDiscount() throws SQLException {
        int storeID = Input.getInt("Enter store ID");
        if (!isValidStore(storeID)) {
            System.out.println(" Invalid store ID. Aborting operation.");
            DBManager.rollbackTransaction();
            return;
        }

        int productID = Input.getInt("Enter product ID to remove discount");
        if (!isValidProductForStore(storeID, productID)) {
            System.out.println(" Invalid product ID for the given store. Aborting operation.");
            DBManager.rollbackTransaction();
            return;
        }

        String updateSQL = String.format(
            "UPDATE Inventory SET discountedPrice = NULL, saleEndDate = NULL " +
            "WHERE storeID = %d AND productID = %d",
            storeID, productID
        );

        DBManager.execute(updateSQL);
        System.out.println("Discount removed successfully.");
    }

    private static boolean isValidStore(int storeID) throws SQLException {
        String query = "SELECT 1 FROM Store WHERE storeID = " + storeID;
        ResultSet rs = DBManager.query(query);
        return rs.next();
    }

    private static boolean isValidProductForStore(int storeID, int productID) throws SQLException {
        String query = String.format(
            "SELECT 1 FROM Inventory WHERE storeID = %d AND productID = %d", storeID, productID
        );
        ResultSet rs = DBManager.query(query);
        return rs.next();
    }
}
