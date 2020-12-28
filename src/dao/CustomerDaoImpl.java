package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;
import utils.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Implementation of CustomerDao for performing CRUD operations on
 * Customer objects in the database.
 */
public class CustomerDaoImpl implements CustomerDao {


    /**
     * Method implementation for Read operation.
     * @return ObservableList of all customer objects in database
     * @throws SQLException
     */
    @Override
    public ObservableList<Customer> getAllCustomers() throws SQLException {

        ObservableList<Customer> allCustomers = FXCollections.observableArrayList();

            //Select String to get all customers
            String selectStatement = "SELECT *\n" +
                    "FROM customers\n" +
                    "JOIN first_level_divisions\n" +
                    "\tON customers.Division_ID = first_level_divisions.Division_ID";
            //Create and get preparedStatement object
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(selectStatement);

            //Execute prepared statement object
            ps.execute();

            //Get ResultSet from query and store in variable rs
            ResultSet rs = ps.getResultSet();

            //Forward Scroll through result set
            while (rs.next()){
                int customerId = rs.getInt("Customer_ID");
                String customerName = rs.getString( "Customer_Name" );
                String address = rs.getString( "Address" );
                String postalCode = rs.getString( "Postal_Code" );
                String phone = rs.getString( "Phone" );
                int divisionId = rs.getInt( "Division_ID" );
                String divisionName = rs.getString("Division");

                //Customer(int id, String name, String address, String postalCode, String phone, String createDate, String createdBy, String lastUpdate, String lastUpdatedBy, int divisionId)
                Customer customerResult = new Customer( customerId, customerName, address, postalCode, phone, divisionId, divisionName );
                allCustomers.add( customerResult );
            }

        return allCustomers;
    }

    /**
     * Method declaration for Update operation.
     * @param customerName Customer name to set
     * @param customerAddress Customer address to set
     * @param customerPostalCode Customer postal code to set
     * @param customerPhone Customer phone number to set
     * @param divisionID ID of division Customer belongs too to set
     * @throws SQLException
     */
    @Override
    public void addCustomer(String customerName, String customerAddress, String customerPostalCode, String customerPhone, int divisionID) throws SQLException {

        String insertString = ("INSERT INTO customers(Customer_Name, Address, Postal_Code, Phone, Created_By, Last_Updated_By, Division_ID) VALUES(?, ?, ?, ?, ?, ?, ?)");

        PreparedStatement ps = DBConnection.getConnection().prepareStatement(insertString);

        ps.setString(1, customerName);
        ps.setString(2, customerAddress);
        ps.setString(3, customerPostalCode);
        ps.setString(4, customerPhone);
        ps.setString(5, "admin");
        ps.setString(6, "admin");
        ps.setInt(7, divisionID);

        ps.execute();
    }

    /**
     * Method implementation for Update operation.
     * @param customerID ID of customer to update
     * @param customerName Customer name to update
     * @param customerAddress Customer address to update
     * @param customerPostalCode Customer postal code to update
     * @param customerPhone Customer phone number to update
     * @param divisionID ID of division Customer belongs too to update
     * @throws SQLException
     */
    @Override
    public void updateCustomer(int customerID, String customerName, String customerAddress, String customerPostalCode, String customerPhone, int divisionID) throws SQLException {

        String updateStatement = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ? WHERE Customer_ID = ?";

        PreparedStatement ps = DBConnection.getConnection().prepareStatement(updateStatement);

        ps.setString(1, customerName);
        ps.setString(2, customerAddress);
        ps.setString(3, customerPostalCode);
        ps.setString(4, customerPhone);
        ps.setInt(5, divisionID);
        ps.setInt(6, customerID);

        ps.execute();
    }

    /**
     * Method implementation for Delete operation. here is a method in the source
     * code that will delete all appointments before deleting customer due to foreign
     * key constraints in the database.
     * @param customerId ID of customer to delete.
     * @throws SQLException
     */
    @Override
    public void deleteCustomer(int customerId) throws SQLException {
        String deleteStatement = "DELETE FROM customers WHERE Customer_ID = ?";

        PreparedStatement ps = DBConnection.getConnection().prepareStatement(deleteStatement);

        ps.setInt(1, customerId);

        ps.execute();
    }


}
