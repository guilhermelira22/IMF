package interfaces;

public interface Enemy {

    String getName();

    void setName(String name);

    Double getPower();

    void setPower(Double power);

    Division getDivision();

    void setDivision(Division divisionImpl);

    String getInicialDivision();

    Double getLifePoints();

    void setLifePoints(Double lifePoints);

    boolean isValid();

    static boolean isValid(Enemy[] enemies) {
        throw new UnsupportedOperationException();
    }

    String toString();
}
