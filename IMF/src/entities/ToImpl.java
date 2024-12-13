/**
 * @author Guilherme Lira - 8210415
 * @author Miguel Cunha - 8210426
 */

package entities;

import enums.Item_Type;
import graph.GraphMatrix;
import interfaces.*;
import stack.LinkedStack;

import java.util.Iterator;

public class ToImpl implements To {

    private Double power;
    private Division division;
    private Double lifePoints;
    private LinkedStack<Item> backpack;


    public ToImpl(Double power) {
        super();
        this.power = power;
        this.lifePoints = 100.0;
        this.backpack = new LinkedStack<>();
    }

    @Override
    public Double getLifePoints() {
        return this.lifePoints;
    }

    @Override
    public void setLifePoints(Double lifePoints) {
        this.lifePoints = lifePoints;
    }

    @Override
    public void addLifePoints(Double lifePoints) {
        this.lifePoints += lifePoints;
    }

    @Override
    public void reduceLifePoints(Double lifePoints) {
        this.lifePoints -= lifePoints;
    }

    @Override
    public Division getDivision() {
        return this.division;
    }

    @Override
    public void setDivision(Division division) {
        this.division = division;
    }

    @Override
    public Item getItem() {
        if(this.backpack.isEmpty()) {
            return null;
        }
        return this.backpack.peek();
    }

    /**
     * Removes the item from the top of the backpack and adds its value to To
     * Cruz's life points.
     */
    public void useItem() {
        if(!this.backpack.isEmpty()) {
            addLifePoints(backpack.pop().getAmount());
        }
    }

    @Override
    public boolean addMedicakKit() {
        if (this.division.getItem() != null) {
            if (this.division.getItem().getItemType() == Item_Type.KIT) {
                backpack.push(this.division.getItem());
                division.setItem(null);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean addVest() {
        if (this.division.getItem() != null) {
            if (this.division.getItem().getItemType() == Item_Type.VEST) {
                this.lifePoints += this.division.getItem().getAmount();
                division.setItem(null);
                return true;
            }
        }
        return false;
    }

    /**
     * Finds the shortest path from the current division to the closest division containing a medical kit.
     *
     * @param mission the mission object containing the building graph and items
     * @return an iterator over the divisions representing the shortest path to the closest medical kit,
     *         or null if no path is found
     */
    public Iterator<Division> getBestPathToClosestKit(Mission mission) {
        GraphMatrix<Division> graph = mission.getBuilding();

        int shortestDistance = Integer.MAX_VALUE;
        Iterator<Division> pathIterator = null;

        for (Item kit : mission.getAllItems()) {
            if (kit.getDivision() != null) {
                Division kitDivision = kit.getDivision();
                pathIterator = graph.iteratorShortestPath(this.division, kitDivision);
                Iterator<Division> pathIteratorCount = graph.iteratorShortestPath(this.division, kitDivision);
                int pathLength = calculatePathLength(pathIteratorCount);

                if (pathLength < shortestDistance) {
                    shortestDistance = pathLength;
                }
            }
        }

        return pathIterator;
    }

    /**
     * Finds the shortest path from the current division to the closest exit.
     *
     * @param mission the mission object containing the building graph and items
     * @return an iterator over the divisions representing the shortest path to the closest exit,
     *         or null if no path is found
     */
    public Iterator<Division> getBestPathToClosestExit(Mission mission) {
        GraphMatrix<Division> graph = mission.getBuilding();

        int shortestDistance = Integer.MAX_VALUE;
        Iterator<Division> pathIterator = null;

        for (Division div : mission.getDivisions()) {
            if (div.isEntryExit()) {
                pathIterator = graph.iteratorShortestPath(this.division, div);
                Iterator<Division> pathIteratorCount = graph.iteratorShortestPath(this.division, div);
                int pathLength = calculatePathLength(pathIteratorCount);

                if (pathLength < shortestDistance) {
                    shortestDistance = pathLength;
                }
            }
        }

        return pathIterator;
    }

/**
 * Calculates the number of divisions in a given path.
 *
 * @param pathIterator an iterator over the divisions representing the path
 * @return the length of the path as an integer, representing the number of divisions
 */
    public int calculatePathLength(Iterator<Division> pathIterator) {
        int length = 0;
        Division previous = null;

        while (pathIterator.hasNext()) {
            Division current = pathIterator.next();
            if (previous != null) {
                length++;
            }
            previous = current;
        }

        return length;
    }
}
