/**
 * @author Guilherme Lira - 8210415
 * @author Miguel Cunha - 8210426
 */

package interfaces;

/**
 * Interface to represent a Target in the game.
 */
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
     */
    boolean isValid();

}
