package interfaces;

public interface Enemy {

    String getName();

    void setName(String name);

    Division getCurrentDivision();

    void setCurrentDivision(Division currentDivision);

    Double getPower();

    void setPower(Double power);

    Division getDivision();

    void setDivision(Division divisionImpl);

    Double getLifePoints();

    void setLifePoints(Double lifePoints);

    boolean isValid();

    static boolean isValid(Enemy[] enemies) {
        throw new UnsupportedOperationException();
    }

    String toString();
}
