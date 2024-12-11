/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import com.google.gson.annotations.SerializedName;

/**
 * Estrutura de Dados - 2020-2021.
 *
 * @author Mariana Ribeiro - 8190573
 * @author André Raro - 8190590
 *
 * Classe enemy representa um inimigo face a missão do Tó Cruz. O inimigo pode
 * ser encontrado nas Divisões, este possui um power(dano) que fere o Tó Cruz e
 * consequentemente retira-lhe pontos de vida.
 */
public class Enemy {

    private static final Double LIFE_DEFAULT = 100.0;
    /**
     * String, que representa o name do Inimigo.
     */
    private String name;

    /**
     * Integer, que representa o power(dano) do Inimigo.
     */
    private Double power;

    /**
     * String, que representa a division onde o Inimigo se encontra.
     */
    private Division division;

    private String inicialDivision;

    private Double lifePoints;

    /**
     * Construtor vazio para o inimigo.
     */
    public Enemy() {
    }

    /**
     * Construtor que cria um inimigo.Recebe como parametro o seu nome e o
     * power(dano) que retira pontos de vida ao Tó Cruz.
     *
     * @param name name do inimigo.
     * @param power power(dano) do inimigo.
     */
    public Enemy(String name, Double power) {
        this.name = name;
        if (power > 0) {
            this.power = power;
        } else {
            this.power = 0.0;
        }
        lifePoints = LIFE_DEFAULT;
    }

    /**
     * Construtor que cria um inimigo atraves dos parametros.Recebe como
     * parametro o seu nome, o power(dano) que retira pontos de vida ao Tó Cruz
     * e uma string que representa o nome da divisão onde este se encontra.
     *
     * @param name name do inimigo.
     * @param power power(dano) do inimigo.
     * @param division string que representa o nome da divisão onde se encontra
     * o inimigo.
     */
    public Enemy(String name, Double power, Division division) {
        this.name = name;
        if (power > 0) {
            this.power = power;
        } else {
            this.power = 0.0;
        }
        this.division = division;
        lifePoints = LIFE_DEFAULT;
    }

    /**
     * Retorna o nome do inimigo.
     *
     * @return name do inimigo.
     */
    public String getName() {
        return name;
    }

    /**
     * Altera o nome do inimigo.
     *
     * @param name novo name do inimigo.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retorna o power(dano) do inimigo.
     *
     * @return power(dano) do inimigo.
     */
    public Double getPower() {
        return power;
    }

    /**
     * Altera o power(dano) do inimigo.Apenas é alterado se o power enviado por
     * parametro for superior a 0.
     *
     * @param power novo power do inimigo.
     */
    public void setPower(Double power) {
        if (power > 0) {
            this.power = power;
        }
    }

    /**
     * Retorna a string que representa o nome da divisão onde se encontra o
     * inimigo.
     *
     * @return a string que representa o nome da divisão onde se encontra o
     * inimigo.
     */
    public Division getDivision() {
        return division;
    }

    /**
     * Altera a a string que representa o nome da divisão onde o inimigo se
     * encontra.
     *
     * @param division nova divisão do inimigo.
     */
    public void setDivision(Division division) {
        this.division = division;
    }

    public String getInicialDivision() {
        return inicialDivision;
    }

    public Double getLifePoints() {
        return lifePoints;
    }

    public void setLifePoints(Double lifePoints) {
        this.lifePoints = lifePoints;
    }

    /**
     * Verifica se o inimigo possui um formato valido. Para o inimigo ser valido
     * o nome e a divisão têm de ser diferente de nulo e não podem estar em
     * branco.Ja o poder do inimigo nao pode ser nulo nem ser inferior a 0.
     *
     * @return verdadeiro se o inimigo for valido, falso caso contrario.
     */
    public boolean isValid() {
        return (this.name != null && !this.name.isBlank() && this.division != null && !this.division.getName().isBlank() && this.power != null && this.power >= 0);
    }

    /**
     * Verifica se um array de Inimigos é valido. Para tal é necessario que o
     * array não seja vazio(tamanho superior a 0)e que cada instancia seja
     * simultanemanete diferente de nula e valida.
     *
     * @param enemies array de inimigos que se pretende validar.
     * @return verdadeiro se o array de inimigos for valido, falso caso contrario.
     */
    public static boolean isValid(Enemy[] enemies) {
        boolean send = true;

        if (enemies.length == 0) {
            send = false;
        }

        for (int i = 0; i < enemies.length; i++) {
            if (enemies[i] == null || !enemies[i].isValid()) {
                send = false;
                break;
            }
        }
        return send;
    }

    /**
     * Retorna uma representação em String do Inimigo
     *
     * @return string representação do Inimmigo
     */
    @Override
    public String toString() {
        return "Enimigo{" + "nome:" + name + ", poder:" + power + ", divisão:" + division + '}';
    }
}
