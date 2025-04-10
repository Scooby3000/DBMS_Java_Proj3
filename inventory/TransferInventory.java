package inventory;

import common.DBManager;
import common.Input;
import java.sql.*;

public class TransferInventory {
    public static void main(String[] args) {
        try {
            DBManager.beginTransaction();

            int fromStore = Input.getInt("Enter source store ID");
            int toStore = Input.getInt("Enter destination store ID");
            int productID = Input.getInt("Enter product ID");
            int quantity = Input.getInt("Enter quantity to transfer");

            // Check if product exists in source store and fetch quantity
            String sourceCheckSQL = String.format(
                "SELECT quantity FROM Inventory WHERE storeID = %d AND productID = %d",
                fromStore, productID
            );
            ResultSet sourceRS = DBManager.query(sourceCheckSQL);

            if (!sourceRS.next()) {
                System.out.println(" Product not found in source store.");
                DBManager.rollbackTransaction();
                return;
            }

            int availableQty = sourceRS.getInt("quantity");
            if (availableQty < quantity) {
                System.out.printf(" Not enough quantity in source store. Available: %d, Requested: %d\n", availableQty, quantity);
                DBManager.rollbackTransaction();
                return;
            }

            // Check if product exists in destination store
            String destCheckSQL = String.format(
                "SELECT 1 FROM Inventory WHERE storeID = %d AND productID = %d",
                toStore, productID
            );
            ResultSet destRS = DBManager.query(destCheckSQL);

            if (destRS.next()) {
                // Product exists in destination store
                String updateTo = String.format(
                    "UPDATE Inventory SET quantity = quantity + %d WHERE storeID = %d AND productID = %d",
                    quantity, toStore, productID
                );
                DBManager.execute(updateTo);
            } else {
                // Product does not exist in destination, copy from source
                String insertSQL = String.format(
                    "INSERT INTO Inventory (storeID, productID, quantity, buyPrice, sellPrice, discountedPrice, saleEndDate) " +
                    "SELECT %d, productID, %d, buyPrice, sellPrice, discountedPrice, saleEndDate " +
                    "FROM Inventory WHERE storeID = %d AND productID = %d",
                    toStore, quantity, fromStore, productID
                );
                DBManager.execute(insertSQL);
            }

            // Subtract from source
            String updateFrom = String.format(
                "UPDATE Inventory SET quantity = quantity - %d WHERE storeID = %d AND productID = %d",
                quantity, fromStore, productID
            );
            DBManager.execute(updateFrom);

            DBManager.commitTransaction();
            System.out.println("Inventory transferred successfully.");

        } catch (Exception e) {
            e.printStackTrace();
            DBManager.rollbackTransaction();
        } finally {
            DBManager.close();
        }
    }
}
