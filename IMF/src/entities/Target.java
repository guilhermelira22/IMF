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
 * Classe que representa o alvo da missão do Tó Cruz. Esta possui uma string que
 * representa a divisão alvo, esta é a divisão onde se deseja chegar no edifício
 * pois é onde se encontra o alvo, possui tambem u ma string que representa o
 * tipo do alvo.
 */
public class Target {

    /**
     * Representa a divisão alvo, ou seja a divisão onde se deseja chegar no
     * edifício;
     */
    @SerializedName(value = "division", alternate = {"divisao"})
    private String division;

    /**
     * Representa o tipo do alvo que se deseja resgatar na missão.
     */
    @SerializedName(value = "type", alternate = {"tipo"})
    private String type;

    /**
     * Construtor vazio que cria um alvo.
     */
    public Target() {
    }

    /**
     * Construtor que cria um alvo.Recebe como paramêtro o nome da divisao alvo
     * bem como o tipo do alvo.
     *
     * @param division divisão alvo.
     * @param type tipo do alvo.
     */
    public Target(String division, String type) {
        this.division = division;
        this.type = type;
    }

    /**
     * Retorna a divisão alvo ou seja a divisão onde se encontra o alvo.
     *
     * @return String divisão alvo
     */
    public String getDivision() {
        return division;
    }

    /**
     * Retorna o tipo alvo.
     *
     * @return String tipo do alvo
     */
    public String getType() {
        return type;
    }

    /**
     * Altera o tipo do alvo
     *
     * @param type novo tipo do alvo
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Verifica se o alvo possui um formato valido.Para o alvo ser valido o tipo
     * e a divisão alvo têm de ser diferente de nulo e não podem estar em
     * branco.
     *
     * @return verdadeiro se o alvo for valido, falso caso contrario.
     */
    public boolean isValid() {
        return (this.division != null && !this.division.isBlank() && this.type != null && !this.type.isBlank());

    }

    /**
     * Retorna uma representação em String do Alvo
     *
     * @return string representação do Alvo
     */
    @Override
    public String toString() {
        return "Alvo{" + "divisão: " + division + ", tipo: " + type + '}';
    }

}
