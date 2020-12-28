package view.controller;

import dao.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import model.Appointment;
import model.Contact;
import model.Customer;
import model.Division;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Controller for the Reports screen. Includes methods
 * for button clicks and displaying reports in TextArea's.
 */
public class ReportsController implements Initializable {

    AppointmentDao appointmentDao = new AppointmentDaoImpl();

    Stage stage;
    Parent scene;

    @FXML
    public TextArea totalAptsMonth;

    @FXML
    public TextArea totalAptsType;

    @FXML
    public TextArea contactScheduleTextArea;

    @FXML
    public TextArea totalCustomersTextArea;

    /**
     * Method called with Reports screen is opened. Sets the reports in the appropriate
     * TextArea's for displaying the report data.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            contactScheduleTextArea.setText(scheduleByContact());
            totalCustomersTextArea.setText(customersPerDivision());
            totalAptsType.setText(aptsByType());
            totalAptsMonth.setText(aptsByMonth());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns user to Appointment Overview screen.
     * @param actionEvent Button click
     * @throws IOException
     */
    public void onActionAppointments(ActionEvent actionEvent) throws IOException {
        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/controller/AptOverview.fxml"));
        stage.setScene(new Scene(scene));
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Takes user to Customer Overview screen.
     * @param actionEvent Button click
     * @throws IOException
     */
    public void onActionCustomers(ActionEvent actionEvent) throws IOException {
        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/controller/CustomerOverview.fxml"));
        stage.setScene(new Scene(scene));
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Exits the user from the application.
     * @param actionEvent Button click
     */
    public void onActionExit(ActionEvent actionEvent) {
        Platform.exit();
    }

    /**
     * Method creates a string of all appointments per contact.
     * @return String of report data to display
     * @throws SQLException
     */
    public String scheduleByContact() throws SQLException {
        ContactDao contactDao = new ContactDaoImpl();
        String contactSchedule = "Appointment Schedule By Contact: \n";
        ObservableList<Contact> allContacts = contactDao.getAllContacts();
        ObservableList<Appointment> contactsApts = FXCollections.observableArrayList();

        for (Contact c : allContacts){
            contactsApts = Appointment.getAllContactApts(c.getId());
            contactSchedule = contactSchedule + "\n" + c.getName() + ": \n";

            for (Appointment a : contactsApts){
                contactSchedule = contactSchedule + "Appointment ID: " + a.getId() +
                        " Title: " + a.getTitle() +
                        " Type: " + a.getType() +
                        " Description: " + a.getDescription() +
                        " Start: " + a.getStart() +
                        " End: " + a.getEnd() +
                        " Customer ID: " + a.getCustomerId() + "\n";
            }
        }
        return contactSchedule;
    }

    /**
     * Creates a string list of every customer within each division.
     * @return String of report data to display
     * @throws SQLException
     */
    public String customersPerDivision() throws SQLException {
        DivisionDao divisionDao = new DivisionDaoImpl();
        String customersPerDivision = "Customers Within Each Division: \n";
        ObservableList<Division> allDivisions = divisionDao.getAllDivisions();
        ObservableList<Customer> customersInDiv = FXCollections.observableArrayList();

        for (Division d : allDivisions){
            customersInDiv = Customer.getAllCustomersByDivision(d.getDivisionId());
            customersPerDivision = customersPerDivision + "\n" + d.getDivisionName() + ": \n";

            for (Customer c : customersInDiv){
                customersPerDivision = customersPerDivision +
                        "ID: " + c.getId() +
                        " Name: " + c.getName() +
                        " Address: " + c.getAddress() +
                        " Postal Code: " + c.getPostalCode() +
                        " Phone: " + c.getPhone() + "\n";
            }
        }
        return customersPerDivision;
    }

    /**
     * Creates a string list of number of appointments in each month of year.
     * @return String of report data to display
     * @throws SQLException
     */
    public String aptsByMonth() throws SQLException {
        ObservableList<Appointment> allAppointments = appointmentDao.getAllAppointments();
        ObservableList<Appointment> appointmentOfMonth = FXCollections.observableArrayList();
        String appointmentString = "Number of Appointments by Month: \n";

        for (int i = 1; i <= 12; i++){
            appointmentOfMonth = Appointment.appointmentByMonth(i);
            appointmentString = appointmentString + "\n" + i + ": " + appointmentOfMonth.size() + "\n";
        }
        return appointmentString;
    }


    /**
     * Creates a string list of the number of appointments of each type.
     * @return String of report data to display
     * @throws SQLException
     */
    public String aptsByType() throws SQLException {
        ObservableList<String> types = Appointment.getTypes();
        ObservableList<Appointment> aptsByType = FXCollections.observableArrayList();
        String appointmentString = "Number of Appointments by Type: \n";

        for (String s: types){
            aptsByType = Appointment.appointmentByType(s);
            appointmentString = appointmentString + "\n" + s + ": \n" + aptsByType.size() + "\n";

        }
        return appointmentString;
    }


}
