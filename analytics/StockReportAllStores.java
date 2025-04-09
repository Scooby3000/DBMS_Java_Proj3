package analytics;

import common.DBManager;
import common.Input;
import java.sql.*;
public class StockReportAllStores {
    public static void main(String[] args) {
        try {
            DBManager.beginTransaction();

            String query = "SELECT s.storeID, p.name AS product_name, i.quantity, i.buyPrice, i.sellPrice, i.discountedPrice, i.saleEndDate " +
                           "FROM Inventory i JOIN Product p ON i.productID = p.productID JOIN Store s ON i.storeID = s.storeID " +
                           "ORDER BY s.storeID, p.name";
            ResultSet rs = DBManager.query(query);

            System.out.println("\nStock Report For All Stores:");
            System.out.println("------------------------------");
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
