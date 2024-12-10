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
