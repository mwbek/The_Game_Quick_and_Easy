package Modelos;

import exceptions.CartaNoExisteException;
import exceptions.MovimientoInvalidoException;

import java.util.ArrayList;
import java.util.List;

public class Mano {

    private List<Carta> cartas;
    private final int cartasMaximas = 2;


    public Mano() {
        cartas = new ArrayList<>();
    }

    /// Agrega una carta al mazo. Si la mano ya tiene 2 cartas no agrega ninguna.
    /// Si el mazo es vacio deja la mano original
    /// @param mazo
    /// @throws IllegalArgumentException cuando Mazo es nulo
    public void agregarCarta(Mazo mazo)
    {
        if(mazo == null) throw new IllegalArgumentException("El mazo no puede ser nulo");
        if(cartas.size() == cartasMaximas) return;
        if(mazo.esMazoVacio()) return;

        cartas.add(mazo.tomarCarta());
    }

    /// Elimina la carta de la mano y retorna true.
    /// Si la carta no se encuentra en la mano retorna false
    /// @param carta
    /// @return True si la carta existia en la mano, caso contrario lanza una excepcion
    /// @throws CartaNoExisteException Si la carta que se intento remover no existe, lanza una excepcion
    public void quitarCarta(Carta carta)
    {
        if(!cartas.remove(carta)) throw new CartaNoExisteException("La carta que se quiso remover no existe");
        cartas.remove(carta);
    }

    public Carta[] getMano(){
        Carta[] mano = new Carta[cartas.size()];
        mano = cartas.toArray(mano);
        return mano;

    }

    public boolean esCartaValida(Carta carta){
        if(carta == null) return false;
        return  cartas.contains(carta);
    }

}
