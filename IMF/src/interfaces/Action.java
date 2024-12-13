/**
 * @author Guilherme Lira - 8210415
 * @author Miguel Cunha - 8210426
 */

package interfaces;

import enums.Action_Type;

/**
 * Interface to represent an Action in the game.
 */
public interface Action {

    /**
     * Returns the type of action.
     *
     * @return Action_Type type of action
     */
    public Action_Type getType();

    /**
     * Sets the type of the action.
     *
     * @param type new type of action
     */
    public void setType(Action_Type type);

    /**
     * Gets the division.
     *
     * @return Division division of the action
     */
    public Division getDivision();

    /**
     * Sets the division.
     *
     * @param division new division
     */
    public void setDivision(Division division);

    /**
     * Determines if the action is related to an enemy.
     *
     * @return true if the action involves an enemy, false otherwise
     */
    public boolean isEnemy();
}
