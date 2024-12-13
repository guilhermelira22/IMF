/**
 * @author Guilherme Lira - 8210415
 * @author Miguel Cunha - 8210426
 */

package interfaces;

/**
 * Interface representing Tó Cruz.
 */
public interface To {

    /**
     * Returns the life points of Tó Cruz.
     *
     * @return Tó Cruz's life points as a Double.
     */
    Double getLifePoints();

    /**
     * Sets the life points of Tó Cruz.
     *
     * @param lifePoints the new life points of Tó Cruz as a Double.
     */
    void setLifePoints(Double lifePoints);

    /**
     * Reduces the life points of Tó Cruz by a specified amount.
     *
     * @param lifePoints the amount to reduce Tó Cruz's life points by as a Double.
     */
    void reduceLifePoints(Double lifePoints);

    /**
     * Adds a specified amount of life points to Tó Cruz.
     *
     * @param lifePoints the amount of life points to add to Tó Cruz as a Double.
     */
    void addLifePoints(Double lifePoints);

    /**
     * Retrieves the division where Tó Cruz is currently located.
     *
     * @return the current division as a Division object.
     */
    Division getDivision();

    /**
     * Sets the division where Tó Cruz is currently located.
     *
     * @param division the new division as a Division object.
     */
    void setDivision(Division division);

    /**
     * Sees the item that is currently on top of the backpack.
     *
     * @return the item that is on top of the backpack.
     */
    Item getItem();

    /**
     * Attempts to add a medical kit from the current division to Tó Cruz's backpack.
     *
     * @return true if a medical kit was successfully added to the backpack, false otherwise.
     */
    boolean addMedicakKit();

    /**
     * Attempts to add a vest from the current division to Tó Cruz's inventory.
     *
     * @return true if a vest was successfully added to the inventory, false otherwise.
     */
    public boolean addVest();



}
