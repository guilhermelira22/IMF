package interfaces;

import entities.SimulationManualImpl;
import exceptions.NullException;
import graph.GraphMatrix;
import orderedUnorderedList.ArrayOrderedList;
import orderedUnorderedList.ArrayUnorderedList;

public interface Mission {

    /**
     * Returns the mission code.
     *
     * @return mission code as a String
     */
    String getCod();

    /**
     * Sets the mission code.
     *
     * @param cod new mission code
     */
    void setCod(String cod);

    /**
     * Returns the mission version.
     *
     * @return mission version as an Integer
     */
    Integer getVersion();

    /**
     * Sets the mission version.
     *
     * @param version new mission version
     */
    void setVersion(Integer version);

    /**
     * Returns the mission target.
     *
     * @return Target object representing the mission's target
     */
    Target getTarget();

    /**
     * Sets the mission target.
     *
     * @param target new Target object
     */
    void setTarget(Target target);

    /**
     * Sets the mission target with specific division and type.
     *
     * @param division target division
     * @param type target type
     */
    void setTarget(Division division, String type);

    /**
     * Returns the list of exit and entry divisions.
     *
     * @return ArrayUnorderedList of DivisionImpl representing exits and entries
     */
    ArrayUnorderedList<Division> getExitEntry();

    /**
     * Returns the building graph representing the mission's structure.
     *
     * @return GraphMatrix of DivisionImpl representing the building
     */
    GraphMatrix<Division> getBuilding();

    /**
     * Returns the list of manual simulations.
     *
     * @return ArrayOrderedList of SimulationManual
     */
    ArrayOrderedList<SimulationManualImpl> getSimulation();

    /**
     * Adds a manual simulation to the mission.
     *
     * @param simulation SimulationManual to add
     * @throws NullException if simulation is null
     */
    void addSimulation(SimulationManualImpl simulation) throws NullException;

    /**
     * Checks if the mission is valid.
     *
     * @return true if the mission is valid, false otherwise
     */
    boolean isValid();

    /**
     * Retrieves the target division.
     *
     * @return DivisionImpl representing the target division
     */
    Division getTargetDivision();

    /**
     * Finds a specific division in the exit/entry list.
     *
     * @param div name of the division to find
     * @return DivisionImpl if found, null otherwise
     */
    Division getDivisionExitEntry(String div);

    /**
     * Finds a specific division in the building graph.
     *
     * @param div name of the division to find
     * @return DivisionImpl if found, null otherwise
     */
    Division getDivision(String div);

    /**
     * Retrieves all divisions in the mission.
     *
     * @return ArrayUnorderedList of Division
     */
    ArrayUnorderedList<Division> getDivisions();

    /**
     * Retrieves all enemies in the mission.
     *
     * @return ArrayUnorderedList of Enemy
     */
    ArrayUnorderedList<Enemy> getAllEnemies();

    /**
     * Retrieves all items in the mission.
     *
     * @return ArrayUnorderedList of Item
     */
    ArrayUnorderedList<Item> getAllItems();
}
