package model;

import java.time.LocalDateTime;

/**
 * Division is the entity used to model
 * division objects from the database
 */
public class Division {

    private int id;
    private String name;
    private LocalDateTime createDate;
    private String createdBy;
    private LocalDateTime lastUpdate;
    private String lastUpdatedBy;
    private int countryId;

    /**
     * Division constructor. Creates new Division
     * @param divisionId sets Division ID with int value
     * @param divisionName sets Division name with String value
     * @param createDate sets date Division was created with LocalDateTime value
     * @param createdBy sets user Division was created by with String value
     * @param lastUpdate sets date Division was last updates with LocalDateTime value
     * @param lastUpdatedBy sets user Division was last updated by with String value
     * @param countryId sets Division country ID with int value
     */
    public Division(int divisionId, String divisionName, LocalDateTime createDate, String createdBy, LocalDateTime lastUpdate, String lastUpdatedBy, int countryId) {
        this.id = divisionId;
        this.name = divisionName;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.countryId = countryId;
    }

    /**
     * @return current Division ID as int
     */
    public int getDivisionId() {
        return id;
    }

    /**
     * @param divisionId Division ID to set
     */
    public void setDivisionId(int divisionId) {
        this.id = divisionId;
    }

    /**
     * @return current Division name as String
     */
    public String getDivisionName() {
        return name;
    }

    /**
     * @param divisionName Division name to set
     */
    public void setDivisionName(String divisionName) {
        this.name = divisionName;
    }

    /**
     * @return current date/time Division was created as LocalDateTime
     */
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate create date to be set
     */
    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    /**
     * @return user who created current Division
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy user who created Division to set
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return date/time Division was last updated as LocalDateTime
     */
    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    /**
     * @param lastUpdate date/time of last update to set
     */
    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /**
     * @return user who last updated Division as String
     */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /**
     * @param lastUpdatedBy user who last updated Division
     */
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * @return country ID Division is a part of as int
     */
    public int getCountryId() {
        return countryId;
    }

    /**
     * @param countryId country ID to be set
     */
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    /**
     * Taking Division names and formatting them for display
     * in ComboBoxes
     * @return formatted string for display
     */
    @Override
    public String toString(){
        return (getDivisionName() + " [" + getDivisionId() + "]");
    }

}
