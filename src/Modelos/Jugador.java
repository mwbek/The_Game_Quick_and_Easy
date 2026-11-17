package Modelos;
import Enumerados.CartaDeIndicacion;
import Interfaces.IJugador;
import Interfaces.IMazo;
import exceptions.CartaNoExisteException;
import exceptions.MovimientoInvalidoException;


public class Jugador implements IJugador {

    private final String nombre;
    private final Mano mano;


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

    public int getCantidadCartasEnMano(){
        return mano.getCantidadCartas();
    }

    public void tomarCarta(IMazo iMazo) {
        mano.agregarCarta(iMazo);
    }

    /// @param pila Pila de indicacion en la que se va a jugar la carta
    /// @param carta Carta elegida para jugar
    /// @exception CartaNoExisteException Si la carta no esta en la mano
    /// @exception MovimientoInvalidoException Si no es posible agregar la carta a la pila seleccionada
    public void jugarCarta(PilaDeIndicacion pila, Carta carta) {

        if(!mano.contiene(carta)) throw new CartaNoExisteException("La carta no esta en la mano");
        if(!pila.agregarCarta((carta)))  throw new MovimientoInvalidoException("No se puede agregar la carta en la pila");
        mano.quitarCarta(carta);
    }

    public Carta[] getCartas() {
        return mano.getMano();
    }
}
