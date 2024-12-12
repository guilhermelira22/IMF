package simulation;

import Queue.Queue;
import entities.*;
import exceptions.InvalidFileException;
import exceptions.InvalidTypeException;
import exceptions.NullException;
import graph.GraphMatrix;
import graph.NetworkMatrix;
import json.Import;
import orderedUnorderedList.ArrayUnorderedList;
import stack.LinkedStack;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

public class Manual {

    private static final Double LIFE_DEFAULT = 100.0;
    private static final int POWER = 30;
    private static final String STRING_AUX = "\n*-*-*-*-*-*-*-*-*\n";

    private Queue<Division> path;
    private Mission mission;
    private double lifePoints;
    private boolean flagLeft;
    private boolean flagTarget;
    private Division currentDiv;
    private LinkedStack<Item> backpack;

    public Manual(Mission mission) {
        this.mission = mission;
        this.lifePoints = LIFE_DEFAULT;
        this.flagLeft = false;
        this.flagTarget = false;
        this.currentDiv = new Division();
        this.path = new Queue<Division>();
        this.backpack = new LinkedStack<>();
    }

    public void start() throws InvalidFileException, NullException, InvalidTypeException {
        //System.out.println(getMissionDetails());

        chooseStartDivision();

        playGame();

        SimulationManual newSimulation = new SimulationManual(lifePoints, path, flagTarget);
        try {
            mission.addSimulation(newSimulation);
        } catch (NullException ex) {
            System.out.println("Erro ao adicionar simulação: " + ex.getMessage());
        }
    }

    private void chooseStartDivision() {
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
                currentDiv = chosenDiv;
                path.enqueue(currentDiv);
                lifePoints -= currentDiv.getEnemiesPower();
                validChoice = true;
                System.out.println("Pontos de Vida: " + lifePoints + "\nDivisão Atual: " + currentDiv.getName());
            } else {
                System.out.println("Opção inválida, tente novamente.");
            }
        }
    }

    private void playGame() throws NullException, InvalidTypeException {
        Division target = mission.getTarget().getDivision();

        while (lifePoints > 0 && !flagLeft) {
            showOptions();

            chooseOption();

            checkMissionStatus(target);
        }

        System.out.println(getFinalPath());
    }

    private void showOptions() {
        System.out.println("Escolha a próxima divisão ou uma ação:\n");
        Iterator<String> it = currentDiv.getEdges().iterator();
        while (it.hasNext()) {
            System.out.println(it.next() + ", ");
        }
        System.out.println("\n1. Esperar\n2. Tomar kit médico\n");
    }

    private void chooseOption() throws NullException, InvalidTypeException {
        Scanner scan = new Scanner(System.in, "ISO-8859-1");
        boolean validChoice = false;
        showRealTimeInfo();
        while (!validChoice) {
            String choice = scan.nextLine();

            if (currentDiv.getEdges().contains(choice) && mission.getDivision(choice).getEnemies() == null) {
                currentDiv = mission.getDivision(choice);
                path.enqueue(currentDiv);
                System.out.println("Pontos de Vida: " + lifePoints + "\nDivisão Atual: " + currentDiv.getName());
                moveEnemies();
                showRealTimeInfo();
                validChoice = true;
            } else if (currentDiv.getEdges().contains(choice) && mission.getDivision(choice).getEnemies() != null) {
                currentDiv = mission.getDivision(choice);
                path.enqueue(currentDiv);
                validChoice = true;
                System.out.println("Entrou numa divisão com inimigos! Divisão Atual: " + currentDiv.getName());
                attackAllEnemies();
                lifePoints -= currentDiv.getEnemiesPower();
                while (lifePoints > 0 && enemiesRemaining()) {
                    System.out.println("\n1. Atacar\n2. Tomar kit médico\n");
                    String action = scan.nextLine();

                    if (action.equals("1")) {
                        attackAllEnemies();
                        lifePoints -= currentDiv.getEnemiesPower();
                    } else if (action.equals("2")) {
                        useMedicalKit();
                    }

                    if (lifePoints <= 0) {
                        System.out.println("Tó Cruz morreu! Missão falhada.");
                        break;
                    }

                    System.out.println("Pontos de Vida: " + lifePoints);
                    moveEnemies();
                    showRealTimeInfo();
                }
            } else if (choice.equals("1")) {
                System.out.println("Esperou na divisão: " + currentDiv.getName());
                moveEnemies();
                showRealTimeInfo();
            } else if (choice.equals("2")) {
                useMedicalKit();
                moveEnemies();
                showRealTimeInfo();
                break;
            } else {
                System.out.println("Opção inválida, tente novamente.");
                break;
            }
        }
    }

    private boolean enemiesRemaining() {
        Enemy[] enemies = currentDiv.getEnemies();
        for (int i = 0; i < enemies.length; i++) {
            if (enemies[i].getLifePoints() > 0) {
                return true;
            }
        }
        return false;
    }

    private void attackAllEnemies() {
        Enemy[] enemies = currentDiv.getEnemies();
        for (int i = 0; i < enemies.length; i++) {
            enemies[i].setLifePoints(enemies[i].getLifePoints() - POWER);
            System.out.println("Atacou o inimigo! Pontos de vida do inimigo: " + enemies[i].getLifePoints());
        }
    }

    private void useMedicalKit() {
        if (backpack.isEmpty()) {
            System.out.println("Nenhum kit médico na mochila.");
        } else {
            Item medicalKit = backpack.pop();
            lifePoints += medicalKit.getAmount();
            if (lifePoints > 100) {
                lifePoints = 100;
            }
            System.out.println("Usou kit médico! Pontos de vida: " + lifePoints);
        }
    }

    private void checkMissionStatus(Division target) {
        if (lifePoints <= 0) {
            System.out.println("Missão falhou! Tó Cruz perdeu toda a vida.");
            flagLeft = true;
        } else if (currentDiv.getName().equals(target) && !flagTarget) {
            System.out.println("Alvo adquirido com sucesso!");
            flagTarget = true;
        } else if (flagTarget && currentDiv.isEntryExit()) {
            System.out.println("Missão concluída com sucesso! Parabéns Tó Cruz!!");
            flagLeft = true;
        } else if (!flagTarget && currentDiv.isEntryExit()) {
            System.out.println("Você não tem o alvo! Quer mesmo sair?");
            String exitChoice = new Scanner(System.in).nextLine();
            if ("Sim".equalsIgnoreCase(exitChoice)) {
                flagLeft = true;
                System.out.println("Missão falhou! Você não completou o objetivo.");
            }
        }
    }

    private void moveEnemies() throws NullException, InvalidTypeException {
        for (Enemy enemy : mission.getAllEnemies()) {
            Division currentDivision = mission.getDivision(enemy.getCurrentDivision().getName());
            Division[] reachableDivisions = getReachableDivisions(currentDivision.getName(), 2, enemy);
            if (reachableDivisions != null && reachableDivisions.length > 0) {
                String newDivisionString = getRandomDivision(reachableDivisions, enemy);
                for (int i = 0; i < reachableDivisions.length; i++) {
                    if (reachableDivisions[i].getName().equals(newDivisionString)) {
                        if (mission.getDivision(currentDivision.getName()).removeEnemy(enemy)) {
                            enemy.setCurrentDivision(reachableDivisions[i]);
                            mission.getDivision(reachableDivisions[i].getName()).addEnemy(enemy);
                        }
                    }
                }

            }
        }
    }

    public Division[] getReachableDivisions(String initialDivisionName, int maxDepth, Enemy enemy) {
        Division[] reachableDivisions = new Division[20];
        int count = 1;

        Division initialDivision = mission.getDivision(enemy.getDivision().getName());
        Queue<Division> queue = new Queue<Division>();
        Queue<Integer> depths = new Queue<Integer>();
        reachableDivisions[0] = initialDivision;
        queue.enqueue(initialDivision);
        depths.enqueue(0);

        while (!queue.isEmpty()) {
            Division current = queue.dequeue();
            int depth = depths.dequeue();

            if (!current.equals(initialDivision) && count < reachableDivisions.length) {
                reachableDivisions[count++] = current;
            }

            if (depth < maxDepth) {
                ArrayUnorderedList<String> edges = current.getEdges();
                for (String edge : edges) {
                    Division neighbor = mission.getDivision(edge);
                    queue.enqueue(neighbor);
                    depths.enqueue(depth + 1);
                }
            }
        }

        return Arrays.copyOf(reachableDivisions, count);
    }

    public String getRandomDivision(Division[] reachableDivisions, Enemy enemy) {
        ArrayUnorderedList<String> currentDivisionEdges = enemy.getCurrentDivision().getEdges();
        ArrayUnorderedList<String> validDivisions = new ArrayUnorderedList<>();
        Iterator<String> iterator;
        int count = 0;
        for (String edge : currentDivisionEdges) {
            for (int i = 0; i < reachableDivisions.length; i++) {
                if (reachableDivisions[i].getName().equals(edge)) {
                    validDivisions.addToRear(edge);
                }
            }
        }

        int randomIndex = (int) (Math.random() * validDivisions.size());
        iterator = validDivisions.iterator();
        String next = null;
        while (iterator.hasNext() && count <= randomIndex) {
            next = iterator.next();
            count++;
        }
        return next;
    }

    private void showRealTimeInfo() {
        System.out.println("Inimigos:");
        for (Enemy enemy : mission.getAllEnemies()) {
            System.out.println("Inimigo em " + enemy.getCurrentDivision());
        }

        System.out.println("Kits médicos:");
        for (Item kit : mission.getAllItems()) {
            if (kit.getItemType() == Item_Type.KIT) {
                System.out.println("Kit médico em " + kit.getDivision());
            }
        }

        System.out.println("Coletes:");
        for (Item vest : mission.getAllItems()) {
            if (vest.getItemType() == Item_Type.VEST) {
                System.out.println("Colete em " + vest.getDivision());
            }
        }

        GraphMatrix<Division> shortestPath = mission.getBuilding();
        String bestPathForTarget = iteratorToString(shortestPath.iteratorShortestPath(currentDiv, mission.getTarget().getDivision()));
        String bestPathForKit = getBestPathToClosestKit();

        System.out.println("Melhor caminho para o alvo: " + bestPathForTarget);
        System.out.println("Melhor caminho para o kit médico mais próximo: " + bestPathForKit);
    }

    public String getBestPathToClosestKit() {
        GraphMatrix<Division> graph = mission.getBuilding();
        Division currentDivision = currentDiv;

        int shortestDistance = Integer.MAX_VALUE;
        String bestPath = "Nenhum kit médico disponível.";

        for (Item kit : mission.getAllItems()) {
            if (kit.getDivision() != null) {
                Division kitDivision = kit.getDivision();
                Iterator<Division> pathIterator = graph.iteratorShortestPath(currentDivision, kitDivision);
                int pathLength = calculatePathLength(pathIterator);

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
