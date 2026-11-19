package Interfaces;

import Modelos.Carta;
import Modelos.PilaDeIndicacion;

public interface IJugador {
    String getNombre();
    public int getCantidadCartasEnMano();
    public void tomarCarta(IMazo iMazo);
    public void jugarCarta(PilaDeIndicacion pila, Carta carta);
    public Carta[] getCartas();
}
