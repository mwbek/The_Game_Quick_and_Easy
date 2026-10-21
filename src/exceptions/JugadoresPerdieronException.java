package exceptions;

public class JugadoresPerdieronException extends RuntimeException {
    public JugadoresPerdieronException(String message) {
        super(message);
    }
    public JugadoresPerdieronException() {
        super();
    }
}
