//For Adding the Sample Data to the Tables.

package common;
import java.sql.*;

public class Setup {
    public static void main(String[] args) {
        Connection connection = null;
        Statement statement = null;

        try {
            connection = DBManager.getConnection();
            statement = connection.createStatement();

            DBManager.beginTransaction();

            // Drop tables
            statement.addBatch("DROP TABLE IF EXISTS Bills");
            statement.addBatch("DROP TABLE IF EXISTS Transaction");
            statement.addBatch("DROP TABLE IF EXISTS Inventory");
            statement.addBatch("DROP TABLE IF EXISTS BillingStaff");
            statement.addBatch("DROP TABLE IF EXISTS CashierStaff");
            statement.addBatch("DROP TABLE IF EXISTS WarehouseWorker");
            statement.addBatch("DROP TABLE IF EXISTS RegistrationOfficer");
            statement.addBatch("DROP TABLE IF EXISTS WorksAt");
            statement.addBatch("DROP TABLE IF EXISTS Product");
            statement.addBatch("DROP TABLE IF EXISTS ClubMember");
            statement.addBatch("DROP TABLE IF EXISTS StaffMember");
            statement.addBatch("DROP TABLE IF EXISTS Supplier");
            statement.addBatch("DROP TABLE IF EXISTS Store");

            // Create tables (reusing the structure from your previous script)
            statement.addBatch("CREATE TABLE Store (storeID INT AUTO_INCREMENT PRIMARY KEY, address VARCHAR(255) NOT NULL, phone VARCHAR(15))");
            statement.addBatch("CREATE TABLE StaffMember (staffID INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(100) NOT NULL, age INT NOT NULL, address TEXT, title ENUM('Billing', 'Cashier', 'Registration Officer', 'Warehouse Worker', 'Manager', 'Assistant Manager', 'Other') NOT NULL DEFAULT 'Other', phone VARCHAR(20), emailAddress VARCHAR(100) UNIQUE, employmentTime INT NOT NULL DEFAULT 0)");
            statement.addBatch("CREATE TABLE ClubMember (customerID INT AUTO_INCREMENT PRIMARY KEY, activeStatus ENUM('Active', 'Inactive') NOT NULL DEFAULT 'Active', firstName VARCHAR(50) NOT NULL, lastName VARCHAR(50) NOT NULL, membershipLevel ENUM('Silver', 'Gold', 'Platinum') NOT NULL DEFAULT 'Silver', emailAddress VARCHAR(100) NOT NULL UNIQUE, phone VARCHAR(15), homeAddress VARCHAR(255), startDate DATE NOT NULL, endDate DATE NOT NULL)");
            statement.addBatch("CREATE TABLE Product (productID INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(100) NOT NULL, expiryDate DATE, productionDate DATE)");
            statement.addBatch("CREATE TABLE Supplier (supplierID INT AUTO_INCREMENT PRIMARY KEY, supplierName VARCHAR(100) NOT NULL, phoneNumber VARCHAR(15), emailAddress VARCHAR(100) UNIQUE, location VARCHAR(255))");
            statement.addBatch("CREATE TABLE WorksAt (staffID INT, storeID INT, PRIMARY KEY (staffID, storeID), FOREIGN KEY (staffID) REFERENCES StaffMember(staffID) ON UPDATE CASCADE ON DELETE CASCADE, FOREIGN KEY (storeID) REFERENCES Store(storeID) ON UPDATE CASCADE ON DELETE CASCADE)");
            statement.addBatch("CREATE TABLE RegistrationOfficer (staffID INT PRIMARY KEY, FOREIGN KEY (staffID) REFERENCES StaffMember(staffID) ON UPDATE CASCADE ON DELETE CASCADE)");
            statement.addBatch("CREATE TABLE WarehouseWorker (staffID INT PRIMARY KEY, FOREIGN KEY (staffID) REFERENCES StaffMember(staffID) ON UPDATE CASCADE ON DELETE CASCADE)");
            statement.addBatch("CREATE TABLE BillingStaff (staffID INT PRIMARY KEY, FOREIGN KEY (staffID) REFERENCES StaffMember(staffID) ON UPDATE CASCADE ON DELETE CASCADE)");
            statement.addBatch("CREATE TABLE CashierStaff (staffID INT PRIMARY KEY, FOREIGN KEY (staffID) REFERENCES StaffMember(staffID) ON UPDATE CASCADE ON DELETE CASCADE)");
            statement.addBatch("CREATE TABLE Transaction (transactionID INT AUTO_INCREMENT PRIMARY KEY, cstaffID INT, storeID INT, customerID INT, purchaseDate DATE, productList TEXT, totalPrice DECIMAL(10, 2) NOT NULL, FOREIGN KEY (cstaffID) REFERENCES CashierStaff(staffID) ON UPDATE CASCADE ON DELETE SET NULL, FOREIGN KEY (storeID) REFERENCES Store(storeID) ON UPDATE CASCADE ON DELETE SET NULL, FOREIGN KEY (customerID) REFERENCES ClubMember(customerID) ON UPDATE CASCADE ON DELETE SET NULL)");
            statement.addBatch("CREATE TABLE Bills (billId INT AUTO_INCREMENT PRIMARY KEY, bstaffid INT, supplierId INT, billDate DATE NOT NULL, billAmount DECIMAL(10, 2) NOT NULL, FOREIGN KEY (supplierId) REFERENCES Supplier(supplierID) ON UPDATE CASCADE ON DELETE SET NULL, FOREIGN KEY (bstaffId) REFERENCES BillingStaff(staffID) ON UPDATE CASCADE ON DELETE SET NULL)");
            statement.addBatch("CREATE TABLE Inventory (storeID INT, productID INT, quantity INT NOT NULL, buyPrice DECIMAL(10, 2) NOT NULL, sellPrice DECIMAL(10, 2) NOT NULL, discountedPrice DECIMAL(10, 2), saleEndDate DATE, PRIMARY KEY (storeID, productID), FOREIGN KEY (storeID) REFERENCES Store(storeID) ON UPDATE CASCADE ON DELETE CASCADE, FOREIGN KEY (productID) REFERENCES Product(productID) ON UPDATE CASCADE ON DELETE CASCADE)");

            statement.executeBatch();

            // Insert store records
            statement.executeUpdate("INSERT INTO Store (address, phone) VALUES ('1021 Main Campus Dr, Raleigh, NC, 27606', '9194789124')");
            statement.executeUpdate("INSERT INTO Store (address, phone) VALUES ('851 Partners Way, Raleigh, NC, 27606', '9195929621')");

            // Insert staff records
            statement.executeUpdate("INSERT INTO StaffMember (name, age, address, title, phone, emailAddress, employmentTime) VALUES ('Alice Johnson', 34, '111 Wolf Street, Raleigh, NC 27606', 'Manager', '9194285357', 'alice.johson@gmail.com',5)");
            statement.executeUpdate("INSERT INTO StaffMember (name, age, address, title, phone, emailAddress, employmentTime) VALUES ('Bob Smith', 29, '222 Fox Ave, Durham, NC 27701', 'Assistant Manager', '9841482375', 'bob.smith@hotmail.com',3)");
            statement.executeUpdate("INSERT INTO StaffMember (name, age, address, title, phone, emailAddress, employmentTime) VALUES ('Charlie Davis', 40, '333 Bear Rd, Greensboro, NC 27282', 'Cashier', '9194856193', 'charlie.davis@gmail.com',7)");
            statement.executeUpdate("INSERT INTO StaffMember (name, age, address, title, phone, emailAddress, employmentTime) VALUES ('David Lee', 45, '444 Eagle Dr, Raleigh, NC 27606', 'Warehouse Worker', '9847028471', 'david.lee@yahoo.com',10)");
            statement.executeUpdate("INSERT INTO StaffMember (name, age, address, title, phone, emailAddress, employmentTime) VALUES ('Emma White', 30, '555 Deer Ln, Durham, NC 27560', 'Billing', '9198247184', 'emma.white@gmail.com',4)");
            statement.executeUpdate("INSERT INTO StaffMember (name, age, address, title, phone, emailAddress, employmentTime) VALUES ('Frank Harris', 38, '666 Owl Ct, Raleigh, NC 27610', 'Billing', '919428535', 'frank.harris@gmail.com',6)");
            statement.executeUpdate("INSERT INTO StaffMember (name, age, address, title, phone, emailAddress, employmentTime) VALUES ('Isla Scott', 33, '777 Lynx Rd, Raleigh, NC 27612', 'Warehouse Worker', '9841298427', 'isla.scott@gmail.com',2)");
            statement.executeUpdate("INSERT INTO StaffMember (name, age, address, title, phone, emailAddress, employmentTime) VALUES ('Jack Lewis', 41, '888 Falcon St, Greensboro, NC 27377', 'Cashier', '9194183951', 'jack.lewis@gmail.com',3)");

            // Assign staff to stores
            for (int i = 1; i <= 8; i++) {
                statement.executeUpdate("INSERT INTO WorksAt (staffID, storeID) VALUES (" + i + ", " + (i % 2 == 1 ? 1 : 2) + ")");
            }

            // Assign roles
            statement.executeUpdate("INSERT INTO RegistrationOfficer (staffID) VALUES (1)");
            statement.executeUpdate("INSERT INTO RegistrationOfficer (staffID) VALUES (2)");
            statement.executeUpdate("INSERT INTO CashierStaff (staffID) VALUES (3)");
            statement.executeUpdate("INSERT INTO WarehouseWorker (staffID) VALUES (4)");
            statement.executeUpdate("INSERT INTO BillingStaff (staffID) VALUES (5)");
            statement.executeUpdate("INSERT INTO BillingStaff (staffID) VALUES (6)");
            statement.executeUpdate("INSERT INTO WarehouseWorker (staffID) VALUES (7)");
            statement.executeUpdate("INSERT INTO CashierStaff (staffID) VALUES (8)");

            // Add club members
            statement.executeUpdate("INSERT INTO ClubMember (activeStatus, firstName, lastName, membershipLevel, emailAddress, phone, homeAddress, startDate, endDate) VALUES " +
               "('Active', 'John', 'Doe', 'Gold', 'john.doe@gmail.com', '9194285314', '12 Elm St, Raleigh, NC 27607', '2024-01-31', '2025-01-31')");
           statement.executeUpdate("INSERT INTO ClubMember (activeStatus, firstName, lastName, membershipLevel, emailAddress, phone, homeAddress, startDate, endDate) VALUES " +
               "('Inactive', 'Emily', 'Smith', 'Silver', 'emily.smith@gmail.com', '9844235314', '34 Oak Ave, Raleigh, NC 27606', '2022-02-28', '2023-02-28')");
           statement.executeUpdate("INSERT INTO ClubMember (activeStatus, firstName, lastName, membershipLevel, emailAddress, phone, homeAddress, startDate, endDate) VALUES " +
               "('Active', 'Michael', 'Brown', 'Platinum', 'michael.brown@gmail.com', '9194820931', '56 Pine Rd, Raleigh, NC 27607', '2020-03-22', '2025-03-22')");
           statement.executeUpdate("INSERT INTO ClubMember (activeStatus, firstName, lastName, membershipLevel, emailAddress, phone, homeAddress, startDate, endDate) VALUES " +
               "('Active', 'Sarah', 'Johnson', 'Gold', 'sarah.johnson@gmail.com', '9841298435', '78 Maple Dr, Raleigh, NC 27607', '2023-03-15', '2024-03-15')");
           statement.executeUpdate("INSERT INTO ClubMember (activeStatus, firstName, lastName, membershipLevel, emailAddress, phone, homeAddress, startDate, endDate) VALUES " +
               "('Inactive', 'David', 'Williams', 'Silver', 'david.williams@gmail.com', '9194829424', '90 Birch Ln, Raleigh, NC 27607', '2024-08-23', '2025-08-23')");
           statement.executeUpdate("INSERT INTO ClubMember (activeStatus, firstName, lastName, membershipLevel, emailAddress, phone, homeAddress, startDate, endDate) VALUES " +
               "('Active', 'Anna', 'Miller', 'Platinum', 'anna.miller@gmail.com', '9848519427', '101 Oak Ct, Raleigh, NC 27607', '2025-02-10', '2026-02-10')");
          
           // Insert products
           statement.executeUpdate("INSERT INTO Product (name, expiryDate, productionDate) VALUES " +
               "('Organic Apples', '2025-04-20', '2025-04-12')");
           statement.executeUpdate("INSERT INTO Product (name, expiryDate, productionDate) VALUES " +
               "('Whole Grain Bread', '2025-04-15', '2025-04-10')");
           statement.executeUpdate("INSERT INTO Product (name, expiryDate, productionDate) VALUES " +
               "('Almond Milk', '2025-04-30', '2025-04-15')");
           statement.executeUpdate("INSERT INTO Product (name, expiryDate, productionDate) VALUES " +
               "('Brown Rice', '2026-04-20', '2025-04-12')");
           statement.executeUpdate("INSERT INTO Product (name, expiryDate, productionDate) VALUES " +
               "('Olive Oil', '2027-04-20', '2025-04-04')");
           statement.executeUpdate("INSERT INTO Product (name, expiryDate, productionDate) VALUES " +
               "('Whole Chicken', '2025-05-12', '2025-04-12')");
           statement.executeUpdate("INSERT INTO Product (name, expiryDate, productionDate) VALUES " +
               "('Cheddar Cheese', '2025-10-12', '2025-04-12')");
           statement.executeUpdate("INSERT INTO Product (name, expiryDate, productionDate) VALUES " +
               "('Dark Chocolate', '2026-06-20', '2025-04-12')");
          
           // Insert suppliers
           statement.executeUpdate("INSERT INTO Supplier (supplierName, phoneNumber, emailAddress, location) VALUES " +
               "('Fresh Farms Ltd.', '9194248251', 'contact@freshfarms.com', '123 Greenway Blvd, Raleigh, NC 27615')");
           statement.executeUpdate("INSERT INTO Supplier (supplierName, phoneNumber, emailAddress, location) VALUES " +
               "('Organic Good Inc.', '9841384298', 'info@orgaincgoods.com', '456 Healthy Rd, Raleigh, NC 27606')");
          
           // Insert inventory
           statement.executeUpdate("INSERT INTO Inventory (storeID, productID, quantity, buyPrice, sellPrice) VALUES " +
               "(2, 1, 120, 1.5, 2.0)");
           statement.executeUpdate("INSERT INTO Inventory (storeID, productID, quantity, buyPrice, sellPrice) VALUES " +
               "(2, 2, 80, 2.0, 3.5)");
           statement.executeUpdate("INSERT INTO Inventory (storeID, productID, quantity, buyPrice, sellPrice) VALUES " +
               "(2, 3, 150, 3.5, 4.0)");
           statement.executeUpdate("INSERT INTO Inventory (storeID, productID, quantity, buyPrice, sellPrice) VALUES " +
               "(2, 4, 200, 2.8, 3.5)");
           statement.executeUpdate("INSERT INTO Inventory (storeID, productID, quantity, buyPrice, sellPrice) VALUES " +
               "(2, 5, 90, 5.0, 7.0)");
           statement.executeUpdate("INSERT INTO Inventory (storeID, productID, quantity, buyPrice, sellPrice, discountedPrice, saleEndDate) VALUES " +
               "(2, 6, 75, 10.0, 13.0, 11.7, '2024-05-10')");
           statement.executeUpdate("INSERT INTO Inventory (storeID, productID, quantity, buyPrice, sellPrice) VALUES " +
               "(2, 7, 60, 3.0, 4.2)");
           statement.executeUpdate("INSERT INTO Inventory (storeID, productID, quantity, buyPrice, sellPrice) VALUES " +
               "(2, 8, 50, 2.5, 3.5)");
          
           // Insert transactions
           statement.executeUpdate("INSERT INTO Transaction (cstaffID, storeID, customerID, purchaseDate, productList, totalPrice) VALUES " +
               "(3, 2, 2, '2024-02-10', 'Organic Apples, Whole Grain Bread', 45.0)");
           statement.executeUpdate("INSERT INTO Transaction (cstaffID, storeID, customerID, purchaseDate, productList, totalPrice) VALUES " +
               "(8, 2, 2, '2024-09-12', 'Almond Milk, Brown Rice, Olive Oil', 60.75)");
           statement.executeUpdate("INSERT INTO Transaction (cstaffID, storeID, customerID, purchaseDate, productList, totalPrice) VALUES " +
               "(8, 2, 2, '2024-09-23', 'Dark Chocolate, Olive Oil, Almond Milk', 78.9)");
           statement.executeUpdate("INSERT INTO Transaction (cstaffID, storeID, customerID, purchaseDate, productList, totalPrice) VALUES " +
               "(8, 2, 4, '2024-07-23', 'Whole Chicken', 32.5)");
          
           // Insert bills
           statement.executeUpdate("INSERT INTO Bills (bstaffid, supplierId, billDate, billAmount) VALUES " +
               "(5, 1, '2025-04-01', 1500.00)");
           statement.executeUpdate("INSERT INTO Bills (bstaffid, supplierId, billDate, billAmount) VALUES " +
               "(6, 2, '2025-04-05', 2300.50)");

            DBManager.commitTransaction();
            System.out.println("Database setup completed successfully!");

        } catch (Exception e) {
            System.err.println("Error during database setup: " + e.getMessage());
            DBManager.rollbackTransaction();
            e.printStackTrace();
        } finally {
            DBManager.close();
        }
    }
}
