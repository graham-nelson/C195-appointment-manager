package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contact;
import utils.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Implementation of ContactDao for performing CRUD
 * operations on Contact objects in the database.
 */
public class ContactDaoImpl implements ContactDao {

    /**
     * Method implementation for Read operation.
     * @return ObservableList of all Contacts in database
     * @throws SQLException
     */
    @Override
    public ObservableList<Contact> getAllContacts() throws SQLException {

        ObservableList<Contact> allContacts = FXCollections.observableArrayList();

        String selectStatement = "SELECT * FROM contacts";

        PreparedStatement ps = DBConnection.getConnection().prepareStatement(selectStatement);

        ps.execute();

        ResultSet rs = ps.getResultSet();

        while (rs.next()) {
            int id = rs.getInt("Contact_ID");
            String name = rs.getString("Contact_Name");
            String email = rs.getString("Email");

            //Contact(int id, String name, String email)
            Contact contactResult = new Contact(id, name, email);
            allContacts.add(contactResult);
        }


        return allContacts;
    }
}
