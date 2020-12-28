package model;

import dao.AppointmentDao;
import dao.AppointmentDaoImpl;
import dao.CustomerDao;
import dao.CustomerDaoImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Customer is the entity used for modeling
 * customer objects from the database
 */
public class Customer {

    private int id;
    private String name;
    private String address;
    private String postalCode;
    private String phone;
    private LocalDateTime createDate; //Should this be LocalDateTime?
    private String createdBy;
    private LocalDateTime lastUpdate; //Should this be Timestamp?
    private String lastUpdatedBy;
    private int divisionId;
    private String divisionName;

    /**
     * Constructor for Customer. Creates new Customer.
     * @param id sets customer id with and int value
     * @param name sets customer name with string value
     * @param address sets customer address with string value
     * @param postalCode sets customer postal code with string value
     * @param phone sets customer phone with string value
     * @param divisionId sets customer division ID with int value
     * @param divisionName sets customer division name with string value
     */
    public Customer(int id, String name, String address, String postalCode, String phone, int divisionId, String divisionName) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.createDate = LocalDateTime.now();
        this.createdBy = "admin";
        this.lastUpdate = LocalDateTime.now();
        this.lastUpdatedBy = "admin";
        this.divisionId = divisionId;
        this.divisionName = divisionName;
    }

    /**
     * @return current customer division name as string
     */
    public String getDivisionName() {
        return divisionName;
    }

    /**
     * @param divisionName division name to set
     */
    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    /**
     * @return current customer id as int
     */
    public int getId() {
        return id;
    }

    /**
     * @param id customer id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return current customer name as string
     */
    public String getName() {
        return name;
    }

    /**
     * @param name customer name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return current customer address as string
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address customer address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return current customer postal code as string
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * @param postalCode customer postal code to set
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * @return current customer phone as string
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone customer phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return current customer create date as LocalDateTime
     */
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate customer create date to set
     */
    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    /**
     * @return user who created customer by as string
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy user who created customer to set
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return current last update as LocalDateTime
     */
    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    /**
     * @param lastUpdate last update to set
     */
    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /**
     * @return current user who last updated customer
     */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /**
     * @param lastUpdatedBy user who last updated customer to set
     */
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * @return current customer division ID as int
     */
    public int getDivisionId() {
        return divisionId;
    }

    /**
     * @param divisionId division ID to set
     */
    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    /**
     * Method takes in a Customer id as an int and returns an
     * ObservableList of all appointments for that customer
     * @param id customer ID used to find all appointments for that customer
     * @return ObservableList of appointments for specified customer
     * @throws SQLException
     */
    public static ObservableList<Appointment> getAllCustomerApts(int id) throws SQLException {
        ObservableList<Appointment> allCustomerApts = FXCollections.observableArrayList();
        AppointmentDao appointmentDao = new AppointmentDaoImpl();

        for (Appointment a : appointmentDao.getAllAppointments()){
            if (a.getCustomerId() == id){
                allCustomerApts.add(a);
            }
        }
        return allCustomerApts;
    }

    /**
     * Method takes a division ID as an int and returns an
     * ObservableList of all customers in that division
     * @param divisionId division ID used to find all customers in that division
     * @return ObservableList of all customers within specified division
     * @throws SQLException
     */
    public static ObservableList<Customer> getAllCustomersByDivision(int divisionId) throws SQLException {
        ObservableList<Customer> allCustomersInDiv = FXCollections.observableArrayList();
        CustomerDao customerDao = new CustomerDaoImpl();

        for (Customer c : customerDao.getAllCustomers()){
            if (c.getDivisionId() == divisionId){
                allCustomersInDiv.add(c);
            }
        }
        return allCustomersInDiv;
    }


    /**
     * Taking Customer names and formatting them for display
     * in ComboBoxes
     * @return formatted string for display
     */
    @Override
    public String toString(){
        return (getName() + " [" + getId() + "]");
    }

}
