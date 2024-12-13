/**
 * @author Guilherme Lira - 8210415
 * @author Miguel Cunha - 8210426
 */

package menu;

import entities.MissionImpl;
import entities.SimulationManualImpl;
import exceptions.InvalidFileException;
import exceptions.InvalidTypeException;
import exceptions.NullException;
import interfaces.Division;
import interfaces.Enemy;
import json.Exporter;
import json.MapBuilder;
import orderedUnorderedList.ArrayOrderedList;
import orderedUnorderedList.ArrayUnorderedList;
import simulation.Automatic;
import simulation.Manual;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Main menu of the game.
 */
public class Menu {

    private String file;
    private MissionImpl mission;

    public Menu() throws InvalidFileException, NullException, InvalidTypeException, FileNotFoundException {
        mission = new MissionImpl();
        selectMission();
        mainMenu();
    }

    /**
     * Displays a list of available missions and asks the user to select one.
     */
    public void selectMission() {
        Scanner scanner = new Scanner(System.in, "ISO-8859-1");
        boolean isMissionValid = false;

        do {
            System.out.println("\n\t\tBEM VINDO AO SIMULADOR DAS MISSÕES DO IMPLACÁVEL TÓ CRUZ\n");
            System.out.println("Missões Disponíveis:");

            File folder = new File("maps");
            File[] missionFiles = folder.listFiles();

            if (missionFiles == null || missionFiles.length == 0) {
                System.out.println("Não há missões disponíveis. Por favor adicione uma no diretório 'maps'.");
                return;
            }

            for (int i = 0; i < missionFiles.length; i++) {
                System.out.println("[" + (i + 1) + "] " + missionFiles[i].getName().replace(".json", ""));
            }

            System.out.print("\nIntroduza a missão que pretende simular: ");
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

                        System.out.println("\nMissão '" + selectedMission.replace(".json", "") + "' carregada e validada com sucesso!\n");
                    } catch (InvalidFileException e) {
                        System.out.println("Erro: " + e.getMessage());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    System.out.println("Opção inválida. Por favor escolha um nº entre 1 e " + missionFiles.length + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Opção inválida. Por favor escolha um nº válido.");
            }

        } while (!isMissionValid);
    }

    /**
     * Displays the main menu of the IMF simulator and handles user interactions.
     *
     * @throws NullException if a required object is null during operation.
     * @throws InvalidTypeException if an invalid type is encountered during operation.
     */
    public void mainMenu() throws NullException, InvalidTypeException {
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

    /**
     * Executes a manual simulation for the given mission file.
     *
     * @param file the name of the mission file to simulate
     * @throws NullException if a required object is null during operation
     * @throws InvalidTypeException if an invalid type is encountered during operation
     */
    private void simManual(String file) throws NullException, InvalidTypeException {
        resetMap();
        Manual m = new Manual(mission);
        try {
            m.start();
        } catch (InvalidFileException | NullException | InvalidTypeException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Executes an automatic simulation for the given mission file.
     *
     * @throws NullException if a required object is null during operation
     * @throws InvalidTypeException if an invalid type is encountered during operation
     */
    private void simAuto() throws NullException, InvalidTypeException {
        resetMap();
        Double MaxLifePoints = 0.0;
        String path = "";

        for(Division div: mission.getExitEntry()){
            Automatic a = new Automatic(mission, div);
            a.start();
            if(a.getLifePoints()> MaxLifePoints){
                path = a.toString();
                MaxLifePoints = a.getLifePoints();
            }
            resetMap();
        }

        if(MaxLifePoints>0) {
            System.out.println(path);
        }else{
            System.out.println("Não foi possivel terminar a missão por qualquer entrada");
        }

    }

    /**
     * Prints the results of the manual simulations that have been run for the
     * current mission.
     *
     * If simulations have been run, prints all of the simulations in the order
     * they were run.
     */
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
    }

    /**
     * Resets the state of the map after a simulation has been run.
     *
     * @throws NullException if a required object is null during operation
     * @throws InvalidTypeException if an invalid type is encountered during operation
     */
    private void resetMap() throws NullException, InvalidTypeException {
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
    }

    /**
     * Pause the program until the user presses Enter.
     *
     * @param scanner a scanner to read the user input
     */
    private void pause(Scanner scanner) {
        System.out.println("\nPressione Enter para continuar...");
        scanner.nextLine();
    }

}
