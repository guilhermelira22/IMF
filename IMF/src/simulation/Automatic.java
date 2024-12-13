/**
 * @author Guilherme Lira - 8210415
 * @author Miguel Cunha - 8210426
 */

package simulation;

import Queue.Queue;
import entities.ActionImpl;
import entities.MissionImpl;
import entities.ToImpl;
import enums.Action_Type;
import exceptions.InvalidTypeException;
import exceptions.NullException;
import heap.PriorityQueue;
import interfaces.*;
import orderedUnorderedList.ArrayUnorderedList;

import java.util.Iterator;

/**
 * Automatic simulation of a mission.
 */
public class Automatic {

    private static final Double POWER = 30.0;


    private PriorityQueue<Action> path;
    Queue<Division> completePath;
    String[] completePathString;
    private MissionImpl mission;
    private boolean flagLeft;
    private boolean flagTarget;
    private boolean flagKit;
    private Division startDiv;
    private ToImpl toCruz;
    ArrayUnorderedList<Enemy> deadEnemies = new ArrayUnorderedList<Enemy>();

    public Automatic(MissionImpl mission, Division startDiv) {
        this.mission = mission;
        this.flagLeft = false;
        this.flagTarget = false;
        this.flagKit = false;
        this.startDiv = startDiv;
        this.path = new PriorityQueue<>();
        this.completePath = new Queue<Division>();
        this.completePathString = new String[100];
        this.toCruz = new ToImpl(POWER);
    }

    /**
     * Returns the life points of Tó Cruz after the simulation.
     *
     * @return Tó Cruz's life points as a Double.
     */
    public Double getLifePoints(){
        return this.toCruz.getLifePoints();
    }

    /**
     * Starts the automatic simulation.
     *
     * @throws NullException if the mission or the start division are null.
     * @throws InvalidTypeException if the start division is not a valid division.
     */
    public void start() throws NullException, InvalidTypeException {
        toCruz.setDivision(startDiv);
        Iterator<Division> pathToTarget = mission.getBuilding().iteratorShortestPath(toCruz.getDivision(), mission.getTarget().getDivision());

        while(pathToTarget.hasNext()){
            Division next = pathToTarget.next();
            Action nextAction = new ActionImpl(next, Action_Type.MOVEMENT);
            path.addElement(nextAction, 2);
        }
        completePath.enqueue(path.removeNext().getDivision());
        if(toCruz.getDivision().sizeEnemies()!=0){
            path.addElement(new ActionImpl(toCruz.getDivision(), Action_Type.ATTACK),1);
        }
        while(!path.isEmpty() && toCruz.getLifePoints()>=0 && !flagLeft){
            Action action = path.getRoot().getElement();

            switch(action.getType()){
                case MOVEMENT:
                    if(!flagTarget && toCruz.getLifePoints() <= 50.0 && toCruz.getItem()!=null){
                        toCruz.useItem();
                    }
                    if(toCruz.getDivision().sizeEnemies()==0){
                        if(checkRoom(action.getDivision())) {
                            toCruz.setDivision(action.getDivision());
                            completePath.enqueue(action.getDivision());
                            path.removeNext();
                            if (toCruz.getDivision().sizeEnemies() != 0) {
                                path.addElement(new ActionImpl(toCruz.getDivision(), Action_Type.ATTACK), 0);
                            }
                            if(toCruz.getDivision() == mission.getTarget().getDivision() && !flagTarget){
                                path.addElement(new ActionImpl(toCruz.getDivision(), Action_Type.GET_TARGET), 1);
                            }
                            else if (toCruz.getDivision().isEntryExit() && flagTarget) {
                                flagLeft = true;
                            }
                            if (toCruz.addMedicakKit()) {
                                this.flagKit = false;
                            } else if (toCruz.addVest()) {
                                this.flagKit = false;
                            }
                        }
                        else {
                            if (toCruz.getItem() != null) {
                                if (toCruz.getItem().getAmount() + toCruz.getLifePoints() <= 100) {
                                    toCruz.useItem();
                                }
                            }
                        }
                    } else{
                        path.addElement(new ActionImpl(toCruz.getDivision(), Action_Type.ATTACK), 0);
                    }
                    Iterator<Division> pathToKit= toCruz.getBestPathToClosestKit(mission);
                    int length = toCruz.calculatePathLength(pathToKit);
                    if (length <= 1 && !flagTarget && !flagKit) {
                        Iterator<Division> pathToKit2= toCruz.getBestPathToClosestKit(mission);
                        goToKit(path, pathToKit2);
                    }
                    else if (length <= 3 && !flagTarget && toCruz.getLifePoints() <= 50 && !flagKit && toCruz.calculatePathLength(pathToTarget) >= length) {
                        Iterator<Division> pathToKit3= toCruz.getBestPathToClosestKit(mission);
                        goToKit(path, pathToKit3);
                    }
                    mission.moveEnemies(mission.getAllEnemies(), toCruz.getDivision(), toCruz.getLifePoints(), false);
                    break;

                case ATTACK:
                    attackAllEnemies();
                    path.removeNext();

                    if(toCruz.getDivision().sizeEnemies()!=0){
                        path.addElement(new ActionImpl(toCruz.getDivision(), Action_Type.ATTACK),0);
                    }
                    toCruz.reduceLifePoints(toCruz.getDivision().getEnemiesPower());
                    mission.moveEnemies(mission.getAllEnemies(), toCruz.getDivision(), toCruz.getLifePoints(), false);
                    break;

                case GET_TARGET:
                    Iterator<Division> pathToExit = toCruz.getBestPathToClosestExit(mission);
                    goExit(pathToExit);
                    flagTarget = true;
                    path.removeNext();
                    break;
            }
        }

        setPath(completePath);
    }

    /**
     * Checks if the given division is a room where Tó Cruz is being attacked.
     *
     * @param division the division to be checked
     * @return true if the division is a room where Tó Cruz is being attacked,
     * false otherwise
     */
    private boolean checkRoom(Division division){
        for(Enemy enemy: mission.getAllEnemies()){
            Division[] reachableDiv = mission.getReachableDivisions(enemy.getCurrentDivision().getName(), 2, enemy);
            for(Division d: reachableDiv){
                if(enemy.getCurrentDivision().getEdges().contains(d.getName())){
                    if (d.equals(toCruz.getDivision())) {
                        return true;
                    }
                    else if(d.equals(division)) {
                        if (!division.equals(enemy.getCurrentDivision())) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Makes Tó Cruz go to the closest kit.
     *
     * @param path the current path of Tó Cruz
     * @param pathToKit the path to the closest kit
     */
    private void goToKit(PriorityQueue<Action> path,Iterator<Division> pathToKit){
        int i=0;
        PriorityQueue<Action> newPath = new PriorityQueue<>();
        Division next = null;

        if(path.getRoot().getElement().getType() == Action_Type.ATTACK){
            newPath.addElement(path.removeNext(),0);
        }

        while(pathToKit.hasNext()){
            next = pathToKit.next();
            Action nextAction = new ActionImpl(next, Action_Type.MOVEMENT);
            newPath.addElement(nextAction, 1);
        }

        Iterator<Division> pathToTarget = mission.getBuilding().iteratorShortestPath(next, mission.getTarget().getDivision());

        while(pathToTarget.hasNext()){
            Division nextTarget = pathToTarget.next();
            Action nextAction = new ActionImpl(nextTarget, Action_Type.MOVEMENT);
            newPath.addElement(nextAction, 2);
        }

        this.path = newPath;
        this.flagKit = true;

    }

    /**
     * Makes Tó Cruz go to the exit of the building.
     *
     * @param pathToExit the path to the exit of the building
     */
    private void goExit(Iterator<Division> pathToExit){
        int i=0;
        while(pathToExit.hasNext()){
            Division next = pathToExit.next();
            if(i>0) {
                Action nextAction = new ActionImpl(next, Action_Type.MOVEMENT);
                path.addElement(nextAction, 2);
            }
            i++;
        }
    }

    /**
     * Attacks all enemies in the current division of Tó Cruz.
     */
    private void attackAllEnemies() {
        Enemy[] enemies = toCruz.getDivision().getEnemies();
        int j = enemies.length;
        for (int i = 0; i < j; i++) {
            enemies[i].setLifePoints(enemies[i].getLifePoints() - POWER);
            if (enemies[i].getLifePoints() <= 0) {
                deadEnemies.addToFront(enemies[i]);
                toCruz.getDivision().removeEnemy(enemies[i]);
                i--;
                j--;
            }
        }
        setgetDeadEnemies();
    }

    /**
     * Sets the dead enemies in the mission with the enemies in the current list.
     */
    private void setgetDeadEnemies() {
        mission.setDeadEnemies(deadEnemies);
    }

    /**
     * Sets the complete path string for the simulation.
     *
     * @param path Queue of Division representing the path taken in the simulation
     */
    private void setPath(Queue<Division> path) {
        this.completePathString = new String[completePath.size()];
        int i = 0;
        while (!path.isEmpty()) {
            Division div = path.dequeue();
            this.completePathString[i] = div.getName();
            i++;
        }
    }


    public String toString() {
        String s = "";
        s += "\n\tCaminho Percorrido: ";
        for (int i = 0; i < completePathString.length; i++) {
            if(completePathString[i]!= null) {
                s += completePathString[i];
                if (i != completePathString.length - 1) {
                    s += ", ";
                }
            }
        }
        return s;
    }

}
