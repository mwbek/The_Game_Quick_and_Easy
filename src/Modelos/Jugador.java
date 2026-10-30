package Modelos;
import Interfaces.IMazo;


public class Jugador {

    private final String nombre;
    private final Mano mano;

    public Jugador(String nombre) {
        if(nombre.isEmpty()) throw new IllegalArgumentException("El nombre no puede estar vacio");
        this.nombre = nombre;
        mano = new Mano();
    }

    public String getNombre() {
        return nombre;
    }

    public int getCantidadCartasEnMano(){
        return mano.getCantidadCartas();
    }

    public void tomarCarta(IMazo iMazo) {
        mano.agregarCarta(iMazo);
    }

    ///
    /// @param pila Pila de indicacion en la que se va a jugar la carta
    /// @param carta Carta elegida para jugar
    /// @return retorna True si la carta se pudo jugar, caso contrario false
    public boolean jugarCarta(PilaDeIndicacion pila, Carta carta) {

        if(!mano.contiene(carta)) return true;
        if(!pila.agregarCarta((carta))) return true;

        mano.quitarCarta(carta);
        return true;
    }

    /// Reviso todas las combinaciones posibles de la mano del jugador.
    /// @param pilaAsc pila Ascendente en juego
    /// @param pilaDesc pila Descendente en juego
    /// @return Retorna true si tiene al menos un movimiento valido, caso contrario retorna false
    public boolean hayMovimientosValidos(PilaDeIndicacion pilaAsc, PilaDeIndicacion pilaDesc){

        //Si el jugador no tiene cartas es porque o no agarro o el mazo esta vacio
        if(getCantidadCartasEnMano() == 0) return true;

        //TODO : Fusionar ambos bucles en uno solo con un OR
        for(Carta c : mano.getMano()){
            if(pilaAsc.puedeAgregarCarta(c)) return true;
        }

        for(Carta c : mano.getMano()){
            if(pilaDesc.puedeAgregarCarta(c)) return true;
        }
        return false;
    }


    public Carta[] getCartas() {
        return mano.getMano();
    }


}
