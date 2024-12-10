/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imf.entity;

import com.google.gson.annotations.SerializedName;
import exception.EmptyCollectionException;
import graph.NetworkMatrix;
import imf.exception.NullException;
import java.util.Iterator;
import listOrderedUnordered.ArrayOrderedList;
import listOrderedUnordered.ArrayUnorderedList;

/**
 * Estrutura de Dados - 2020-2021.
 *
 * @author Mariana Ribeiro - 8190573
 * @author André Raro - 8190590
 *
 * Classe Mission representa uma missão do Tó Cruz. A missão contém um código
 * que a representa, uma versão, o alvo da missão, as saidas e entradas do
 * edifício,um grafo que representa o mapa do edifício e uma lista ordenada que
 * contem todas as simulações testadas para a missão.
 */
public class Mission {

    /**
     * Código da missão do tipo String.
     */
    @SerializedName(value = "cod", alternate = {"cod-missao"})
    private String cod;
    /**
     * Versão da missão do tipo Inteiro.
     */
    @SerializedName(value = "version", alternate = {"versao"})
    private Integer version;

    /**
     * Instancia da classe Target que representa o alvo da missão.
     */
    @SerializedName(value = "target", alternate = {"alvo"})
    private Target target;

    /**
     * Array Desordenado que representa as entradas ou saidas do edifício.
     */
    private ArrayUnorderedList<imf.entity.Division> exitEntry;

    /**
     * Grafo Pesado(Network ou Rede) que representa o edifício.
     */
    private NetworkMatrix<imf.entity.Division> building;

    /**
     * Array Ordenado onde estão inseridas todas as simulações manuais testadas
     */
    private ArrayOrderedList<SimulationManual> simulation;

    /**
     * Construtor vazio que cria uma Mission para o Tó Cruz.
     */
    public Mission() {
    }

    /**
     * Construtor que cria uma Missão.Recebe como parâmetro o codigo da missão,
     * a sua versão para poder testar varios cenarios, a divisao alvo ou seja a
     * divisao onde se encontra o alvo, bem como o tipo do alvo que se trata,
     * estes dois ultimos parâmetros sao recebidos individualdemente apesar de
     * se tratar de apenas uma varivel de classe, esta faz referencia a classe
     * {@link imf.entity.Target}.
     *
     * @param cod codigo da missão.
     * @param version versão da missão.
     * @param division divisão alvo.
     * @param type tipo do alvo.
     */
    public Mission(String cod, Integer version, String division, String type) {
        this.cod = cod;
        this.version = version;
        this.target = new Target(division, type);
    }

    /**
     * Construtor que cria uma Missão.Recebe como parâmetro o codigo da missão,
     * a sua versão para poder testar varios cenarios e uma instancia da classe
     * {@link imf.entity.Target} que possui a divisao alvo ou seja a divisao
     * onde se encontra o alvo, bem como o tipo do alvo que se trata.
     *
     * @param cod codigo da missão.
     * @param version versão da missão.
     * @param target instancia da classe Target.
     */
    public Mission(String cod, Integer version, Target target) {
        this.cod = cod;
        this.version = version;
        this.target = target;
    }

    /**
     * Construtor que cria uma Missão.Recebe como parâmetro o codigo da missão,
     * a sua versão para poder testar varios cenarios, uma instancia da classe
     * {@link imf.entity.Target} que possui a divisao alvo ou seja a divisao
     * onde se encontra o alvo, bem como o tipo do alvo que se trata,recebe
     * tambem uma lista desordenada que representa as possiveis entradas e
     * saidas do edificio por ultimo recebe um grafo pesado(network ou rede) com
     * os vertices e as ligações ja tratadas, este representa o edifício.
     *
     * @param cod codigo da missão.
     * @param version versão da missão.
     * @param target instancia da classe Target.
     * @param exitEntry lista desordenada que possui entradas e saidas do
     * edifício
     * @param building grafo pesado(network ou rede) que representa o edifício
     *
     */
    public Mission(String cod, Integer version, imf.entity.Target target, ArrayUnorderedList<imf.entity.Division> exitEntry, NetworkMatrix<imf.entity.Division> building) {
        this.cod = cod;
        this.version = version;
        this.target = target;
        this.exitEntry = exitEntry;
        this.building = building;
        this.simulation = new ArrayOrderedList<SimulationManual>();
    }

    /**
     * Retorna um array desordenado que possui o nome das divisões que são
     * simultanemaente entradas e saidas do grafo.
     *
     * @return um array ordenado que possui as entradas e saidas do grafo.
     */
    public ArrayUnorderedList<imf.entity.Division> getExitEntry() {
        return this.exitEntry;
    }

    /**
     * Retorna o grafo pesado(network ou grafo) que possui as divisões e as
     * ligações entre estas.
     *
     * @return o grafo pesado(network ou grafo) que possui as divisões e as
     * ligações entre estas.
     */
    public NetworkMatrix<imf.entity.Division> getBuilding() {
        return building;
    }

    /**
     * Retorna uma string que representa codigo da missão.
     *
     * @return uma string que representa o codigo da missão.
     */
    public String getCod() {
        return cod;
    }

    /**
     * Altera o codigo da missão.
     *
     * @param cod novo codigo da missão.
     */
    public void setCod(String cod) {
        this.cod = cod;
    }

    /**
     * Retorna um inteiro que representa a versão da missão.
     *
     * @return um inteiro que representa a versão da missão.
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * Altera a versão da da missão.
     *
     * @param version nova versão da missão.
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * Retorna o alvo da missão instancia da classe {@link imf.entity.Target}.
     *
     * @return alvo da missão.
     */
    public Target getTarget() {
        return target;
    }

    /**
     * Altera o alvo da missão, este deve ser da instancia da classe
     * {@link imf.entity.Target}.
     *
     * @param target novo alvo da missão.
     *
     */
    public void setTarget(Target target) {
        this.target = target;
    }

    /**
     * Altera o alvo da missão. Recebe como parâmetro as variaveis da classe
     * {@link imf.entity.Target}.
     *
     * @param division divisão alvo.
     * @param type tipo do alvo.
     */
    public void setTarget(String division, String type) {
        this.target = new Target(division, type);
    }

    /**
     * Retorna a lista ordenada que contém todas as simulações manuais
     * efetuadas, a lista ordenadas possui instancias da classe
     * {@link imf.entity.SimulationManual}.
     *
     * @return a lista ordenada que contém todas as simulações manuais
     * efetuadas.
     */
    public ArrayOrderedList<SimulationManual> getSimulation() {
        return simulation;
    }

    /**
     * Adiciona a lista ordenada que contém as simulações manuais mais uma
     * simulação manual.
     *
     * @param simulation simulação manual a adicionar.
     * @throws NullException caso a simulação que se deseja adicionar for nula.
     */
    public void addSimulation(SimulationManual simulation) throws NullException {
        if (simulation == null) {
            throw new NullException(NullException.SIMULATION_NULL);
        }

        this.simulation.add(simulation);
    }

    /**
     * Verifica se a missão possui um formato valido. Para a missao ser valida o
     * cod têm de ser diferente de nulo e não pode estar em branco.A versão deve
     * ser diferente de nula e superior a 0. A instancia da classe
     * {@link imf.entity.Target} tem de ser tambem valida
     *
     * @return verdadeiro se a missão for valida, falso caso contrario.
     */
    public boolean isValid() {
        return (this.cod != null && !this.cod.isBlank() && this.version > 0 && this.version != null && this.target.isValid());
    }

    /**
     * Retorna a divisão alvo num formato da classe {@link imf.entity.Division}.
     * É feita uma comparação entre strings visto que a divisão alvo apenas
     * contem o nome da divisão guardado,
     *
     * @return a divisão alvo num formato da classe {@link imf.entity.Division}
     */
    public imf.entity.Division getTargetDivision() {
        Iterator<imf.entity.Division> targetDivision = this.building.iteratorBFS(this.building.getFirst());
        imf.entity.Division current = null;

        while (targetDivision.hasNext()) {
            current = targetDivision.next();
            if (current.getName().equals(this.target.getDivision())) {
                return current;
            }
        }

        return current;
    }

    /**
     * Verifica através do nome de uma divisão se essa divisão é uma divisão de
     * saida ou entrada.Para tal uma pesquisa pelo array desordenado de entradas
     * e saidas, e uma comparação de strings com o nome da Divisão presente no
     * array desordenado de entradas e saidas e com a string recebida como
     * parâmetro. Caso o nome da divisão (string recebida como parâmetro )
     * esteja presente no array desordenado é retornado essa divisão caso
     * contrario é retornado uma divisão nula.
     *
     * @param div string que representa o nome da divisão a procurar no array
     * desordenado de entradas e saidas.
     * @return caso encontre a divisão esta é retornada, se não é retornada uma
     * divisão nula.
     */
    public imf.entity.Division getDivisionExitEntry(String div) {
        Iterator<imf.entity.Division> divisionExitEntry = this.exitEntry.iterator();
        imf.entity.Division current = null;

        while (divisionExitEntry.hasNext()) {
            current = divisionExitEntry.next();
            if (current.getName().compareTo(div) == 0) {
                return current;
            }
        }

        return null;
    }

    /**
     * Procura no grafo pesado(network ou rede) uma divisão atraves de uma
     * string, esta representa o nome da divisão.É feita uma comparação de
     * strings entre o nome das divisões do grafo e a string recebida por
     * parâmetro.Caso o nome da divisão (string recebida como parâmetro ) esteja
     * presente grafo pesado(network ou rede) é retornado essa divisão caso
     * contrario é retornado uma divisão nula.
     *
     * @param div string que representa o nome da divisão a procurar no array
     * desordenado de entradas e saidas.
     * @return caso encontre a divisão esta é retornada, se não é retornada uma
     * divisão nula.
     */
    public Division getDivision(String div) {
        Iterator<imf.entity.Division> division = this.building.iteratorBFS(this.building.getFirst());
        imf.entity.Division current = null;

        while (division.hasNext()) {
            current = division.next();
            if (current.getName().equals(div)) {
                return current;
            }
        }
        return null;
    }

    /**
     * Retorna uma representação em String da Missão
     *
     * @return string representação da Missão
     */
    @Override
    public String toString() {
        String s = "*-*-*-*-*-*-*--*-*-*-*--*-*-*-*--*-*-*--*-*-*--*-*-*-*--*-*-*-*-*-*-*\n";
        s += "  Missão: \n";
        s += "\tCodigo-Missão:" + this.getCod() + "\n";
        s += "\tVersão:" + this.getVersion() + "\n";
        s += "\tAlvo:\n";
        s += "\t\tDivisão:" + this.getTarget().getDivision() + "\n";
        s += "\t\tTipo:" + this.getTarget().getType() + "\n";
        Integer tam = this.getBuilding().size();
        if (this.getBuilding().isEmpty()) {
            s += EmptyCollectionException.GRAFO;
            return s;
        }

        s += "\nValor\t\t\tIndex\t";

        for (int i = 0; i < tam; i++) {
            if (i < 10) {
                s += "   |" + i + "|   ";
            } else {
                s += "  |" + i + "|   ";
            }
        }
        s += "\n\n";

        Iterator itBuilding = this.getBuilding().iteratorBFS(this.getBuilding().getFirst());

        int k = 0;
        while (itBuilding.hasNext()) {
            imf.entity.Division currentDiv = (imf.entity.Division) itBuilding.next();
            s += String.format("%-20s     %-7s", currentDiv.getName(), k);
            for (int j = 0; j < tam; j++) {
                String resultado = "";
                if (this.building.getEdge()[k][j] < Double.POSITIVE_INFINITY) {
                    if (this.building.getEdge()[k][j] == 0) {
                        resultado = String.format(" | %.2f| ", this.building.getEdge()[k][j]);
                    } else {
                        resultado = String.format(" |%.2f| ", this.building.getEdge()[k][j]);
                    }
                } else {
                    resultado += " | INF | ";
                }

                s += String.format("%2s", resultado);
            }
            s += "\n";
            k++;
        }

        return s;
    }
}
