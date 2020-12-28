package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import utils.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Implementation of AppointmentDao Interface for performing
 * CRUD operations on Appointment object in the database.
 */
public class AppointmentDaoImpl implements AppointmentDao {

    /**
     * Method implementation for Read operation.
     * @return ObservableList of all appointments in database
     * @throws SQLException
     */
    @Override
    public ObservableList<Appointment> getAllAppointments() throws SQLException {

        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();

        String selectStatement = "SELECT * FROM appointments\n" +
                "JOIN contacts \n" +
                "ON appointments.Contact_ID = contacts.Contact_ID;";

        PreparedStatement ps = DBConnection.getConnection().prepareStatement(selectStatement);

        ps.execute();

        ResultSet rs = ps.getResultSet();

        while (rs.next()){
            int aptId = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String contact = rs.getString("Contact_Name");
            String type = rs.getString("Type");
            LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
            LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
            LocalDate createDate = rs.getDate("Create_Date").toLocalDate();
            int customerId = rs.getInt("Customer_ID");
            int userId = rs.getInt("User_ID");
            int contactId = rs.getInt("Contact_ID");


            //Appointment(int id, String title, String description, String location, String type, LocalDateTime start, LocalDateTime end, int customerId, int userId, int contactId, String contactName)
            Appointment appointmentResults = new Appointment(aptId, title, description, location, type, start, end, customerId, userId, contactId, contact);
            allAppointments.add(appointmentResults);

        }
        return allAppointments;
    }

    /**
     * Method implementation for Create operation.
     * @param title appointment title to set
     * @param description appointment description to set
     * @param location appointment location to set
     * @param type appointment type to set
     * @param start appointment start to set
     * @param end appointment end to set
     * @param customerId appointment customer ID to set
     * @param userId appointment user ID to set
     * @param contactId appointment contact ID to set
     * @throws SQLException
     */
    @Override
    public void addAppointment(String title, String description, String location, String type, Timestamp start, Timestamp end, int customerId, int userId, int contactId) throws SQLException {

        String insertStatement = "INSERT INTO appointments(Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID)\n" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";

        PreparedStatement ps = DBConnection.getConnection().prepareStatement(insertStatement);

        ps.setString(1, title);
        ps.setString(2, description);
        ps.setString(3, location);
        ps.setString(4, type);
        ps.setTimestamp(5, start);
        ps.setTimestamp(6, end);
        ps.setInt(7, customerId);
        ps.setInt(8, userId);
        ps.setInt(9, contactId);

        ps.execute();
    }

    /**
     * Method implementation for Delete operation.
     * @param appointmentId ID of desired appointment to delete
     * @throws SQLException
     */
    @Override
    public void deleteAppointment(int appointmentId) throws SQLException {
        String deleteStatement = "DELETE FROM appointments WHERE Appointment_ID = ?";

        PreparedStatement ps = DBConnection.getConnection().prepareStatement(deleteStatement);

        ps.setInt(1, appointmentId);

        ps.execute();
    }

    /**
     * Method implementation for Update operation.
     * @param aptId ID of appointment to update
     * @param title appointment title to update
     * @param description appointment description to update
     * @param location appointment location to update
     * @param type appointment type to update
     * @param start appointment start to update
     * @param end appointment end to update
     * @param customerId appointment customer ID to update
     * @param userId appointment user ID to update
     * @param contactId appointment contact ID to update
     * @throws SQLException
     */
    @Override
    public void updateAppointment(int aptId, String title, String description, String location, String type, Timestamp start, Timestamp end, int customerId, int userId, int contactId) throws SQLException {

        String updateStatement = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";

        PreparedStatement ps = DBConnection.getConnection().prepareStatement(updateStatement);

        ps.setString(1, title);
        ps.setString(2, description);
        ps.setString(3, location);
        ps.setString(4, type);
        ps.setTimestamp(5, start);
        ps.setTimestamp(6, end);
        ps.setInt(7, customerId);
        ps.setInt(8, userId);
        ps.setInt(9, contactId);
        ps.setInt(10, aptId);

        ps.execute();
    }


}
