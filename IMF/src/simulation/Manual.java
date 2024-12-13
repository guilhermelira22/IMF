package simulation;

import Queue.Queue;
import entities.*;
import enums.Item_Type;
import exceptions.InvalidFileException;
import exceptions.InvalidTypeException;
import exceptions.NullException;
import graph.GraphMatrix;
import interfaces.Division;
import interfaces.Enemy;
import interfaces.Item;
import interfaces.Mission;
import json.Exporter;
import orderedUnorderedList.ArrayUnorderedList;
import stack.LinkedStack;
import interfaces.To;

import java.io.File;
import java.util.*;

public class Manual {

    private static final Double LIFE_DEFAULT = 100.0;
    private static final int POWER = 40;
    private static final String STRING_AUX = "\n*-*-*-*-*-*-*-*-*\n";

    private Queue<Division> path;
    private MissionImpl mission;
    private boolean flagLeft;
    private boolean flagTarget;
    private To toCruz;
    ArrayUnorderedList<Enemy> deadEnemies = new ArrayUnorderedList<Enemy>();

    public Manual(MissionImpl mission) {
        this.mission = mission;
        this.flagLeft = false;
        this.flagTarget = false;
        this.path = new Queue<Division>();
        this.toCruz = new ToImpl(40.0);
    }

    public void start(String file) throws InvalidFileException, NullException, InvalidTypeException {

        chooseStartDivision();

        playGame();

        SimulationManualImpl newSimulation = new SimulationManualImpl(toCruz.getLifePoints(), path, flagTarget);
        try {
            mission.addSimulation(newSimulation);
        } catch (NullException ex) {
            System.out.println("Erro ao adicionar simulação: " + ex.getMessage());
        }
    }

    private void chooseStartDivision() throws NullException, InvalidTypeException {
        System.out.println("Escolha uma divisão de entrada/saída:");
        Iterator<Division> it = mission.getExitEntry().iterator();
        while (it.hasNext()) {
            System.out.print(it.next().getName() + " ");
        }

        Scanner scan = new Scanner(System.in, "ISO-8859-1");
        boolean validChoice = false;
        while (!validChoice) {
            String choice = scan.nextLine();
            Division chosenDiv = mission.getDivision(choice);

            if (chosenDiv != null && mission.getDivision(choice).getName().equals(choice)) {
                toCruz.setDivision(chosenDiv);
                path.enqueue(toCruz.getDivision());
                if (toCruz.getDivision().sizeEnemies() != 0) {
                    System.out.println("Entrou numa divisão com inimigos! Divisão Atual: " + toCruz.getDivision().getName());
                    attackAllEnemies();
                    toCruz.reduceLifePoints(toCruz.getDivision().getEnemiesPower());
                    for (Enemy enemy : toCruz.getDivision().getEnemies()) {
                        System.out.println("Inimigo " + enemy.getName() + " atacou! Dano: " + enemy.getPower());
                    }
                    if (toCruz.getDivision().sizeEnemies() != 0) {
                        mission.moveEnemies(mission.getAllEnemies(), toCruz.getDivision(), toCruz.getLifePoints(), true);
                        encounter();
                    }
                } else {
                    System.out.println("Pontos de Vida: " + toCruz.getLifePoints() + "\nDivisão Atual: " + toCruz.getDivision().getName());
                }
                if (toCruz.addMedicakKit()) {
                    System.out.println("Pegou um kit médico!");
                } else if(toCruz.addVest()){
                    System.out.println("Vestiu um colete!");
                }
                validChoice = true;
            } else {
                System.out.println("Opção inválida, tente novamente.");
            }
        }
    }

    private void playGame() throws NullException, InvalidTypeException {
        Division target = mission.getTarget().getDivision();

        while (toCruz.getLifePoints() > 0 && !flagLeft) {
            showOptions();

            chooseOption();

            checkMissionStatus(target);
        }

        System.out.println(getFinalPath());
    }

    private void showOptions() {
        System.out.println("Escolha a próxima divisão ou uma ação:\n");
        Iterator<String> it = toCruz.getDivision().getEdges().iterator();
        while (it.hasNext()) {
            System.out.println(it.next() + ", ");
        }
        if (toCruz.getDivision().isEntryExit()) {
            System.out.println("\n1. Esperar\n2. Tomar kit médico\n3. Sair\n");
        } else {
            System.out.println("\n1. Esperar\n2. Tomar kit médico\n");
        }
    }

    private void chooseOption() throws NullException, InvalidTypeException {
        Scanner scan = new Scanner(System.in, "ISO-8859-1");
        boolean validChoice = false;
        showRealTimeInfo();

        while (!validChoice) {
            String choice = scan.nextLine();

            if (toCruz.getDivision().getEdges().contains(choice)) {
                toCruz.setDivision(mission.getDivision(choice));
                path.enqueue(toCruz.getDivision());

                if (toCruz.getDivision().sizeEnemies() != 0) {
                    System.out.println("Entrou numa divisão com inimigos! Divisão Atual: " + toCruz.getDivision().getName());
                    attackAllEnemies();
                    toCruz.reduceLifePoints(toCruz.getDivision().getEnemiesPower());
                    if (toCruz.getDivision().sizeEnemies() != 0) {
                        mission.moveEnemies(mission.getAllEnemies(), toCruz.getDivision(), toCruz.getLifePoints(), true);
                        encounter();
                    }
                } else {
                    System.out.println("Pontos de Vida: " + toCruz.getLifePoints() + "\nDivisão Atual: " + toCruz.getDivision().getName());
                    mission.moveEnemies(mission.getAllEnemies(), toCruz.getDivision(), toCruz.getLifePoints(), true);
                    if (toCruz.getDivision().sizeEnemies() != 0) {
                        encounter();
                    }
                }
                if (toCruz.addMedicakKit()) {
                    System.out.println("Pegou um kit médico!");
                } else if(toCruz.addVest()){
                    System.out.println("Vestiu um colete!");
                }
                validChoice = true;

            } else if (choice.equals("1")) {
                System.out.println("Esperou na divisão: " + toCruz.getDivision().getName());
                mission.moveEnemies(mission.getAllEnemies(), toCruz.getDivision(), toCruz.getLifePoints(), true);
                validChoice = true;
            } else if (choice.equals("2")) {
                useMedicalKit();
                validChoice = true;
            } else if (choice.equals("3") && toCruz.getDivision().isEntryExit()) {
                finishMission(mission.getTarget().getDivision());
                validChoice = true;
            } else {
                System.out.println("Opção inválida, tente novamente.");
                break;
            }
        }
    }

    protected void attackAllEnemies() {
        Enemy[] enemies = toCruz.getDivision().getEnemies();
        int j = enemies.length;
        for (int i = 0; i < j; i++) {
            enemies[i].setLifePoints(enemies[i].getLifePoints() - POWER);
            if (enemies[i].getLifePoints() <= 0) {
                System.out.println("Matou o inimigo: " + enemies[i].getName());
                deadEnemies.addToFront(enemies[i]);
                toCruz.getDivision().removeEnemy(enemies[i]);
                i--;
                j--;
            } else {
                System.out.println("Atacou o inimigo " + enemies[i].getName() + "! Pontos de vida do inimigo: " + enemies[i].getLifePoints());
            }
        }
        setgetDeadEnemies();
    }

    public void setgetDeadEnemies() {
        mission.setDeadEnemies(deadEnemies);
    }

    private void encounter() throws NullException, InvalidTypeException {
        Scanner scan = new Scanner(System.in, "ISO-8859-1");

        while (toCruz.getLifePoints() > 0 && enemiesRemaining()) {
            System.out.println("\n1. Atacar\n2. Tomar kit médico\n");
            String action = scan.nextLine();

            switch (action) {
                case "1":
                    attackAllEnemies();
                    toCruz.reduceLifePoints(toCruz.getDivision().getEnemiesPower());
                    for (Enemy enemy : toCruz.getDivision().getEnemies()) {
                        System.out.println("Inimigo " + enemy.getName() + " atacou! Dano: " + enemy.getPower());
                    }
                    System.out.println("Pontos de Vida: " + toCruz.getLifePoints());
                    break;

                case "2":
                    useMedicalKit();
                    break;

                default:
                    System.out.println("Opção inválida. Tente novamente.");
                    continue;
            }

            if (toCruz.getLifePoints() <= 0) {
                break;
            }

            mission.moveEnemies(mission.getAllEnemies(), toCruz.getDivision(), toCruz.getLifePoints(), true);
        }
    }

    private boolean enemiesRemaining() {
        Enemy[] enemies = toCruz.getDivision().getEnemies();
        for (int i = 0; i < enemies.length; i++) {
            if (enemies[i].getLifePoints() > 0) {
                return true;
            }
        }
        return false;
    }

    private void useMedicalKit() throws NullException, InvalidTypeException {
        Item medicalKit = toCruz.getItem();
        if (medicalKit==null) {
            System.out.println("Nenhum kit médico na mochila.");
        } else {

            if (toCruz.getLifePoints() != 100) {
                toCruz.addLifePoints(medicalKit.getAmount());
                if (toCruz.getLifePoints() > 100) {
                    toCruz.setLifePoints(100.0);
                }
                System.out.println("Usou kit médico! Pontos de vida: " + toCruz.getLifePoints());
            } else {
                System.out.println("Já está com 100 pontos de vida.");
            }
            mission.moveEnemies(mission.getAllEnemies(), toCruz.getDivision(), toCruz.getLifePoints(), true);
        }
    }

    private void checkMissionStatus(Division target) {
        if (toCruz.getLifePoints() <= 0) {
            System.out.println("Missão falhou! Tó Cruz perdeu toda a vida.");
            flagLeft = true;
        } else if (toCruz.getDivision().getName().equals(target.getName()) && !flagTarget) {
            System.out.println("Alvo adquirido com sucesso!");
            flagTarget = true;
        }
    }

    private void finishMission(Division target) {
        if (flagTarget && toCruz.getDivision().isEntryExit()) {
            System.out.println("Missão concluída com sucesso! Parabéns Tó Cruz!!");
            flagLeft = true;
        } else if (!flagTarget && toCruz.getDivision().isEntryExit()) {
            System.out.println("Você ainda não tem o alvo! Quer sair?");
            String exitChoice = new Scanner(System.in).nextLine();
            if ("Sim".equalsIgnoreCase(exitChoice)) {
                flagLeft = true;
                System.out.println("Missão falhou! Você não completou o objetivo.");
            }
        }
    }

    private void showRealTimeInfo() {
        System.out.println("Inimigos:");
        for (Enemy enemy : mission.getAllEnemies()) {
            System.out.println("Inimigo [nome: " + enemy.getName() + ", poder: " + enemy.getPower() + "], em " + enemy.getCurrentDivision().getName());
        }

        System.out.println("Kits médicos:");
        for (Item kit : mission.getAllItems()) {
            if (kit.getItemType() == Item_Type.KIT) {
                System.out.println("Kit médico em " + kit.getDivision().getName() + ", cura: " + kit.getAmount());
            }
        }

        System.out.println("Coletes:");
        for (Item vest : mission.getAllItems()) {
            if (vest.getItemType() == Item_Type.VEST) {
                System.out.println("Colete em " + vest.getDivision().getName() + ", protege: " + vest.getAmount());
            }
        }

        GraphMatrix<Division> shortestPath = mission.getBuilding();
        String bestPathForTarget = iteratorToString(shortestPath.iteratorShortestPath(toCruz.getDivision(), mission.getTarget().getDivision()));
        String bestPathForKit = getBestPathToClosestKit();

        System.out.println("Melhor caminho para o alvo: " + bestPathForTarget);
        System.out.println("Melhor caminho para o kit médico mais próximo: " + bestPathForKit);
    }

    public String getBestPathToClosestKit() {
        GraphMatrix<Division> graph = mission.getBuilding();
        Division currentDivision = toCruz.getDivision();

        int shortestDistance = Integer.MAX_VALUE;
        String bestPath = "Nenhum kit médico disponível.";

        for (Item kit : mission.getAllItems()) {
            if (kit.getDivision() != null) {
                Division kitDivision = kit.getDivision();
                Iterator<Division> pathIterator = graph.iteratorShortestPath(currentDivision, kitDivision);
                Iterator<Division> pathIteratorCount = graph.iteratorShortestPath(currentDivision, kitDivision);
                int pathLength = calculatePathLength(pathIteratorCount);

                if (pathLength < shortestDistance) {
                    shortestDistance = pathLength;
                    bestPath = iteratorToString(pathIterator);
                }
            }
        }

        return bestPath;
    }

    private String iteratorToString(Iterator<Division> pathIterator) {
        StringBuilder path = new StringBuilder();
        while (pathIterator.hasNext()) {
            path.append(pathIterator.next().getName());
            if (pathIterator.hasNext()) {
                path.append(" -> ");
            }
        }
        return path.toString();
    }

    private int calculatePathLength(Iterator<Division> pathIterator) {
        int length = 0;
        Division previous = null;

        while (pathIterator.hasNext()) {
            Division current = pathIterator.next();
            if (previous != null) {
                length++;
            }
            previous = current;
        }

        return length;
    }

    private String getFinalPath() {
        StringBuilder finalPath = new StringBuilder("Caminho percorrido:\n");
        Queue<Division> tempPath = new Queue<Division>();
        while (!path.isEmpty()) {
            Division division = path.dequeue();
            tempPath.enqueue(division);
            finalPath.append("-> " + division.getName() + "\n");
        }

        this.path = tempPath;
        return finalPath.toString();
    }
}
