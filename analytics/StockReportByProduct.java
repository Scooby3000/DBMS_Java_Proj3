package analytics;

import common.DBManager;
import common.Input;
import java.sql.*;
public class StockReportByProduct {
    public static void main(String[] args) {
        try {
            DBManager.beginTransaction();

            int productID = Input.getInt("Enter product ID");

            String query = String.format(
                "SELECT s.storeID, p.name AS product_name, i.quantity, i.buyPrice, i.sellPrice, i.discountedPrice, i.saleEndDate " +
                "FROM Inventory i JOIN Product p ON i.productID = p.productID JOIN Store s ON i.storeID = s.storeID " +
                "WHERE p.productID = %d ORDER BY s.storeID",
                productID);

            ResultSet rs = DBManager.query(query);

            System.out.println("\nStock Report For Product ID " + productID + ":");
            System.out.println("---------------------------------------------------");
            while (rs.next()) {
                int storeID = rs.getInt("storeID");
                String product = rs.getString("product_name");
                int qty = rs.getInt("quantity");
                float buy = rs.getFloat("buyPrice");
                float sell = rs.getFloat("sellPrice");
                Float discount = rs.getObject("discountedPrice", Float.class);
                String saleEnd = rs.getString("saleEndDate");

                System.out.printf("Store %d | %s | Qty: %d | Buy: %.2f | Sell: %.2f | Discount: %s | Sale Ends: %s\n",
                    storeID, product, qty, buy, sell,
                    (discount == null ? "N/A" : String.format("%.2f", discount)),
                    (saleEnd == null ? "N/A" : saleEnd));
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