package Modelos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Jugador {

    private final String nombre;
    private final int maxCantidadCartas;

    private Mano mano;
    public Jugador(String nombre) {
        if(nombre.isEmpty()) throw new IllegalArgumentException("El nombre no puede estar vacio");
        this.nombre = nombre;
        maxCantidadCartas = 2;
        mano = new Mano();
    }


    public String getNombre() {
        return nombre;
    }


    public void tomarCarta(Mazo mazo) {
        mano.agregarCarta(mazo);
    }

    ///
    /// @param pila
    /// @param carta
    /// @return retorna True si la carta se pudo jugar, caso contrario false
    public boolean jugarCarta(PilaDeIndicacion pila, Carta carta) {

        if(!mano.esCartaValida(carta)) return true;
        if(!pila.agregarCarta((carta))) return true;

        mano.quitarCarta(carta);
        return true;
    }

    //TODO: Mover metodo a la clase juego porque el jugador no puede decidir si puede o no pasar de ronda
    public boolean puedePasarRonda() {
        return mano.getMano().length < 2;
    }

    public Carta[] getCartas()
    {
        return mano.getMano();
    }


}
