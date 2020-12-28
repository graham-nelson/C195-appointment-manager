package view.controller;

import javafx.scene.control.*;
import model.Appointment;
import utils.TimeUtils;
import dao.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Contact;
import model.Customer;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.*;
import java.time.chrono.ChronoLocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TimeZone;

/**
 * Controller for Add Appointment screen. Contains methods for adding a new appointment
 * to the database and doing input validation.
 */
public class AddAppointmentController implements Initializable {

    AppointmentDao appointmentDao = new AppointmentDaoImpl();
    ContactDao contactDao = new ContactDaoImpl();
    CustomerDao customerDao = new CustomerDaoImpl();
    UserDao userDao = new UserDaoImpl();

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
    private Button addAppointmentButton;

    @FXML
    private Button cancelButton;

    /**
     * Clears all fields and takes user back to Appointment Overview screen.
     * @param actionEvent Button click
     * @throws IOException
     */
    @FXML
    public void onActionCancel(ActionEvent actionEvent) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Cancelling will erase all the information entered.");
        alert.setHeaderText("Do you wish to continue?");
        alert.setTitle("Cancel");

        Optional<ButtonType> result =  alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK){
            stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/controller/AptOverview.fxml"));
            stage.setScene(new Scene(scene));
            stage.setResizable(false);
            stage.show();
        }
    }

    /**
     * Validates inputs and creates new appointment if no validation errors are present.
     * @param actionEvent Button click
     * @throws IOException
     * @throws SQLException
     */
    @FXML
    public void onActionAddAppointment(ActionEvent actionEvent) throws IOException, SQLException {

        //If any fields are not filled out, display an error
        if (inputsEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Add Appointment Error");
            alert.setHeaderText("One or more fields are empty.");
            alert.setContentText("Please complete all fields to add appointment.");
            alert.showAndWait();
        }
        //if all fields are filled out, proceed with other input validation statements
        else {
            LocalDate selectedDate = dateField.getValue();
            LocalTime selectedStart = startTimeField.getValue();
            LocalTime selectedEnd = endTimeField.getValue();
            LocalDateTime startDateTime = LocalDateTime.of(selectedDate, selectedStart);
            LocalDateTime endDateTime = LocalDateTime.of(selectedDate, selectedEnd);
            Appointment conflictingAppointment = Appointment.isScheduleConflict(customerIDField.getSelectionModel().getSelectedItem(), startDateTime, endDateTime);

            //Checks if current inputs conflict with selected customers other appointments
            if (conflictingAppointment != null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Scheduling Conflict");
                alert.setHeaderText("This Customer already has an appointment at the selected time. Please select a time outside of the times below.");
                alert.setContentText("Appointment ID: " + conflictingAppointment.getId() + "\n" +
                        "Date: " + conflictingAppointment.getStart().toLocalDate() + "\n" +
                        "Start: " + conflictingAppointment.getStart().toLocalTime() + "\n" +
                        "End: " + conflictingAppointment.getEnd().toLocalTime());
                alert.showAndWait();
                startTimeField.setValue(conflictingAppointment.getEnd().toLocalTime());
            }
            //Start and End must be in the future
            else if (startDateTime.isBefore(LocalDateTime.now()) || endDateTime.isBefore(LocalDateTime.now())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Time Error");
                alert.setHeaderText("Selected time is before current time.");
                alert.setContentText("Please select an appointment time and date in the future.");
                alert.showAndWait();
            }
            //No input validation errors, create new appointment
            else {
                //Takes input values and selections and creates a new appointment with those values
                String title = titleField.getText();
                String description = descriptionField.getText();
                String location = locationField.getText();
                Contact contact = contactField.getSelectionModel().getSelectedItem();
                int contactId = contact.getId();
                String type = typeField.getValue();
                Timestamp startTimestamp = Timestamp.valueOf(startDateTime);
                Timestamp endTimestamp = Timestamp.valueOf(endDateTime);
                int customerId = customerIDField.getSelectionModel().getSelectedItem();
                int userId = userField.getSelectionModel().getSelectedItem().getId();

                appointmentDao.addAppointment(title, description, location, type, startTimestamp, endTimestamp, customerId, userId, contactId);

                stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/controller/AptOverview.fxml"));
                stage.setScene(new Scene(scene));
                stage.setResizable(false);
                stage.show();
            }
        }
    }

    /**
     * initialize method called when Add Appointment screen is loaded. Populates Combobox's
     * with options for creating new appointment objects.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try { //Loading appointment options into ComboBoxes
            ObservableList<Customer> allCustomers = customerDao.getAllCustomers();
            ObservableList<Integer> allCustomerIds = FXCollections.observableArrayList();

            //Getting Customer ID's to be displayed in CustomerIDField
            for (Customer c : allCustomers){
                if (c.getId() != 0){
                    int customerId = c.getId();
                    allCustomerIds.add(customerId);
                }
            }

            //Setting Options
            contactField.setItems(contactDao.getAllContacts());
            customerIDField.setItems(allCustomerIds);
            userField.setItems(userDao.getAllUsers());
            typeField.setItems(Appointment.getTypes());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        LocalDateTime localStart = TimeUtils.getLocalStartTime();
        LocalDateTime localEnd = TimeUtils.getLocalEndTime();


        while (localStart.isBefore(localEnd.plusSeconds(1))){
                startTimeField.getItems().add(LocalTime.from(localStart));
                localStart = localStart.plusHours(1);
        }
    }

    /**
     * Populates End Time combobox. Once a start time is selected for an appointment,
     * this method populates end time options starting an hour after start time
     * to the end of business hours.
     * @param actionEvent Start Time selected
     */
    public void onActionStartTimeSelected(ActionEvent actionEvent) {
        endTimeField.getItems().clear();

        LocalDate selectedDate = dateField.getValue();
        //Decouples Date from ZonedDateTime and uses the selected date as part of localEnd
        LocalTime localEndTime = TimeUtils.getLocalEndTime().toLocalTime();
        LocalDateTime localEnd = LocalDateTime.of(selectedDate, localEndTime);

        LocalTime selectedStart = startTimeField.getSelectionModel().getSelectedItem();
        LocalDateTime selectedStartDate = LocalDateTime.of(selectedDate, selectedStart);

        endTimeField.setValue(selectedStart.plusHours(1));

        while (selectedStartDate.isBefore(localEnd.plusSeconds(1))) {
            endTimeField.getItems().add(LocalTime.from(selectedStartDate.plusHours(1)));
            selectedStartDate = selectedStartDate.plusHours(1);
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
                || contactField.getValue() == null
                || typeField.getValue() == null
                || dateField.getValue() == null
                || startTimeField.getValue() == null
                || endTimeField.getValue() == null
                || customerIDField.getValue() == null
                || userField.getValue() == null
        );
    }
}
