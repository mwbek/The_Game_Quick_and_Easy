package Modelos;


import Enumerados.ColorCarta;

import java.util.*;

public class Mazo {

    private Stack<Carta> pilaMazo;

    public Mazo() {
        pilaMazo = new Stack<>();

    }

    private void generarCartasPorColor(ColorCarta color, List<Carta> cartas) {
        for(int i = 1; i < 11; i++){
            cartas.add(new Carta(color, i));
        }
        //Mezcla las cartas
        Collections.shuffle(cartas);
    }

    //Genera 50 cartas de forma aleatoria y las agrega a una PILA
    public Stack<Carta> llenarMazo() {

        //Si ya tiene cartas no se puede volver a llenar
        if(!this.pilaMazo.isEmpty()) return this.pilaMazo;

        Stack<Carta> pilaMazo = new Stack<>();
        List<Carta> cartas = new ArrayList<>();

        ColorCarta[] colores = ColorCarta.values();
        for(int i = 0; i < 5; i++){
            generarCartasPorColor(colores[i], cartas);
        }
        boolean mazoCreado = pilaMazo.addAll(cartas);
        if(mazoCreado) {
            this.pilaMazo = pilaMazo;
            return pilaMazo;
        }
        else throw new EmptyStackException();


    }

    public List<Carta> getMazo() {
        return pilaMazo.stream().toList();
    }

    public boolean esMazoVacio(){
        return pilaMazo.isEmpty();
    }

    public Carta tomarCarta(){
        if(esMazoVacio()) return null;
        return pilaMazo.pop();
    }
}
