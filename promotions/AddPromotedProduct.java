package promotions;
import common.DBManager;
import common.Input;
import java.sql.*;
public class AddPromotedProduct {
    public static void main(String[] args) {
        try {
            DBManager.beginTransaction();

            int storeID = Input.getInt("Enter store ID");
            int productID = Input.getInt("Enter product ID");
            int quantity = Input.getInt("Enter quantity");
            float buyPrice = Input.getFloat("Enter buy price");
            float sellPrice = Input.getFloat("Enter sell price");
            float discountedPrice = Input.getFloat("Enter discounted price");
            String saleEndDate = Input.getString("Enter sale end date (YYYY-MM-DD)");

            String insertSQL = String.format(
                "INSERT INTO Inventory (storeID, productID, quantity, buyPrice, sellPrice, discountedPrice, saleEndDate) " +
                "VALUES (%d, %d, %d, %.2f, %.2f, %.2f, '%s')",
                storeID, productID, quantity, buyPrice, sellPrice, discountedPrice, saleEndDate);
            DBManager.execute(insertSQL);

            DBManager.commitTransaction();
            System.out.println("Promoted product added successfully.");

        } catch (Exception e) {
            e.printStackTrace();
            DBManager.rollbackTransaction();
        } finally {
            DBManager.close();
        }
    }
}