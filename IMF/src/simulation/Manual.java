package simulation;

import Queue.Queue;
import entities.*;
import exceptions.InvalidFileException;
import exceptions.NullException;
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

    public void start() throws InvalidFileException {
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
            Division chosenDiv = mission.getDivisionExitEntry(choice);

            if (chosenDiv != null && mission.getExitEntry().contains(chosenDiv)) {
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

    private void playGame() {
        String target = mission.getTarget().getDivision();

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

    private void chooseOption() {
        Scanner scan = new Scanner(System.in, "ISO-8859-1");
        boolean validChoice = false;
        while (!validChoice) {
            String choice = scan.nextLine();
            if (currentDiv.getEdges().contains(choice) && mission.getDivision(choice).getEnemies() == null) {
                currentDiv = mission.getDivision(choice);
                path.enqueue(currentDiv);
                System.out.println("Pontos de Vida: " + lifePoints + "\nDivisão Atual: " + currentDiv.getName());
                validChoice = true;
            } else if (currentDiv.getEdges().contains(choice) && mission.getDivision(choice).getEnemies() != null) {
                currentDiv = mission.getDivision(choice);
                path.enqueue(currentDiv);
                validChoice = true;
                System.out.println("Entrou numa divisão com inimigos! Divisão Atual: " + currentDiv.getName());
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
                    //mover inimigos
                    //mostrar info
                }
            } else if (choice.equals("1")) {
                System.out.println("Esperou na divisão: " + currentDiv.getName());
            } else if (choice.equals("2")) {
                useMedicalKit();
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

    private void checkMissionStatus(String target) {
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

    private void moveEnemies() {
        for (Enemy enemy : mission.getAllEnemies()) {
            Division currentDivision = mission.getDivision(enemy.getDivision());
            Division[] reachableDivisions = getReachableDivisions(currentDivision.getName(), 2);
            if (reachableDivisions != null && reachableDivisions.length > 0) {
                Division newDivision = getRandomDivision(reachableDivisions);
                enemy.setDivision(newDivision.getName());
            }
        }
    }

    public Division[] getReachableDivisions(String initialDivisionName, int maxDepth) {
        Division[] reachableDivisions = new Division[20];
        int count = 0;

        Division initialDivision = mission.getDivision(initialDivisionName);
        Queue<Division> queue = new Queue<Division>();
        Queue<Integer> depths = new Queue<Integer>();

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

    public Division getRandomDivision(Division[] reachableDivisions) {
        int randomIndex = (int) (Math.random() * reachableDivisions.length);
        return reachableDivisions[randomIndex];
    }

    private void showRealTimeInfo() {
        System.out.println("Inimigos:");
        for (Enemy enemy : mission.getAllEnemies()) {
            System.out.println("Inimigo em " + enemy.getDivision());
        }

        System.out.println("Kits médicos:");
        for (Item kit : mission.getAllItems()) {
            // Exibir a divisão onde o kit médico está localizado
            System.out.println("Kit médico em " + kit.getDivision().getName());
        }

        // Exibir coletes em tempo real
        System.out.println("Coletes:");
        for (Vest vest : mission.getVests()) {
            // Exibir a divisão onde o colete está localizado
            System.out.println("Colete em " + vest.getDivision().getName());
        }

        // Mostrar melhores caminhos para o alvo e o kit mais próximo
        String bestPathForTarget = getBestPathToTarget();  // Método que retorna o melhor caminho para o alvo
        String bestPathForKit = getBestPathToClosestKit();  // Método que retorna o melhor caminho para o kit médico mais próximo

        // Exibir os melhores caminhos
        System.out.println("Melhor caminho para o alvo: " + bestPathForTarget);
        System.out.println("Melhor caminho para o kit médico mais próximo: " + bestPathForKit);
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
