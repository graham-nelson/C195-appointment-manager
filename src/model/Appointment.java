package model;

import dao.AppointmentDao;
import dao.AppointmentDaoImpl;
import dao.CustomerDao;
import dao.CustomerDaoImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Enumeration;
import java.util.TimeZone;

/**
 * Appointment is the entity used for modeling
 * appointment objects from the database
 */
public class Appointment {

    private int id;
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDateTime start;
    private LocalDateTime end;
    private LocalDateTime createDate;
    private String createdBy;
    private LocalDateTime lastUpdate;
    private String lastUpdatedBy;
    private int customerId;
    private int userId;
    private int contactId;
    private String contactName;

    /**
     * Constructor for Appointment. Creates new appointment.
     * @param id sets appointment id with an int value
     * @param title sets appointment title with a String value
     * @param description sets appointment description with a String value
     * @param location sets appointment location with a String value
     * @param type sets appointment type with a String value
     * @param start sets appointment start with a LocalDateTime object
     * @param end sets appointment end with a LocalDateTime object
     * @param customerId sets appointment customer ID with an int value
     * @param userId sets appointment user ID with an int value
     * @param contactId sets appointment contact ID with an int value
     * @param contactName sets appointment contact name with a String value
     */
    public Appointment(int id, String title, String description, String location, String type, LocalDateTime start, LocalDateTime end, int customerId, int userId, int contactId, String contactName) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.createDate = LocalDateTime.now();
        this.createdBy = "admin";
        this.lastUpdate = LocalDateTime.now();
        this.lastUpdatedBy = "admin";
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;
        this.contactName = contactName;
    }

    /**
     * @return current appointment ID as int
     */
    public int getId() {
        return id;
    }

    /**
     * @param id id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return current appointment title as String
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return current appointment description as String
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return current appointment location as String
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return current appointment type as String
     */
    public String getType() {
        return type;
    }

    /**
     * @param type type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return appointment start converted into system default time zone
     */
    //gets the appointment start time and converts it to system default time
    public LocalDateTime getStart() {

        ZonedDateTime zonedStart = ZonedDateTime.of(start, ZoneId.of(TimeZone.getDefault().getID()));

        return zonedStart.toLocalDateTime();
    }

    /**
     * @param start start to set
     */
    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    /**
     * @return appointment end converted into system default time zone
     */
    public LocalDateTime getEnd() {
        ZonedDateTime zonedEnd = ZonedDateTime.of(end, ZoneId.of(TimeZone.getDefault().getID()));

        return zonedEnd.toLocalDateTime();
    }

    /**
     * @param end end to set
     */
    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    /**
     * @return current appointment created date as LocalDateTime
     */
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate createDate to set
     */
    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    /**
     * @return current appointment CreatedBy as String
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy createdBy to set
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return current appointment LastUpdate as LocalDateTime
     */
    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    /**
     * @param lastUpdate lastUpdate to set
     */
    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /**
     * @return current appointment LastUpdatedBy as String
     */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /**
     * @param lastUpdatedBy lastUpdatedBy to set
     */
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * @return current appointment CustomerID as int
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId customerId to set
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * @return current appointment UserID as int
     */
    public int getUserId() {
        return userId;
    }

    /**
     * @param userId userID to set
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * @return current appointment contactID as int
     */
    public int getContactId() {
        return contactId;
    }

    /**
     * @param contactId contactId to set
     */
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    /**
     * @return current appointment contact name as String
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * @param contactName contactName to set
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     * Gets the current appointment start date/time
     * converts it to system default and formats it for display
     * @return formatted and zoned appointment start date/time
     */
    public String getFormattedStart() {

        ZonedDateTime zonedStart = ZonedDateTime.of(start, ZoneId.of(TimeZone.getDefault().getID()));

        LocalDateTime localStart = zonedStart.toLocalDateTime();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm - MM/dd/yyyy");

        return dtf.format(localStart);
    }

    /**
     * Gets the current appointment end date/time
     * converts it to system default and formats it for display
     * @return formatted and zoned appointment end date/time
     */
    public String getFormattedEnd() {

        ZonedDateTime zonedEnd = ZonedDateTime.of(end, ZoneId.of(TimeZone.getDefault().getID()));

        LocalDateTime localEnd = zonedEnd.toLocalDateTime();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm - MM/dd/yyyy");

        return dtf.format(localEnd);
    }

    /**
     * Takes Customer ID, start and end date/times and tests if that customer has any conflicting
     * appointments between start and end.
     *
     * @param id Customer ID for retrieving all appointments for that customer
     * @param start date/time that is to be tested if there is a conflict or not
     * @param end date/time that is to be tested if there is a conflict or not
     * @return true if conflict, false if no conflict
     * @throws SQLException
     */
    public static Appointment isScheduleConflict(int id, LocalDateTime start, LocalDateTime end) throws SQLException {
        Appointment conflictingApt = null;

        ObservableList<Appointment> allCustomerApts = Customer.getAllCustomerApts(id);

        for (Appointment a : allCustomerApts) {
            if (start.isAfter(a.getStart()) && start.isBefore(a.getEnd()) ||
                    end.isAfter(a.getStart()) && end.isBefore(a.getEnd()) ||
                    start.isBefore(a.getStart()) && end.isAfter(a.getEnd()) ||
                    start.equals(a.getStart()) && end.equals(a.getEnd()) ||
                    start.equals(a.getStart()) || end.equals(a.getEnd())) {
                conflictingApt = a;
            }
        }
        return conflictingApt;
    }

    /**
     * Takes Contact ID and returns all the appointments for that contact.
     *
     * @param contactId the ID of the contact
     * @return ObservableList of all appointments of a specific contact
     * @throws SQLException
     */
    public static ObservableList<Appointment> getAllContactApts(int contactId) throws SQLException {
        AppointmentDao appointmentDao = new AppointmentDaoImpl();
        ObservableList<Appointment> allApts = appointmentDao.getAllAppointments();
        ObservableList<Appointment> contactApts = FXCollections.observableArrayList();

        for (Appointment a : allApts) {
            if (a.getContactId() == contactId) {
                contactApts.add(a);
            }
        }
        return contactApts;
    }


    /**
     * Populates an ObservableList of String with all possible Type options
     * used to populate a combobox in the application.
     *
     * @return ObservableList of all possible Types
     */
    public static ObservableList<String> getTypes(){
        ObservableList<String> types = FXCollections.observableArrayList();

        types.add("Business");
        types.add("Personal");
        types.add("Sales");
        types.add("Accounting");
        types.add("Miscellaneous");

        return types;
    }

    /**
     * Takes type and populates an ObservableList of all appointments of that Type.
     *
     * Lambda expression used here for the purpose of increased readability
     * along with one less ObservableList used than a previous implementation with
     * a for loop iterating through all appointments and populating a second list if the type's matched
     *
     * @param type String value type of an appointment object
     * @return ObservableList of all appointments of specified type
     * @throws SQLException
     */
    public static ObservableList<Appointment> appointmentByType(String type) throws SQLException {
        AppointmentDao appointmentDao = new AppointmentDaoImpl();

        ObservableList<Appointment> appointmentsOfType = appointmentDao.getAllAppointments().filtered(appointment -> {
            if (appointment.getType().equals(type))
                return true;
            return false;
        });
        return appointmentsOfType;
    }

    /**
     * Takes integer month of the year and returns all appointments scheduled in that month.
     *
     * @param month integer month of year to be compared to appointments
     * @return ObservableList of appointments within specified month
     * @throws SQLException
     */
    public static ObservableList<Appointment> appointmentByMonth(int month) throws SQLException {
        AppointmentDao appointmentDao = new AppointmentDaoImpl();
        ObservableList<Appointment> allAppointments = appointmentDao.getAllAppointments();
        ObservableList<Appointment> appointmentsOfMonth = FXCollections.observableArrayList();

        for (Appointment a : allAppointments){
            LocalDateTime start = a.getStart();
            LocalDate date = start.toLocalDate();

            if (date.getMonth().getValue() == month){
                appointmentsOfMonth.add(a);
            }
        }
        return appointmentsOfMonth;
    }

}
