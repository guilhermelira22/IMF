/**
 * @author Guilherme Lira - 8210415
 * @author Miguel Cunha - 8210426
 */

package exceptions;

public class InvalidTypeException extends Exception {

    public InvalidTypeException() {
    }

    /**
     * Cria uma nova instancia  <code>InvalidTypeException</code> com uma
     * mensagem detalhada
     *
     * @param message mensagem detalhada
     */
    public InvalidTypeException(String message) {
        super(message);
    }

}
