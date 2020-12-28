package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Division;
import utils.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Implementation of DivisionDao for performing CRUD operations
 * on Division objects in the database.
 */
public class DivisionDaoImpl implements DivisionDao {

    /**
     * Method implementation for Read operation.
     * @return ObservableList of all Division objects in the database
     * @throws SQLException
     */
    @Override
    public ObservableList<Division> getAllDivisions() throws SQLException {
        ObservableList<Division> allDivisions = FXCollections.observableArrayList();
        //Select String to get all customers
        String selectStatement = "SELECT * FROM first_level_divisions";
        //Create and get preparedStatement object
        PreparedStatement ps = DBConnection.getConnection().prepareStatement( selectStatement );

        //Execute prepared statement object
        ps.execute();

        ResultSet rs = ps.getResultSet();

        //Forward Scroll through result set
        while (rs.next()) {
            int divisionId = rs.getInt( "Division_ID" );
            String division = rs.getString( "Division" );
            LocalDateTime createDate = rs.getTimestamp( "Create_Date" ).toLocalDateTime();
            String createdBy = rs.getString( "Created_By" );
            LocalDateTime lastUpdate = rs.getTimestamp( "Last_Update" ).toLocalDateTime();
            String lastUpdatedBy = rs.getString( "Last_Updated_By" );
            int countryId = rs.getInt( "COUNTRY_ID" );

            //Division(int divisionId, String divisionName, String createDate, String createdBy, String lastUpdate, String lastUpdatedBy, int countryId)
            Division divisionResult = new Division( divisionId, division, createDate, createdBy, lastUpdate, lastUpdatedBy, countryId );
            allDivisions.add( divisionResult );
        }

        return allDivisions;
    }

}
