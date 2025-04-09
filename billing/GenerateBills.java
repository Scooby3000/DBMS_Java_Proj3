package billing;

import common.DBManager;
import common.Input;
import java.sql.*;
public class GenerateBills {
    public static void main(String[] args) {
        try {
            DBManager.beginTransaction();

            String query = "SELECT SUM(billAmount) AS TOTAL_DUE, supplierName " +
                           "FROM Bills NATURAL JOIN Supplier GROUP BY supplierName";

            ResultSet rs = DBManager.query(query);
            System.out.println("\nTotal Due Per Supplier:");
            System.out.println("------------------------");
            while (rs.next()) {
                String supplierName = rs.getString("supplierName");
                float totalDue = rs.getFloat("TOTAL_DUE");
                System.out.printf("%s: $%.2f\n", supplierName, totalDue);
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