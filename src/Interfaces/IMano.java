package Interfaces;

import Modelos.Carta;

public interface IMano {

    public void agregarCarta(IMazo iMazo);
    public void quitarCarta(Carta carta);
    public Carta[] getMano();
    public boolean contiene(Carta carta);
    public int getCantidadCartas();

}
