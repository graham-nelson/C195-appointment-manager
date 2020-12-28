package model;

import java.time.LocalDateTime;

/**
 * Country is the entity used to model
 * country objects from the database
 */
public class Country {

    private int id;
    private String name;
    private LocalDateTime createDate;
    private String createdBy;
    private LocalDateTime lastUpdated;
    private String lastUpdatedBy;

    /**
     * Constructor for Country. Creates new Country objects.
     * @param id sets Country ID with int value
     * @param name sets Country name with String value
     * @param createDate sets date Country was created with LocalDateTime value
     * @param createdBy sets user Country was created by with String value
     * @param lastUpdated sets date Country was last updated with LocalDateTime value
     * @param lastUpdatedBy sets user Country was last updated by with String value
     */
    public Country(int id, String name, LocalDateTime createDate, String createdBy, LocalDateTime lastUpdated, String lastUpdatedBy) {
        this.id = id;
        this.name = name;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdated = lastUpdated;
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * @return current Country ID as int
     */
    public int getId() {
        return id;
    }

    /**
     * @param id Country ID to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return current Country name as String
     */
    public String getName() {
        return name;
    }

    /**
     * @param name Country name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return current date Country was created as LocalDateTime
     */
    public LocalDateTime getDate() {
        return createDate;
    }

    /**
     * @param date date Country was created
     */
    public void setDate(LocalDateTime date) {
        this.createDate = date;
    }

    /**
     * @return current user who created Country as String
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy user who created Country
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return current LocalDateTime when the Country was last updated
     */
    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    /**
     * @param lastUpdated last LocalDateTime Country was updated
     */
    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    /**
     * @return last user to update Country as String.
     */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /**
     * @param lastUpdatedBy last user to update Country.
     */
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * Taking Country names and formatting them for display
     * in ComboBoxes
     * @return formatted string for display
     */
    @Override
    public String toString(){
        return (getName() + " [" + getId() + "]");
    }
}
