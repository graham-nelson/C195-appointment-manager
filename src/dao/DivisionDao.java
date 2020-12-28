package dao;

import javafx.collections.ObservableList;
import model.Division;

import java.sql.SQLException;

/**
 * Interface for performing CRUD operations on
 * Division objects in the database.
 */
public interface DivisionDao {

    /**
     * Method declaration for Read operation.
     * @return ObservableList of all Division objects in the database
     * @throws SQLException
     */
    ObservableList<Division> getAllDivisions() throws SQLException;
}
