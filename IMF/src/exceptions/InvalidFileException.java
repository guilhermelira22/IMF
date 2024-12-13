/**
 * @author Guilherme Lira - 8210415
 * @author Miguel Cunha - 8210426
 */

package exceptions;

public class InvalidFileException extends Exception {

    public InvalidFileException() {
    }

    /**
     * Cria uma nova instancia <code>InvalidFileException</code> com uma
     * mensagem detalhada
     *
     * @param message mensagem detalhada
     */
    public InvalidFileException(String message) {
        super(message);
    }

}
