/**
 * @author Guilherme Lira - 8210415
 * @author Miguel Cunha - 8210426
 */

package entities;


import Queue.Queue;
import exceptions.UnsupportedDataTypeException;
import interfaces.Division;
import interfaces.SimulationManual;

/**
 * Estrutura de Dados - 2020-2021.
 *
 * @author Mariana Ribeiro - 8190573
 * @author André Raro - 8190590
 *
 * Classe que representa uma simulação manual.Esta possui o numero de vida com
 * que o Tó Cruz terminou a missão, o caminho que percorreu e um valor booleano
 * que nos indica se ele apanhou ou não o alvo.
 */
public class SimulationManualImpl implements Comparable, SimulationManual {

    /**
     * Double que representa o numero de vida com que o Tó Cruz terminou a
     * simulação.
     */
    private Double lifePoints;

    /**
     * Array de String que contem o caminho que o Tó Cruz fez na simulação.
     */
    private String[] path;


    /**
     * Valor booleano que representa se o Tó Cruz possui ou não o alvo.
     */
    private boolean getTarget;

    /**
     * Construtor vazio que cria uma Simulação Manual
     */
    public SimulationManualImpl() {
    }

    /**
     * Construtor que cria uma simulação manual.Recebe como parametro o numero
     * de vida com que a simulação foi terminada.Recebe uma LinkedQueue que
     * representa o caminho percorrido, a informação que esta possui é tratada e
     * armazenada no array de Strings.Por ultimo recebe um valor booleano que
     * representa se o alvo foi ou não resgatado.
     *
     * @param lifePoints o numero de vida com que a simulação foi terminada.
     * @param path caminho percorrido na simulação.
     * @param target valor booleano que representa se o alvo foi ou não
     * resgatado.
     */
    public SimulationManualImpl(Double lifePoints, Queue<Division> path, boolean target) {
        this.lifePoints = lifePoints;
        this.getTarget = target;
        this.path = new String[path.size()];
        int i = 0;
        while (!path.isEmpty()) {
            Division div = path.dequeue();
            this.path[i] = div.getName();
            i++;
        }
    }

    public SimulationManualImpl(Double lifePoints, String[] path, boolean target) {
        this.lifePoints = lifePoints;
        this.getTarget = target;
        this.path = new String[path.length];
        this.path = path;
    }

    /**
     * Retorna um Double que representa o numero de vida com que terminou a
     * Simulação.
     *
     * @return o numero de vida com que terminou a Simulação.
     */
    public Double getLifePoints() {
        return lifePoints;
    }

    /**
     * Altera o numero de vida com que terminou a Simulação.
     *
     * @param lifePoints, novo numero de vida com que terminou a Simulação.
     */
    public void setLifePoints(Double lifePoints) {
        this.lifePoints = lifePoints;
    }

    /**
     * Retorna um array de strings que representa o caminho percorrido na
     * Simulação.
     *
     * @return o caminho percorrido na Simulação.
     */
    public String[] getPath() {
        return path;
    }

    /**
     * Altera o caminho percorrido na Simulação.Recebe uma LinkedQueue que
     * representa o caminho percorrido, a informação que esta possui é tratada e
     * armazenada no array de Strings.
     *
     * @param path LinkedQueue que representa o caminho percorrido na Simulação
     */
    public void setPath(Queue<Division> path) {
        this.path = new String[path.size()];
        int i = 0;
        while (!path.isEmpty()) {
            Division div = path.dequeue();
            this.path[i] = div.getName();
            i++;
        }
    }

    /**
     * Retorna um inteiro que representa o numero de quantas divisões foram
     * percorridas na Simulação.
     *
     * @return o numero de quantas divisões foram percorridas na Simulação.
     */
    public Integer getNumDivisions() {
        int countNumDivisions = 0;
        while (countNumDivisions < path.length) {
            countNumDivisions++;
        }
        return countNumDivisions;
    }

    /**
     * Retorna o valor booleano que representa se na simulação efetuada foi
     * resgatado o alvo.
     *
     * @return o valor booleano que representa se o alvo foi ou não resgatado.
     */
    public boolean isGetTarget() {
        return getTarget;
    }

    /**
     * Altera o valor booleano que representa se na simulação efetuada foi
     * resgatado o alvo.
     *
     * @param getTarget, novo valor booleano que representa se o alvo foi ou não
     * resgatado.
     */
    public void setGetTarget(boolean getTarget) {
        this.getTarget = getTarget;
    }

    /**
     * Compara um objeto que representa uma simulação manual com outra, para
     * posteriormente ao adicionarmos as simulações estas ficarem
     * ordenadas.Primeiramente é comparado os pontos de vida com que o Tó Cruz
     * finalizou a missão, caso estes sejam diferentes é entao retornado a sua
     * comparação.Caso o numero de pontos seja igual é comparado então o numero
     * de divisões percorridas e retornado a sua comparação
     *
     * @param obj objeto que representa a simulação manual a comparar
     * @return um inteiro que representa da comparação da simulação manual com o
     * objeto recebido por parametro
     */
    @Override
    public int compareTo(Object obj) {
        if (!(obj instanceof Comparable)) {
            try {
                throw new UnsupportedDataTypeException("UnsupportedDataTypeException.NOT_COMPARABLE");
            } catch (UnsupportedDataTypeException e) {
                throw new RuntimeException(e);
            }
        }

        SimulationManual simulation = (SimulationManual) obj;
        int result;

        if (this.lifePoints.equals(simulation.getLifePoints())) {
            result = this.getNumDivisions().compareTo(simulation.getNumDivisions());
        } else {
            result = simulation.getLifePoints().compareTo(this.lifePoints);
        }
        return result;
    }

    /**
     * /**
     * Retorna uma representação em String da Simulação Manual.
     *
     * @return string representação da Simulação Manual.
     */
    @Override
    public String toString() {
        String s = "";
        s += "\n\tPontos de vida: " + lifePoints;
        s += "\n\tAlvo: " + getTarget;
        s += "\n\tCaminho Percorrido: ";
        for (int i = 0; i < path.length; i++) {
            s += path[i];
            if (i != path.length - 1) {
                s += ", ";
            }
        }
        return s;
    }

}
