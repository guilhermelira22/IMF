package simulation;

import Queue.Queue;
import heap.PriorityQueue;
import interfaces.Division;
import interfaces.Item;
import interfaces.Mission;
import stack.LinkedStack;

public class Automatic {

    private static final Double LIFE_DEFAULT = 100.0;
    private static final int POWER = 30;


    private PriorityQueue<Division> path;
    private Mission mission;
    private double lifePoints;
    private boolean flagLeft;
    private boolean flagTarget;
    private Division currentDiv;
    private LinkedStack<Item> backpack;

    public Automatic(Mission mission) {
        this.mission = mission;
        this.lifePoints = LIFE_DEFAULT;
        this.flagLeft = false;
        this.flagTarget = false;
        this.backpack = new LinkedStack<>();
        this.path = new PriorityQueue<Division>();
    }

}
