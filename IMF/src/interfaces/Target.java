package interfaces;

public interface Target {

    /**
     * Returns the target division.
     *
     * @return Division representing the target division
     */
    Division getDivision();

    /**
     * Returns the type of the target.
     *
     * @return String representing the target type
     */
    String getType();

    /**
     * Sets the type of the target.
     *
     * @param type new target type
     */
    void setType(String type);

    /**
     * Checks if the target is valid.
     *
     * @return true if the target is valid, false otherwise
     *
     * Validity criteria:
     * - Division must not be null
     * - Division name must not be blank
     * - Type must not be null
     * - Type must not be blank
     */
    boolean isValid();

}
