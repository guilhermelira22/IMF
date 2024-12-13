/**
 * @author Guilherme Lira - 8210415
 * @author Miguel Cunha - 8210426
 */

package entities;

import interfaces.Division;
import interfaces.Target;

public class TargetImpl implements Target {

    private Division division;

    private String type;

    public TargetImpl() {
    }

    public TargetImpl(Division division, String type) {
        this.division = division;
        this.type = type;
    }

    public Division getDivision() {
        return division;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isValid() {
        return (this.division != null && !this.division.getName().isBlank() && this.type != null && !this.type.isBlank());
    }

    @Override
    public String toString() {
        return "Alvo{" + "divisão: " + division + ", tipo: " + type + '}';
    }

}
