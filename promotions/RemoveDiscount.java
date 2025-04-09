package promotions;

import common.DBManager;
import common.Input;
import java.sql.*;

public class RemoveDiscount {
    public static void main(String[] args) {
        try {
            DBManager.beginTransaction();

            int productID = Input.getInt("Enter product ID to remove discount");

            String updateSQL = String.format(
                "UPDATE Inventory SET discountedPrice = NULL, saleEndDate = NULL WHERE productID = %d",
                productID);
            DBManager.execute(updateSQL);

            DBManager.commitTransaction();
            System.out.println("Discount removed successfully.");

        } catch (Exception e) {
            e.printStackTrace();
            DBManager.rollbackTransaction();
        } finally {
            DBManager.close();
        }
    }
}