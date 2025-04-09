package rewards;

import common.DBManager;
import common.Input;

import java.sql.*;
public class GenerateRewards {
    public static void main(String[] args) {
        try {
            DBManager.beginTransaction();

            String query = "SELECT cm.firstName, cm.lastName, cm.emailAddress, " +
                           "SUM(t.totalPrice) AS totalSpent, (SUM(t.totalPrice) * 0.02) AS rewardAmount " +
                           "FROM Transaction t JOIN ClubMember cm ON t.customerID = cm.customerID " +
                           "WHERE cm.membershipLevel = 'Platinum' AND YEAR(t.purchaseDate) = YEAR(CURRENT_DATE) " +
                           "GROUP BY cm.customerID";

            ResultSet rs = DBManager.query(query);
            System.out.println("\nRewards for Platinum Customers:");
            System.out.println("----------------------------------");
            while (rs.next()) {
                String name = rs.getString("firstName") + " " + rs.getString("lastName");
                String email = rs.getString("emailAddress");
                float totalSpent = rs.getFloat("totalSpent");
                float reward = rs.getFloat("rewardAmount");
                System.out.printf("%s (%s): Spent $%.2f → Reward $%.2f\n", name, email, totalSpent, reward);
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