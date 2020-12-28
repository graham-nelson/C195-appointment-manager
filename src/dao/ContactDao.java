package dao;

import javafx.collections.ObservableList;
import model.Contact;

import java.sql.SQLException;

/**
 * Interface for CRUD operations on Contact objects within database.
 */
public interface ContactDao {

    /**
     * Method deceleration for Read operation.
     * @return ObservableList of all Contacts in database
     * @throws SQLException
     */
    ObservableList<Contact> getAllContacts() throws SQLException;
}
