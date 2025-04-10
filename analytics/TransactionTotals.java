package analytics;
import java.sql.Date;
import common.DBManager;
import common.Input;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class TransactionTotals {
    public static void main(String[] args) {
        try {
            DBManager.beginTransaction();

            System.out.println("\nTransaction Operations:");
            System.out.println("1. View transaction totals");
            System.out.println("2. Create new transaction");

            int choice = Input.getInt("Choose an option:");

            switch (choice) {
                case 1:
                    viewTransactions();
                    break;
                case 2:
                    createTransaction();
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

    private static void viewTransactions() throws SQLException {
        String query = "SELECT transactionID, totalPrice FROM Transaction";
        ResultSet rs = DBManager.query(query);

        System.out.println("\nTransaction Totals:");
        System.out.println("----------------------");
        while (rs.next()) {
            int id = rs.getInt("transactionID");
            float total = rs.getFloat("totalPrice");
            System.out.printf("Transaction ID %d → $%.2f\n", id, total);
        }
    }

    private static void createTransaction() throws SQLException {
        int storeID = Input.getInt("Enter store ID");
        int customerID = Input.getInt("Enter customer ID");
        int cashierID = Input.getInt("Enter cashier staff ID");
        String purchaseDate = Input.getString("Enter purchase date (YYYY-MM-DD)");
    
        List<Integer> productIDs = new ArrayList<>();
        List<Integer> quantities = new ArrayList<>();
    
        System.out.println("Enter product IDs and quantities (enter -1 to stop):");
        while (true) {
            int pid = Input.getInt("Product ID: ");
            if (pid == -1) break;
            int qty = Input.getInt("Quantity: ");
            productIDs.add(pid);
            quantities.add(qty);
        }
    
        float totalPrice = 0;
        StringBuilder productList = new StringBuilder();
        LocalDate today = LocalDate.parse(purchaseDate);
    
        // Pre-check all quantities first
        for (int i = 0; i < productIDs.size(); i++) {
            int pid = productIDs.get(i);
            int qty = quantities.get(i);
    
            String inventoryCheck = String.format(
                "SELECT quantity FROM Inventory WHERE storeID = %d AND productID = %d",
                storeID, pid
            );
            ResultSet rs = DBManager.query(inventoryCheck);
            if (!rs.next()) {
                System.out.printf(" Product ID %d not found in store %d.\n", pid, storeID);
                DBManager.rollbackTransaction();
                return;
            }
    
            int available = rs.getInt("quantity");
            if (available < qty) {
                System.out.printf(" Not enough quantity for product ID %d. Available: %d, Requested: %d\n", pid, available, qty);
                DBManager.rollbackTransaction();
                return;
            }
        }
    
        // All checks passed — proceed with calculation, update, insert
        for (int i = 0; i < productIDs.size(); i++) {
            int pid = productIDs.get(i);
            int qty = quantities.get(i);
    
            String inventoryQuery = String.format(
                "SELECT i.sellPrice, i.discountedPrice, i.saleEndDate, p.name " +
                "FROM Inventory i JOIN Product p ON i.productID = p.productID " +
                "WHERE i.storeID = %d AND i.productID = %d",
                storeID, pid
            );
            ResultSet rs = DBManager.query(inventoryQuery);
            rs.next(); // Already validated
    
            String productName = rs.getString("name");
            float price = rs.getFloat("sellPrice");
            Float discounted = rs.getObject("discountedPrice", Float.class);
            String saleEnd = rs.getString("saleEndDate");
    
            if (discounted != null && saleEnd != null) {
                LocalDate endDate = LocalDate.parse(saleEnd);
                if (!endDate.isBefore(today)) {
                    price = discounted;
                }
            }
    
            totalPrice += price * qty;
            productList.append(productName).append(":").append(qty);
            if (i < productIDs.size() - 1) productList.append(", ");
    
            // Reduce inventory
            String updateInventory = String.format(
                "UPDATE Inventory SET quantity = quantity - %d WHERE storeID = %d AND productID = %d",
                qty, storeID, pid
            );
            DBManager.execute(updateInventory);
        }
    
        // Insert into Transaction table
        String insertTransaction = "INSERT INTO Transaction (cstaffID, storeID, customerID, purchaseDate, productList, totalPrice) " +
                                   "VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = DBManager.getConnection().prepareStatement(insertTransaction);
        ps.setInt(1, cashierID);
        ps.setInt(2, storeID);
        ps.setInt(3, customerID);
        ps.setDate(4, Date.valueOf(purchaseDate));
        ps.setString(5, productList.toString());
        ps.setFloat(6, totalPrice);
        ps.executeUpdate();
    
        System.out.println("✅ Transaction recorded successfully.");
        System.out.printf("Total Price: $%.2f\n", totalPrice);
    }
    
}    