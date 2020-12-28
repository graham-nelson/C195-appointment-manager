package view.controller;

import dao.AppointmentDao;
import dao.AppointmentDaoImpl;
import dao.UserDao;
import dao.UserDaoImpl;
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
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.LocalDateTimeStringConverter;
import model.Appointment;
import model.Contact;
import model.Customer;
import model.User;

import java.io.IOException;
import java.lang.management.PlatformLoggingMXBean;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for Appointment Overview screen. Contains methods for handling button clicks,
 * displaying all appointments, and filtering appointment views.
 */
public class AptOverviewController implements Initializable {

    UserDao userDao = new UserDaoImpl();
    AppointmentDao appointmentDao = new AppointmentDaoImpl();

    Stage stage;
    Parent scene;

    @FXML
    public Button filterAllButton;

    @FXML
    public Button filterMonthButton;

    @FXML
    public Button filterWeekButton;

    @FXML
    public Button addAptButton;

    @FXML
    public Button updateAptButton;

    @FXML
    public Button deleteAptButton;

    @FXML
    public Button customersButton;

    @FXML
    public Button reportsButton;

    @FXML
    public TableView<Appointment> aptTableView;

    @FXML
    public TableColumn<Appointment, Integer> idColumn;

    @FXML
    public TableColumn<Appointment, String> titleColumn;

    @FXML
    public TableColumn<Appointment, String> descriptionColumn;

    @FXML
    public TableColumn<Appointment, String> locationColumn;

    @FXML
    public TableColumn<Appointment, String> contactColumn;

    @FXML
    public TableColumn<Appointment, String> typeColumn;

    @FXML
    public TableColumn<Appointment, String> startColumn;

    @FXML
    public TableColumn<Appointment, String> endColumn;

    @FXML
    public TableColumn<Appointment, Integer> customerIDColumn;

    /**
     * Takes user to Customer Overview screen.
     * @param actionEvent Button click
     * @throws IOException
     */
    @FXML
    public void onActionCustomers(ActionEvent actionEvent) throws IOException {

        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/controller/CustomerOverview.fxml"));
        stage.setScene(new Scene(scene));
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Takes user to Add Appointment screen.
     * @param actionEvent Button click
     * @throws IOException
     */
    @FXML
    public void onActionAddApt(ActionEvent actionEvent) throws IOException {

        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/controller/AddAppointment.fxml"));
        stage.setScene(new Scene(scene));
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Takes user to Update Appointment screen if an appointment was
     * selected from the Appointment TableView. If no appointment is selected
     * when Update Appointment button is clicked, an error is displayed to user.
     *
     * @param actionEvent Button click
     * @throws IOException
     * @throws SQLException
     */
    @FXML
    public void onActionUpdateApt(ActionEvent actionEvent) throws IOException, SQLException {

        if (aptTableView.getSelectionModel().getSelectedItem() != null){
            Stage stage;
            Parent root;
            stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/controller/UpdateAppointment.fxml"));
            root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            stage.setResizable(false);
            UpdateAppointmentController controller = loader.getController();
            Appointment appointment = aptTableView.getSelectionModel().getSelectedItem();
            controller.setAppointment(appointment);
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Appointment Selection Error");
            alert.setHeaderText("No Appointment Selected For Modification");
            alert.setContentText("Please Select a Appointment to Update");
            alert.showAndWait();
        }
    }

    /**
     * Deletes appointment. If appointment is selected when delete
     * button is clicked, appointment will be deleted. If no appointment
     * is selected an error is displayed to user.
     * @param actionEvent Button click
     * @throws SQLException
     */
    @FXML
    public void onActionDeleteApt(ActionEvent actionEvent) throws SQLException {

        if (aptTableView.getSelectionModel().getSelectedItem() != null) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Continuing will delete the selected appointment.");
            alert.setHeaderText("Do you wish to continue?");
            alert.setTitle("Delete Appointment");

            Optional<ButtonType> result =  alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {

                try {
                    Appointment selectedApt = aptTableView.getSelectionModel().getSelectedItem();
                    appointmentDao.deleteAppointment(selectedApt.getId());
                    ObservableList<Appointment> allAppointments = appointmentDao.getAllAppointments();
                    ObservableList<Appointment> futureAppointments = FXCollections.observableArrayList();

                    for (Appointment a : allAppointments){
                        if (a.getEnd().isAfter(LocalDateTime.now())){
                            futureAppointments.add(a);
                        }
                    }

                    aptTableView.setItems(futureAppointments);
                    idColumn.setCellValueFactory(new PropertyValueFactory<>("id") );
                    titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
                    descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
                    locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
                    contactColumn.setCellValueFactory(new PropertyValueFactory<>("contactName"));
                    typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
                    startColumn.setCellValueFactory(new PropertyValueFactory<>("formattedStart"));//Calls formatted version of getter
                    endColumn.setCellValueFactory(new PropertyValueFactory<>("formattedEnd"));//Calls formatted version of getter
                    customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));

                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

        } else { //Displays error message if no appointment is selected to be deleted
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Appointment Selection Error");
            errorAlert.setHeaderText("No Appointment Selected");
            errorAlert.setContentText("Please Select an appointment to Delete");
            errorAlert.showAndWait();
        }


    }

    /**
     * Takes user to Reports screen.
     * @param actionEvent Button click
     * @throws IOException
     */
    @FXML
    public void onActionReports(ActionEvent actionEvent) throws IOException {
        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/controller/Reports.fxml"));
        stage.setScene(new Scene(scene));
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Exits user from application.
     * @param actionEvent Button click
     */
    @FXML
    public void onActionExit(ActionEvent actionEvent) {
        Platform.exit();
    }

    /**
     * Shows all appointments in database that are in the future.
     * @param actionEvent Button click
     */
    @FXML
    public void onActionFilterAll(ActionEvent actionEvent){

        try {
            ObservableList<Appointment> allAppointments = appointmentDao.getAllAppointments();
            ObservableList<Appointment> futureAppointments = FXCollections.observableArrayList();

            for (Appointment a : allAppointments){
                if (a.getEnd().isAfter(LocalDateTime.now())){
                    futureAppointments.add(a);
                }
            }

            aptTableView.setItems(futureAppointments);
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id") );
            titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
            descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
            locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
            contactColumn.setCellValueFactory(new PropertyValueFactory<>("contactName"));
            typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
            startColumn.setCellValueFactory(new PropertyValueFactory<>("formattedStart"));//Calls formatted version of getter
            endColumn.setCellValueFactory(new PropertyValueFactory<>("formattedEnd"));//Calls formatted version of getter
            customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows all appointments between now and one week.
     * @param actionEvent Button click
     * @throws SQLException
     */
    @FXML
    public void onActionFilterWeek(ActionEvent actionEvent) throws SQLException {
        ObservableList<Appointment> filteredApts = FXCollections.observableArrayList();

        for (Appointment a : appointmentDao.getAllAppointments()){
            if (a.getStart().isAfter(LocalDateTime.now()) && a.getStart().isBefore(LocalDateTime.now().plusWeeks(1))){
                filteredApts.add(a);
            }
        }

        aptTableView.setItems(filteredApts);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id") );
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("formattedStart"));//Calls formatted version of getter
        endColumn.setCellValueFactory(new PropertyValueFactory<>("formattedEnd"));//Calls formatted version of getter
        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));

    }

    /**
     * Shows all appointments between now and one month.
     * @param actionEvent Button click
     * @throws SQLException
     */
    @FXML
    public void onActionFilterMonth(ActionEvent actionEvent) throws SQLException {
        ObservableList<Appointment> filteredApts = FXCollections.observableArrayList();

        for (Appointment a : appointmentDao.getAllAppointments()){
            if (a.getStart().isAfter(LocalDateTime.now()) && a.getStart().isBefore(LocalDateTime.now().plusMonths(1))){
                filteredApts.add(a);
            }
        }

        aptTableView.setItems(filteredApts);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id") );
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("formattedStart"));//Calls formatted version of getter
        endColumn.setCellValueFactory(new PropertyValueFactory<>("formattedEnd"));//Calls formatted version of getter
        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
    }

    /**
     * Method for sending an appointment reminder to user when logging in.
     * Checks if user has any appointments within 15 minutes of logging in.
     * Displays a customer message to user reminding them of any appointments within 15 minutes,
     * or that they have no upcoming appointments depending on the users schedule.
     * @param username String entered by user on Login screen
     * @throws SQLException
     */
    public void userAppointmentReminder(String username) throws SQLException {
        ObservableList<Appointment> allAppointments = appointmentDao.getAllAppointments();
        ObservableList<User> allUsers = userDao.getAllUsers();
        Appointment approachingAppointment = null;
        User user = null;

        //retrieving User object from username entered at login
        for (User u : allUsers){
            if (u.getName().equals(username)) {
                user = u;
            }
        }

        //for each appointment, checking if there are any appointments that are within 15 minutes and belong to User object entered at login
        for (Appointment a : allAppointments){
            long timeDifference = ChronoUnit.MINUTES.between(LocalDateTime.now(), a.getStart());
            assert user != null;
            if (a.getUserId() == user.getId() && timeDifference > 0 && timeDifference <= 15){
                approachingAppointment = a;
            }
        }

        //if there is an appointment within 15 minutes for that user, display a reminder
        if (approachingAppointment != null){
                LocalDate appointmentDate = approachingAppointment.getStart().toLocalDate();
                LocalTime appointmentTime = approachingAppointment.getStart().toLocalTime();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Appointment Reminder");
                alert.setHeaderText("You have an upcoming appointment.");
                alert.setContentText("Appointment ID: " + approachingAppointment.getId() + "\n" +
                                    " Date: " + appointmentDate + "\n" +
                                    " Time: " + appointmentTime);
                alert.showAndWait();
        }
        //if no appointment was found, display no upcoming appointment reminder
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Appointment Reminder");
            alert.setHeaderText("You have no upcoming appointment's.");
            alert.setContentText("Follow the 'Add Appointment' link to schedule an appointment.");
            alert.showAndWait();
        }
    }

    /**
     * initialize method called when Appointment Overview screen is loaded. Contains code to populate
     * the appointment table of all appointments in the database.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Populating all appointments currently in database that are in the future or currently going on
        try {

            ObservableList<Appointment> allAppointments = appointmentDao.getAllAppointments();
            ObservableList<Appointment> futureAppointments = FXCollections.observableArrayList();

            for (Appointment a : allAppointments){
                if (a.getEnd().isAfter(LocalDateTime.now())){
                    futureAppointments.add(a);
                }
            }

            aptTableView.setItems(futureAppointments);
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id") );
            titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
            descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
            locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
            contactColumn.setCellValueFactory(new PropertyValueFactory<>("contactName"));
            typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
            startColumn.setCellValueFactory(new PropertyValueFactory<>("formattedStart"));//Calls formatted version of getter
            endColumn.setCellValueFactory(new PropertyValueFactory<>("formattedEnd"));//Calls formatted version of getter
            customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
