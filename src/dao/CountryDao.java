package dao;

import javafx.collections.ObservableList;
import model.Country;
import model.Division;

import java.sql.SQLException;

/**
 * Interface for performing CRUD operations
 * on Country objects in database.
 */
public interface CountryDao {

    /**
     * Method declaration for Read operation.
     * @return ObservableList of all Country objects in database
     * @throws SQLException
     */
    ObservableList<Country> getAllCountries() throws SQLException;
}
