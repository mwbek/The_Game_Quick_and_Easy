package Interfaces;

import Modelos.Carta;

import java.util.Optional;

public interface PilaEnJuego {

    public void agregarCarta(Carta cartaNueva);
    public String verUltimaCartaComoTexto();
    public Optional<Carta> getUltimaCarta();
    public String tipoPila();
}