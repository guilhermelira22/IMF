/**
 * @author Guilherme Lira - 8210415
 * @author Miguel Cunha - 8210426
 */

package entities;

import enums.Item_Type;
import interfaces.Division;
import interfaces.Item;

public class ItemImpl implements Item {

    private double amount;

    private Item_Type type;

    private Division division;

    public ItemImpl(double amount, Item_Type type, DivisionImpl divisionImpl) {
        this.amount = amount;
        this.type = type;
        this.division = divisionImpl;
    }

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Item_Type getItemType() {
        return this.type;
    }

    public void setItemType(Item_Type type) {
        this.type = type;
    }

    public Division getDivision() {
        return this.division;
    }

    public void setDivision(Division divisionImpl) {
        this.division = divisionImpl;
    }

    public boolean isValid() {
        return this.amount > 0 && this.division != null && this.type != null;
    }

    @Override
    public String toString() {
        return "Item{" + "type:" + type + ", points:" + amount + '}';
    }
}
