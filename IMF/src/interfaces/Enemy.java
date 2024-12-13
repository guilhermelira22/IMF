/**
 * @author Guilherme Lira - 8210415
 * @author Miguel Cunha - 8210426
 */

package interfaces;

/**
 * Interface that represents an Enemy in the game.
 */
public interface Enemy {

    /**
     * Returns the name of the enemy.
     *
     * @return the name of the enemy.
     */
    String getName();

    /**
     * Sets the name of the enemy.
     *
     * @param name the new name of the enemy.
     */
    void setName(String name);

    /**
     * Returns the current division of the enemy.
     *
     * @return the current division of the enemy.
     */
    Division getCurrentDivision();

    /**
     * Sets the current division of the enemy.
     *
     * @param currentDivision the new current division of the enemy.
     */
    void setCurrentDivision(Division currentDivision);

    /**
     * Returns the power of the enemy.
     *
     * @return the power of the enemy.
     */
    Double getPower();

    /**
     * Sets the power of the enemy.
     *
     * @param power the new power of the enemy.
     */
    void setPower(Double power);

    /**
     * Returns the initial division of the enemy.
     *
     * @return the initial division of the enemy.
     */
    Division getDivision();

    /**
     * Sets the initial division of the enemy.
     *
     * @param divisionImpl the new initial division of the enemy.
     */
    void setDivision(Division divisionImpl);

    /**
     * Returns the life points of the enemy.
     *
     * @return the life points of the enemy.
     */
    Double getLifePoints();

    /**
     * Sets the life points of the enemy.
     *
     * @param lifePoints the new life points of the enemy.
     */
    void setLifePoints(Double lifePoints);

    /**
     * Checks if the enemy is valid.
     *
     * @return true if the enemy is valid, false otherwise
     */
    boolean isValid();

    /**
     * Checks if all the enemies in the array are valid.
     *
     * @param enemies the array of enemies to check.
     * @return true if all the enemies are valid, false otherwise
     */
    static boolean isValid(Enemy[] enemies) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns a string representation of the enemy.
     *
     * @return a string representation of the enemy.
     */
    String toString();
}
