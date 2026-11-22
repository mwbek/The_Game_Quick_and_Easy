package Interfaces;

import Modelos.Carta;
import Modelos.PilaDeIndicacion;

import java.util.UUID;

public interface IJugador {
    String getNombre();
    public int getCantidadCartasEnMano();
    public void tomarCarta(IMazo iMazo);
    public void jugarCarta(IPilaDeIndicacion pila, ICarta carta);
    public ICarta[] getCartas();
    public UUID getId();
}
