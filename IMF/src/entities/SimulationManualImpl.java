/**
 * @author Guilherme Lira - 8210415
 * @author Miguel Cunha - 8210426
 */

package entities;


import Queue.Queue;
import exceptions.UnsupportedDataTypeException;
import interfaces.Division;
import interfaces.SimulationManual;

public class SimulationManualImpl implements Comparable, SimulationManual {

    private Double lifePoints;

    private String[] path;

    private boolean getTarget;

    public SimulationManualImpl() {
    }

    public SimulationManualImpl(Double lifePoints, Queue<Division> path, boolean target) {
        this.lifePoints = lifePoints;
        this.getTarget = target;
        this.path = new String[path.size()];
        int i = 0;
        while (!path.isEmpty()) {
            Division div = path.dequeue();
            this.path[i] = div.getName();
            i++;
        }
    }

    public SimulationManualImpl(Double lifePoints, String[] path, boolean target) {
        this.lifePoints = lifePoints;
        this.getTarget = target;
        this.path = new String[path.length];
        this.path = path;
    }

    public Double getLifePoints() {
        return lifePoints;
    }

    public void setLifePoints(Double lifePoints) {
        this.lifePoints = lifePoints;
    }

    public String[] getPath() {
        return path;
    }

    public void setPath(Queue<Division> path) {
        this.path = new String[path.size()];
        int i = 0;
        while (!path.isEmpty()) {
            Division div = path.dequeue();
            this.path[i] = div.getName();
            i++;
        }
    }

    public Integer getNumDivisions() {
        int countNumDivisions = 0;
        while (countNumDivisions < path.length) {
            countNumDivisions++;
        }
        return countNumDivisions;
    }

    public boolean isGetTarget() {
        return getTarget;
    }

    public void setGetTarget(boolean getTarget) {
        this.getTarget = getTarget;
    }

    @Override
    public int compareTo(Object obj) {
        if (!(obj instanceof Comparable)) {
            try {
                throw new UnsupportedDataTypeException("UnsupportedDataTypeException.NOT_COMPARABLE");
            } catch (UnsupportedDataTypeException e) {
                throw new RuntimeException(e);
            }
        }

        SimulationManual simulation = (SimulationManual) obj;
        int result;

        if (this.lifePoints.equals(simulation.getLifePoints())) {
            result = this.getNumDivisions().compareTo(simulation.getNumDivisions());
        } else {
            result = simulation.getLifePoints().compareTo(this.lifePoints);
        }
        return result;
    }

    @Override
    public String toString() {
        String s = "";
        s += "\n\tPontos de vida: " + lifePoints;
        s += "\n\tAlvo: " + getTarget;
        s += "\n\tCaminho Percorrido: ";
        for (int i = 0; i < path.length; i++) {
            s += path[i];
            if (i != path.length - 1) {
                s += ", ";
            }
        }
        return s;
    }

}
