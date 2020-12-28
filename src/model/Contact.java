package model;

/**
 * Contact entity is used to model
 * contact object from database
 */
public class Contact {

    private int id;
    private String name;
    private String email;

    /**
     * Constructor for contact. Creates new Contact.
     * @param id sets Contact ID with int value
     * @param name sets Contact name with String value
     * @param email sets Contact email with String value
     */
    public Contact(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    /**
     * @return current Contact ID as int
     */
    public int getId() {
        return id;
    }

    /**
     * @param id Contact ID to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return current Contact Name as String
     */
    public String getName() {
        return name;
    }

    /**
     * @param name Contact name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return current Contact email as String
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email Contact email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Taking Contact names and formatting them for display
     * in ComboBoxes
     * @return formatted string for display
     */
    @Override
    public String toString(){
        return (getName() + " [" + getId() + "]");
    }
}
