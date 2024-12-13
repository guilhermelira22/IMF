/**
 * @author Guilherme Lira - 8210415
 * @author Miguel Cunha - 8210426
 */

package simulation;

import Queue.Queue;
import entities.DivisionImpl;
import enums.Item_Type;
import exceptions.InvalidTypeException;
import exceptions.NullException;
import interfaces.Division;
import interfaces.Enemy;
import interfaces.Item;
import interfaces.Mission;
import orderedUnorderedList.ArrayUnorderedList;
import stack.LinkedStack;

import java.util.Arrays;
import java.util.Iterator;

public class SimulationExecute {

    private static final Double LIFE_DEFAULT = 100.0;
    private static final int POWER = 40;
    private static final String STRING_AUX = "\n*-*-*-*-*-*-*-*-*\n";

    private Queue<Division> path;
    private Queue<Division> invertedPath;
    private Mission mission;
    private double lifePoints;
    private boolean flagLeft;
    private boolean flagTarget;
    private Division currentDiv;
    private LinkedStack<Item> backpack;

    public SimulationExecute(Mission mission) {
        this.mission = mission;
        this.lifePoints = LIFE_DEFAULT;
        this.flagLeft = false;
        this.flagTarget = false;
        this.currentDiv = new DivisionImpl();
        this.path = new Queue<Division>();
        this.backpack = new LinkedStack<>();
    }

    protected void attackAllEnemies() {
        Enemy[] enemies = currentDiv.getEnemies();
        int j = enemies.length;
        for (int i = 0; i < j; i++) {
            enemies[i].setLifePoints(enemies[i].getLifePoints() - POWER);
            if (enemies[i].getLifePoints() <= 0) {
                System.out.println("Matou o inimigo: " + enemies[i].getName());
                currentDiv.removeEnemy(enemies[i]);
                i--;
                j--;
            } else {
                System.out.println("Atacou o inimigo " + enemies[i].getName() + "! Pontos de vida do inimigo: " + enemies[i].getLifePoints());
            }
        }
    }

    public void enemiesAttack(Enemy enemy) {
        lifePoints -= enemy.getPower();
        System.out.println("Inimigo " + enemy.getName() + " atacou! Dano: " + enemy.getPower());
    }

    private boolean enemiesRemaining() {
        Enemy[] enemies = currentDiv.getEnemies();
        for (int i = 0; i < enemies.length; i++) {
            if (enemies[i].getLifePoints() > 0) {
                return true;
            }
        }
        return false;
    }

    protected void moveEnemies() throws NullException, InvalidTypeException {
        for (Enemy enemy :  mission.getAllEnemies()) {
            Division currentDivision = mission.getDivision(enemy.getCurrentDivision().getName());
            Division[] reachableDivisions = getReachableDivisions(currentDivision.getName(), 2, enemy);
            if (reachableDivisions != null && reachableDivisions.length > 0) {
                String newDivisionString = getRandomDivision(reachableDivisions, enemy);
                for (int i = 0; i < reachableDivisions.length; i++) {
                    if (reachableDivisions[i].getName().equals(newDivisionString)) {
                        if (mission.getDivision(currentDivision.getName()).removeEnemy(enemy)) {
                            enemy.setCurrentDivision(reachableDivisions[i]);
                            mission.getDivision(reachableDivisions[i].getName()).addEnemy(enemy);
                            if (reachableDivisions[i].equals(currentDiv)) {
                                System.out.println("Um inimigo entrou na divisão! ");
                                enemiesAttack(enemy);
                                System.out.println("Pontos de Vida: " + lifePoints);
                            }
                        }
                    }
                }
            }
        }
    }

    protected Division[] getReachableDivisions(String initialDivisionName, int maxDepth, Enemy enemy) {
        Division[] reachableDivisions = new Division[20];
        int count = 1;

        Division initialDivision = mission.getDivision(enemy.getDivision().getName());
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
                    Division neighbor = mission.getDivision(edge);
                    queue.enqueue(neighbor);
                    depths.enqueue(depth + 1);
                }
            }
        }

        return Arrays.copyOf(reachableDivisions, count);
    }

    protected String getRandomDivision(Division[] reachableDivisions, Enemy enemy) {
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

    protected String getFinalPath() {
        StringBuilder finalPath = new StringBuilder("Caminho percorrido:\n");
        Queue<Division> tempPath = new Queue<Division>();
        while (!path.isEmpty()) {
            Division division = path.dequeue();
            tempPath.enqueue(division);
            finalPath.append("-> " + division.getName() + "\n");
        }

        this.path = tempPath;
        return finalPath.toString();
    }

    private void useMedicalKit() throws NullException, InvalidTypeException {
        if (backpack.isEmpty()) {
            System.out.println("Nenhum kit médico na mochila.");
        } else {
            Item medicalKit = backpack.pop();
            if (lifePoints != 100) {
                lifePoints += medicalKit.getAmount();
                if (lifePoints > 100) {
                    lifePoints = 100;
                }
                System.out.println("Usou kit médico! Pontos de vida: " + lifePoints);
            } else {
                System.out.println("Já está com 100 pontos de vida.");
            }
            moveEnemies();
        }
    }

    private boolean addMedicakKit() {
        if (currentDiv.getItem() != null) {
            if (currentDiv.getItem().getItemType() == Item_Type.KIT) {
                backpack.push(currentDiv.getItem());
                return true;
            }
        }
        return false;
    }

}
