package models;


import java.util.*;

public class Mazo {

    private Stack<CartaJugador> pilaMazo;

    public Mazo() {
        pilaMazo = new Stack<>();

    }

    private void generarCartasPorColor(ColorCartaJugador color, List<CartaJugador> cartas) {
        for(int i = 1; i < 11; i++){
            cartas.add(new CartaJugador(color, i));
        }
        //Mezcla las cartas
        Collections.shuffle(cartas);
    }

    //Genera 50 cartas de forma aleatoria y las agrega a una PILA
    public Stack<CartaJugador> llenarMazo() {

        //Si ya tiene cartas no se puede volver a llenar
        if(!this.pilaMazo.isEmpty()) return this.pilaMazo;

        Stack<CartaJugador> pilaMazo = new Stack<>();
        List<CartaJugador> cartas = new ArrayList<>();

        ColorCartaJugador[] colores = ColorCartaJugador.values();
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

    public List<CartaJugador> getMazo() {
        return pilaMazo.stream().toList();
    }

    public boolean esMazoVacio(){
        return pilaMazo.isEmpty();
    }

    public CartaJugador tomarCarta(){
        if(esMazoVacio()) return null;
        return pilaMazo.pop();
    }
}
