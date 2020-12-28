package dao;

import javafx.collections.ObservableList;
import model.Customer;

import java.sql.SQLException;

/**
 * Interface for performing CRUD operations on
 * Customer objects in the database.
 */
public interface CustomerDao {

    /**
     * Method declaration for Read operation.
     * @return ObservableList of all customer objects in database
     * @throws SQLException
     */
    ObservableList<Customer> getAllCustomers() throws SQLException;

    /**
     * Method declaration for Create operation.
     * @param customerName Customer name to set
     * @param customerAddress Customer address to set
     * @param customerPostalCode Customer postal code to set
     * @param customerPhone Customer phone number to set
     * @param divisionID ID of division Customer belongs too to set
     * @throws SQLException
     */
    void addCustomer(String customerName, String customerAddress, String customerPostalCode, String customerPhone, int divisionID) throws SQLException;

    /**
     * Method declaration for Update operation.
     * @param customerID ID of customer to update
     * @param customerName Customer name to update
     * @param customerAddress Customer address to update
     * @param customerPostalCode Customer postal code to update
     * @param customerPhone Customer phone number to update
     * @param divisionID ID of division Customer belongs too to update
     * @throws SQLException
     */
    void updateCustomer(int customerID, String customerName, String customerAddress, String customerPostalCode, String customerPhone, int divisionID) throws SQLException;

    /**
     * Method declaration for Delete operation. There is a method in the source
     * code that will delete all appointments before deleting customer due to foreign
     * key constraints in the database.
     * @param customerId ID of customer to delete.
     * @throws SQLException
     */
    void deleteCustomer(int customerId) throws SQLException;
}
