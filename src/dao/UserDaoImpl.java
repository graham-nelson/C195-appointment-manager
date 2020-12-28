package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;
import utils.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Implementation of UserDao for performing CRUD operations
 * on User objects in the database.
 */
public class UserDaoImpl implements UserDao {

    /**
     * Method implementation for Read operation.
     * @return ObservableList of all User objects in the database
     * @throws SQLException
     */
    @Override
    public ObservableList<User> getAllUsers() throws SQLException {

        ObservableList<User> allUsers = FXCollections.observableArrayList();

        String selectStatement = "SELECT * FROM users";

        PreparedStatement ps = DBConnection.getConnection().prepareStatement(selectStatement);

        ps.execute();

        ResultSet rs = ps.getResultSet();

        while (rs.next()){
            int userId =rs.getInt("User_ID");
            String userName = rs.getString("User_Name");
            String password = rs.getString("Password");
            LocalDateTime createDate = rs.getTimestamp("Create_Date").toLocalDateTime();
            String createdBy = rs.getString("Created_By");
            LocalDateTime lastUpdate = rs.getTimestamp("Last_Update").toLocalDateTime();
            String lastUpdatedBy = rs.getString("Last_Updated_By");

            //User(int id, String name, String password, LocalDateTime createDate, String createdBy, LocalDateTime lastUpdate, String lastUpdatedBy)
            User userResult = new User(userId, userName, password, createDate, createdBy, lastUpdate, lastUpdatedBy);
            allUsers.add(userResult);
        }


        return allUsers;
    }
}
