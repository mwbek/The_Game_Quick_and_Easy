package Interfaces;

import Enumerados.CartaDeIndicacion;
import Modelos.Carta;

public interface IPilaDeIndicacion {

    public boolean agregarCarta(Carta carta);
    public boolean puedeAgregarCarta(Carta carta);
    public CartaDeIndicacion getTipo();
    public Carta getUltimaCarta();

}
