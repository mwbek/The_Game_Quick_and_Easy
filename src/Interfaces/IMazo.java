package Interfaces;

import Modelos.Carta;

public interface IMazo {
    Carta[] getMazo();

    boolean esVacio();

    Carta tomarCarta();

    void mezclarMazo();
}
