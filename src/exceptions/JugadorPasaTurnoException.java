package exceptions;

public class JugadorPasaTurnoException extends RuntimeException {
    public JugadorPasaTurnoException(String message) {
        super(message);
    }

    public  JugadorPasaTurnoException(){
        super();
    }
}
