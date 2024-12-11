package entities;

import com.google.gson.annotations.SerializedName;

public class Item {

    private double amount;

    private Item_Type type;

    private String division;

    public Item(double amount, Item_Type type, String division) {
        this.amount = amount;
        this.type = type;
        this.division = division;
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


    public String getDivision() {
        return this.division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public boolean isValid() {
        return this.amount > 0 && this.division != null && this.type != null;
    }

    @Override
    public String toString() {
        return "Item{" + "type:" + type + ", points:" + amount + '}';
    }
}
