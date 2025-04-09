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

            String checkSQL = String.format("SELECT * FROM Inventory WHERE storeID = %d AND productID = %d", toStore, productID);
            ResultSet rs = DBManager.query(checkSQL);

            if (rs.next()) {
                // Product exists in destination store
                String updateTo = String.format(
                    "UPDATE Inventory SET quantity = quantity + %d WHERE storeID = %d AND productID = %d",
                    quantity, toStore, productID);
                DBManager.execute(updateTo);
            } else {
                // Product does not exist in destination, copy it
                String insertSQL = String.format(
                    "INSERT INTO Inventory (storeID, productID, quantity, buyPrice, sellPrice, discountedPrice, saleEndDate) " +
                    "SELECT %d, productID, %d, buyPrice, sellPrice, discountedPrice, saleEndDate FROM Inventory WHERE storeID = %d AND productID = %d",
                    toStore, quantity, fromStore, productID);
                DBManager.execute(insertSQL);
            }

            String updateFrom = String.format(
                "UPDATE Inventory SET quantity = quantity - %d WHERE storeID = %d AND productID = %d",
                quantity, fromStore, productID);
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
