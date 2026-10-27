package models;

import java.util.Optional;

public interface PilaEnJuego {

    public void agregarCarta(CartaJugador cartaNueva);
    public String verUltimaCartaComoTexto();
    public Optional<CartaJugador> getUltimaCarta();
    public String tipoPila();
}