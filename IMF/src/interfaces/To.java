package interfaces;

import java.util.Iterator;

public interface To {

    Double getPower();
    void setPower(Double power);

    Double getLifePoints();
    void setLifePoints(Double lifePoints);
    void reduceLifePoints(Double lifePoints);
    void addLifePoints(Double lifePoints);

    Division getDivision();
    void setDivision(Division division);

    Item getItem();
    boolean addMedicakKit();
    public boolean addVest();

    public Iterator<Division> getBestPathToClosestKit(Mission mission);



}
