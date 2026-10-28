package Modelos;


import Enumerados.ColorCarta;

import java.util.*;

public class Mazo {

    private Stack<Carta> pilaMazo;

    public Mazo() {
        pilaMazo = new Stack<>();
    }


    /// @return Retorna una array inmutable de las cartas del mazo
    public Carta[] getMazo() {
        Carta[] mazoCartas = new Carta[pilaMazo.size()];
        return pilaMazo.toArray(mazoCartas);
    }
    /// Retorna true si el mazo esta vacio, caso contrario retorna false
    public boolean esMazoVacio(){
        return pilaMazo.isEmpty();
    }

    /// Toma la carta que se encuentre en el tope del mazo
    /// @return Retorna una carta, si el mazo esta vacio retorna Null
    public Carta tomarCarta(){
        if(esMazoVacio()) return null;
        return pilaMazo.pop();
    }


    ///  Mezcla las 50 cartas del juego en forma aleatoria
    public void mezclarMazo() {

        //Si ya tiene cartas no se puede volver a llenar
        if(!this.pilaMazo.isEmpty()) return;

        Stack<Carta> pilaMazo = new Stack<>();
        List<Carta> cartas = new ArrayList<>();

        ColorCarta[] colores = ColorCarta.values();
        for(int i = 0; i < 5; i++){
            generarCartasPorColor(colores[i], cartas);
        }
        boolean mazoCreado = pilaMazo.addAll(cartas);
        if(mazoCreado) {
            this.pilaMazo = pilaMazo;
        }
        else throw new EmptyStackException();
    }

    private static void generarCartasPorColor(ColorCarta color, List<Carta> cartas) {
        for(int i = 1; i < 11; i++){
            cartas.add(new Carta(color, i));
        }
        //Mezcla las cartas
        Collections.shuffle(cartas);
    }

}
