package model;

import java.time.LocalDateTime;

/**
 * User is the entity used to model
 * user objects from the database
 */
public class User {

    private int id;
    private String name;
    private String password; //String?
    private LocalDateTime createDate; //Should this be LocalDateTime?
    private String createdBy;
    private LocalDateTime lastUpdate; //Should this be Timestamp?
    private String lastUpdatedBy;

    /**
     * Constructor for User. Creates new Users.
     * @param id sets User ID with int value
     * @param name sets User name with String value
     * @param password sets User password with String value
     * @param createDate sets date/time User was created as LocalDateTime
     * @param createdBy sets User created by with String value
     * @param lastUpdate sets date/time User was last created with LocalDateTime value
     * @param lastUpdatedBy sets User last updated by with String value
     */
    public User(int id, String name, String password, LocalDateTime createDate, String createdBy, LocalDateTime lastUpdate, String lastUpdatedBy) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * @return current User ID as int
     */
    public int getId() {
        return id;
    }

    /**
     * @param id User id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return current User name as String
     */
    public String getName() {
        return name;
    }

    /**
     * @param name User name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return current User password as String
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password User password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return date/time current User was created as LocalDateTime
     */
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate date/time User created to set
     */
    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    /**
     * @return User who created current User object as String
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy User who created User object
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return date/time User was last updated as LocalDateTime
     */
    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    /**
     * @param lastUpdate date/time User was last updated
     */
    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /**
     * @return current User that last updated User object as String
     */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /**
     * @param lastUpdatedBy User that is updating User object
     */
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * Taking User names and formatting them for display
     * in ComboBoxes
     * @return formatted string for display
     */
    @Override
    public String toString(){
        return (getName() + " [" + getId() + "]");
    }
}
