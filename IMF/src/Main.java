import entities.MissionImpl;
import exceptions.InvalidFileException;
import exceptions.InvalidTypeException;
import exceptions.NullException;
import json.MapBuilder;
import menu.Menu;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException, InvalidFileException, NullException, InvalidTypeException {
        MapBuilder map = new MapBuilder("mapa_defesa.json");
        MissionImpl mission = map.createMission();

        System.out.println(mission.getCod() + " " + mission.getTarget());

        Menu m = new Menu();

        m.mainMenu(mission);
    }
}