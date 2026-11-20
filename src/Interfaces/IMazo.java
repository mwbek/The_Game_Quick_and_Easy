package Interfaces;

import Modelos.Carta;

import java.util.List;

public interface IMazo {
    ICarta[] getMazo();

    boolean esVacio();

    ICarta tomarCarta();

    void mezclarMazo();
    int getCantidad();

    public void devolverCartas(List<ICarta> cartas);
}
