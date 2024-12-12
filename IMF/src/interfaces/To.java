package interfaces;

import java.util.Stack;

public interface To {

    Stack<Item> getBackpack();
    void setItem(Item item);

    Double getlifePoints();
    void setlifePoints(Double lifePoints);

    Double getPower();
    void setPower(Double power);



}
