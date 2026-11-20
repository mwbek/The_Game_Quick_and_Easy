package Modelos;
import Enumerados.CartaDeIndicacion;
import Interfaces.ICarta;
import Interfaces.IJugador;
import Interfaces.IMano;
import Interfaces.IMazo;
import exceptions.CartaNoExisteException;
import exceptions.MovimientoInvalidoException;

import java.io.Serializable;
import java.util.UUID;


public class Jugador implements IJugador, Serializable {

    private final String nombre;
    private final IMano mano;
    private final UUID id = UUID.randomUUID();

    public Jugador(String nombre) {
        //Valido que el nombre no sea null ni este vacio, teniendo espacios en blanco como " "
        if(nombre == null || nombre.isBlank()) throw new IllegalArgumentException("El nombre no puede estar vacio");
        this.nombre = nombre;
        mano = new Mano();
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public int getCantidadCartasEnMano(){
        return mano.getCantidadCartas();
    }

    @Override
    public UUID getId() { return id; }

    @Override
    public void tomarCarta(IMazo iMazo) {
        mano.agregarCarta(iMazo);
    }

    /// @param pila Pila de indicacion en la que se va a jugar la carta
    /// @param carta Carta elegida para jugar
    /// @exception CartaNoExisteException Si la carta no esta en la mano
    /// @exception MovimientoInvalidoException Si no es posible agregar la carta a la pila seleccionada
    @Override
    public void jugarCarta(PilaDeIndicacion pila, ICarta carta) {

        if(!mano.contiene(carta)) throw new CartaNoExisteException("La carta no esta en la mano");
        if(!pila.agregarCarta((carta)))  throw new MovimientoInvalidoException("No se puede agregar la carta en la pila");
        mano.quitarCarta(carta);
    }
    @Override
    public ICarta[] getCartas() {
        return mano.getMano();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Jugador)) return false;
        return id.equals(((Jugador) o).id);
    }

    @Override
    public int hashCode() { return id.hashCode(); }
}
