package entities;

import enums.Action_Type;
import interfaces.Action;
import interfaces.Division;

public class ActionImpl implements Action {

    private Division division;
    private Action_Type actionType;
    private boolean isEnemy;

    public ActionImpl(Division division, Action_Type actionType, boolean isEnemy) {
        this.division = division;
        this.actionType = actionType;
        this.isEnemy = isEnemy;
    }

    public ActionImpl(Division division, Action_Type actionType) {
        this.division = division;
        this.actionType = actionType;
        this.isEnemy = false;
    }

    @Override
    public Action_Type getType() {
        return this.actionType;
    }

    @Override
    public void setType(Action_Type type) {
        this.actionType = type;
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
    public boolean isEnemy() {
        return this.isEnemy;
    }
}
