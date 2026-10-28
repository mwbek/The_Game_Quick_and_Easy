package Modelos;


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


    public void tomarCarta(Mazo mazo) {
        mano.agregarCarta(mazo);
    }

    ///
    /// @param pila Pila de indicacion en la que se va a jugar la carta
    /// @param carta Carta elegida para jugar
    /// @return retorna True si la carta se pudo jugar, caso contrario false
    public boolean jugarCarta(PilaDeIndicacion pila, Carta carta) {

        if(!mano.esCartaValida(carta)) return true;
        if(!pila.agregarCarta((carta))) return true;

        mano.quitarCarta(carta);
        return true;
    }

    /// Reviso todas las combinaciones posibles de la mano del jugador.
    /// @param pilaAsc pila Ascendente en juego
    /// @param pilaDesc pila Descendente en juego
    /// @return Retorna true si tiene al menos un movimiento valido, caso contrario retorna false
    public boolean tieneMovimientosValidos(PilaDeIndicacion pilaAsc, PilaDeIndicacion pilaDesc){

        for(Carta c : mano.getMano()){
            if(pilaAsc.puedeAgregarCarta(c)) return true;
        }

        for(Carta c : mano.getMano()){
            if(pilaDesc.puedeAgregarCarta(c)) return true;
        }

        return false;
    }


    public Carta[] getCartas()
    {
        return mano.getMano();
    }


}
