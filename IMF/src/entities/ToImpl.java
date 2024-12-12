package entities;

import enums.Item_Type;
import interfaces.Division;
import interfaces.Item;
import interfaces.To;
import stack.LinkedStack;

public class ToImpl implements To {

    private Double power;
    private Division division;
    private Double lifePoints;
    private LinkedStack<Item> backpack;


    public ToImpl(Double power) {
        super();
        this.power = power;
        this.lifePoints = 100.0;
        this.backpack = new LinkedStack<>();
    }

    @Override
    public Double getPower() {
        return this.power;
    }

    @Override
    public void setPower(Double power) {
        this.power = power;
    }

    @Override
    public Double getLifePoints() {
        return this.lifePoints;
    }

    @Override
    public void setLifePoints(Double lifePoints) {
        this.lifePoints = lifePoints;
    }

    @Override
    public void addLifePoints(Double lifePoints) {
        this.lifePoints += lifePoints;
    }

    @Override
    public void reduceLifePoints(Double lifePoints) {
        this.lifePoints -= lifePoints;
    }

    @Override
    public Division getDivision() {
        return this.division;
    }

    @Override
    public void setDivision(Division division) {
        this.division = division;
    }

    @Override
    public Item getItem() {
        if(this.backpack.isEmpty()) {
            return null;
        }
        return this.backpack.pop();
    }

    @Override
    public boolean addMedicakKit() {
        if (this.division.getItem() != null) {
            if (this.division.getItem().getItemType() == Item_Type.KIT) {
                backpack.push(this.division.getItem());
                division.setItem(null);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean addVest() {
        if (this.division.getItem() != null) {
            if (this.division.getItem().getItemType() == Item_Type.VEST) {
                this.lifePoints += this.division.getItem().getAmount();
                division.setItem(null);
                return true;
            }
        }
        return false;
    }
}
