package interfaces;

public interface To {

    Double getLifePoints();
    void setLifePoints(Double lifePoints);
    void reduceLifePoints(Double lifePoints);
    void addLifePoints(Double lifePoints);

    Division getDivision();
    void setDivision(Division division);

    Item getItem();
    boolean addMedicakKit();
    public boolean addVest();



}
