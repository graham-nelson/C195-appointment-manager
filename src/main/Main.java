package main;

import utils.DBConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Scanner;

/**
 * The program is designed to allow the user to manage customers and appointments.
 * All data resides in a mySQL database.
 * Appointments and customers can be added, updated and deleted.
 *
 * @author  Graham Nelson
 * @version 1.0
 * @since   2020-10-21
 */
public class Main extends Application {

    /**
     * This method loads the Login screen to start the application.
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/view/controller/Login.fxml"));
        primaryStage.setTitle("Global Consulting - Appointments");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * Main Method opens database connection, calls launch and closes database connection
     * when the program is closed.
     * @param args
     * @throws SQLException
     */
    public static void main(String[] args) throws SQLException {
        //Open DB connection at beginning of program
        DBConnection.startConnection();
        launch(args);
        //Close DB connection at end of program
        DBConnection.closeConnection();
    }
}

