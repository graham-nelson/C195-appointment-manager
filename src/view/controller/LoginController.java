package view.controller;

import dao.UserDao;
import dao.UserDaoImpl;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Customer;
import model.User;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Controller for Login screen. Includes methods for
 * validating usernames/passwords and also translates all
 * screen text to French if the user is in a French speaking
 * country when logging in.
 */
public class LoginController implements Initializable {


    UserDao userDao = new UserDaoImpl();

    Stage stage;
    Parent scene;

    @FXML
    public Label messageLabel;

    @FXML
    public Label countryTitleLabel;

    @FXML
    public Label usernameLabel;

    @FXML
    public Label passwordLabel;

    @FXML
    public Label countryLabel;

    @FXML
    public TextField usernameTextField;

    @FXML
    public PasswordField passwordTextField;

    @FXML
    public Button loginButton;

    /**
     * Takes password and username as input and checks if username
     * and password match user records in database.
     * @param username String entered by user from usernameTextField
     * @param password String entered by user from passwordTextField
     * @return true if username/password combination matches a User in the database, false if no matches are found
     * @throws SQLException
     */
    private Boolean checkUser(String username, String password) throws SQLException {
        Boolean userApproved = false;

        ObservableList<User> allUsers = userDao.getAllUsers();

        for (User u : allUsers){
            if (u.getName().equals(username) && u.getPassword().equals(password)){
                userApproved = true;
            }
        }
        return userApproved;
    };

    /**
     * When user clicks login button the checkUser method is called to validate username/password.
     * If username and password match database records, user is allowed into application.
     * If records do not match, an error is shown to user in system Locale Language
     * @param event Button clicked
     * @throws IOException
     * @throws SQLException
     */
    @FXML
    void onActionLogin(ActionEvent event) throws IOException, SQLException {
        ResourceBundle rb = ResourceBundle.getBundle("utils.Login", Locale.getDefault());
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();

        String filename = "login_activity.txt";
        FileWriter fileWriter = new FileWriter(filename, true);
        PrintWriter outputFile = new PrintWriter(fileWriter);

        if (checkUser(username, password)){
            //Tracks successful user login attempts and writes to login_activity.txt file
            String loginActivity = "Successful Login Attempt: " + username + " Date/Time: " + LocalDateTime.now();
            outputFile.println(loginActivity);
            outputFile.close();

            //passes username entered to the appointment overview screen to check against any upcoming appointments
            Stage stage;
            Parent root;
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/controller/AptOverview.fxml"));
            root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
            stage.setResizable(false);
            AptOverviewController controller = loader.getController();
            controller.userAppointmentReminder(username);
        }
        else {
            //Tracks unsuccessful user login attempts and writes to login_activity.txt file
            String loginActivity = "Unsuccessful Login Attempt: " + username + " Date/Time: " + LocalDateTime.now();
            outputFile.println(loginActivity);
            outputFile.close();

            if (Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("en")){
                messageLabel.setText(rb.getString("Error"));
            }
        }
    }

    /**
     * initialize method called when Login screen is loaded. Method checks users Locale and
     * displays all screen text in English if user Locale is English speaking or French if
     * user Locale is French speaking.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Uncomment line below to test login Internationalization for France Locale
        //Locale.setDefault(Locale.FRANCE);

        //Creating new ResourceBundle
        ResourceBundle rb = ResourceBundle.getBundle("utils.Login", Locale.getDefault());

        //Getting system Locale and translating all text on login screen to system Locale language
        if (Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("en")){
            loginButton.setText(rb.getString("Login"));
            usernameLabel.setText(rb.getString("Username"));
            usernameTextField.setPromptText(rb.getString("Username"));
            passwordLabel.setText(rb.getString("Password"));
            passwordTextField.setPromptText(rb.getString("Password"));
            countryTitleLabel.setText(rb.getString("CountryText"));
            countryLabel.setText(rb.getString("Country"));
        }
    }
}
