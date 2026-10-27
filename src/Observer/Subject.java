package Observer;

public interface Subject {
    public void suscribirse(Observer observador);
    public void desuscribirse(Observer observador);
    public void notificarMensaje();
}
