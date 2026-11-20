package Modelos;

import Interfaces.ICarta;
import Interfaces.IMano;
import Interfaces.IMazo;
import exceptions.CartaNoExisteException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Mano implements IMano, Serializable {

    private List<ICarta> cartas;
    private static final int CARTAS_MAXIMAS = 2; //Se hizo static para que cada instancia tenga el mismo limite sin duplicar el atributo por instancia


    public Mano() {
        cartas = new ArrayList<>();
    }

    /// Agrega una carta al mazo. Si la mano ya tiene 2 cartas no agrega ninguna.
    /// Si el mazo es vacio deja la mano original
    /// @param iMazo
    /// @throws IllegalArgumentException cuando Mazo es nulo
    @Override
    public void agregarCarta(IMazo iMazo)
    {
        if(iMazo == null) throw new IllegalArgumentException("El mazo no puede ser nulo");
        if(cartas.size() == CARTAS_MAXIMAS) return;
        if(iMazo.esVacio()) return;

        cartas.add(iMazo.tomarCarta());
    }

    /// Elimina la carta de la mano y retorna true.
    /// Si la carta no se encuentra en la mano retorna false
    /// @param carta
    /// @return True si la carta existia en la mano, caso contrario lanza una excepcion
    /// @throws CartaNoExisteException Si la carta que se intento remover no existe, lanza una excepcion
    @Override
    public void quitarCarta(ICarta carta) {
        if(!cartas.remove(carta)) throw new CartaNoExisteException("La carta que se quiso remover no existe");
    }
    @Override
    public Carta[] getMano(){
        Carta[] mano = new Carta[cartas.size()];
        mano = cartas.toArray(mano);
        return mano;

    }
    @Override
    public boolean contiene(ICarta carta){
        if(carta == null) return false;
        return cartas.contains(carta);
    }
    @Override
    public int getCantidadCartas(){
        if(cartas.isEmpty()) return 0;
        return cartas.size();
    }

    public static int getCartasMaximas(){ return CARTAS_MAXIMAS;}
}
