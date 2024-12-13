/**
 * @author Guilherme Lira - 8210415
 * @author Miguel Cunha - 8210426
 */

package entities;

import interfaces.Division;
import interfaces.Enemy;

public class EnemyImpl implements Enemy {

    private static final Double LIFE_DEFAULT = 100.0;

    private String name;

    private Double power;

    private Division division;

    private Division currentDivision;

    private Double lifePoints;

    public EnemyImpl() {
    }

    public EnemyImpl(String name, Double power) {
        this.name = name;
        if (power > 0) {
            this.power = power;
        } else {
            this.power = 0.0;
        }
        lifePoints = LIFE_DEFAULT;
    }

    public EnemyImpl(String name, Double power, DivisionImpl divisionImpl) {
        this.name = name;
        if (power > 0) {
            this.power = power;
        } else {
            this.power = 0.0;
        }
        this.division = divisionImpl;
        lifePoints = LIFE_DEFAULT;
        currentDivision = division;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPower() {
        return power;
    }

    public void setPower(Double power) {
        if (power > 0) {
            this.power = power;
        }
    }

    public Division getDivision() {
        return division;
    }

    public void setDivision(Division division) {
        this.division = division;
    }

    public Division getCurrentDivision() {
        return currentDivision;
    }

    public void setCurrentDivision(Division currentDivision) {
        this.currentDivision = currentDivision;
    }

    public Double getLifePoints() {
        return lifePoints;
    }

    public void setLifePoints(Double lifePoints) {
        this.lifePoints = lifePoints;
    }

    public boolean isValid() {
        return (this.name != null && !this.name.isBlank() && this.currentDivision != null && !this.currentDivision.getName().isBlank() && this.power != null && this.power >= 0);
    }

    public static boolean isValid(EnemyImpl[] enemies) {
        boolean send = enemies.length != 0;

        for (EnemyImpl enemy : enemies) {
            if (enemy == null || !enemy.isValid()) {
                send = false;
                break;
            }
        }
        return send;
    }

    @Override
    public String toString() {
        return "Enimigo{" + "nome:" + name + ", poder:" + power + ", divisão:" + division + '}';
    }

}
