package exceptions;

public class CartaNoExisteException extends RuntimeException {
    public CartaNoExisteException(String message) {
        super(message);
    }
}
