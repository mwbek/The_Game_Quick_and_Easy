package models;

public interface PilaEnJuego {

    public void agregarCarta(CartaJugador cartaNueva);
    public String mostrarUltimaCarta();
    public CartaJugador getUltimaCarta();
    public String tipoPila();
}