package promotions;

import common.DBManager;
import common.Input;
import java.sql.*;
public class ApplyDiscount {
    public static void main(String[] args) {
        try {
            DBManager.beginTransaction();

            int productID = Input.getInt("Enter product ID to apply discount");
            String saleEndDate = Input.getString("Enter sale end date (YYYY-MM-DD)");

            String updateSQL = String.format(
                "UPDATE Inventory SET discountedPrice = sellPrice * 0.80, saleEndDate = '%s' WHERE productID = %d",
                saleEndDate, productID);
            DBManager.execute(updateSQL);

            DBManager.commitTransaction();
            System.out.println("Discount applied successfully.");

        } catch (Exception e) {
            e.printStackTrace();
            DBManager.rollbackTransaction();
        } finally {
            DBManager.close();
        }
    }
}