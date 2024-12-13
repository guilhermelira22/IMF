/**
 * @author Guilherme Lira - 8210415
 * @author Miguel Cunha - 8210426
 */

package interfaces;

import exceptions.InvalidTypeException;
import exceptions.NullException;
import orderedUnorderedList.ArrayUnorderedList;

/**
 * Interface to represent a Division in the game.
 */
public interface Division {


    /**
     * Returns the name of the division.
     *
     * @return the name of the division.
     */
    String getName();

    /**
     * Sets the name of the division.
     *
     * @param name the new name of the division.
     */
    void setName(String name);

    /**
     * Removes the given enemy from the division.
     *
     * @param enemy the enemy to be removed.
     * @return true if the enemy was successfully removed, false otherwise.
     */
    boolean removeEnemy(Enemy enemy);

    /**
     * Verifies if the division is an entry or exit of the building.
     *
     * @return true if the division is an entry or exit, false otherwise.
     */
    boolean isEntryExit();

    /**
     * Changes the division to an entry or exit.
     */
    void setEntryExit();

    /**
     * Returns the array of enemies in the division.
     *
     * @return the array of enemies in the division.
     */
    Enemy[] getEnemies();

    /**
     * Returns the total power of all enemies present in the division.
     *
     * @return the sum of the power values of all enemies in the division.
     */
    Double getEnemiesPower();

    /**
     * Changes the array of enemies in the division.
     *
     * @param enemies the new array of enemies.
     */
    void setEnemies(Enemy[] enemies);

    /**
     * Returns the number of enemies present in the division.
     *
     * @return the number of enemies in the division.
     */
    Integer sizeEnemies();

    /**
     * Adds a new enemy to the division.
     *
     * @param newEnemy the new enemy to be added to the division.
     * @throws NullException if the newEnemy is null.
     * @throws InvalidTypeException if the newEnemy is already present in the division.
     */
    void addEnemy(Enemy newEnemy) throws NullException, InvalidTypeException;

    /**
     * Returns an unordered list of strings representing the connections
     * that the division has to other divisions.
     *
     * @return an unordered list of strings representing the connections.
     */
    ArrayUnorderedList<String> getEdges();

    /**
     * Sets the item that is present in the division.
     *
     * @param item the new item to be added to the division.
     */
    void setItem(Item item);

    /**
     * Returns the item that is present in the division.
     *
     * @return the item present in the division.
     */
    Item getItem();

    /**
     * Verifies if the division is valid.
     *
     * @return true if the division is valid, false otherwise.
     */
    boolean isValid();

}
