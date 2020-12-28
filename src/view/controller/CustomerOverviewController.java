package view.controller;

import dao.AppointmentDao;
import dao.AppointmentDaoImpl;
import dao.CustomerDao;
import dao.CustomerDaoImpl;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for Customer Overview screen. Contains methods for handling button clicks
 * and populating Customer TableView.
 */
public class CustomerOverviewController implements Initializable {

    Stage stage;
    Parent scene;

    //Create DAO object
    CustomerDao customerDao = new CustomerDaoImpl();
    AppointmentDao appointmentDao = new AppointmentDaoImpl();

    @FXML
    private Button addCustomerButton;

    @FXML
    private Button updateCustomerButton;

    @FXML
    private Button deleteCustomerButton;

    @FXML
    private Button aptsButton;

    @FXML
    private TableView<Customer> customerTable;

    @FXML
    private TableColumn<Customer, Integer> idColumn;

    @FXML
    private TableColumn<Customer, String> nameColumn;

    @FXML
    private TableColumn<Customer, String> addressColumn;

    @FXML
    private TableColumn<Customer, String> postalCodeColumn;

    @FXML
    private TableColumn<Customer, String> phoneColumn;

    @FXML
    private TableColumn<Customer, Integer> divisionIDColumn;

    @FXML
    private TableColumn<Customer, String> divisionColumn;

    @FXML
    private Button exitButton;

    /**
     * Takes user to Add Customer screen.
     * @param actionEvent Button click
     * @throws IOException
     */
    @FXML
    public void onActionAddCustomer(ActionEvent actionEvent) throws IOException {

        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/controller/AddCustomer.fxml"));
        stage.setScene(new Scene(scene));
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Takes user to Update Customer screen. If Customer is selected from TableView when button is clicked,
     * takes user to Update Customer screen. If no Customer is selected, an error is displayed to user.
     * @param actionEvent Button click
     * @throws IOException
     * @throws SQLException
     */
    @FXML
    public void onActionUpdateCustomer(ActionEvent actionEvent) throws IOException, SQLException {

        if (customerTable.getSelectionModel().getSelectedItem() != null){
            Stage stage;
            Parent root;
            stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Controller/UpdateCustomer.fxml"));
            root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            stage.setResizable(false);
            UpdateCustomerController controller = loader.getController();
            Customer customer = customerTable.getSelectionModel().getSelectedItem();
            controller.setCustomer(customer);
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Customer Selection Error");
            alert.setHeaderText("No Customer Selected For Modification");
            alert.setContentText("Please Select a Customer to Update");
            alert.showAndWait();
        }
    }

    /**
     * Deletes selected Customer from database. If Customer is selected from TableView when
     * button is clicked, takes selected Customer is deleted. If no Customer is selected,
     * an error is displayed to user. Due to foreign key constraints in the database, all
     * appointments for selected Customer are deleted prior to Customer being deleted.
     * @param actionEvent Button click
     * @throws SQLException
     */
    @FXML
    public void onActionDeleteCustomer(ActionEvent actionEvent) throws SQLException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Continuing will delete the selected customer and all appointments associated with that customer.");
        alert.setHeaderText("Do you wish to continue?");
        alert.setTitle("Delete Customer");

        Optional<ButtonType> result =  alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {

            if (customerTable.getSelectionModel().getSelectedItem() != null) {
                Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();

                ObservableList<Appointment> allAppointments = appointmentDao.getAllAppointments();

                //Deletes all appointments containing the customer selected to be deleted due to foreign key constraints in database
                for (Appointment a : allAppointments) {
                    if (a.getCustomerId() == selectedCustomer.getId()) {
                        appointmentDao.deleteAppointment(a.getId());
                    }
                }

                customerDao.deleteCustomer(selectedCustomer.getId());

                customerTable.setItems(customerDao.getAllCustomers());

                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Customer Deleted Successfully");
                alert1.setHeaderText("The following customer was deleted from the database:");
                alert1.setContentText("ID: " + selectedCustomer.getId() + "\n" +
                                        "Name: " + selectedCustomer.getName() + "\n" +
                                        "Address: " + selectedCustomer.getAddress() + "\n");
                alert1.showAndWait();

            } else { //Displays error message if no customer is selected to be deleted
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Customer Selection Error");
                errorAlert.setHeaderText("No Customer Selected");
                errorAlert.setContentText("Please Select a Customer to Delete");
                errorAlert.showAndWait();
            }
        }
    }

    /**
     * Takes user to Appointment Overview screen.
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
     * Exits user from application.
     * @param actionEvent Button clicked
     */
    @FXML
    public void onActionExit(ActionEvent actionEvent) {
        Platform.exit();
    }

    /**
     * initialize method is called when Customer Overview screen is loaded.
     * Contains code to display all Customers in database in Customer TableView.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        //get customer data from database and display in TableView
        try {
            customerTable.setItems(customerDao.getAllCustomers());
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
            postalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
            phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
            divisionIDColumn.setCellValueFactory(new PropertyValueFactory<>("divisionId"));
            divisionColumn.setCellValueFactory(new PropertyValueFactory<>("divisionName"));

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
