package entities;

import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName(value = "amount", alternate = {"pontos-recuperados"})
    private double amount;

    @SerializedName(value = "type", alternate = {"tipo"})
    private Item_Type type;

    @SerializedName(value = "division", alternate = {"divisao"})
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

    public static boolean isValid(Item[] items){
        if(items.length == 0){
            return false;
        }

        for(Item item : items){
            if(!item.isValid()){
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        return "Item{" + "type:" + type + ", points:" + amount + '}';
    }
}
