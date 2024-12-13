/**
 * @author Guilherme Lira - 8210415
 * @author Miguel Cunha - 8210426
 */

package interfaces;

import enums.Item_Type;

/**
 * Interface to represent an Item in the game.
 */
public interface Item {

    /**
     * Retrieves the amount of points associated with this item.
     *
     * @return the amount as a double value
     */
    double getAmount();

    /**
     * Sets the amount of points associated with this item.
     *
     * @param amount the amount as a double value
     */
    void setAmount(double amount);

    /**
     * Retrieves the type of the item.
     *
     * @return the type as an {@link Item_Type} object
     */
    Item_Type getItemType();

    /**
     * Sets the type of the item.
     *
     * @param type the new type of the item as an {@link Item_Type} object
     */
    void setItemType(Item_Type type);

    /**
     * Retrieves the division where this item is located.
     *
     * @return the division where the item is located as a {@link Division} object
     */
    Division getDivision();

    /**
     * Sets the division where this item is located.
     *
     * @param divisionImpl the new division as a {@link Division} object
     */
    void setDivision(Division divisionImpl);

    /**
     * Checks if the item is valid.
     *
     * @return true if the item is valid, false otherwise
     */
    boolean isValid();

    /**
     * Retrieves a string representation of the item.
     *
     * @return a string representing the item
     */
    String toString();
}
