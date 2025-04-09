package inventory;

import common.DBManager;
import common.Input;
import java.sql.*;
public class RestockInventory {
    public static void main(String[] args) {
        try {
            DBManager.beginTransaction();

            int storeID = Input.getInt("Enter store ID");
            int productID = Input.getInt("Enter product ID");
            int addQty = Input.getInt("Enter quantity to restock");

            String updateSQL = String.format(
                "UPDATE Inventory SET quantity = quantity + %d WHERE storeID = %d AND productID = %d",
                addQty, storeID, productID);
            DBManager.execute(updateSQL);

            DBManager.commitTransaction();
            System.out.println("Inventory restocked successfully.");

        } catch (Exception e) {
            e.printStackTrace();
            DBManager.rollbackTransaction();
        } finally {
            DBManager.close();
        }
    }
}