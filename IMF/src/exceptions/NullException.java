package exceptions;

public class NullException extends Exception {

    public NullException() {
    }

    /**
     * Cria uma nova instancia  <code>NullException</code> com uma mensagem
     * detalhada
     *
     * @param message mensagem detalhada
     */
    public NullException(String message) {
        super(message);
    }

}
