package exceptions;

public class CartaDuplicadaException extends RuntimeException {
    public CartaDuplicadaException(String message) {
        super(message);
    }
}
