/**
 * @author Guilherme Lira - 8210415
 * @author Miguel Cunha - 8210426
 */

package entities;

import Queue.Queue;
import exceptions.InvalidTypeException;
import exceptions.NullException;
import graph.GraphMatrix;
import interfaces.*;
import orderedUnorderedList.ArrayOrderedList;
import orderedUnorderedList.ArrayUnorderedList;


import java.util.Arrays;
import java.util.Iterator;

public class MissionImpl implements Mission {

    private String cod;

    private Integer version;

    private Target target;

    private ArrayUnorderedList<Division> exitEntry;

    private GraphMatrix<Division> building;


    private ArrayOrderedList<SimulationManualImpl> simulation;

    ArrayUnorderedList<Enemy> deadEnemies = new ArrayUnorderedList<Enemy>();


    public MissionImpl() {
    }

    public MissionImpl(String cod, Integer version, Target target) {
        this.cod = cod;
        this.version = version;
        this.target = target;
    }

    public MissionImpl(String cod, Integer version, Target target, ArrayUnorderedList<Division> exitEntry, GraphMatrix<Division> building) {
        this.cod = cod;
        this.version = version;
        this.target = target;
        this.exitEntry = exitEntry;
        this.building = building;
        this.simulation = new ArrayOrderedList<>();
    }

    public ArrayUnorderedList<Division> getExitEntry() {
        return this.exitEntry;
    }

    public GraphMatrix<Division> getBuilding() {
        return building;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    public void setTarget(Division divisionImpl, String type) {
        this.target = new TargetImpl(divisionImpl, type);
    }

    public ArrayOrderedList<SimulationManualImpl> getSimulation() {
        return simulation;
    }

    public void addSimulation(SimulationManualImpl simulation) throws NullException {
        if (simulation == null) {
            throw new NullException("");
        }

        this.simulation.add(simulation);
    }

    public boolean isValid() {
        return (this.cod != null && !this.cod.isBlank() && this.version > 0 && this.version != null && this.target.isValid());
    }

    public DivisionImpl getTargetDivision() {
        Iterator<DivisionImpl> targetDivision = this.building.iteratorBFS(this.building.getFirst());
        DivisionImpl current = null;

        while (targetDivision.hasNext()) {
            current = targetDivision.next();
            if (current.getName().equals(this.target.getDivision())) {
                return current;
            }
        }

        return current;
    }

    public Division getDivisionExitEntry(String div) {
        Iterator<Division> divisionExitEntry = this.exitEntry.iterator();
        Division current = null;

        while (divisionExitEntry.hasNext()) {
            current = divisionExitEntry.next();
            if (current.getName().compareTo(div) == 0) {
                return current;
            }
        }

        return null;
    }

    public DivisionImpl getDivision(String div) {
        Iterator<DivisionImpl> division = this.building.iteratorBFS(this.building.getFirst());
        DivisionImpl current = null;

        while (division.hasNext()) {
            current = division.next();
            if (current.getName().equals(div)) {
                return current;
            }
        }
        return null;
    }

    public ArrayUnorderedList<Division> getDivisions() {
        Iterator<Division> divisionIterator = this.building.iteratorBFS(this.building.getFirst());
        ArrayUnorderedList<Division> divisions = new ArrayUnorderedList<>();

        while (divisionIterator.hasNext()) {
            divisions.addToRear(divisionIterator.next());
        }

        return divisions;
    }

    public ArrayUnorderedList<Enemy> getAllEnemies() {
        ArrayUnorderedList<Enemy> allEnemies = new ArrayUnorderedList<>();

        ArrayUnorderedList<Division> divisions = getDivisions();

        for (Division division : divisions) {
            Enemy[] enemies =  division.getEnemies();
            for (Enemy enemy : enemies) {
                if(enemy!=null) {
                    if (enemy.isValid()) {
                        if (enemy.getLifePoints() == 100) {
                            allEnemies.addToRear(enemy);
                        }
                    }
                }
            }
        }

        return allEnemies;
    }

    public ArrayUnorderedList<Item> getAllItems() {
        ArrayUnorderedList<Item> allItems = new ArrayUnorderedList<>();

        ArrayUnorderedList<Division> divisions = getDivisions();

        for (Division division : divisions) {
            Item items = division.getItem();
            if (items != null) {
                allItems.addToRear(items);
            }
        }
        return allItems;
    }

    /**
     * Gets the list of all enemies that were killed.
     *
     * @return the list of all enemies that were killed.
     */
    public ArrayUnorderedList<Enemy> getDeadEnemies() {
        return deadEnemies;
    }

    /**
     * Sets the list of dead enemies for the mission.
     *
     * @param deadEnemies the new list of enemies that have been killed in the mission.
     */
    public void setDeadEnemies(ArrayUnorderedList<Enemy> deadEnemies) {
        this.deadEnemies = deadEnemies;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("*-*-*-*-*-*-*--*-*-*-*--*-*-*-*--*-*-*--*-*-*--*-*-*-*--*-*-*-*-*-*-*\n");
        s.append("  Missão: \n");
        s.append("\tCodigo-Missão: ").append(this.getCod()).append("\n");
        s.append("\tVersão: ").append(this.getVersion()).append("\n");
        s.append("\tAlvo:\n");
        s.append("\t\tDivisão: ").append(this.getTarget().getDivision()).append("\n");
        s.append("\t\tTipo: ").append(this.getTarget().getType()).append("\n");

        int tam = this.getBuilding().size();
        if (this.getBuilding().isEmpty()) {
            s.append("\nGrafo vazio.\n");
            return s.toString();
        }

        s.append("\nValor\t\t\t\tIndex\t\t");
        for (int i = 0; i < tam; i++) {
            if (i < 10) {
                s.append("   |" + i + "|   ");
            } else {
                s.append("  |" + i + "|   ");
            }
        }
        s.append("\n\n");

        Iterator<Division> itBuilding = this.getBuilding().iteratorBFS(this.getBuilding().getFirst());

        int k = 0;
        boolean[][] matrix = this.getBuilding().getEdge();

        while (itBuilding.hasNext()) {
            Division currentDiv = itBuilding.next();
            s.append(String.format("%-20s     %-7s", currentDiv.getName(), k));
            for (int j = 0; j < tam; j++) {
                String resultado;
                if (matrix[k][j]) {
                    resultado = " | YES | ";
                } else {
                    resultado = " |  NO | ";
                }
                s.append(String.format("%2s", resultado));
            }
            s.append("\n");
            k++;
        }

        return s.toString();
    }


    /**
     * Enemies attack the player. The player's life points are decreased by the
     * power of the enemy.
     *
     * @param enemy the enemy that is attacking the player.
     * @param lifePoints the current life points of the player.
     */
    public void enemiesAttack(Enemy enemy, double lifePoints) {
        lifePoints -= enemy.getPower();
    }

    /**
     * Moves enemies to new divisions if possible, and makes them attack the player if they
     * encounter them in the target division. Enemies are moved randomly within 2
     * divisions from their initial position, and any enemy moved to the target division
     * attacks the player, reducing the player's life points.
     *
     * @param enemies the list of enemies to be moved.
     * @param currentDivTo the current division the player is in.
     * @param lifePoints the current life points of the player, which will be decreased
     *                   if an enemy attacks.
     * @param isManuel a flag indicating if the simulation is manual, to print enemy encounter
     *                 messages.
     * @return a list of enemies that have moved to the target division.
     * @throws NullException if any operation involving a null object occurs.
     * @throws InvalidTypeException if an invalid operation is performed on the types involved.
     */
    public ArrayUnorderedList<Enemy> moveEnemies(ArrayUnorderedList<Enemy> enemies, Division currentDivTo, double lifePoints, boolean isManuel) throws NullException, InvalidTypeException {
        ArrayUnorderedList<Enemy> enemiesEncountered = new ArrayUnorderedList<>();
        for (Enemy enemy :  enemies) {
            Division currentDivision = getDivision(enemy.getCurrentDivision().getName());
            Division[] reachableDivisions = getReachableDivisions(currentDivision.getName(), 2, enemy);
            if (currentDivision != currentDivTo) {
                if (reachableDivisions != null && reachableDivisions.length > 0) {
                    String newDivisionString = getRandomDivision(reachableDivisions, enemy);
                    for (int i = 0; i < reachableDivisions.length; i++) {
                        if (reachableDivisions[i].getName().equals(newDivisionString)) {
                            if (getDivision(currentDivision.getName()).removeEnemy(enemy)) {
                                enemy.setCurrentDivision(reachableDivisions[i]);
                                getDivision(reachableDivisions[i].getName()).addEnemy(enemy);
                                if (reachableDivisions[i].equals(currentDivTo)) {
                                    enemiesEncountered.addToRear(enemy);
                                    enemiesAttack(enemy, lifePoints);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (!enemiesEncountered.isEmpty() && isManuel) {
            for (Enemy enemy : enemiesEncountered) {
                System.out.println("O inimigo " + enemy.getName() + " entrou na sua divisão!");
                System.out.println("Inimigo " + enemy.getName() + " atacou! Dano: " + enemy.getPower());
            }
        }
        return enemiesEncountered;
    }

    /**
     * Finds all reachable divisions from the given initial division, up to the
     * given maximum depth, that the given enemy can reach.
     *
     * @param initialDivisionName the name of the initial division.
     * @param maxDepth the maximum depth to search for reachable divisions.
     * @param enemy the enemy whose reachable divisions are to be found.
     * @return an array of all reachable divisions that the enemy can reach,
     *         starting from the given initial division, up to the given
     *         maximum depth. The array does not include the initial division.
     */
    public Division[] getReachableDivisions(String initialDivisionName, int maxDepth, Enemy enemy) {
        Division[] reachableDivisions = new Division[20];
        int count = 1;

        Division initialDivision = getDivision(enemy.getDivision().getName());
        Queue<Division> queue = new Queue<Division>();
        Queue<Integer> depths = new Queue<Integer>();
        reachableDivisions[0] = initialDivision;
        queue.enqueue(initialDivision);
        depths.enqueue(0);

        while (!queue.isEmpty()) {
            Division current = queue.dequeue();
            int depth = depths.dequeue();

            if (!current.equals(initialDivision) && count < reachableDivisions.length) {
                reachableDivisions[count++] = current;
            }

            if (depth < maxDepth) {
                ArrayUnorderedList<String> edges = current.getEdges();
                for (String edge : edges) {
                    Division neighbor = getDivision(edge);
                    queue.enqueue(neighbor);
                    depths.enqueue(depth + 1);
                }
            }
        }

        return Arrays.copyOf(reachableDivisions, count);
    }

    /**
     * Gets a random division that the given enemy can reach, from the given
     * list of reachable divisions.
     *
     * @param reachableDivisions the list of reachable divisions.
     * @param enemy the enemy whose reachable divisions are to be used.
     * @return a random division that the enemy can reach, chosen from the given
     *         list of reachable divisions.
     */
    public String getRandomDivision(Division[] reachableDivisions, Enemy enemy) {
        ArrayUnorderedList<String> currentDivisionEdges = enemy.getCurrentDivision().getEdges();
        ArrayUnorderedList<String> validDivisions = new ArrayUnorderedList<>();
        Iterator<String> iterator;
        int count = 0;
        for (String edge : currentDivisionEdges) {
            for (int i = 0; i < reachableDivisions.length; i++) {
                if (reachableDivisions[i].getName().equals(edge)) {
                    validDivisions.addToRear(edge);
                }
            }
        }

        int randomIndex = (int) (Math.random() * validDivisions.size());
        iterator = validDivisions.iterator();
        String next = null;
        while (iterator.hasNext() && count <= randomIndex) {
            next = iterator.next();
            count++;
        }
        return next;
    }

}