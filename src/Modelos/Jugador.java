package Modelos;

import Interfaces.PilaEnJuego;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Jugador {

    private final String nombre;
    private final int maxCantidadCartas;

    List<Carta> cartas;
    public Jugador(String nombre) {
        this.nombre = nombre;
        maxCantidadCartas = 2;

        cartas = new ArrayList<>();
    }


    public String getNombre() {
        return nombre;
    }


    public void tomarCarta(Mazo mazo) {
        cartas.add(mazo.tomarCarta());

    }

    public void jugarCarta(PilaEnJuego pila, Carta carta) {
            pila.agregarCarta(carta);
            cartas.remove(carta);
    }

    public boolean pasarRonda()
    {
        if(cartas.size() < 2){
            return true;
        }
        return false;
    }

    public String verCartas()
    {
        if(cartas.isEmpty()) return "[]";
        return cartas.get(0).toString() + " | " + cartas.get(1).toString();
    }

    public List<Carta> getCartas()
    {
        return Collections.unmodifiableList(cartas);
    }




    //Eliminar

    /// El jugador toma cartas unicamente si necesita, caso contrario retorna las mismas cartas que ya tiene
    /// @param mazo
    public void tomarCartas(Mazo mazo)
    {
        for(int i = cartas.size(); i < maxCantidadCartas; i++){
            cartas.add(mazo.tomarCarta());
        }
    }

    public Carta getCarta1(){
        return cartas.getFirst();
    }

    public Carta getCarta2(){
        return cartas.getLast();
    }



}
