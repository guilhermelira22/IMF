package interfaces;

import enums.Item_Type;

public interface Item {

    double getAmount();

    void setAmount(double amount);

    Item_Type getItemType();

    void setItemType(Item_Type type);

    Division getDivision();

    void setDivision(Division divisionImpl);

    boolean isValid();

    String toString();
}
