/**
 * @author Guilherme Lira - 8210415
 * @author Miguel Cunha - 8210426
 */

package exceptions;

public class UnsupportedDataTypeException extends Exception {

    public UnsupportedDataTypeException() {
    }

    /**
     * Cria uma nova instancia  <code>UnsupportedTypeException</code> com uma mensagem
     * detalhada
     *
     * @param message mensagem detalhada
     */
    public UnsupportedDataTypeException(String message) {
        super(message);
    }

}
