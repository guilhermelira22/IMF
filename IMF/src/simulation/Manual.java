package simulation;

import Queue.Queue;
import entities.*;
import exceptions.InvalidFileException;
import exceptions.NullException;
import stack.LinkedStack;

import java.util.Iterator;
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
