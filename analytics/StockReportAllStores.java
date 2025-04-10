package analytics;

import common.DBManager;
import common.Input;

import java.sql.*;

public class StockReportAllStores {
    public static void main(String[] args) {
        try {
            DBManager.beginTransaction();

            int storeID = -1;
            boolean isAdmin = false;

            if (args != null && args.length > 0) {
                storeID = Integer.parseInt(args[0]);
                isAdmin = true;
                System.out.println("[Admin] Viewing stock report for store ID: " + storeID);
            } else {
                int staffID = Input.getInt("Enter your staff ID to view stock report for your store only");

                // Get the store ID for the manager/staff
                String storeQuery = String.format("SELECT storeID FROM WorksAt WHERE staffID = %d", staffID);
                ResultSet storeRS = DBManager.query(storeQuery);

                if (storeRS.next()) {
                    storeID = storeRS.getInt("storeID");
                } else {
                    System.out.println("No store found for the given staff ID.");
                    return;
                }
            }

            // Query inventory for that store only
            String query = String.format(
                "SELECT s.storeID, p.name AS product_name, i.quantity, i.buyPrice, i.sellPrice, i.discountedPrice, i.saleEndDate " +
                "FROM Inventory i " +
                "JOIN Product p ON i.productID = p.productID " +
                "JOIN Store s ON i.storeID = s.storeID " +
                "WHERE s.storeID = %d " +
                "ORDER BY p.name", storeID
            );

            ResultSet rs = DBManager.query(query);

            System.out.printf("\nStock Report for Store ID %d:\n", storeID);
            System.out.println("-----------------------------------------------------------");
            while (rs.next()) {
                String product = rs.getString("product_name");
                int qty = rs.getInt("quantity");
                float buy = rs.getFloat("buyPrice");
                float sell = rs.getFloat("sellPrice");
                Float discount = rs.getObject("discountedPrice", Float.class);
                String saleEnd = rs.getString("saleEndDate");

                System.out.printf("Product: %s | Qty: %d | Buy: %.2f | Sell: %.2f | Discount: %s | Sale Ends: %s\n",
                    product, qty, buy, sell,
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
