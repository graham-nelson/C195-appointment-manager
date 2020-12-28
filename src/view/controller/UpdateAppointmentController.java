package view.controller;

import dao.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Appointment;
import model.Contact;
import model.Customer;
import model.User;
import utils.TimeUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDate;

/**
 * Controller for Update Appointment screen. Contains methods for updating appointments,
 * handling button clicks and performing input validation.
 */
public class UpdateAppointmentController {

    AppointmentDao appointmentDao = new AppointmentDaoImpl();
    CustomerDao customerDao = new CustomerDaoImpl();
    UserDao userDao = new UserDaoImpl();
    ContactDao contactDao = new ContactDaoImpl();

    Stage stage;
    Parent scene;

    @FXML
    private TextField aptIDField;

    @FXML
    private TextField titleField;

    @FXML
    private TextField descriptionField;

    @FXML
    private TextField locationField;

    @FXML
    private ComboBox<Contact> contactField;

    @FXML
    private ComboBox<String> typeField;

    @FXML
    private DatePicker dateField;

    @FXML
    private ComboBox<LocalTime> startTimeField;

    @FXML
    private ComboBox<LocalTime> endTimeField;

    @FXML
    private ComboBox<Integer> customerIDField;

    @FXML
    private ComboBox<User> userField;

    @FXML
    private Button updateAppointmentButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Button appointmentsButton;

    /**
     * Clears all changes and returns user to Appointment Overview screen.
     * @param actionEvent Button click
     * @throws IOException
     */
    @FXML
    public void onActionCancel(ActionEvent actionEvent) throws IOException {

        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/controller/AptOverview.fxml"));
        stage.setScene(new Scene(scene));
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Clears all changes and returns user to Appointment Overview screen.
     * @param actionEvent Button click
     * @throws IOException
     */
    @FXML
    public void onActionAppointments(ActionEvent actionEvent) throws IOException {

        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/controller/AptOverview.fxml"));
        stage.setScene(new Scene(scene));
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Taking modified values from user and updating the appointment object.
     * @param actionEvent Button click
     * @throws IOException
     * @throws SQLException
     */
    @FXML
    public void onActionUpdateAppointment(ActionEvent actionEvent) throws IOException, SQLException {
        //If any fields are empty, displays error message
        if (inputsEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Add Appointment Error");
            alert.setHeaderText("One or more fields are empty.");
            alert.setContentText("Please complete all fields to add appointment.");
            alert.showAndWait();
        }
         else {
            //Taking selected date and time, converting to LocalDateTime and then converting to Timestamp for database entry
            LocalDate date = dateField.getValue();
            LocalTime startTime = startTimeField.getValue();
            LocalTime endTime = endTimeField.getValue();
            LocalDateTime start = LocalDateTime.of(date, startTime);
            LocalDateTime end = LocalDateTime.of(date, endTime);
            Timestamp startTimestamp = Timestamp.valueOf(start);
            Timestamp endTimestamp = Timestamp.valueOf(end);
            Appointment conflictingAppointment = Appointment.isScheduleConflict(customerIDField.getSelectionModel().getSelectedItem(), start, end);

            //If selected customer has a conflicting appointment, displays error message
            if (conflictingAppointment != null && conflictingAppointment.getId() != Integer.parseInt(aptIDField.getText())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Scheduling Conflict");
                alert.setHeaderText("This Customer already has an appointment at the selected time. Please select a time outside of the times below.");
                alert.setContentText("Appointment ID: " + conflictingAppointment.getId() + "\n" +
                        "Date: " + conflictingAppointment.getStart().toLocalDate() + "\n" +
                        "Start: " + conflictingAppointment.getStart().toLocalTime() + "\n" +
                        "End: " + conflictingAppointment.getEnd().toLocalTime());
                alert.showAndWait();
                //If the conflicting appointment ends at the end of business hours, clear the selection time.
                //Else set the start time field to the end time of the conflicting appointment
                if (conflictingAppointment.getEnd().equals(TimeUtils.getLocalEndTime())){
                    startTimeField.getSelectionModel().clearSelection();
                }
                else {
                    startTimeField.setValue(conflictingAppointment.getEnd().toLocalTime());
                }
            }
            //If selected Date/Time are before the current Date/Time, displays an error
            else if (start.isBefore(LocalDateTime.now()) || end.isBefore(LocalDateTime.now())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Time Error");
                alert.setHeaderText("Selected time is before current time.");
                alert.setContentText("Please select an appointment time and date in the future.");
                alert.showAndWait();
            }
            //No validation issues, proceed with updating customer
            else {
                //Selected values and updating appointment object with those values
                int id = Integer.parseInt(aptIDField.getText());
                String title = titleField.getText();
                String description = descriptionField.getText();
                String location = locationField.getText();
                int contactId = contactField.getSelectionModel().getSelectedItem().getId();
                String type = typeField.getValue();
                int customerId = customerIDField.getValue();
                int userId = userField.getValue().getId();

                //updateAppointment(int aptId, String title, String description, String location, String type, Timestamp start, Timestamp end, int customerId, int userId, int contactId)
                appointmentDao.updateAppointment(id, title, description, location, type, startTimestamp, endTimestamp, customerId, userId, contactId);

                stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/controller/AptOverview.fxml"));
                stage.setScene(new Scene(scene));
                stage.setResizable(false);
                stage.show();
            }
        }
    }

    /**
     * Presets appointment attributes. Taking selected Appointment object
     * from Appointment Overview screen and populating fields with attributes
     * form selected appointment object.
     * @param appointment Selected appointment object from Appointment Overview screen
     * @throws SQLException
     */
    public void setAppointment(Appointment appointment) throws SQLException {
        //Converting the start and end dates from LocalDateTime to LocalDate and LocalTime objects for display and modification
        LocalDate date = appointment.getStart().toLocalDate();
        LocalTime start = appointment.getStart().toLocalTime();
        LocalTime end = appointment.getEnd().toLocalTime();
        Contact selectedContact = null;
        int selectedCustomerId = 0;
        User selectedUser = null;

        //Retrieving all Contact, Customer, and User objects for display and selection
        ObservableList<Contact> allContacts = contactDao.getAllContacts();
        ObservableList<Customer> allCustomers = customerDao.getAllCustomers();
        ObservableList<User> allUsers = userDao.getAllUsers();
        ObservableList<Integer> allCustomerIds = FXCollections.observableArrayList();
        ObservableList<String> types = Appointment.getTypes();
        String selectedType = null;

        //Setting business hours from 8:00AM to 10:00PM EST
        LocalDateTime startHours = TimeUtils.getLocalStartTime();
        LocalDateTime endHours = TimeUtils.getLocalEndTime();

        //Setting appointment hour selection options
        while (startHours.isBefore(endHours.plusSeconds(1))){
            //System.out.println(startHours);
            //System.out.println(endHours);
            startTimeField.getItems().add(LocalTime.from(startHours));
            startHours = startHours.plusHours(1);
        }

        //Pre-selecting the user attributed to the appointment object that was passed in
        for (Customer c : allCustomers) {
            if (c.getId() == appointment.getCustomerId()){
                selectedCustomerId = c.getId();
            }
        }

        //Pre-selecting the User attributed to the appointment object that was passed in
        for (User u : allUsers) {
            if (u.getId() == appointment.getUserId()){
                selectedUser = u;
            }
        }

        //Pre-selecting the Contact attributed to the appointment object that was passed in
        for (Contact c : allContacts)
            if (c.getId() == appointment.getContactId()) {
                selectedContact = c;
            }

        //Taking all customers and getting a list of just Customer_ID's to be displayed and selected
        for (Customer c : allCustomers){
            if (c.getId() != 0){
                int customerId = c.getId();
                allCustomerIds.add(customerId);
            }
        }

        //Setting type
        for (String s : types){
            if (s.equals(appointment.getType())){
                selectedType = s;
            }
        }

        //Taking all passed values and presetting them into the Text Fields/ComboBoxes/DatePicker
        aptIDField.setText(Integer.toString(appointment.getId() ));
        titleField.setText(appointment.getTitle());
        descriptionField.setText(appointment.getTitle());
        locationField.setText(appointment.getLocation());
        typeField.setValue(selectedType);
        typeField.setItems(types);
        contactField.setItems(allContacts);
        contactField.setValue(selectedContact);
        dateField.setValue(date);
        startTimeField.setValue(start);
        endTimeField.setValue(end);
        customerIDField.setItems(allCustomerIds);
        customerIDField.setValue(selectedCustomerId);
        userField.setItems(allUsers);
        userField.setValue(selectedUser);
    }

    /**
     * Sets End Time combobox. Once start time is selected, this method is
     * called to populate the end time selection options to be after the
     * selected start time and before the end of business hours.
     * @param actionEvent Start Time selected
     */
    public void onActionStartTimeSelected(ActionEvent actionEvent) {
        endTimeField.getItems().clear();
        LocalDate selectedDate = dateField.getValue();
        LocalTime localEndTime = TimeUtils.getLocalEndTime().toLocalTime();
        LocalDateTime localEnd = LocalDateTime.of(selectedDate, localEndTime);
        LocalTime selectedStart = startTimeField.getSelectionModel().getSelectedItem();
        LocalDateTime selectedStartDate = LocalDateTime.of(selectedDate, selectedStart);
        endTimeField.setValue(selectedStart.plusHours(1));

        while (selectedStart.isBefore(localEndTime.plusSeconds(1))) {
            endTimeField.getItems().add(LocalTime.from(selectedStart.plusHours(1)));
            selectedStart = selectedStart.plusHours(1);
        }
    }

    /**
     * Checks if any inputs are empty.
     * @return true if any inputs are empty, false if no inputs are empty
     */
    public boolean inputsEmpty(){
        return (titleField.getText().isEmpty()
                || descriptionField.getText().isEmpty()
                || locationField.getText().isEmpty()
                || contactField.getSelectionModel().getSelectedItem() == null
                || typeField.getValue() == null
                || dateField.getValue() == null
                || startTimeField.getSelectionModel().getSelectedItem() == null
                || endTimeField.getSelectionModel().getSelectedItem() == null
                || customerIDField.getSelectionModel().getSelectedItem() == null
                || userField.getSelectionModel().getSelectedItem() == null
        );
    }
}
