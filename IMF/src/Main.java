import entities.Division;
import entities.Mission;
import exceptions.InvalidFileException;
import exceptions.InvalidTypeException;
import exceptions.NullException;
import json.Import;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException, InvalidFileException, NullException, InvalidTypeException {
        Import imp = new Import("mapa_defesa.json");
        Mission mission = imp.importMission();
        Division[] division = imp.importDivisions();

        for(int i=0;i<division.length;i++){
            System.out.println(division[i].toString());
        }
        System.out.println(mission.getCod() + " " + mission.getTarget());
    }
}