package dao;

import javafx.collections.ObservableList;
import model.Appointment;

import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Interface for CRUD operations on Appointment objects within database.
 */
public interface AppointmentDao {

    /**
     * Method declaration for Read operation.
     * @return ObservableList of all appointments in database
     * @throws SQLException
     */
    ObservableList<Appointment> getAllAppointments() throws SQLException;

    /**
     * Method declaration for Create operation.
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
    void addAppointment(String title, String description, String location, String type, Timestamp start, Timestamp end, int customerId, int userId, int contactId) throws SQLException;

    /**
     * Method declaration for Delete operation.
     * @param appointmentId ID of desired appointment to delete
     * @throws SQLException
     */
    void deleteAppointment(int appointmentId) throws SQLException;

    /**
     * Method declaration for Update operation.
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
    void updateAppointment(int aptId, String title, String description, String location, String type, Timestamp start, Timestamp end, int customerId, int userId, int contactId) throws SQLException;
}
