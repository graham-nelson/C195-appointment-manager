package view.controller;

import dao.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Country;
import model.Customer;
import model.Division;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Controller for Update Customer screen. Contains methods for updating customer objects,
 * performing input validation and handling button clicks.
 */
public class UpdateCustomerController {

    Stage stage;
    Parent scene;
    DivisionDao divisionDao = new DivisionDaoImpl();
    CountryDao countryDao = new CountryDaoImpl();
    CustomerDao customerDao = new CustomerDaoImpl();

    @FXML
    private TextField customerIDField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField addressField;

    @FXML
    private TextField postalCodeField;

    @FXML
    private ComboBox<Country> countryField;

    @FXML
    private ComboBox<Division> divisionField;

    @FXML
    private TextField phoneField;

    @FXML
    private Button updateCustomerButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Button appointmentsButton;

    /**
     * Returns Division object from Division ID. Takes Division ID from selected
     * Customer object and returns the Division object associated with that ID.
     * @param id ID for desired Division object
     * @return Division object for specified Division ID
     * @throws SQLException
     */
    public Division getDivisionFromId(int id) throws SQLException {

        ObservableList<Division> allDivisions = divisionDao.getAllDivisions();
        Division selectedDivision = null;

        for (Division d : allDivisions){
            if (id == d.getDivisionId()){
                selectedDivision = d;
            }
        }

        return selectedDivision;
    }

    /**
     * Returns Country object from Country ID. Takes Country ID from selected
     * Customer objects Division and returns the Country object associated with
     * that Division/Customer.
     * @param countryId ID for desired Country object
     * @return Country object belonging to specified Country ID
     * @throws SQLException
     */
    public Country getCountryFromDivision(int countryId) throws SQLException {
        ObservableList<Country> allCountries = countryDao.getAllCountries();
        Country selectedCountry = null;

        for (Country c : allCountries){
            if (countryId == c.getId()){
                selectedCountry = c;
            }
        }

        return selectedCountry;
    }

    /**
     * Presets fields with selected Customer attributes. Takes selected customer
     * from Customer Overview screen and pre-populates all data for modification.
     * @param customer selected Customer object from Customer Overview screen
     * @throws SQLException
     */
    public void setCustomer(Customer customer) throws SQLException {

        //Get Customers Division object from divisionId
        Division selectedDivision = getDivisionFromId(customer.getDivisionId());
        //Get Customer Country object from Customers divisionId
        Country selectedCountry = getCountryFromDivision(selectedDivision.getCountryId());

        //list to hold division options within selected country
        ObservableList<Division> divInSelectedCountry = FXCollections.observableArrayList();

        try {
            //populating Country ComboBox with all country data
            countryField.setItems(countryDao.getAllCountries());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //setting current country object
        countryField.setValue(selectedCountry);

        //finding all divisions within selected country
        for (Division d : divisionDao.getAllDivisions()) {

            if (selectedCountry.getId() == d.getCountryId()) {
                divInSelectedCountry.add(d);
            }
        }

        //setting all divisions within selected country
        divisionField.setItems(divInSelectedCountry);
        //setting current division object
        divisionField.setValue(selectedDivision);

        //pre-populating all current customer attributes for modification
        customerIDField.setText(Integer.toString(customer.getId()));
        nameField.setText(customer.getName());
        addressField.setText(customer.getAddress());
        postalCodeField.setText(customer.getPostalCode());
        phoneField.setText(customer.getPhone());
    }

    /**
     * Clears all changes and returns user to Customer Overview screen.
     * @param actionEvent Button Click
     * @throws IOException
     */
    @FXML
    public void onActionCancel(ActionEvent actionEvent) throws IOException {

        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/controller/CustomerOverview.fxml"));
        stage.setScene(new Scene(scene));
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Clears all changes and returns user to Appointment Overview screen.
     * @param actionEvent
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
     * Updates Customer object with modified attributes. Takes modified values from user,
     * performs input validation and updates Customer object if no input validation issues
     * are present.
     * @param actionEvent Button click
     * @throws IOException
     * @throws SQLException
     */
    @FXML
    public void onActionUpdateCustomer(ActionEvent actionEvent) throws IOException, SQLException {
        //Checks if any fields are empty and displays error message
        if (
                nameField.getText().isEmpty()
                || addressField.getText().isEmpty()
                || postalCodeField.getText().isEmpty()
                || phoneField.getText().isEmpty()
        ) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Add Customer Error");
            alert.setHeaderText("One or more fields are empty.");
            alert.setContentText("Please complete all fields to add customer.");
            alert.showAndWait();
        }
        //If all fields are filled. Customer update proceeds
        else {
            //Retrieves updated values and stores them in temp variables
            int id = Integer.parseInt(customerIDField.getText());
            String name = String.valueOf(nameField.getText());
            String address = String.valueOf(addressField.getText());
            String postalCode = String.valueOf(postalCodeField.getText());
            String phone = String.valueOf(phoneField.getText());
            int divisionID = divisionField.getValue().getDivisionId();

            //Temp variables used to update customer in database
            customerDao.updateCustomer(id, name, address, postalCode, phone, divisionID);

            //Returns user to CustomerOverview screen
            stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/controller/CustomerOverview.fxml"));
            stage.setScene(new Scene(scene));
            stage.setResizable(false);
            stage.show();
        }
    }

    /**
     * Populates Division ComboBox once Country is selected. Once Country is selected, method
     * populates Division ComboBox with Division options that are within selected Country.
     *
     * Lambda expression used here for the purpose of increased readability,
     * and one less ObservableList used than a previous implementation with
     * a for loop iterating through all divisions and populating a second list
     * if the countryID's matched.Returns true if division belongs in filtered list
     * returns false if division does not belong in filtered list.
     *
     * @param actionEvent Country is selected
     * @throws SQLException
     */
    @FXML
    public void onActionSelectCountry(ActionEvent actionEvent) throws SQLException {
        DivisionDao divisionDao = new DivisionDaoImpl();
        Country selectedCountry = countryField.getSelectionModel().getSelectedItem();

        ObservableList<Division> divsInCountry = divisionDao.getAllDivisions().filtered(division -> {
            if (division.getCountryId() == selectedCountry.getId())
                return true;
            return false;
        });

        divisionField.setItems(divsInCountry);
    }
}