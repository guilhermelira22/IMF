package entities;

import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName(value = "amount", alternate = {"pontos-recuperados"})
    private double amount;

    @SerializedName(value = "type", alternate = {"tipo"})
    private Item_Type type;

    @SerializedName(value = "division", alternate = {"divisao"})
    private Division division;

    public Item(double amount, Item_Type type, Division division) {
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
}
