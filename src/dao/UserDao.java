package dao;

import javafx.collections.ObservableList;
import model.User;

import java.sql.SQLException;

/**
 * Interface for performing CRUD operations
 * on User objects in the database.
 */
public interface UserDao {

    /**
     * Method declaration for Read operation.
     * @return ObservableList of all User objects in the database
     * @throws SQLException
     */
    ObservableList<User> getAllUsers() throws SQLException;
}
