/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import exceptions.InvalidTypeException;
import exceptions.NullException;
import interfaces.Division;
import interfaces.Enemy;
import interfaces.Item;
import list.LinkedList;
import orderedUnorderedList.ArrayUnorderedList;

import java.util.Arrays;

/**
 * Estrutura de Dados - 2020-2021.
 *
 * @author Guilherme Lira - 8210415
 * @author Miguel Cunha - 8210426
 *
 * Classe Division representa uma divisão do edifício na missão do Tó Cruz. A
 * divisão pode conter inimigos e possuir caminhos para outras divisões.
 */
public class DivisionImpl implements Division {

    /**
     * Capacidade por defeito para o número de inimigos numa divisão.
     */
    private static int DEFAULT_CAPACITY = 1;

    /**
     * String, que identifca o nome da divisão.
     */
    private String name;

    /**
     * Tipo boleano que identifica se uma divisão é uma entrada ou saída.
     */
    private boolean entryExit;

    /**
     * Array de inimigos presentes na divisão.
     */
    private Enemy[] enemies;

    /**
     * Número de inimigos presentes na divisão.
     */
    private Integer numEnemies;

    /**
     * Array Desordenado do tipo String que representa as ligações da divisão
     */
    private ArrayUnorderedList<String> edges;

    private Item item;

    /**
     * Construtor vazio que cria uma divisão.
     */
    public DivisionImpl() {
    }

    /**
     * Construtor que cria uma divisão. Recebe como parametro o seu nome e o
     * valor boleano que identifica que se trata de uma divisão de entrada ou
     * saída. O array dos inimigos é criado com uma capacidade por defeito, cada
     * elemento do array de inimigos deve ser uma instancia de
     * {}.O array desordenado que representa as ligações
     * da divisão é tambem instanciado mas vazio.
     *
     * @param name String que identifica a divisão.
     * @param entryExit Boleano que identifica que se trata de uma divisão de
     * entrada ou saída.
     */
    public DivisionImpl(String name, boolean entryExit) {
        this.name = name;
        this.entryExit = entryExit;
        this.enemies = new EnemyImpl[DEFAULT_CAPACITY];
        this.numEnemies = 0;
        this.edges = new ArrayUnorderedList<>();
        this.item = null;
    }

    /**
     * Construtor que cria uma divisão. Recebe como parametro o seu nome. O
     * valor boleano que identifica se a divisão é de entrada ou saída é
     * colocado por defeito a false . O array dos inimigos é criado com uma
     * capacidade por defeito, cada elemento do array de inimigos deve ser uma
     * instancia de {}.O array desordenado que representa
     * as ligações da divisão é tambem instanciado mas vazio.
     *
     * @param name String que identifica a divisão.
     */
    public DivisionImpl(String name) {
        this.name = name;
        this.entryExit = false;
        this.enemies = new EnemyImpl[DEFAULT_CAPACITY];
        this.numEnemies = 0;
        this.edges = new ArrayUnorderedList<>();
    }

    /**
     * Construtor que cria uma divisão. Recebe como parametro o seu nome, o
     * valor boleano que identifica que se trata de uma divisão de entrada ou
     * saída e o array com os inimigos que se encontram na divisão, os inimigos
     * são instancias da classe {}.O array desordenado que
     * representa as ligações da divisão é tambem instanciado mas vazio.
     *
     * @param name String que identifica a divisão.
     * @param entryExit Boleano que identifica que se trata de uma divisão de
     * entrada ou saída.
     * @param enemies Array com os inimigos que a divisão contém.
     */
    public DivisionImpl(String name, boolean entryExit, EnemyImpl[] enemies) {
        this.name = name;
        this.entryExit = entryExit;
        this.enemies = enemies;
        this.numEnemies = enemies.length;
        this.edges = new ArrayUnorderedList<>();
    }

    /**
     * Retorna o nome da divisão tipo String.
     *
     * @return String o nome da divisão.
     */
    public String getName() {
        return name;
    }

    /**
     * Altera o nome da divisão.
     *
     * @param name novo nome da divisão.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Verifica se a divisão é uma entrada ou saída.
     *
     * @return True se for uma entrada ou saída e False se não o for.
     */
    public boolean isEntryExit() {
        return entryExit;
    }

    /**
     * Troca o valor boleano da divisão em relação a ser uma entrada e saída.
     */
    public void setEntryExit() {
        if (this.entryExit) {
            this.entryExit = false;
        } else {
            this.entryExit = true;
        }
    }

    /**
     * Retorna o array dos inimigos presentes na divisão.
     *
     * @return o array de inimigos.
     */
    public Enemy[] getEnemies() {
        return enemies;
    }

    /**
     * Retorna a soma do poder de todos os inimigos presentes na divisão.
     *
     * @return a soma do poder de todos os inimigos presentes na divisão.
     */
    public Double getEnemiesPower() {
        Double power = 0.0;
        for (int i = 0; i < this.numEnemies; i++) {
            power += this.getEnemies()[i].getPower();
        }
        return power;
    }

    /**
     * Altera todo array de inimigos.
     *
     * @param enemies novo array dos inimigos presentes na divisão.
     */
    public void setEnemies(Enemy[] enemies) {
        this.enemies = enemies;
        this.numEnemies = enemies.length;
    }

    /**
     * Retorna o número de inimigos presentes na divisão.
     *
     * @return o número de inimigos.
     */
    public Integer sizeEnemies() {
        return numEnemies;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    /**
     * Retorna o array desordenado do tipo String que representa as ligações que
     * a divisão possui.
     *
     * @return o array desordenado do tipo String que representa as ligações que
     * a divisão possui.
     */
    public ArrayUnorderedList<String> getEdges() {
        return edges;
    }

    /**
     * Adiciona um novo inimigo ao array de inimigos que a divisão possui.
     *
     * @param newEnemy novo inimigo instancia da classe {}
     * @throws NullException caso o inimigo seja do tipo NULL.
     * @throws InvalidTypeException caso o inimigo ja se encontre no array.
     */
    public void addEnemy(Enemy newEnemy) throws NullException, InvalidTypeException {
        if (newEnemy == null) {
            throw new NullException("");
        }
        if (numEnemies == enemies.length) {
            expandCapacity();
        }
        if (findEnemy(newEnemy.getName()) != -1) {
            throw new InvalidTypeException("");
        }
        enemies[numEnemies] = newEnemy;
        numEnemies++;

    }

    public boolean removeEnemy(Enemy enemy) {
        int index = -1;
        for (int i = 0; i < numEnemies; i++) {
            if (enemies[i].equals(enemy)) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            return false;
        }

        for (int i = index; i < numEnemies - 1; i++) {
            enemies[i] = enemies[i + 1];
        }

        enemies[numEnemies - 1] = null;
        numEnemies--;

        reduceEnemies();

        return true;
    }

    private void reduceEnemies() {
        // Conta os inimigos não nulos
        int validCount = 0;
        for (int i = 0; i < numEnemies; i++) {
            if (enemies[i] != null) {
                validCount++;
            }
        }

        // Cria um novo array com o tamanho exato
        Enemy[] newEnemies = new Enemy[validCount];
        int index = 0;

        // Copia os elementos não nulos para o novo array
        for (int i = 0; i < numEnemies; i++) {
            if (enemies[i] != null) {
                newEnemies[index++] = enemies[i];
            }
        }

        // Substitui o array antigo pelo novo
        enemies = newEnemies;
    }


    /**
     * Procura um inimigo no array de inimigos atraves do seu nome. Caso o
     * inimigo ja exista no array de inimigos este ira retornar a sua posição,
     * caso contrario retorna uma posição invalida.
     *
     * @param name nome do inimigo a procurar no array de inimigos.
     * @return posição do array caso o inimigo exista no array de inimigos ou -1
     * caso contrario.
     */
    private int findEnemy(String name) {
        Integer send = -1;
        for (int i = 0; i < this.numEnemies; i++) {
            if (name.equals(enemies[i])) {
                send = i;
                break;
            }
        }
        return send;
    }

    /**
     *
     * Expande a capacidade do array de inimigos que a divisão contém. Cria um
     * novo Array com o tamanho da array atual mais a constante da capacidade
     * por defeito. Copia todos os elementos do array atual, para um novo array.
     * Substitui o array de inimigos por o novo array de inimigos, expandindo
     * assim a sua capacidade.
     *
     *
     */
    protected void expandCapacity() {
        EnemyImpl[] newEnemies = new EnemyImpl[enemies.length + DEFAULT_CAPACITY];
        System.arraycopy(enemies, 0, newEnemies, 0, numEnemies);
        enemies = newEnemies;
        /* Enemy[] newEnemies = new Enemy[this.numEnemies + DEFAULT_CAPACITY];
        for (int i = 0; i < this.numEnemies; i++) {
            newEnemies[i] = this.enemies[i];
        }
        this.enemies = newEnemies;*/
    }

    /**
     * Verifica se a divisão possui um formato valido. Para a divisão ser valida
     * o nome têm de ser diferente de nulo e não pode estar em branco. Ja o array
     * do inimigo deve ser valido e diferente de nulo. A varivel que me da o
     * numero de inimigos presente na divisão não pode ser inferior a 0.
     *
     * @return verdadeiro se a divisao for valida, falso caso contrario.
     */
    public boolean isValid() {
        if (this.numEnemies > 0) {
            return (this.name != null && !this.name.isBlank() && Enemy.isValid(this.enemies));
        }
        return (this.name != null && !this.name.isBlank());
    }

    /**
     * Procura uma divisão através do seu nome, ou seja comparando strings, num
     * array de divisões. Se a encontrar retorna uma divisão valida se não
     * retorna uma divisão nula.
     *
     * @param div, array de divisões onde se deseja procurar o nome da divisão.
     * @param division, nome da divisão que se deseja encontrar no array de
     * divisões.
     * @return uma divisão valida, caso encontre o nome da divisão no array.
     * recebido como parametro, caso contario retorna uma divisão nula.
     */
    public static DivisionImpl getDivision(DivisionImpl[] div, String division) {

        for (int i = 0; i < div.length; i++) {
            if (div[i].getName().compareTo(division) == 0) {
                return div[i];
            }
        }
        return null;
    }

    /**
     * Retorna uma representação em String da Divisão.
     *
     * @return string representação da Divisão.
     */
    @Override
    public String toString() {
        String enemiesString = "[";
        for (int i = 0; i < numEnemies; i++) {
            enemiesString += "{nome: " + enemies[i].getName() + ", poder: " + enemies[i].getPower() + "}";
            if (i < numEnemies - 1) {
                enemiesString += ", ";
            }
        }
        enemiesString += "]";
        return "Divisão{" + "nome: " + name + ", SaidaOuEntrada: " + entryExit + ", Inimigos: " + enemiesString + ", numInimigos: " + numEnemies + ", ligações: " + edges.toString() + "item: "+ (item != null ? item.toString() : "null") +'}';
    }
}