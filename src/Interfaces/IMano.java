package Interfaces;

import Modelos.Carta;

public interface IMano {

    public void agregarCarta(IMazo iMazo);
    public void quitarCarta(ICarta carta);
    public ICarta[] getMano();
    public boolean contiene(ICarta carta);
    public int getCantidadCartas();

}
