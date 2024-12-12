package menu;

import entities.Mission;
import exceptions.InvalidFileException;
import exceptions.InvalidTypeException;
import exceptions.NullException;
import simulation.Manual;

import java.util.Scanner;

public class Menu {

    public Menu() {
    }

    public void mainMenu(Mission mission) throws InvalidFileException, NullException, InvalidTypeException {
        System.out.println("1. Iniciar Missão\n2. Sair");
        String choice = new Scanner(System.in).nextLine();
        if (choice.equals("1")) {
            beginMission(mission);
        }
    }

    public void beginMission(Mission mission) throws InvalidFileException, NullException, InvalidTypeException {
        System.out.println("Iniciar Missão");

        Manual manual = new Manual(mission);
        manual.start();
    }

}
