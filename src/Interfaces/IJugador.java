package Interfaces;

import Modelos.Carta;
import Modelos.PilaDeIndicacion;

public interface IJugador {
    String getNombre();
    public int getCantidadCartasEnMano();
    public void tomarCarta(IMazo iMazo);
}
