package Modelos;


import Enumerados.ColorCarta;
import Interfaces.ICarta;
import Interfaces.IMazo;
import exceptions.CartaDuplicadaException;

import java.io.Serializable;
import java.util.*;

public class Mazo implements IMazo, Serializable {

    private Stack<ICarta> pilaMazo;

    public Mazo() {
        pilaMazo = new Stack<>();
    }


    /// @return Retorna una array inmutable de las cartas del mazo
    @Override
    public ICarta[] getMazo() {
        ICarta[] mazoCartas = new Carta[pilaMazo.size()];
        return pilaMazo.toArray(mazoCartas);
    }
    /// Retorna true si el mazo esta vacio, caso contrario retorna false
    @Override
    public boolean esVacio(){
        return pilaMazo.isEmpty();
    }

    /// Toma la carta que se encuentre en el tope del mazo
    /// @return Retorna una carta, si el mazo esta vacio retorna Null
    @Override
    public ICarta tomarCarta(){
        if(esVacio()) return null;
        return pilaMazo.pop();
    }

    @Override
    public int getCantidad(){
        return pilaMazo.size();
    }

    @Override
    public void devolverCartas(List<ICarta> cartas) {
        for (ICarta c : cartas) {
            if (c != null && !pilaMazo.contains(c)) pilaMazo.push(c);
        }
        List<ICarta> todas = new ArrayList<>(pilaMazo);
        Collections.shuffle(todas);
        pilaMazo.clear();            // esto era lo que faltaba
        pilaMazo.addAll(todas);
    }


    ///  Mezcla las 50 cartas del juego en forma aleatoria
    @Override
    public void mezclarMazo() {

        //Si ya tiene cartas no se puede volver a llenar
        if(!this.pilaMazo.isEmpty()) return;

        Stack<ICarta> pilaMazo = new Stack<>();
        List<ICarta> cartas = new ArrayList<>();

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

    private static void generarCartasPorColor(ColorCarta color, List<ICarta> cartas) {
        for(int i = 1; i < 11; i++){
            cartas.add(new Carta(color, i));
        }
        //Mezcla las cartas
        Collections.shuffle(cartas);
    }

}
