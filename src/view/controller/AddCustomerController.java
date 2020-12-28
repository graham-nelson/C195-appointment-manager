package view.controller;

import dao.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Country;
import model.Division;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for Add Customer screen. Contains methods for adding customers to the database,
 * handling button clicks, and performing input validation.
 */
public class AddCustomerController implements Initializable {

    Stage stage;
    Parent scene;


    @FXML //Auto Incrementing Field. Not used.
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
    private Button addCustomerButton;

    @FXML
    private Button cancelButton;

    /**
     * Clears all fields and takes user to Customer Overview screen.
     *
     * Lambda used here for the purpose of increased readability and an easier implementation
     * of an alert response.
     *
     * @param actionEvent Button clicked
     * @throws IOException
     */
    @FXML
    public void onActionCancel(ActionEvent actionEvent) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Cancelling will erase all the information entered.");
        alert.setHeaderText("Do you wish to continue?");
        alert.setTitle("Cancel");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                    scene = FXMLLoader.load(getClass().getResource("/view/controller/CustomerOverview.fxml"));
                    stage.setScene(new Scene(scene));
                    stage.setResizable(false);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Creates new Customer. Retrieves data from inputs, creates new customer from
     * data and inputs that customer to database. Also performs input validation.
     * @param actionEvent Button click
     * @throws IOException
     * @throws SQLException
     */
    @FXML
    public void onActionAddCustomer(ActionEvent actionEvent) throws IOException, SQLException {
        //Checks if all fields have been filled out before creating new customer
        if (
                nameField.getText().isEmpty()
                || addressField.getText().isEmpty()
                || postalCodeField.getText().isEmpty()
                || phoneField.getText().isEmpty()
                || divisionField.getSelectionModel().getSelectedItem() == null
                || countryField.getSelectionModel().getSelectedItem() == null
        ) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Add Customer Error");
            alert.setHeaderText("One or more fields are empty.");
            alert.setContentText("Please complete all fields to add customer.");
            alert.showAndWait();
        }
        else {
            //creating dao object
            CustomerDao customerDao = new CustomerDaoImpl();
            //getting input data and storing in variables
            String name = nameField.getText();
            String address = addressField.getText();
            String postalCode = postalCodeField.getText();
            String phone = phoneField.getText();
            int divisionId = divisionField.getValue().getDivisionId();

            //addCustomer method with input data arguments to add new customer to database
            customerDao.addCustomer(name, address, postalCode, phone, divisionId);

            //after adding customer to database, load CustomerOverview Screen
            stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/controller/CustomerOverview.fxml"));
            stage.setScene(new Scene(scene));
            stage.setResizable(false);
            stage.show();
        }
    }

    /**
     * Populates Division ComboBox. Once Country is selected, method
     * populates Division ComboBox with Division options that are
     * within selected Country.
     *
     * Lambda expression used here for the purpose of increased readability,
     * and one less ObservableList used than a previous implementation with
     * a for loop iterating through all divisions and populating a second list
     * if the countryID's matched.Returns true if division belongs in filtered list
     * returns false if division does not belong in filtered list.
     * @param actionEvent Country Combobox selected
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

    /**
     * initialize method called when Add Customer screen is loaded.
     * Populates Country ComboBox with options.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        CountryDao countryDao = new CountryDaoImpl();

        try {
            //populating Country ComboBox with data
            countryField.setItems(countryDao.getAllCountries());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


}
