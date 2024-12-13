/**
 * @author Guilherme Lira - 8210415
 * @author Miguel Cunha - 8210426
 */

package entities;

import exceptions.InvalidTypeException;
import exceptions.NullException;
import interfaces.Division;
import interfaces.Enemy;
import interfaces.Item;
import orderedUnorderedList.ArrayUnorderedList;

public class DivisionImpl implements Division {

    private static final int DEFAULT_CAPACITY = 1;

    private String name;

    private boolean entryExit;

    private Enemy[] enemies;

    private Integer numEnemies;

    private ArrayUnorderedList<String> edges;

    private Item item;

    public DivisionImpl() {
    }

    public DivisionImpl(String name) {
        this.name = name;
        this.entryExit = false;
        this.enemies = new EnemyImpl[DEFAULT_CAPACITY];
        this.numEnemies = 0;
        this.edges = new ArrayUnorderedList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEntryExit() {
        return entryExit;
    }

    public void setEntryExit() {
        this.entryExit = !this.entryExit;
    }

    public Enemy[] getEnemies() {
        return enemies;
    }

    public Double getEnemiesPower() {
        Double power = 0.0;
        for (int i = 0; i < this.numEnemies; i++) {
            power += this.getEnemies()[i].getPower();
        }
        return power;
    }

    public void setEnemies(Enemy[] enemies) {
        this.enemies = enemies;
        this.numEnemies = enemies.length;
    }

    public Integer sizeEnemies() {
        return numEnemies;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    public ArrayUnorderedList<String> getEdges() {
        return edges;
    }

    public void addEnemy(Enemy newEnemy) throws NullException, InvalidTypeException {
        if (newEnemy == null) {
            throw new NullException("");
        }
        if (numEnemies == enemies.length) {
            expandCapacity();
        }
        if (findEnemy(newEnemy.getName()) != -1) {
            throw new InvalidTypeException("");
        }
        enemies[numEnemies] = newEnemy;
        numEnemies++;

    }

    public boolean removeEnemy(Enemy enemy) {
        int index = -1;
        for (int i = 0; i < numEnemies; i++) {
            if (enemies[i].equals(enemy)) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            return false;
        }

        for (int i = index; i < numEnemies - 1; i++) {
            enemies[i] = enemies[i + 1];
        }

        enemies[numEnemies - 1] = null;
        numEnemies--;

        reduceEnemies();

        return true;
    }

    private void reduceEnemies() {
        int validCount = 0;
        for (int i = 0; i < numEnemies; i++) {
            if (enemies[i] != null) {
                validCount++;
            }
        }

        Enemy[] newEnemies = new Enemy[validCount];
        int index = 0;

        for (int i = 0; i < numEnemies; i++) {
            if (enemies[i] != null) {
                newEnemies[index++] = enemies[i];
            }
        }

        enemies = newEnemies;
    }

    private int findEnemy(String name) {
        int send = -1;
        for (int i = 0; i < this.numEnemies; i++) {
            if (name.equals(enemies[i])) {
                send = i;
                break;
            }
        }
        return send;
    }

    /**
     *Expande a capacidade do array de inimigos da divisão.
     */
    protected void expandCapacity() {
        EnemyImpl[] newEnemies = new EnemyImpl[enemies.length + DEFAULT_CAPACITY];
        System.arraycopy(enemies, 0, newEnemies, 0, numEnemies);
        enemies = newEnemies;
    }

    public boolean isValid() {
        if (this.numEnemies > 0) {
            return (this.name != null && !this.name.isBlank() && Enemy.isValid(this.enemies));
        }
        return (this.name != null && !this.name.isBlank());
    }

    public static DivisionImpl getDivision(DivisionImpl[] div, String division) {

        for (int i = 0; i < div.length; i++) {
            if (div[i].getName().compareTo(division) == 0) {
                return div[i];
            }
        }
        return null;
    }

    @Override
    public String toString() {
        String enemiesString = "[";
        for (int i = 0; i < numEnemies; i++) {
            enemiesString += "{nome: " + enemies[i].getName() + ", poder: " + enemies[i].getPower() + "}";
            if (i < numEnemies - 1) {
                enemiesString += ", ";
            }
        }
        enemiesString += "]";
        return "Divisão{" + "nome: " + name + ", SaidaOuEntrada: " + entryExit + ", Inimigos: " + enemiesString + ", numInimigos: " + numEnemies + ", ligações: " + edges.toString() + "item: "+ (item != null ? item.toString() : "null") +'}';
    }
}
