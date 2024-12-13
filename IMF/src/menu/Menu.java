package menu;

import entities.MissionImpl;
import entities.SimulationManualImpl;
import exceptions.InvalidFileException;
import exceptions.InvalidTypeException;
import exceptions.NullException;
import interfaces.Division;
import interfaces.Enemy;
import interfaces.SimulationManual;
import json.Exporter;
import json.MapBuilder;
import orderedUnorderedList.ArrayOrderedList;
import orderedUnorderedList.ArrayUnorderedList;
import simulation.Manual;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Scanner;

public class Menu {

    private String file;
    private MissionImpl mission;
    private int x = 0;

    public Menu() throws InvalidFileException, NullException, InvalidTypeException, FileNotFoundException {
        mission = new MissionImpl();
        selectMission();
        mainMenu();
    }

    public void selectMission() {
        Scanner scanner = new Scanner(System.in, "ISO-8859-1");
        boolean isMissionValid = false;

        do {
            System.out.println("\n\t\tWELCOME TO THE IMF SIMULATOR\n");
            System.out.println("Available Missions:");

            File folder = new File("maps");
            File[] missionFiles = folder.listFiles();

            if (missionFiles == null || missionFiles.length == 0) {
                System.out.println("No missions available. Please add mission files to the 'maps' directory.");
                return;
            }

            for (int i = 0; i < missionFiles.length; i++) {
                System.out.println("[" + (i + 1) + "] " + missionFiles[i].getName().replace(".json", ""));
            }

            System.out.print("\nEnter the number of the mission you want to play: ");
            String input = scanner.nextLine();

            try {
                int selectedIndex = Integer.parseInt(input) - 1;

                if (selectedIndex >= 0 && selectedIndex < missionFiles.length) {
                    String selectedMission = missionFiles[selectedIndex].getName();

                    try {
                        MapBuilder importer = new MapBuilder(selectedMission);
                        this.mission = importer.createMission();
                        this.file = selectedMission;
                        isMissionValid = true;

                        System.out.println("\nMission '" + selectedMission.replace(".json", "") + "' successfully loaded and validated!\n");
                    } catch (InvalidFileException e) {
                        System.out.println("Error: " + e.getMessage());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    System.out.println("Invalid option. Please select a number between 1 and " + missionFiles.length + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }

        } while (!isMissionValid);
    }

    public void mainMenu() throws InvalidFileException, FileNotFoundException, NullException, InvalidTypeException {
        Scanner scanner = new Scanner(System.in, "ISO-8859-1");
        boolean exit = false;

        while (!exit) {
            System.out.println("\n\t\tIMF SIMULATOR\n");
            System.out.println("1. Simulação Manual");
            System.out.println("2. Simulação Automática");
            System.out.println("3. Ver Mapa");
            System.out.println("4. Ver Resultados da Simulação Manual");
            System.out.println("5. Mudar de Mapa");
            System.out.println("6. Sair\n");

            System.out.print("Escolha uma opção: ");
            String option = scanner.nextLine().trim();

            switch (option) {
                case "1":
                    simManual(file);
                    pause(scanner);
                    break;

                case "2":
                    simAuto();
                    pause(scanner);
                    break;

                case "3":
                    System.out.println(mission.toString());
                    pause(scanner);
                    break;

                case "4":
                    simResult();
                    pause(scanner);
                    break;

                case "5":
                    Exporter.exportToJson(mission.getSimulation(), this.file);
                    selectMission();
                    break;

                case "6":
                    Exporter.exportToJson(mission.getSimulation(), this.file);
                    exit = true;
                    break;

                default:
                    System.out.println("\nOpção inválida. Por favor, escolha uma opção válida.");
                    break;
            }
        }
    }

    private void simManual(String file) throws InvalidFileException, FileNotFoundException, NullException, InvalidTypeException {
        resetMap();
        Manual m = new Manual(mission);
        try {
            m.start(file);
        } catch (InvalidFileException | NullException | InvalidTypeException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void simAuto() {

    }

    private void simResult() {

        ArrayOrderedList<SimulationManualImpl> simulation = mission.getSimulation();
        Iterator<SimulationManualImpl> it = simulation.iterator();
        System.out.println("\nSimulações: ");
        if (simulation.isEmpty()) {
            System.out.println("\tAinda Não Existem Simulações\n\tFaça A Sua Primeira Simulação!");
        } else {
            while (it.hasNext()) {
                System.out.println(it.next());
            }
        }
        System.out.println("\n");

        /*try
            MissionImpl simulation = new MapBuilder(file).readJsonLeaderboard(mission);

            for (SimulationManualImpl m : simulation.getSimulation()) {
                System.out.println(m.toString());
            }*/

            /*System.out.println("\nSimulações: \n");
            if (simulation.isEmpty()) {
                System.out.println("Nenhuma simulação efetuada.");
            } else {
                while (it.hasNext()) {
                    System.out.println(it.next());
                }
            }
        } catch (InvalidFileException e) {
            System.out.println("Erro ao importar as simulações: " + e.getMessage());
        } catch (FileNotFoundException | NullException | InvalidTypeException e) {
            throw new RuntimeException(e);
        }*/

        /*ArrayOrderedList<SimulationManualImpl> simulation = mission.getSimulation();
        Iterator<SimulationManualImpl> it = simulation.iterator();
        System.out.println("\nSimulações: \n");
        if (simulation.isEmpty()) {
            System.out.println("Nenhuma simulação efetuada.");
        } else {
            while (it.hasNext()) {
                System.out.println(it.next());
            }
        }
        System.out.println("\n");
        */
    }

    private void resetMap() throws InvalidFileException, FileNotFoundException, NullException, InvalidTypeException {
        for (Division d : mission.getDivisions()) {
            for (Enemy e : d.getEnemies()) {
                if (e != null) {
                    if (d != e.getDivision()) {
                        e.setCurrentDivision(e.getDivision());
                        d.removeEnemy(e);
                        e.getDivision().addEnemy(e);
                        e.setLifePoints(100.0);
                    }
                }
            }
        }

        ArrayUnorderedList<Enemy> deadEnemies = mission.getDeadEnemies();
        for (Enemy e : deadEnemies) {
            e.setCurrentDivision(e.getDivision());
            e.setLifePoints(100.0);
            e.getDivision().addEnemy(e);
        }

        //MapBuilder importer = new MapBuilder(file);
        //this.mission = importer.createMission();

        //this.mission = importer.readJsonLeaderboard(this.mission);
    }

    private void pause(Scanner scanner) {
        System.out.println("\nPressione Enter para continuar...");
        scanner.nextLine();
    }

}
