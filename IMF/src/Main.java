import entities.Division;
import entities.Mission;
import exceptions.InvalidFileException;
import exceptions.InvalidTypeException;
import exceptions.NullException;
import json.Import;
import json.MapBuilder;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException, InvalidFileException, NullException, InvalidTypeException {
        MapBuilder map = new MapBuilder("mapa_defesa.json");
        Mission mission = map.createMission();

        System.out.println(mission.getCod() + " " + mission.getTarget());
    }
}