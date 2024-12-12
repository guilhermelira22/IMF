package interfaces;

import Queue.Queue;

public interface SimulationManual {

    /**
            * Returns the life points at the end of the simulation.
            *
            * @return number of life points as a Double
     */
    Double getLifePoints();

    /**
     * Sets the life points for the simulation.
     *
     * @param lifePoints new number of life points
     */
    void setLifePoints(Double lifePoints);

    /**
     * Returns the path taken during the simulation.
     *
     * @return Queue of DivisionImpl representing the path
     */
    Queue<Division> getPath();

    /**
     * Sets the path for the simulation.
     *
     * @param path Queue of DivisionImpl representing the new path
     */
    void setPath(Queue<Division> path);

    /**
     * Returns the number of divisions traversed during the simulation.
     *
     * @return number of divisions as an Integer
     */
    Integer getNumDivisions();

    /**
     * Checks if the target was retrieved during the simulation.
     *
     * @return true if the target was gotten, false otherwise
     */
    boolean isGetTarget();

    /**
     * Sets whether the target was retrieved during the simulation.
     *
     * @param getTarget boolean indicating target retrieval status
     */
    void setGetTarget(boolean getTarget);

    /**
     * Compares this simulation manual with another object.
     * Primarily compares life points, and if equal, compares number of divisions.
     *
     * @param obj the object to compare with
     * @return a negative integer, zero, or a positive integer as this object
     *         is less than, equal to, or greater than the specified object
     */
    int compareTo(Object obj);
}
