package interfaces;

import enums.Action_Type;

public interface Action {
    public Action_Type getType();
    public void setType(Action_Type type);

    public Division getDivision();
    public void setDivision(Division division);

}
