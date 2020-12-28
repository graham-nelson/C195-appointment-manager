package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;
import utils.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Implementation of CountryDao for performing CRUD
 * operations on Country objects in database.
 */
public class CountryDaoImpl implements CountryDao {

    /**
     * Method implementation for Read operation.
     * @return ObservableList of all Country objects in database
     * @throws SQLException
     */
    @Override
    public ObservableList<Country> getAllCountries() throws SQLException {

        ObservableList<Country> allCountries = FXCollections.observableArrayList();
        //Select String to get all customers
        String selectStatement = "SELECT * FROM countries";
        //Create and get preparedStatement object
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(selectStatement);

        //Execute prepared statement object
        ps.execute();

        ResultSet rs = ps.getResultSet();

        //Forward Scroll through result set
        while (rs.next()){
            int countryId = rs.getInt("Country_ID");
            String country = rs.getString( "Country" );
            LocalDateTime createDate = rs.getTimestamp("Create_Date").toLocalDateTime();
            String createdBy = rs.getString( "Created_By" );
            LocalDateTime lastUpdate = rs.getTimestamp( "Last_Update" ).toLocalDateTime();
            String lastUpdatedBy = rs.getString( "Last_Updated_By" );

            //Country(int id, String name, String createDate, String createdBy, String lastUpdate, String lastUpdatedBy)
            Country countryResult = new Country( countryId, country, createDate, createdBy,lastUpdate, lastUpdatedBy );
            allCountries.add(countryResult);
        }

        return allCountries;
    }
}
