package models;

public interface PilaEnJuego {

    public boolean agregarCarta(CartaJugador cartaNueva);
    public String mostrarUltimaCarta();
    public String tipoPila();
}