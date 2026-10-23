package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Jugador {

    private final String nombre;
    private int cantidadCartas;
    private final int maxCantidadCartas;

    List<CartaJugador> cartas;
    public Jugador(String nombre) {
        this.nombre = nombre;
        maxCantidadCartas = 2;
        cantidadCartas = 0;

        cartas = new ArrayList<>();
    }


    public String getNombre() {
        return nombre;
    }


    /// El jugador toma cartas unicamente si necesita, caso contrario retorna las mismas cartas que ya tiene
    /// @param mazo
    public void tomarCarta(Mazo mazo)
    {
        for(int i = cantidadCartas; i < maxCantidadCartas; i++){
            cartas.add(mazo.tomarCarta());
        }
    }

    public String verCartas()
    {
        if(cartas.isEmpty()) return "[]";
        String mensaje = cartas.get(0).toString() + " | " + cartas.get(1).toString();
        return mensaje;
    }

    public CartaJugador getCarta1(){
        return cartas.getFirst();
    }

    public CartaJugador getCarta2(){
        return cartas.getLast();
    }

    public List<CartaJugador> getCartas()
    {
        return Collections.unmodifiableList(cartas);
    }

    public boolean jugarCarta(PilaEnJuego pila, CartaJugador carta) {
        boolean resultado = pila.agregarCarta(carta);
        cartas.remove(carta);
        return resultado;
    }



}
