package Interfaces;

import Enumerados.CartaDeIndicacion;
import Modelos.Carta;

public interface IPilaDeIndicacion {

    public boolean agregarCarta(ICarta carta);
    public boolean puedeAgregarCarta(ICarta carta);
    public CartaDeIndicacion getTipo();
    public ICarta getUltimaCarta();

}
